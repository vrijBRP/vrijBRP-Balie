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

package nl.procura.gba.web.services.zaken.vog;

import static nl.procura.gba.common.MiscUtils.*;
import static nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.DATABASE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.VogDao;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Timer;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.zaken.algemeen.AbstractZaakContactService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class VogsService extends AbstractZaakContactService<VogAanvraag>
    implements ZaakService<VogAanvraag> {

  public static final long INTEGRITEITSVERKLARING_BEROEPSVERVOER = 24;

  public VogsService() {
    super("Vog", ZaakType.COVOG);
  }

  @ThrowException("Fout bij het verwijderen van de belanghebbende")
  public void deleteBelanghebbende(VogBelanghebbende bel) {
    removeEntity(bel);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de vogaanvragen")
  public int getZakenCount(ZaakArgumenten zaakArgumenten) {
    return VogDao.findCount(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  public VogAanvraag setVolledigeZaakExtra(VogAanvraag zaak) {

    // Voeg persoonlijst toe

    VogAanvraag impl = to(zaak, VogAanvraag.class);

    if (impl.getAanvrager().getPersoon() == null) {

      DocumentPL dmps = new DocumentPL(
          findPL(impl.getAnummer(), impl.getBurgerServiceNummer()));

      to(impl.getAanvrager(), VogAanvrager.class).setPersoon(dmps);
    }

    impl.setLocatieInvoer(copy(impl.getLocation(), Locatie.class));

    return super.setVolledigeZaakExtra(zaak);
  }

  @ThrowException("Fout bij het opvragen van de vog belanghebbende")
  public List<VogBelanghebbende> getBelangHebbenden() {

    List<VogBelanghebbende> l = new ArrayList<>();
    for (VogBelang b : VogDao.findBelanghebbenden()) {
      VogBelanghebbende bel = copy(b, VogBelanghebbende.class);
      bel.setPc(new FieldValue(b.getPcB(), b.getPcB()));
      bel.setLand(GbaTables.LAND.get(b.getCLandB()));
      l.add(bel);
    }

    return l;
  }

  @Override
  public ZaakContact getContact(VogAanvraag zaak) {

    ZaakContact zaakContact = new ZaakContact();
    BasePLExt basisPersoon = getBasisPersoon(zaak);

    if (basisPersoon != null) {
      ZaakContactpersoon persoon = new ZaakContactpersoon(AANGEVER, basisPersoon);
      persoon.setContactgegevens(getServices().getContactgegevensService().getContactgegevens(zaak));
      zaakContact.add(persoon);
    }

    return zaakContact;
  }

  public List<VogDoel> getDoelen() {

    List<VogDoel> l = new ArrayList<>();

    try {
      for (VogDoelTab b : VogDao.findVogDoelen()) {
        VogDoel vd = copy(b, VogDoel.class);
        if (vd.isActueel()) {
          l.add(vd);
        }
      }
    } catch (Exception e) {
      throw new ProException(DATABASE, ERROR, "Fout bij het opvragen van de vog doelen.", e);
    }

    return l;
  }

  public List<VogFunctie> getFuncties() {

    List<VogFunctie> l = new ArrayList<>();

    try {
      for (VogFuncTab b : VogDao.findVogFuncties()) {
        VogFunctie vd = copy(b, VogFunctie.class);
        if (vd.isActueel()) {
          l.add(vd);
        }
      }
    } catch (Exception e) {
      throw new ProException(DATABASE, ERROR, "Fout bij het opvragen van de vog functies.", e);
    }

    Collections.sort(l);

    return l;
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van de vogaanvragen")
  public List<VogAanvraag> getMinimalZaken(ZaakArgumenten zaakArgumenten) {
    return new ArrayList<>(copyList(VogDao.find(getArgumentenToMap(zaakArgumenten)), VogAanvraag.class));
  }

  @Override
  public Zaak getNewZaak() {
    return aanvullenZaak(new VogAanvraag());
  }

  public VogProfiel getProfiel(long codeVogProfiel) {
    for (VogProfiel vp : getProfielen()) {
      if (along(vp.getVogProfTab()) == codeVogProfiel) {
        return vp;
      }
    }

    return null;
  }

  public List<VogProfiel> getProfielen() {

    List<VogProfiel> l = new ArrayList<>();

    try {
      for (VogProfTab b : VogDao.findVogProfielen()) {
        VogProfiel vd = copy(b, VogProfiel.class);
        if (vd.isActueel()) {
          l.add(vd);
        }
      }
    } catch (Exception e) {
      throw new ProException(DATABASE, ERROR, "Fout bij het opvragen van de vog profielen.", e);
    }

    Collections.sort(l);

    return l;
  }

  @Override
  @ThrowException("Het is niet mogelijk om de zaak op te vragen")
  public VogAanvraag getStandardZaak(VogAanvraag zaak) {

    VogAanvraag aanvraag = to(zaak, VogAanvraag.class);

    aanvraag.getVogNummer().setCodeGemeente(along(aanvraag.getCGem()));
    aanvraag.getVogNummer().setCodeLocatie(along(aanvraag.getCLoc()));
    aanvraag.getVogNummer().setDatumAanvraag(along(aanvraag.getDAanvr()));
    aanvraag.getVogNummer().setVolgnummer(along(aanvraag.getVAanvr()));

    aanvraag.setAanvrager(getAanvrager(aanvraag));
    aanvraag.setBelanghebbende(getBelanghebbende(aanvraag));
    aanvraag.setDoel(getDoel(aanvraag));
    aanvraag.setScreening(getScreening(aanvraag));
    aanvraag.setOpmerkingen(getOpmerkingen(aanvraag));

    return super.getStandardZaak(zaak);
  }

  @Override
  @Timer
  @ThrowException("Fout bij het zoeken van zaak-ids")
  public List<ZaakKey> getZaakKeys(ZaakArgumenten zaakArgumenten) {
    return VogDao.findZaakKeys(getArgumentenToMap(zaakArgumenten));
  }

  @Override
  @Transactional
  @ThrowException("Fout bij het opslaan van de vog aanvraag")
  public void save(VogAanvraag zaak) {

    getZaakStatussen().setInitieleStatus(zaak, ZaakStatusType.INCOMPLEET);

    setVogNummer(zaak);

    VogAanvr a = zaak;

    if (zaak.getDoel().getDoel().getCVogDoelTab() != null) {
      a.setVogDoelTab(findEntity(VogDoelTab.class, zaak.getDoel().getDoel().getCVogDoelTab()));
    }

    if (zaak.getScreening().getProfiel() != null) {
      a.setVogProfTab(findEntity(VogProfTab.class, zaak.getScreening().getProfiel().getCVogProfTab()));
    } else {
      a.setVogProfTab(null);
    }

    refreshVogFuncties(zaak);

    opslaanStandaardZaak(zaak);

    refreshHist(zaak);

    callListeners(ServiceEvent.CHANGE);
  }

  @ThrowException("Fout bij het opslaan van de belanghebbende")
  public void saveBelanghebbende(VogBelanghebbende bel) {
    saveEntity(bel);
  }

  public void setVogNummer(VogAanvraag a) {

    VogNummer nr = a.getVogNummer();

    if ((aval(nr.getDatumAanvraag()) == aval(getSystemDate())) && pos(nr.getVolgnummer())) {
      return;
    }

    nr.setDatumAanvraag(along(getSystemDate()));
    nr.setCodeLocatie(getServices().getGebruiker().getLocatie().getCLocation());
    nr.setCodeGemeente(along(getServices().getGebruiker().getGemeenteCode()));
    a.setIngevoerdDoor(new UsrFieldValue(getServices().getGebruiker()));

    int volgNr = 5000;

    ParameterService db = getServices().getParameterService();

    String volgId = db.getSysteemParameter(ParameterConstant.COVOG_VOLGNR).getValue();

    if (fil(volgId) && (volgId.split("_").length == 2)) {
      int cur_d = aval(volgId.split("_")[0]);
      int cur_nr = aval(volgId.split("_")[1]);

      if ((aval(getSystemDate()) == cur_d)) {
        volgNr = ((cur_nr >= 5000) ? cur_nr : 5000) + 1;
      }
    }

    db.setSysteemParameter(ParameterConstant.COVOG_VOLGNR, getSystemDate() + "_" + volgNr);

    nr.setVolgnummer(volgNr);

    a.setVogNummer(nr);
  }

  /**
   * Verwijderen van de VOG-aanvraag
   */
  @Override
  @Transactional
  @ThrowException("Fout bij het verwijderen van de vog aanvraag")
  public void delete(VogAanvraag zaak) {

    if (pos(zaak.getCVogAanvr())) {

      ConditionalMap map = new ConditionalMap();
      map.put(VogDao.C_VOG_AANVR, zaak.getCVogAanvr());
      map.put(VogDao.MAX_CORRECT_RESULTS, 1);

      removeEntities(VogDao.find(map));

      deleteZaakRelaties(zaak);

      callListeners(ServiceEvent.CHANGE);
    }
  }

  private VogAanvrager getAanvrager(VogAanvraag tm) {

    VogAanvrager a = new VogAanvrager(tm);

    a.setAnummer(new AnrFieldValue(astr(tm.getAnr())));
    a.setBurgerServiceNummer(new BsnFieldValue(astr(tm.getBsnA())));
    a.setGeslachtsnaam(tm.getNaamA());
    a.setVoorvoegsel(tm.getVoorvA());
    a.setVoornamen(tm.getVoornA());
    a.setDatumGeboorte(new GbaDateFieldValue(tm.getDGebA()));

    FieldValue gebPlaats = new FieldValue(tm.getPGebA(), tm.getPGebA());

    if (pos(tm.getPGebA())) {
      gebPlaats = GbaTables.PLAATS.get(tm.getPGebA());
    }

    a.setPlaatsGeboren(gebPlaats);
    a.setLandGeboren(GbaTables.LAND.get(tm.getLGebA()));
    a.setGeslacht(Geslacht.get(tm.getGeslA()));
    a.setNationaliteiten(getNationaliteiten(tm.getNatioA()));

    a.setAanschrijf(tm.getAanschrijf());
    a.setStraat(tm.getStraatA());
    a.setHnr(along(tm.getHnrA()));
    a.setHnrL(tm.getHnrLA());
    a.setHnrT(tm.getHnrTA());
    a.setPc(new FieldValue(tm.getPcA(), tm.getPcA()));
    a.setPlaats(tm.getPlaatsA());
    a.setLand(GbaTables.LAND.get(tm.getCLandA()));
    a.setAfwijkendAdres(pos(tm.getAdresAfwA()));

    a.setTelThuis(tm.getTelThuisA());
    a.setTelMobiel(tm.getTelMobA());
    a.setTelWerk(tm.getTelWerkA());
    a.setEmail(tm.getEmailA());

    return a;
  }

  private ConditionalMap getArgumentenToMap(ZaakArgumenten zaakArgumenten) {
    return getAlgemeneArgumentenToMap(zaakArgumenten);
  }

  private VogAanvraagBelanghebbende getBelanghebbende(VogAanvraag tm) {

    VogAanvraagBelanghebbende b = new VogAanvraagBelanghebbende(tm);

    b.setNaam(tm.getInstellingB());
    b.setVertegenwoordiger(tm.getNaamB());
    b.setStraat(tm.getStraatB());
    b.setHnr(along(tm.getHnrB()));
    b.setHnrL(tm.getHnrLB());
    b.setHnrT(tm.getHnrTB());
    b.setPc(new FieldValue(tm.getPcB(), tm.getPcB()));
    b.setPlaats(tm.getPlaatsB());
    b.setLand(GbaTables.LAND.get(tm.getCLandB()));
    b.setTel(tm.getTelB());

    return b;
  }

  private VogAanvraagDoel getDoel(VogAanvraag tm) {

    VogAanvraagDoel d = new VogAanvraagDoel(tm);

    if (tm.getVogDoelTab() != null) {
      d.setDoel(copy(tm.getVogDoelTab(), VogDoel.class));
    }

    d.setFunctie(tm.getDoelFunc());
    d.setDoelTekst(tm.getDoelTekst());

    return d;
  }

  private List<VogNationaliteit> getNationaliteiten(String natioString) {

    List<VogNationaliteit> l = new ArrayList<>();

    for (String s : natioString.split(",")) {
      if (fil(s)) {
        String c_natio = s.trim();
        String s_natio = GbaTables.NATIO.get(c_natio).getDescription();
        l.add(new VogNationaliteit(c_natio, s_natio));
      }
    }

    return l;
  }

  private VogAanvraagOpmerkingen getOpmerkingen(VogAanvraag tm) {

    VogAanvraagOpmerkingen o = new VogAanvraagOpmerkingen(tm);

    o.setBurgemeesterAdvies(BurgemeesterAdvies.get(tm.getBurgAdvies()));
    o.setByzonderhedenTekst(tm.getBijzonderTekst());
    o.setCovogAdviesTekst(tm.getVogAdviesTekst());
    o.setPersisterenTekst(tm.getVogPersistTekst());
    o.setToelichtingTekst(tm.getToelichtingTekst());

    return o;
  }

  private VogAanvraagScreening getScreening(VogAanvraag tm) {

    VogAanvraagScreening i = new VogAanvraagScreening(tm);

    if (tm.getVogProfTab() != null) {
      i.setProfiel(copy(tm.getVogProfTab(), VogProfiel.class));
    }

    for (VogFuncTab f : tm.getVogFuncTabs()) {
      i.getFunctiegebieden().add(copy(f, VogFunctie.class));
    }

    i.setOmstandighedenTekst(tm.getOmstandigTekst());

    return i;
  }

  private void refreshHist(VogAanvraag aanvraag) {

    int i = 0;

    for (VogAanvragerHistorie h : aanvraag.getHistorie()) {

      i++;
      h.setVolgCode(i);
      h.setCVogAanvr(aanvraag.getCVogAanvr());

      saveEntity(h);
    }
  }

  private void refreshVogFuncties(VogAanvraag aanvraag) {

    VogAanvraag aan = aanvraag;
    aan.getVogFuncTabs().clear();

    for (VogFunctie vogFunctie : aanvraag.getScreening().getFunctiegebieden()) {
      VogFuncTab vogFunctieTab = findEntity(VogFuncTab.class, vogFunctie.getCVogFuncTab());
      aan.getVogFuncTabs().add(vogFunctieTab);
    }
  }
}
