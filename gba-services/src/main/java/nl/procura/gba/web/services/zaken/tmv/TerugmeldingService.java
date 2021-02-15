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

package nl.procura.gba.web.services.zaken.tmv;

import static nl.procura.gba.common.MiscUtils.*;
import static nl.procura.gba.web.common.tables.GbaTables.PLAATS;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.TerugmeldingDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.TerugmTmv;
import nl.procura.gba.jpa.personen.db.TerugmTmvRel;
import nl.procura.gba.jpa.personen.db.TerugmTmvRelPK;
import nl.procura.gba.jpa.personen.db.Terugmelding;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.standard.exceptions.ProException;

public class TerugmeldingService extends AbstractZaakContactService<TerugmeldingAanvraag>
    implements ZaakService<TerugmeldingAanvraag> {

  public TerugmeldingService() {
    super("Tmv", ZaakType.TERUGMELDING);
  }

  public REGISTRATIE_STATUS actualiseer(TerugmeldingAanvraag aanvraag) {

    TerugmeldingBerichtService berichten = getServices().getTerugmeldingBerichtService();

    TerugmeldingRegistratie tmv = berichten.inzage(aanvraag);

    REGISTRATIE_STATUS status = saveRegistratie(aanvraag, tmv);

    if (status == REGISTRATIE_STATUS.TOEGEVOEGD) {

      getServices().getTerugmeldingService().herlaad(aanvraag);
    }

    return status;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de terugmeldingen")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return TerugmeldingDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public ZaakContact getContact(TerugmeldingAanvraag zaak) {

    ZaakContact zaakContact = new ZaakContact();
    BasePLExt basisPersoon = getBasisPersoon(zaak);

    if (basisPersoon != null) {
      ZaakContactpersoon persoon = new ZaakContactpersoon(AANGEVER, basisPersoon);
      persoon.setContactgegevens(getServices().getContactgegevensService().getContactgegevens(zaak));
      zaakContact.add(persoon);
    }

    return zaakContact;
  }

  /**
   * Geeft de aanvragen van deze zaak terug
   */
  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de terugmeldingen")
  public List<TerugmeldingAanvraag> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList(
        copyList(TerugmeldingDao.find(getArgumentenToMap(zaakArgumenten)), TerugmeldingAanvraag.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new TerugmeldingAanvraag());
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public TerugmeldingAanvraag getStandardZaak(TerugmeldingAanvraag zaak) {

    TerugmeldingAanvraag tm = to(zaak, TerugmeldingAanvraag.class);

    tm.setDetails(copyList(tm.getTerugmDetails(), TerugmeldingDetail.class));
    tm.setReacties(copyList(tm.getTerugmReacties(), TerugmeldingReactie.class));

    tm.getRegistraties().clear();
    for (TerugmTmvRel tmvDossier : tm.getTerugmTmvRels()) {

      for (TerugmTmv tmTmv : TerugmeldingDao.findTmv(tmvDossier.getId().getDossiernummer())) {

        TerugmeldingRegistratie rImpl = copy(tmTmv, TerugmeldingRegistratie.class);
        rImpl.setUsr(getGebruiker(along(tmTmv.getCUsr())));
        rImpl.setGemBeh(PLAATS.get(rImpl.getCGemBeh().toString()));
        tm.getRegistraties().add(rImpl);
      }
    }

    return super.getStandardZaak(zaak);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return TerugmeldingDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  /**
   * Herlaad de terugmelding met gegevens uit de database
   */
  public void herlaad(TerugmeldingAanvraag tmv) {

    Terugmelding jpaTerugmelding = findEntity(Terugmelding.class, tmv.getCTerugmelding());
    copy(getCompleteZaak(copy(jpaTerugmelding, TerugmeldingAanvraag.class)), tmv);
  }

  public TerugmeldingRegistratie intrekken(TerugmeldingAanvraag aanvraag) {

    TerugmeldingBerichtService berichten = getServices().getTerugmeldingBerichtService();

    TerugmeldingRegistratie tmv = berichten.intrekken(aanvraag);

    switch (saveRegistratie(aanvraag, tmv)) {

      case BESTAAT_ALL:
        throw new ProException(INFO, "Deze terugmelding is al ingetrokken.");

      default:
        break;
    }

    getServices().getTerugmeldingService().herlaad(aanvraag);

    return tmv;
  }

  public TerugmeldingRegistratie inzage(TerugmeldingAanvraag aanvraag) {

    TerugmeldingBerichtService berichten = getServices().getTerugmeldingBerichtService();

    TerugmeldingRegistratie tmv = berichten.inzage(aanvraag);

    switch (saveRegistratie(aanvraag, tmv)) {

      case BESTAAT_ALL:
        throw new ProException(INFO,
            "De terugmelding is niet gewijzigd sinds de vorige inzage. De laatste status is nog steeds actueel");

      default:
        break;
    }

    getServices().getTerugmeldingService().herlaad(aanvraag);

    return tmv;
  }

  /**
   * Terugmelding opslaan
   */
  @Override
  @Transactional(closeAfterCommit = true)
  @ThrowException("Fout bij het opslaan van de aanvraag")
  public void save(TerugmeldingAanvraag zaak) {

    TerugmeldingAanvraag zaakImpl = getInstance(zaak, TerugmeldingAanvraag.class);

    if (emp(zaakImpl.getTerugmelding())) {
      throw new ProException(ENTRY, WARNING, "Geen toelichting");
    }

    getZaakStatussen().setInitieleStatus(zaakImpl);

    opslaanStandaardZaak(zaakImpl);

    opslaanDetails(zaakImpl);

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Reactie opslaan
   */
  @ThrowException("Fout bij het opslaan van de reactie")
  public void saveReactie(TerugmeldingAanvraag tmv, TerugmeldingReactie reactie) {
    TerugmeldingReactie rImpl = reactie;
    rImpl.setTerugmelding(findEntity(Terugmelding.class, tmv.getCTerugmelding()));
    saveEntity(rImpl);
  }

  /**
   * Registratie opslaan
   */
  @Transactional
  @ThrowException("Fout bij het opslaan van de terugmeld registratie")
  public REGISTRATIE_STATUS saveRegistratie(TerugmeldingAanvraag tmv, TerugmeldingRegistratie tmvR) {

    TerugmeldingRegistratie rImpl = tmvR;
    if (exists(tmvR)) {
      return REGISTRATIE_STATUS.BESTAAT_ALL;
    }

    rImpl.setCTerugmelding(toBigDecimal(tmv.getCTerugmelding()));
    saveEntity(rImpl);

    TerugmTmvRel rel = new TerugmTmvRel();
    TerugmTmvRelPK relPk = new TerugmTmvRelPK();
    relPk.setCTerugmelding(tmv.getCTerugmelding());
    relPk.setDossiernummer(rImpl.getDossiernummer());
    rel.setId(relPk);
    rel.setTerugmelding(copy(tmv, Terugmelding.class));

    saveEntity(rel);

    return REGISTRATIE_STATUS.TOEGEVOEGD;
  }

  public TerugmeldingRegistratie registeer(TerugmeldingAanvraag aanvraag) {

    TerugmeldingBerichtService berichten = getServices().getTerugmeldingBerichtService();

    TerugmeldingRegistratie tmv = berichten.registeer(aanvraag);

    switch (saveRegistratie(aanvraag, tmv)) {

      case BESTAAT_ALL:
        throw new ProException(INFO, "Deze terugmelding is al ingevoerd.");

      default:
        break;
    }

    getServices().getTerugmeldingService().herlaad(aanvraag);

    return tmv;
  }

  /**
   * Update terugmelding
   */
  public void updateTerugmelding(TerugmeldingAanvraag aanvraag) {

    if (aanvraag.getStatus() == ZaakStatusType.VERWERKT) {
      aanvraag.setAfgehandeldDoor(new UsrFieldValue(getServices().getGebruiker()));
      aanvraag.setDatumTijdAfhandeling(new DateTime());
    }

    saveEntity(aanvraag);
  }

  /**
   * Verwijderen van de geboorteaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen terugmelding.")
  public void delete(TerugmeldingAanvraag zaak) {

    TerugmeldingDao.removeTerugmelding(zaak);

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Verwijder reactie
   */
  public void deleteReactie(TerugmeldingReactie reactie) {
    removeEntity(reactie);
  }

  /**
   * Controle of registratie met dat dossiernummers, status en berichtcode al voorkomt.
   */
  private boolean exists(TerugmeldingRegistratie r) {
    return TerugmeldingDao.exists(r.getDossiernummer(), r.getStatus().getCode(), r.getBerichtcode());
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    return getAlgemeneArgumentenToMap(zaakArgumenten);
  }

  private void opslaanDetails(TerugmeldingAanvraag tmv) {

    for (TerugmeldingDetail detail : tmv.getDetails()) {
      detail.getId().setCTerugmelding(tmv.getCTerugmelding());
      detail.setTerugmelding(findEntity(Terugmelding.class, tmv.getCTerugmelding()));
      saveEntity(detail);
    }
  }

  public enum REGISTRATIE_STATUS {
    BESTAAT_ALL,
    TOEGEVOEGD
  }
}
