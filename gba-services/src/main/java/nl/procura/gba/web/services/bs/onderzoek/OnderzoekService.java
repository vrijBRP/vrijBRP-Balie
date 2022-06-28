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

package nl.procura.gba.web.services.bs.onderzoek;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.BETROKKENE_ONDERZOEK;
import static nl.procura.standard.Globalfunctions.emp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.bsm.rest.v1_0.objecten.gba.probev.onderzoek.VerwerkOnderzoekAntwoordRestElement;
import nl.procura.bsm.rest.v1_0.objecten.gba.probev.onderzoek.VerwerkOnderzoekVraagRestElement;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.DossOnderzBron;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.GemeenteService;
import nl.procura.gba.web.services.zaken.algemeen.*;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class OnderzoekService extends AbstractZaakContactService<Dossier> implements ZaakService<Dossier> {

  private static final String BSM_ONDERZOEK_PROCESSOR_ID = "procura.personen.probev.onderzoek";

  public OnderzoekService() {
    super("Onderzoek", ZaakType.ONDERZOEK);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van onderzoeken")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZakenCount(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public ZaakContact getContact(Dossier zaak) {
    ZaakContact contact = new ZaakContact();
    DossierOnderzoek onderzoek = (DossierOnderzoek) zaak.getZaakDossier();
    contact.add(getServices().getDossierService().getContactPersoon(AANGEVER, onderzoek.getAangever()));
    // Betrokkenen toevoegen
    for (DossierPersoon persoon : onderzoek.getBetrokkenen()) {
      contact.add(getServices().getDossierService().getContactPersoon(BETROKKENE_ONDERZOEK, persoon));
    }
    return contact;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van onderzoeken")
  public List<Dossier> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getMinimalZaken(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  public Zaak getNewZaak() {
    Dossier dossier = aanvullenZaak(new DossierOnderzoek().getDossier());
    dossier.setDatumIngang(new DateTime());
    return dossier;
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public Dossier getStandardZaak(Dossier zaak) {

    DossierOnderzoek zaakDossier = getServices().getDossierService().getZaakDossier(zaak, DossierOnderzoek.class);

    zaakDossier.setAanleidingPlaats(GbaTables.WOONPLAATS.get(zaakDossier.getAanlPlaats()));
    zaakDossier.setAanleidingGemeente(GbaTables.PLAATS.get(zaakDossier.getAanlCGem()));
    zaakDossier.setAanleidingLand(GbaTables.LAND.get(zaakDossier.getAanlCLand()));

    zaakDossier.setResultaatPlaats(GbaTables.WOONPLAATS.get(zaakDossier.getResPlaats()));
    zaakDossier.setResultaatGemeente(GbaTables.PLAATS.get(zaakDossier.getResCGem()));
    zaakDossier.setResultaatLand(GbaTables.LAND.get(zaakDossier.getResCLand()));

    // Zet gemeente postbus
    GemeenteService gemService = getServices().getGemeenteService();
    zaakDossier.setVermoedelijkeGemeentePostbus(gemService.getGemeente(zaakDossier.getAanleidingGemeente()));
    zaakDossier.setResultaatGemeentePostbus(gemService.getGemeente(zaakDossier.getResultaatGemeente()));

    List<DossierOnderzoekBron> bronnen = copyList(zaakDossier.getDossOnderzBronnen(), DossierOnderzoekBron.class);
    bronnen.forEach(bron -> {
      bron.setAdresPlaats(GbaTables.WOONPLAATS.get(bron.getPlaats()));
      bron.setGemeente(GbaTables.PLAATS.get(bron.getcGem()));
      bron.setLand(GbaTables.LAND.get(bron.getcLand()));
      zaakDossier.getBronnen().add(bron);
    });

    getServices().getDossierService().getOverigDossier(zaak);

    return super.getStandardZaak(zaak);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return getServices().getDossierService().getZaakKeys(new ZaakArgumenten(zaakArgumenten, getZaakType()));
  }

  @Override
  @Transactional
  @ThrowException("Fout bij opslaan van het dossier")
  public void save(Dossier zaak) {

    DossierOnderzoek zaakDossier = ZaakUtils.newZaakDossier(zaak, getServices());

    setDerivedValues(zaakDossier);
    getServices().getDossierService().saveDossier(zaak);
    getServices().getDossierService().savePersonen(zaak);
    opslaanBronnen(zaakDossier);
    getServices().getDossierService().saveZaakDossier(zaak.getZaakDossier());

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Algemene afleidingen.
   * Gegevens die bedoeld zijn voor bijvoorbeeld het afdrukken van documenten.
   */
  @Override
  public Dossier setVolledigeZaakExtra(Dossier zaak) {
    DossierOnderzoek zaakDossier = to(zaak.getZaakDossier(), DossierOnderzoek.class);
    zaakDossier.setAantalOnderzoek(getAantalInhoudingen(zaakDossier));
    return super.setVolledigeZaakExtra(zaak);
  }

  private static void setDerivedValues(DossierOnderzoek zaakDossier) {
    zaakDossier.getDossier().setDescr(getDescription(zaakDossier));
  }

  private static String getDescription(DossierOnderzoek zaakDossier) {
    return zaakDossier.getOnderzoekBron().getOms() +
        zaakDossier.getBetrokkenen().stream().findFirst()
            .map(OnderzoekService::personDescription)
            .orElse("");
  }

  private static String personDescription(DossierPersoon p) {
    return " / " + p.getNaam().getGeslachtsnaam()
        + " / " + p.getDatumGeboorteStandaard()
        + " / " + p.getAdres().getAdres();
  }

  @Override
  @Transactional
  public void delete(Dossier zaak) {
    getServices().getDossierService().deleteDossier(zaak);
  }

  /**
   * Verwerk het onderzoek op de persoonslijst
   */
  public void setInOnderzoek(DossierOnderzoek zaakDossier, BsnFieldValue burgerServiceNummer) {
    if (StringUtils.isBlank(zaakDossier.getAanduidingGegevensOnderzoek().getCode())) {
      throw new ProException(ProExceptionSeverity.INFO, "Leg eerst een onderzoeksaanduiding vast bij de zaak");
    }
    UsrFieldValue ingevoerdDoor = zaakDossier.getDossier().getIngevoerdDoor();
    VerwerkOnderzoekVraagRestElement vraag = new VerwerkOnderzoekVraagRestElement();
    vraag.setToevoegen(true);
    vraag.setIngevoerdDoor(ingevoerdDoor.getStringValue(), ingevoerdDoor.getDescription());
    vraag.setBsn(burgerServiceNummer.getStringValue());
    vraag.setAanduiding(zaakDossier.getAanduidingGegevensOnderzoek().getCode());
    vraag.setDatum(zaakDossier.getDatumAanvangOnderzoek().getStringDate());
    VerwerkOnderzoekAntwoordRestElement antwoord = new VerwerkOnderzoekAntwoordRestElement();
    getServices().getBsmService().bsmQuery(BSM_ONDERZOEK_PROCESSOR_ID, vraag, antwoord);
  }

  /**
   * Verwerk het deelresultaat op de persoonslijst
   */
  public void setDeelresultaat(DossierOnderzoek zaakDossier, BsnFieldValue burgerServiceNummer) {
    if (StringUtils.isBlank(zaakDossier.getAanduidingGegevensDeelresultaat().getCode())) {
      throw new ProException(ProExceptionSeverity.WARNING, "Leg eerst een deelresultaat vast bij de zaak");
    }
    UsrFieldValue ingevoerdDoor = zaakDossier.getDossier().getIngevoerdDoor();
    VerwerkOnderzoekVraagRestElement vraag = new VerwerkOnderzoekVraagRestElement();
    vraag.setToevoegen(true);
    vraag.setIngevoerdDoor(ingevoerdDoor.getStringValue(), ingevoerdDoor.getDescription());
    vraag.setBsn(burgerServiceNummer.getStringValue());
    vraag.setAanduiding(zaakDossier.getAanduidingGegevensDeelresultaat().getCode());
    vraag.setDatum(zaakDossier.getDatumAanvangDeelresultaat().getStringDate());
    VerwerkOnderzoekAntwoordRestElement antwoord = new VerwerkOnderzoekAntwoordRestElement();
    getServices().getBsmService().bsmQuery(BSM_ONDERZOEK_PROCESSOR_ID, vraag, antwoord);
  }

  /**
   * Verwerk de aanduiding op de persoonslijst
   */
  public void haalUitOnderzoek(DossierOnderzoek zaakDossier, BsnFieldValue burgerServiceNummer) {
    UsrFieldValue ingevoerdDoor = zaakDossier.getDossier().getIngevoerdDoor();
    VerwerkOnderzoekVraagRestElement vraag = new VerwerkOnderzoekVraagRestElement();
    vraag.setToevoegen(false);
    vraag.setIngevoerdDoor(ingevoerdDoor.getStringValue(), ingevoerdDoor.getDescription());
    vraag.setBsn(burgerServiceNummer.getStringValue());
    vraag.setAanduiding(zaakDossier.getAanduidingGegevensOnderzoek().getCode());
    vraag.setDatum(zaakDossier.getDatumAanvangOnderzoek().getStringDate());
    VerwerkOnderzoekAntwoordRestElement antwoord = new VerwerkOnderzoekAntwoordRestElement();
    getServices().getBsmService().bsmQuery(BSM_ONDERZOEK_PROCESSOR_ID, vraag, antwoord);
  }

  @Transactional
  public void deleteBetrokkene(DossierOnderzoek dossierOnderzoek, DossierPersoon persoon) {
    dossierOnderzoek.getDossier().verwijderPersoon(persoon);
    if (persoon.getCode() != null) {
      removeEntity(persoon);
    }
  }

  @Transactional
  public void deleteBron(DossierOnderzoek zaakDossier, DossierOnderzoekBron bron) {
    if (bron.getCode() != null) {
      DossierOnderzoek zaakDossierImpl = to(zaakDossier, DossierOnderzoek.class);
      zaakDossierImpl.getBronnen().remove(bron);
      zaakDossierImpl.getDossOnderzBronnen().remove(ReflectionUtil.deepCopyBean(DossOnderzBron.class, bron));
      removeEntity(bron);
    }
  }

  public long getAantalInhoudingen(DossierOnderzoek zaakDossier) {
    // Search
    PLEArgs args = new PLEArgs();
    args.setShowArchives(false);
    args.setShowRemoved(false);
    args.setDatasource(PLEDatasource.PROCURA);

    List<DossierPersoon> personen = zaakDossier.getBetrokkenen();
    long count = 0;

    if (!personen.isEmpty()) {
      for (DossierPersoon dossierPersoon : personen) {
        args.addNummer(dossierPersoon.getBurgerServiceNummer().getStringValue());
      }
      count = getServices().getPersonenWsService()
          .getPersoonslijsten(args, false)
          .getBasisPLWrappers()
          .stream()
          .filter(this::isIngehouden)
          .count();
    }
    return count;
  }

  private void opslaanBronnen(DossierOnderzoek zaakDossier) {
    for (DossierOnderzoekBron bron : zaakDossier.getBronnen()) {
      bron.setDossier(zaakDossier);
      saveEntity(bron);
    }
  }

  private boolean isIngehouden(BasePLExt pl) {
    BasePLRec record = pl.getCat(GBACat.VB).getLatestRec();
    if (record.hasElems()) {
      String aand = record.getElemVal(GBAElem.AAND_GEG_IN_ONDERZ).getVal();
      String aandEnd = record.getElemVal(GBAElem.DATUM_EINDE_ONDERZ).getVal();
      return StringUtils.isNotBlank(aand) && emp(aandEnd);
    }

    return false;
  }
}
