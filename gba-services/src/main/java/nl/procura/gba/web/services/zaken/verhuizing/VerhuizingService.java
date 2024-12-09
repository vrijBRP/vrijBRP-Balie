/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.zaken.verhuizing;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VB;
import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.formatPostcode;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.common.ZaakStatusType.OPGENOMEN;
import static nl.procura.gba.web.common.tables.GbaTables.LAND;
import static nl.procura.gba.web.common.tables.GbaTables.PLAATS;
import static nl.procura.gba.web.common.tables.GbaTables.STRAAT;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.MEEVERHUIZER;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.ArrayList;
import java.util.List;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.templates.custom.CustomTemplate;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.BvhParkDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.BvhPark;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.common.misc.ZakenList;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijstHandler;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import org.apache.commons.lang3.math.NumberUtils;

public class VerhuizingService extends AbstractZaakContactService<VerhuisAanvraag>
    implements ZaakService<VerhuisAanvraag> {

  public VerhuizingService() {
    super("Verhuizing", ZaakType.VERHUIZING);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de verhuisaanvragen")
  public int getZakenCount(final ZaakArgumenten zaakArgumenten) {
    return BvhParkDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public VerhuisAanvraag setVolledigeZaakExtra(VerhuisAanvraag zaak) {

    VerhuisAanvraag impl = to(zaak, VerhuisAanvraag.class);

    String anrAangever = impl.getAangever().getAnummer().getStringValue();
    String bsnAangever = impl.getAangever().getBurgerServiceNummer().getStringValue();

    String bsnToestemming = impl.getToestemminggever().getBurgerServiceNummer().getStringValue();
    String bsnHoofdbewoner = impl.getHoofdbewoner().getBurgerServiceNummer().getStringValue();

    String nrAangever = pos(bsnAangever) ? bsnAangever : anrAangever;

    if (pos(nrAangever) && impl.getAangever().getPersoon() == null) {
      impl.getAangever().setPersoon(new DocumentPL(getPersoonslijst(nrAangever)));
    }

    if (pos(bsnToestemming) && impl.getToestemminggever().getPersoon() == null) {
      impl.getToestemminggever().setPersoon(new DocumentPL(getPersoonslijst(bsnToestemming)));
    }

    if (pos(bsnHoofdbewoner) && impl.getHoofdbewoner().getPersoon() == null) {
      impl.getHoofdbewoner().setPersoon(new DocumentPL(getPersoonslijst(bsnHoofdbewoner)));
    }

    for (VerhuisPersoon p : impl.getPersonen()) {
      if (p.getPersoon() == null) {
        String nummer = ZaakUtils.getNummer(p);
        if (pos(nummer)) {
          p.setPersoon(new DocumentPL(getPersoonslijst(nummer)));
        }
      }
    }

    for (VerhuisPersoon p : impl.getPersonen()) {
      if (!p.getPersoon().getVerblijfplaatsen().isEmpty()) {
        BasePLRec r = getLaatsteVerblijfplaats(impl, p);
        if (r != null) {
          BasePLElem straat = r.getElem(GBAElem.STRAATNAAM);
          BasePLElem huisnummer = r.getElem(GBAElem.HNR);
          BasePLElem huisletter = r.getElem(GBAElem.HNR_L);
          BasePLElem huisnummertoev = r.getElem(GBAElem.HNR_T);
          BasePLElem huisnummeraand = r.getElem(GBAElem.HNR_A);
          BasePLElem locatie = r.getElem(GBAElem.LOCATIEBESCHR);
          BasePLElem postcode = r.getElem(GBAElem.POSTCODE);
          BasePLElem gemeentedeel = r.getElem(GBAElem.GEM_DEEL);
          BasePLElem woonplaats = r.getElem(GBAElem.WPL_NAAM);
          BasePLElem gemeente = r.getElem(GBAElem.GEM_INSCHR);
          BasePLElem datum_aanvang = r.getElem(GBAElem.DATUM_INSCHR);
          BasePLElem emigratieland = r.getElem(GBAElem.LAND_VERTREK);
          BasePLElem emigratiedatum = r.getElem(GBAElem.DATUM_VERTREK_UIT_NL);
          BasePLElem buitenland1 = r.getElem(GBAElem.ADRES_BUITENL_1);
          BasePLElem buitenland2 = r.getElem(GBAElem.ADRES_BUITENL_2);
          BasePLElem buitenland3 = r.getElem(GBAElem.ADRES_BUITENL_3);
          BasePLElem functieAdres = r.getElem(GBAElem.FUNCTIE_ADRES);

          Adres adres = new Adres(straat, huisnummer, huisletter, huisnummertoev, huisnummeraand, locatie,
              postcode, gemeentedeel, woonplaats, gemeente, datum_aanvang, emigratieland,
              emigratiedatum, buitenland1, buitenland2, buitenland3);

          VerhuisAanvraagAdres na = impl.getHuidigAdres();

          String pc = adres.getPostcode().getValue().getDescr();
          String gemDeel = adres.getGemeentedeel().getValue().getDescr();
          String wpl = adres.getWoonplaats().getValue().getDescr();
          String loc = adres.getLocatie().getValue().getDescr();

          na.setFunctieAdres(FunctieAdres.get(functieAdres.getValue().getVal()));
          na.setStraat(new FieldValue(adres.getStraat().getValue().getVal()));
          na.setHnr(along(adres.getHuisnummer().getValue().getVal()));
          na.setHnrL(adres.getHuisletter().getValue().getVal());
          na.setHnrA(adres.getHuisnummeraand().getValue().getVal());
          na.setHnrT(adres.getHuisnummertoev().getValue().getVal());
          na.setPc(new FieldValue(pc, formatPostcode(pc)));
          na.setGemeenteDeel(new FieldValue(gemDeel));
          na.setGemeente(PLAATS.get(impl.getCGemHerkomst()));
          na.setWoonplaats(new FieldValue(wpl));
          na.setLocatie(new FieldValue(loc));
        }
      }
    }

    impl.setLocatieInvoer(copy(impl.getLocation(), Locatie.class));
    return super.setVolledigeZaakExtra(zaak);
  }

  @Override
  public ZaakContact getContact(VerhuisAanvraag zaak) {

    ZaakContact zaakContact = new ZaakContact();
    BasePLExt basisPersoon = getBasisPersoon(zaak);

    // Aangever
    if (basisPersoon != null) {
      ZaakContactpersoon persoon = new ZaakContactpersoon(AANGEVER, basisPersoon);
      persoon.setContactgegevens(getServices().getContactgegevensService().getContactgegevens(zaak));
      zaakContact.add(persoon);
    }

    // (Mee)verhuizers
    for (VerhuisPersoon vPersoon : zaak.getPersonen()) {
      if (vPersoon.getPersoon() != null) {
        String vnaam = vPersoon.getPersoon()
            .getPersoon()
            .getFormats()
            .getNaam()
            .getPred_eerstevoorn_adel_voorv_gesl();
        ZaakContactpersoon persoon = new ZaakContactpersoon(MEEVERHUIZER, vnaam);
        long bsn = vPersoon.getBurgerServiceNummer().getLongValue();
        persoon.setContactgegevens(
            getServices().getContactgegevensService().getContactgegevens(-1, bsn, "Meeverhuizer"));
        zaakContact.add(persoon);
      }
    }

    return zaakContact;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de verhuisaanvragen")
  public List<VerhuisAanvraag> getMinimalZaken(final ZaakArgumenten zaakArgumenten) {

    final List<VerhuisAanvraag> zaken = new ZakenList<>();

    for (BvhPark g : BvhParkDao.find(getArgumentenToMap(zaakArgumenten))) {

      VerhuisAanvraag aanvraag = copy(g, VerhuisAanvraag.class);

      // Nieuwe adres is nodig in zakenregister

      getNieuwAdres(aanvraag);
      getEmigratie(aanvraag);
      getHervestiging(aanvraag);

      zaken.add(aanvraag);
    }

    return zaken;
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new VerhuisAanvraag());
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public VerhuisAanvraag getStandardZaak(VerhuisAanvraag zaak) {

    VerhuisAanvraag aanvraag = to(zaak, VerhuisAanvraag.class);
    aanvraag.setBestemdVoorMidoffice(pos(aanvraag.getMidoffice()));

    getPersonen(aanvraag);
    getAangever(aanvraag);
    getToestemminggever(aanvraag);
    getHoofdbewoner(aanvraag);
    setHuidigeGemeente(aanvraag);

    return super.getStandardZaak(zaak);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return BvhParkDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  @ThrowException("Fout bij het opslaan van de verhuisaanvraag")
  @Transactional
  public void save(VerhuisAanvraag zaak) {
    save(zaak, true, "");
  }

  @ThrowException("Fout bij het opslaan van de verhuisaanvraag")
  @Transactional
  public void save(VerhuisAanvraag zaak, boolean isCompleet, String incompleetMelding) {

    ZaakStatusService zaakStatus = getZaakStatussen();

    if (isCompleet) {
      zaakStatus.setInitieleStatus(zaak);

    } else {
      String reden = "Reden: " + incompleetMelding;
      zaakStatus.setInitieleStatus(zaak, ZaakStatusType.INCOMPLEET, reden);
    }

    if (isSaved(zaak)) {
      opslaanStandaardZaak(zaak);
    } else {
      for (VerhuisPersoon p : zaak.getPersonen()) {
        zaak.setAnr(astr(p.getAnummer().getValue()));
        zaak.setBsn(p.getBurgerServiceNummer().getBigDecimalValue());
        zaak.setAangifte(p.getAangifte().getCode());
        zaak.setIndVerwerkt(toBigDecimal(zaak.getStatus().getCode()));
        zaak.setCGemHerkomst(p.getGemeenteHerkomst().getBigDecimalValue());
        zaak.setUsr(findEntity(Usr.class, along(zaak.getIngevoerdDoor().getValue())));
        zaak.setGeenVerwerking(p.isGeenVerwerking() ? 1 : 0);
        zaak.setId(null); // Null maken, zodat de volgende zaak ook wordt opgeslagen
        opslaanStandaardZaak(zaak);
      }
    }

    // create risk analysis case when applicable
    RiskAnalysisService riskAnalysisService = getServices().getRiskAnalysisService();
    riskAnalysisService.onSaveZaak(zaak);

    callListeners(ServiceEvent.CHANGE);
  }

  @Transactional
  @ThrowException("Fout bij het opslaan van de verhuisaanvraag")
  public void saveToestemming(VerhuisAanvraag zaak) {
    String opmerking = "Toestemming / aangifte is gewijzigd naar ";
    switch (zaak.getToestemminggever().getAangifteStatus()) {
      case GEACCEPTEERD:
      case GEACCEPTEERD_ZONDER_TOESTEMMING:
        opmerking += "'geaccepteerd'.";
        String parm = getParm(ParameterConstant.ZAKEN_STATUS_VERH_TOEST);
        ZaakStatusType defaultStatus = ZaakStatusType.get(NumberUtils.toLong(parm, OPGENOMEN.getCode()));
        updateStatus(zaak, zaak.getStatus(), defaultStatus, opmerking);
        break;

      case NIET_GEACCEPTEERD:
        opmerking += "'niet geaccepteerd'.";
        updateStatus(zaak, zaak.getStatus(), ZaakStatusType.GEWEIGERD, opmerking);
        break;

      case NIET_INGEVULD:
      default:
        opmerking += "'niet ingevuld'.";
        updateStatus(zaak, zaak.getStatus(), ZaakStatusType.INCOMPLEET, opmerking);
        break;
    }

    saveEntity(zaak);

    callListeners(ServiceEvent.CHANGE);
  }

  @ThrowException("Fout bij het opslaan van de verhuisaanvraag")
  @Transactional
  public void saveVerwerkingPersonen(VerhuisAanvraag zaak) {
    for (VerhuisPersoon p : zaak.getPersonen()) {
      BvhParkDao.updateVerwerking(p.getCode(), p.isGeenVerwerking() ? 1 : 0);
    }
  }

  /**
   * Status wijzigen van de verhuisaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het wijzigingen van de status")
  public void updateStatus(VerhuisAanvraag zaak, ZaakStatusType huidigeStatus, ZaakStatusType newStatus,
      String opmerking) {

    if (fil(zaak.getZaakId())) {
      super.updateStatus(zaak, huidigeStatus, newStatus, opmerking);

      ConditionalMap map = new ConditionalMap();
      map.put(BvhParkDao.ZAAK_ID, zaak.getZaakId());

      for (BvhPark e : BvhParkDao.find(map)) {
        e.setIndVerwerkt(toBigDecimal(newStatus.getCode()));
        saveEntity(e);
      }
    }

    callListeners(ServiceEvent.CHANGE);
  }

  /**
   * Verwijderen van de verhuisaanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen")
  public void delete(VerhuisAanvraag zaak) {

    if (fil(zaak.getZaakId())) {
      ConditionalMap map = new ConditionalMap(BvhParkDao.ZAAK_ID, zaak.getZaakId());
      removeEntities(BvhParkDao.find(map));
      getServices().getPresentievraagService().delete(zaak.getZaakId());
      deleteZaakRelaties(zaak);
    }

    callListeners(ServiceEvent.CHANGE);
  }

  public List<Relatie> findVerhuisRelaties(BasePLExt pl, PLEArgs args, boolean magZijnOpgeschort) {

    List<Relatie> verhuisRelaties = new ArrayList<>();
    boolean isNaamgebruik = isTru(getParm(ParameterConstant.ZOEK_PLE_NAAMGEBRUIK));

    args.setCustomTemplate(CustomTemplate.WK);
    args.setDatasource(PLEDatasource.STANDAARD);
    args.setShowHistory(false);
    args.setShowArchives(false);
    args.addCat(PERSOON, VB, INSCHR);
    args.setCat(HUW_GPS, isNaamgebruik);

    PersonenWsService gbaWs = getServices().getPersonenWsService();
    List<BasePLExt> bpls = gbaWs.getPersoonslijsten(args, false).getBasisPLWrappers();

    for (BasePLExt bpl : bpls) {

      Relatie relatie = new Relatie();
      relatie.setRelatieType(RelatieLijstHandler.getRelatieType(pl, bpl));
      relatie.setPl(bpl);

      boolean isOpgeschort = bpl.getPersoon().getStatus().isOpgeschort();
      boolean isAdres = bpl.getVerblijfplaats()
          .getAdres()
          .getAdres()
          .equals(pl.getVerblijfplaats().getAdres().getAdres());
      relatie.setHuisgenoot(isAdres);

      if (!isOpgeschort || magZijnOpgeschort) {
        verhuisRelaties.add(relatie);
      }
    }

    return verhuisRelaties;
  }

  private void getAangever(VerhuisAanvraag tm) {

    VerhuisAangever a = tm.getAangever();

    AangifteSoort aangifte = AangifteSoort.get(tm.getAangifte());

    a.setHoofdInstelling(aangifte == AangifteSoort.HOOFDINSTELLING);
    a.setAmbtshalve(aangifte == AangifteSoort.AMBTSHALVE);
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    return getAlgemeneArgumentenToMap(zaakArgumenten);
  }

  private void getEmigratie(VerhuisAanvraag tm) {

    VerhuisEmigratie e = tm.getEmigratie();

    e.setAdres1(tm.getBAdr1());
    e.setAdres2(tm.getBAdr2());
    e.setAdres3(tm.getBAdr3());
    e.setLand(LAND.get(tm.getLVertrek()));
    e.setDatumVertrek(new DateTime(tm.getDVertrek()));
    e.setDuur(tm.getDuur());
  }

  private void getHervestiging(VerhuisAanvraag tm) {

    VerhuisHerVestiging hv = tm.getHervestiging();

    hv.setLand(LAND.get(tm.getLVestiging()));
    hv.setDatumHervestiging(new DateTime(tm.getDVestiging()));
    hv.setDuur(tm.getDuur());
    hv.setRechtsfeiten(tm.getRechtsfeiten());
  }

  private void getHoofdbewoner(VerhuisAanvraag tm) {

    VerhuisHoofdbewoner t = tm.getHoofdbewoner();

    t.setBurgerServiceNummer(new BsnFieldValue(tm.getHoofdBsn()));
  }

  private void getNieuwAdres(VerhuisAanvraag tm) {

    VerhuisAanvraagAdres na = tm.getNieuwAdres();

    na.setFunctieAdres(FunctieAdres.get(tm.getFuncAdr()));

    FieldValue straat = new FieldValue(tm.getStraat());

    if (pos(tm.getCStraat())) {
      straat = STRAAT.getByKey(tm.getCStraat());
    }

    na.setStraat(straat);
    na.setHnr(along(tm.getHnr()));
    na.setHnrL(tm.getHnrL());
    na.setHnrA(tm.getHnrA());
    na.setHnrT(tm.getHnrT());
    na.setPc(new FieldValue(tm.getPc(), formatPostcode(tm.getPc())));

    na.setGemeenteDeel(new FieldValue(tm.getGemeentedeel()));
    na.setWoonplaats(new FieldValue(tm.getWoonplaats()));
    na.setGemeente(PLAATS.get(getServices().getGebruiker().getGemeenteCode()));
    na.setLocatie(new FieldValue(tm.getLocatie()));
    na.setAantalPersonen(aval(tm.getAantPers()));
  }

  private void getPersonen(VerhuisAanvraag tm) {

    tm.getPersonen().clear();

    for (Object[] nrs : BvhParkDao.findNrs(tm.getZaakId())) {

      VerhuisPersoon p = new VerhuisPersoon();
      p.setAnummer(new AnrFieldValue(astr(nrs[0])));
      p.setBurgerServiceNummer(new BsnFieldValue(astr(nrs[1])));
      p.setGemeenteHerkomst(PLAATS.get(astr(nrs[2])));
      p.setAangifte(AangifteSoort.get(astr(nrs[4])));
      p.setCode(along(nrs[5]));
      p.setGeenVerwerking(pos(nrs[6]));

      tm.getPersonen().add(p);
    }
  }

  private void getToestemminggever(VerhuisAanvraag tm) {
    VerhuisToestemminggever t = tm.getToestemminggever();
    t.setBurgerServiceNummer(new BsnFieldValue(tm.getToestBsn()));
  }

  private void setHuidigeGemeente(VerhuisAanvraag aanvraag) {
    aanvraag.getHuidigAdres().setGemeente(PLAATS.get(aanvraag.getCGemHerkomst()));
  }

  private BasePLRec getLaatsteVerblijfplaats(VerhuisAanvraag impl, VerhuisPersoon p) {
    for (BasePLSet set : p.getPersoon()
        .getBasisPl()
        .getCat(VB)
        .getSets()) {
      for (BasePLRec record : set.getRecs()) {
        if (!(record.isIncorrect() || record.isBagChange())) {
          long dGeld = record.getElemVal(GBAElem.INGANGSDAT_GELDIG).toLong();
          long dIng = impl.getDatumIngang().getLongDate();
          // Datum geldigheid nog niet ingegaan.
          if (dGeld >= dIng) {
            continue;
          }
          return record;
        }
      }
    }
    return null;
  }
}
