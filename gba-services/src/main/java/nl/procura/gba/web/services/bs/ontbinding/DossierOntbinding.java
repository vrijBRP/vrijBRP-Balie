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

package nl.procura.gba.web.services.bs.ontbinding;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER1;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER2;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.DossOntb;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierLatereVermelding;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierMetAkte;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNaamgebruik;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.ontbinding.LatereVermeldingOntbinding;
import nl.procura.gba.web.services.bs.lv.ontbinding.LvOntbinding;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierOntbinding extends DossOntb
    implements ZaakDossier, DossierLatereVermelding, DossierNaamgebruik, DossierMetAkte {

  private final Advocatenkantoor advocatenkantoor    = new Advocatenkantoor(this);
  private Dossier                dossier             = new Dossier(ZaakType.OMZETTING_GPS);
  private FieldValue             ontbindingsGemeente = new FieldValue();
  private FieldValue             plaatsVb            = new FieldValue();
  private FieldValue             landVb              = new FieldValue();
  private FieldValue             aktePlaatsVb        = new FieldValue();
  private FieldValue             titelPartner1       = new FieldValue();
  private FieldValue             titelPartner2       = new FieldValue();

  public DossierOntbinding() {

    super();

    setDossier(new Dossier(ZaakType.ONTBINDING_GEMEENTE, this));
    getDossier().toevoegenPersoon(PARTNER1);
    getDossier().toevoegenPersoon(PARTNER2);
  }

  @Override
  public void beforeSave() {
    setCDossOntb(getDossier().getCode());
  }

  public Advocatenkantoor getAdvocatenkantoor() {
    return advocatenkantoor;
  }

  @Override
  public String getAkteAanduiding() {
    return ""; // Niet van toepassing
  }

  @Override
  public DateTime getAkteDatum() {
    return null; // Niet van toepassing
  }

  public int getAkteJaarVerbintenis() {
    return getAkteJaar().intValue();
  }

  public void setAkteJaarVerbintenis(int waarde) {
    setAkteJaar(toBigDecimal(waarde));
  }

  @Override
  public List<DossierPersoon> getAktePersonen() {
    final List<DossierPersoon> personen = new ArrayList<>();
    personen.add(getPartner1());
    personen.add(getPartner2());
    return personen;
  }

  public FieldValue getAktePlaatsVerbintenis() {
    return aktePlaatsVb;
  }

  public void setAktePlaatsVerbintenis(FieldValue waarde) {
    this.aktePlaatsVb = FieldValue.from(waarde);
    setAktePlaats(aktePlaatsVb.getStringValue());
  }

  @Override
  public AnrFieldValue getAnummer() {
    return getPartner1().getAnummer();
  }

  @Override
  public void setAnummer(AnrFieldValue anummer) {
    getPartner1().setAnummer(anummer);
  }

  public String getBrpAkteNummerVerbintenis() {
    return getBrpAkteNr();
  }

  public void setBrpAkteNummerVerbintenis(String aktenummer) {
    setBrpAkteNr(aktenummer);
  }

  public String getBsAkteNummerVerbintenis() {
    return getAkteNr();
  }

  public void setBsAkteNummerVerbintenis(String aktenummer) {
    setAkteNr(aktenummer);
  }

  @Override
  public BsnFieldValue getBurgerServiceNummer() {
    return getPartner1().getBurgerServiceNummer();
  }

  @Override
  public void setBurgerServiceNummer(BsnFieldValue burgerServiceNummer) {
    getPartner1().setBurgerServiceNummer(burgerServiceNummer);
  }

  public Long getCode() {
    return getCDossOntb();
  }

  public DateTime getDatumGewijsde() {
    return new DateTime(getdGewijsde());
  }

  public void setDatumGewijsde(DateTime dateTime) {
    setdGewijsde(BigDecimal.valueOf(dateTime.getLongDate()));
  }

  public DateTime getDatumIngang() {
    return new DateTime(getdIn());
  }

  public void setDatumIngang(DateTime dateTime) {
    setdIn(BigDecimal.valueOf(dateTime.getLongDate()));
    if (dateTime.getLongDate() > 0) {
      getDossier().setDatumIngang(dateTime);
    }
  }

  public DateTime getDatumOndertekening() {
    return new DateTime(getdOndert());
  }

  public void setDatumOndertekening(DateTime dateTime) {
    setdOndert(BigDecimal.valueOf(dateTime.getLongDate()));
  }

  public DateTime getDatumUitspraak() {
    return new DateTime(getdUitspraak());
  }

  public void setDatumUitspraak(DateTime dateTime) {
    setdUitspraak(BigDecimal.valueOf(dateTime.getLongDate()));
  }

  public DateTime getDatumVerbintenis() {
    return new DateTime(getdVb());
  }

  public void setDatumVerbintenis(DateTime dateTime) {
    setdVb(BigDecimal.valueOf(dateTime.getLongDate()));
  }

  public DateTime getDatumVerklaring() {
    return new DateTime(getdVerklaring());
  }

  public void setDatumVerklaring(DateTime dateTime) {
    setdVerklaring(BigDecimal.valueOf(dateTime.getLongDate()));
  }

  public DateTime getDatumVerzoek() {
    return new DateTime(getdVerzoek());
  }

  public void setDatumVerzoek(DateTime dateTime) {
    setdVerzoek(BigDecimal.valueOf(dateTime.getLongDate()));
  }

  @Override
  public Dossier getDossier() {
    return dossier;
  }

  @Override
  public void setDossier(Dossier dossier) {
    this.dossier = dossier;
  }

  /*
   *  public DossierPersoon getAmbtenaar1 () { return getDossier
   * ().getPersoon (0, AMBTENAAR); }
   */

  /*
   *  public FieldValue getHuwelijksGemeente () { return
   * huwelijksGemeente; }
   *
   *  public void setHuwelijksGemeente (FieldValue huwelijksGemeente)
   * { this.huwelijksGemeente = FieldValue.from (huwelijksGemeente);
   * setcHuwGem (this.huwelijksGemeente.getBigDecimalValue ()); }
   */

  public FieldValue getLandVerbintenis() {
    return landVb;
  }

  public void setLandVerbintenis(FieldValue waarde) {
    this.landVb = FieldValue.from(waarde);
    setLandVb(landVb.getBigDecimalValue());
  }

  @Override
  public LatereVermelding<LvOntbinding> getLatereVermelding() {
    return new LatereVermeldingOntbinding(this);
  }

  @Override
  public String getNaamGebruikPartner1() {
    return getP1Ng();
  }

  @Override
  public void setNaamGebruikPartner1(String ng) {
    setP1Ng(ng);
  }

  @Override
  public String getNaamGebruikPartner2() {
    return getP2Ng();
  }

  @Override
  public void setNaamGebruikPartner2(String ng) {
    setP2Ng(ng);
  }

  @Override
  public String getNaamPartner1() {
    return getP1Naam();
  }

  @Override
  public void setNaamPartner1(String naam) {
    setP1Naam(naam);
  }

  @Override
  public String getNaamPartner2() {
    return getP2Naam();
  }

  @Override
  public void setNaamPartner2(String naam) {
    setP2Naam(naam);
  }

  public String getOndertekendDoor() {
    return getOndertDoor();
  }

  public void setOndertekendDoor(String waarde) {
    setOndertDoor(waarde);
  }

  public FieldValue getOntbindingsGemeente() {
    return ontbindingsGemeente;
  }

  public void setOntbindingsGemeente(FieldValue ontbindingsGemeente) {
    this.ontbindingsGemeente = FieldValue.from(ontbindingsGemeente);
    setcOntbGem(this.ontbindingsGemeente.getBigDecimalValue());
  }

  @Override
  public DossierPersoon getPartner1() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(DossierPersoonType.PARTNER1));
  }

  @Override
  public DossierPersoon getPartner2() {
    return getDossier().getPersoon(DossierPersoonFilter.filter(DossierPersoonType.PARTNER2));
  }

  @Override
  public List<DossierPersoon> getPartners() {
    return asList(getPartner1(), getPartner2());
  }

  public FieldValue getPlaatsVerbintenis() {
    return plaatsVb;
  }

  public void setPlaatsVerbintenis(FieldValue waarde) {
    this.plaatsVb = FieldValue.from(waarde);
    setPlaatsVb(plaatsVb.getStringValue());
  }

  public SoortVerbintenis getSoortVerbintenis() {
    return SoortVerbintenis.get(getSoortVb());
  }

  public void setSoortVerbintenis(SoortVerbintenis soort) {
    setSoortVb(soort.getCode());
  }

  @Override
  public FieldValue getTitelPartner1() {
    return titelPartner1;
  }

  @Override
  public void setTitelPartner1(FieldValue titel) {
    this.titelPartner1 = FieldValue.from(titel);
    setP1Titel(titel.getStringValue());
  }

  @Override
  public FieldValue getTitelPartner2() {
    return titelPartner2;
  }

  @Override
  public void setTitelPartner2(FieldValue titel) {
    this.titelPartner2 = FieldValue.from(titel);
    setP2Titel(titel.getStringValue());
  }

  public RechtbankLocatie getUitspraakDoor() {
    return RechtbankLocatie.get(getUitspraak());
  }

  public void setUitspraakDoor(RechtbankLocatie waarde) {
    setUitspraak(waarde.getCode());
  }

  public FieldValue getVerzoekTotInschrijvingDoor() {
    return new FieldValue(getVerzoekDoor(), getVerzoekDoorOms());
  }

  public void setVerzoekTotInschrijvingDoor(FieldValue waarde) {
    setVerzoekDoor(waarde.getStringValue());
    setVerzoekDoorOms(waarde.getDescription());
  }

  @Override
  public String getVoorvPartner1() {
    return getP1Voorv();
  }

  @Override
  public void setVoorvPartner1(String voorv) {
    setP1Voorv(voorv);
  }

  @Override
  public String getVoorvPartner2() {
    return getP2Voorv();
  }

  @Override
  public void setVoorvPartner2(String voorv) {
    setP2Voorv(voorv);
  }

  public WijzeBeeindigingVerbintenis getWijzeBeeindigingVerbintenis() {
    return WijzeBeeindigingVerbintenis.get(getSoortEindeVb());
  }

  public void setWijzeBeeindigingVerbintenis(WijzeBeeindigingVerbintenis wijze) {
    setSoortEindeVb(wijze.getCode());
  }

  @Override
  public boolean isAdelPartner1() {
    return BsDossierNaamgebruikUtils.isAdel(titelPartner1);
  }

  @Override
  public boolean isAdelPartner2() {
    return BsDossierNaamgebruikUtils.isAdel(titelPartner2);
  }

  public boolean isBinnenTermijn() {
    return pos(getBinnenTerm());
  }

  public void setBinnenTermijn(boolean binnenTermijn) {
    setBinnenTerm(toBigDecimal(binnenTermijn ? 1 : 0));
  }

  @Override
  public boolean isPredikaatPartner1() {
    return BsDossierNaamgebruikUtils.isPredikaat(titelPartner1);
  }

  @Override
  public boolean isPredikaatPartner2() {
    return BsDossierNaamgebruikUtils.isPredikaat(titelPartner2);
  }

  @Override
  public boolean isSprakeLatereVermelding() {
    return true;
  }

  @Override
  public boolean isVolledig() {

    // if (getDatumVerbintenis ().getLongDate () <= 0 || getTijdVerbintenis
    // ().getLongTime () <= 0) {
    // throw new ProException (ProExceptionSeverity.WARNING, "De datum en/of
    // het tijdstip van de verbintenis is niet gevuld");
    // }
    //
    // if (getHuwelijksLocatie () == null || !pos (getHuwelijksLocatie
    // ().getCodeHuwelijksLocatie ())) {
    // throw new ProException (ProExceptionSeverity.WARNING, "De
    // huwelijkslocatie is niet gevuld");
    // }
    //
    // if (getStatusVerbintenis () != StatusVerbintenis.DEFINITIEF) {
    // throw new ProException (ProExceptionSeverity.WARNING, "De status van
    // het dossier is niet definitief");
    // }
    //
    // for (final DossierVereiste v : getDossier ().getVereisten ()) {
    // if (!v.isHeeftVoldaan ()) {
    // throw new ProException (ProExceptionSeverity.WARNING, "Er is niet
    // voldaan aan alle vereisten.");
    // }
    // }

    return true;
  }
}
