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

package nl.procura.gba.web.services.zaken.indicaties;

import static ch.lambdaj.Lambda.*;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.fil;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.IndicatieDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;

public class IndicatiesService extends AbstractZaakContactService<IndicatieAanvraag>
    implements ZaakService<IndicatieAanvraag> {

  public IndicatiesService() {
    super("Indicatie", ZaakType.INDICATIE);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de indicaties")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return IndicatieDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  public Set<PlAantekeningIndicatie> getAllIndicaties() {

    Set<PlAantekeningIndicatie> indicaties = new HashSet<>();

    indicaties.addAll(getProwebIndicaties(IndicatieActie.TOEVOEGEN));
    indicaties.addAll(getPersoonslijstIndicaties());

    Set<PlAantekeningIndicatie> teVerwijderenIndicaties = getProwebIndicaties(IndicatieActie.VERWIJDEREN);

    for (PlAantekeningIndicatie indicatie : new ArrayList<>(indicaties)) {
      if (get(teVerwijderenIndicaties, indicatie.getProbev()) != null) {
        indicaties.remove(indicatie);
      }
    }

    return indicaties;
  }

  @Override
  public ZaakContact getContact(IndicatieAanvraag zaak) {

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
  @Timer
  @ThrowException("Fout bij het zoeken van de indicaties")
  public List<IndicatieAanvraag> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ZakenList(
        copyList(IndicatieDao.find(getArgumentenToMap(zaakArgumenten)), IndicatieAanvraag.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new IndicatieAanvraag());
  }

  /**
   * Alle PL indicaties die deze gebruiker mag zien
   */
  public List<PlAantekeningIndicatie> getPersoonslijstIndicaties() {

    BasePLExt pl = getServices().getPersonenWsService().getHuidige();

    BasePLCat soort = pl.getCat(GBACat.LOK_AF_IND);

    List<PlAantekeningIndicatie> indicaties = new ArrayList<>();

    Set<PlAantekeningIndicatie> toegestaneIndicaties = getToegestaneIndicaties();

    for (BasePLSet set : soort.getSets()) {

      BasePLRec r = set.getLatestRec();

      if (r != null) {

        String aant = r.getElemVal(GBAElem.LOK_AFN_AANT_SOORT).getDescr();

        PlAantekeningIndicatie indicatie = get(toegestaneIndicaties, aant);

        if (indicatie != null) {
          indicaties.add(indicatie);
        }
      }
    }

    return indicaties;
  }

  /**
   * De toegevoegde indicatie in Proweb die op opgenomen staan.
   */
  public Set<PlAantekeningIndicatie> getProwebIndicaties(IndicatieActie actie) {

    Set<PlAantekeningIndicatie> indicaties = new HashSet<>();

    BasePLExt pl = getServices().getPersonenWsService().getHuidige();
    ZaakArgumenten za = new ZaakArgumenten(pl, ZaakType.INDICATIE);
    za.setStatussen(ZaakStatusType.OPGENOMEN);

    for (Zaak zaak : getMinimalZaken(za)) {

      IndicatieAanvraag aanvraag = (IndicatieAanvraag) zaak;

      if (aanvraag.getActie() == actie) {
        indicaties.add(aanvraag.getIndicatie());
      }
    }

    return indicaties;
  }

  /**
   * Alle indicatie die deze gebruiker mag zien en muteren
   */
  public Set<PlAantekeningIndicatie> getToegestaneIndicaties() {

    Set<PlAantekeningIndicatie> indicaties = new HashSet<>();
    for (Profiel profiel : getServices().getGebruiker().getProfielen().getAlle()) {
      for (PlAantekeningIndicatie indicatie : profiel.getIndicaties().getAlle()) {
        if (!indicatie.isAantekening()) {
          indicaties.add(indicatie);
        }
      }
    }

    return indicaties;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return IndicatieDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  /**
   * Opslaan van de naamgebruikaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan van het record")
  public void save(IndicatieAanvraag zaak) {

    getZaakStatussen().setInitieleStatus(zaak);

    opslaanStandaardZaak(zaak);

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Verwijderen van de naamgebruikaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen van het record")
  public void delete(IndicatieAanvraag zaak) {

    if (fil(zaak.getZaakId())) {

      ConditionalMap map = new ConditionalMap();
      map.put(IndicatieDao.ZAAK_ID, zaak.getZaakId());
      map.put(IndicatieDao.MAX_CORRECT_RESULTS, 1);

      removeEntities(IndicatieDao.find(map));

      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  private PlAantekeningIndicatie get(Set<PlAantekeningIndicatie> indicaties, String aant) {
    return selectFirst(indicaties, having(on(PlAantekeningIndicatie.class).getProbev(), equalToIgnoringCase(aant)));
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    return getAlgemeneArgumentenToMap(zaakArgumenten);
  }
}
