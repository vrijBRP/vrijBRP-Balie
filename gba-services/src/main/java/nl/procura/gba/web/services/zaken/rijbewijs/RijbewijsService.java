/*
 * Copyright 2021 - 2022 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.gba.web.services.zaken.rijbewijs;

import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingIds.RDW_BLOK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.NrdDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.Nrd;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingIds;
import nl.procura.gba.web.services.applicatie.meldingen.types.RdwMelding;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.standard.ProcuraDate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RijbewijsService extends AbstractZaakContactService<RijbewijsAanvraag>
    implements ZaakService<RijbewijsAanvraag>, ControleerbareService {

  public RijbewijsService() {
    super("Rijbewijzen", ZaakType.RIJBEWIJS);
  }

  @Override
  public void check() {
    checkRdw();
  }

  public boolean isRijbewijzenServiceActive() {
    return isTru(getServices().getParameterService().getSysteemParameter(ParameterConstant.RYB_ENABLED).getValue());
  }

  @Override
  @Timer
  @ThrowException("Fout bij zoeken van de rijbewijsaanvragen")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return NrdDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public ZaakContact getContact(RijbewijsAanvraag zaak) {
    ZaakContact zaakContact = new ZaakContact();
    BasePLExt basisPersoon = getBasisPersoon(zaak);

    if (basisPersoon != null) {
      ZaakContactpersoon persoon = new ZaakContactpersoon(AANGEVER, basisPersoon);
      persoon.setContactgegevens(getServices().getContactgegevensService().getContactgegevens(zaak));
      zaakContact.add(persoon);
    }

    return zaakContact;
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    return new RijbewijsZaakControles(this).getControles(listener);
  }

  public Controles getControles(ControlesListener listener, RijbewijsStatusType status) {
    return new RijbewijsZaakControles(this).getControlesByStatus(listener, status);
  }

  @Override
  @Timer
  @ThrowException("Fout bij zoeken van de rijbewijsaanvragen")
  public List<RijbewijsAanvraag> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList(copyList(NrdDao.find(getArgumentenToMap(zaakArgumenten)), RijbewijsAanvraag.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new RijbewijsAanvraag());
  }

  @Override
  public RijbewijsAanvraag getStandardZaak(RijbewijsAanvraag zaak) {
    vulRijbewijsStatussen(zaak);
    return new RijbewijsZaakControles(this).controleer(super.getStandardZaak(zaak));
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return NrdDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan van de rijbewijs aanvraag")
  public void save(RijbewijsAanvraag zaak) {

    RijbewijsAanvraag zaakImpl = getInstance(zaak, RijbewijsAanvraag.class);
    ZaakStatusService statussen = getZaakStatussen();
    statussen.setInitieleStatus(zaakImpl);

    if (!isSaved(zaakImpl)) {
      zaakImpl.setCAanvr(NrdDao.findAanvraagNummer(zaakImpl.getAanvraagNummer()));
      zaakImpl.setUsr(findEntity(Usr.class, along(defaultNul(zaakImpl.getGebruikerAanvraag().getStringValue()))));
      zaakImpl.setUsrUitgifte(
          findEntity(Usr.class, along(defaultNul(zaakImpl.getGebruikerUitgifte().getStringValue()))));
    }

    opslaanStandaardZaak(zaakImpl);

    for (RijbewijsAanvraagStatus status : zaakImpl.getStatussen().getStatussen()) {
      opslaanRdwStatus(zaakImpl, status);
    }

    // Als sprake is van bepaalde RDW status dan op verwerkt zetten

    RijbewijsAanvraagStatus status = zaakImpl.getStatussen().getStatus();

    if (zaakImpl.getStatus().isKleinerDan(ZaakStatusType.VERWERKT)) {

      if (status.isStatus(GEACCORDEERD)) {
        statussen.updateStatus(zaakImpl, statussen.getInitieleStatus(zaakImpl), "");
      }

      if (status.isStatus(GEANNULEERD)) {
        statussen.updateStatus(zaakImpl, ZaakStatusType.GEANNULEERD, "");
      }

      if (status.getStatus().getCode() >= RIJBEWIJS_NIET_UITGEREIKT.getCode()) {
        statussen.updateStatus(zaakImpl, ZaakStatusType.VERWERKT, "");
      }
    }

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Registeer vermissing
   */
  public void saveVermissing(RijbewijsAanvraag aanvraag) {
    String dIn = aanvraag.getDatumIngang().getStringDate();
    String pv = aanvraag.getProcesVerbaalVerlies();
    BasePLExt basisPersoon = aanvraag.getBasisPersoon();
    String rbwNr = aanvraag.getVervangingsRbwNr();

    if (RijbewijsAanvraagReden.WEGENS_VERLIES_OF_DIEFSTAL.equals(aanvraag.getRedenAanvraag()) && fil(pv)) {
      Services.getInstance()
          .getDocumentInhoudingenService()
          .setRijbewijsInhouding(basisPersoon, dIn, rbwNr, pv, InhoudingType.VERMISSING);
    }
  }

  /**
   * Verwijderen van de geboorteaanvraag
   */
  @Override
  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(RijbewijsAanvraag zaak) {

    if (fil(zaak.getAanvraagNummer())) {
      ConditionalMap map = new ConditionalMap();
      map.put(NrdDao.ZAAK_ID, zaak.getAanvraagNummer());
      map.put(NrdDao.MAX_CORRECT_RESULTS, 1);

      for (Nrd e : NrdDao.find(map)) {
        removeEntities(e.getNrdStatuses());
        removeEntity(e);
      }

      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  public void vulRijbewijsStatussen(RijbewijsAanvraag zaak) {
    new RijbewijsZaakControles(this).vulRijbewijsStatussen(zaak);
  }

  public RijbewijsAanvraag findByAanvraagnummer(String aanvraagnummer) {
    Nrd nrd = NrdDao.findByAanvraagNummer(aanvraagnummer);
    return (nrd != null) ? getCompleteZaak(copy(nrd, RijbewijsAanvraag.class)) : null;
  }

  @Transactional
  @ThrowException("Fout bij het opslaan")
  public void deblokkeer() {
    ParameterService parameters = getServices().getParameterService();
    parameters.setSysteemParameter(ParameterConstant.RYB_GEBLOKKEERD, null);
  }

  @Transactional
  @ThrowException("Fout bij het opslaan")
  public void blokkeer() {
    ParameterService parameters = getServices().getParameterService();
    parameters.setSysteemParameter(ParameterConstant.RYB_GEBLOKKEERD, "1");
  }

  @Transactional
  @ThrowException("Fout bij het opslaan")
  public void setAccount(String wachtwoord, String datumGewijzigd) {
    ParameterService parameters = getServices().getParameterService();
    parameters.setSysteemParameter(RYB_WACHTWOORD, wachtwoord);
    parameters.setSysteemParameter(RYB_DATUM_GEWIJZIGD, datumGewijzigd);
  }

  public RijbewijsAccount getAccount() {
    ParameterService parameters = getServices().getParameterService();
    String geb = parameters.getSysteemParameter(RYB_GEBRUIKERSNAAM).getValue();
    String url = parameters.getSysteemParameter(RYB_URL).getValue();
    String datumGewijzigd = parameters.getSysteemParameter(RYB_DATUM_GEWIJZIGD).getValue();
    int vervaltermijn = aval(parameters.getSysteemParameter(RYB_VERVALTERMIJN_DAGEN).getValue());
    boolean geblokkeerd = isTru(parameters.getSysteemParameter(RYB_GEBLOKKEERD).getValue());
    boolean isIngevuld = fil(geb) && fil(url);

    String datumVerloop = "";
    int dagenGeldig = 0;
    if (pos(datumGewijzigd) && vervaltermijn > 0) {
      datumVerloop = new ProcuraDate(datumGewijzigd).addDays(vervaltermijn).getSystemDate();
      dagenGeldig = new ProcuraDate().diffInDays(datumVerloop);
    }

    RijbewijsAccount account = new RijbewijsAccount();
    account.setDagenGeldig(dagenGeldig);
    account.setDatumGewijzigd(datumGewijzigd);
    account.setDatumVerloop(datumVerloop);
    account.setGeblokkeerd(geblokkeerd);
    account.setGebruikersnaam(geb);
    account.setIngevuld(isIngevuld);

    return account;
  }

  public boolean isAanpassingVanToepassing(String deel) {
    return aval(getParm(ParameterConstant.RYB_AANPASSINGEN)) >= aval(deel);
  }

  private void checkRdw() {
    RijbewijsAccount account = getAccount();

    if (account != null) {
      if (fil(account.getDatumVerloop())) {
        int dagen = account.getDagenGeldig();
        String melding = "";

        if (dagen <= 0) {
          melding = "Een RDW wachtwoord is verlopen.";
        } else if (dagen < 10) {
          melding = "Een RDW wachtwoord verloopt over " + (dagen == 1 ? "1 dag" : (dagen + " dagen"));
        }

        if (fil(melding)) {
          String id = ServiceMeldingIds.RDW_VERLOOP + account.getGebruikersnaam();
          getServices().getMeldingService().add(new RdwMelding(id, account, melding));
        }
      }

      if (account.isGeblokkeerd()) {
        String melding = "Het RDW account is momenteel geblokkeerd. De applicatiebeheerder kan deze deblokkeren.";
        String id = RDW_BLOK + account.getGebruikersnaam();
        getServices().getMeldingService().add(new RdwMelding(id, account, melding));
      }
    }
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    ConditionalMap map = getAlgemeneArgumentenToMap(zaakArgumenten);
    if (!zaakArgumenten.isAll()) {
      if (zaakArgumenten instanceof RijbewijsZaakArgumenten) {
        String documentNummer = ((RijbewijsZaakArgumenten) zaakArgumenten).getDocumentnummer();
        map.putString(NrdDao.RBW_NR, documentNummer);
      }
    }

    return map;
  }

  private void opslaanRdwStatus(RijbewijsAanvraag zaak, RijbewijsAanvraagStatus status) {

    RijbewijsAanvraagStatus statusImpl = status;
    Nrd nrd = findEntity(Nrd.class, zaak.getCAanvr());

    statusImpl.setNrd(nrd);
    statusImpl.getId().setCAanvr(nrd.getCAanvr());

    saveEntity(status);
  }
}
