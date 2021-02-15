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

import static nl.procura.standard.Globalfunctions.*;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class VogAanvrager implements Serializable {

  private static final long serialVersionUID = -1119197648880110145L;

  private String     geslachtsnaam = "";
  private String     voorvoegsel   = "";
  private String     voornamen     = "";
  private FieldValue plaatsGeboren = new FieldValue();
  private FieldValue landGeboren   = new FieldValue();
  private Geslacht   geslacht      = Geslacht.ONBEKEND;
  private String     aanschrijf    = "";

  private String telThuis  = "";
  private String telWerk   = "";
  private String telMobiel = "";
  private String email     = "";

  private boolean                afwijkendAdres  = false;
  private String                 straat          = "";
  private long                   hnr             = -1;
  private String                 hnrL            = "";
  private String                 hnrT            = "";
  private FieldValue             pc              = new FieldValue();
  private String                 plaats          = "";
  private FieldValue             land            = new FieldValue();
  private DocumentPL             persoon         = null;
  private List<VogNationaliteit> nationaliteiten = new ArrayList<>();

  private VogAanvraag aanvraag;

  public VogAanvrager(VogAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  public String getAanschrijf() {
    return aanschrijf;
  }

  public void setAanschrijf(String aanschrijf) {

    this.aanschrijf = aanschrijf;
    getAanvraag().setAanschrijf(aanschrijf);
  }

  public VogAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VogAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public String getAdres() {
    return trim(MessageFormat.format("{0} {1} {2} {3}", getStraat(), getHnr(), getHnrL(), getHnrT()));
  }

  public AnrFieldValue getAnummer() {
    return new AnrFieldValue(astr(getAanvraag().getAnr()));
  }

  public void setAnummer(AnrFieldValue anr) {
    getAanvraag().setAnr(anr.getBigDecimalValue());
  }

  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(astr(getAanvraag().getBsnA()));
  }

  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    getAanvraag().setBsnA(bsn.getBigDecimalValue());
  }

  public GbaDateFieldValue getDatumGeboorte() {
    return new GbaDateFieldValue(getAanvraag().getDGebA());
  }

  public void setDatumGeboorte(GbaDateFieldValue datumGeboorte) {
    getAanvraag().setDGebA(toBigDecimal(datumGeboorte.getLongDate()));
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {

    this.email = email;
    getAanvraag().setEmailA(email);
  }

  public Geslacht getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(Geslacht geslacht) {

    this.geslacht = geslacht;
    getAanvraag().setGeslA(geslacht.getAfkorting());
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {

    this.geslachtsnaam = geslachtsnaam;
    getAanvraag().setNaamA(geslachtsnaam);
  }

  public long getHnr() {
    return hnr;
  }

  public void setHnr(long hnr) {

    this.hnr = hnr;
    getAanvraag().setHnrA(toBigDecimal(hnr));
  }

  public String getHnrL() {
    return hnrL;
  }

  public void setHnrL(String hnrL) {

    this.hnrL = hnrL;
    getAanvraag().setHnrLA(hnrL);
  }

  public String getHnrT() {
    return hnrT;
  }

  public void setHnrT(String hnrT) {

    this.hnrT = hnrT;
    getAanvraag().setHnrTA(hnrT);
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = FieldValue.from(land);
    getAanvraag().setCLandA(this.land.getBigDecimalValue());
  }

  public FieldValue getLandGeboren() {
    return landGeboren;
  }

  public void setLandGeboren(FieldValue landGeboren) {
    this.landGeboren = FieldValue.from(landGeboren);
    getAanvraag().setLGebA(this.landGeboren.getBigDecimalValue());
  }

  public List<VogNationaliteit> getNationaliteiten() {
    return nationaliteiten;
  }

  public void setNationaliteiten(List<VogNationaliteit> nationaliteiten) {
    this.nationaliteiten = nationaliteiten;
    StringBuilder sb = new StringBuilder();
    for (VogNationaliteit n : nationaliteiten) {
      sb.append(n.getCode() + ",");
    }
    getAanvraag().setNatioA(trim(sb.toString()));
  }

  public FieldValue getPc() {
    return pc;
  }

  public void setPc(FieldValue pc) {
    this.pc = FieldValue.from(pc);
    getAanvraag().setPcA(astr(this.pc.getValue()));
  }

  public DocumentPL getPersoon() {
    return persoon;
  }

  public void setPersoon(DocumentPL persoon) {
    this.persoon = persoon;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {

    this.plaats = plaats;
    getAanvraag().setPlaatsA(plaats);
  }

  public FieldValue getPlaatsGeboren() {
    return plaatsGeboren;
  }

  public void setPlaatsGeboren(FieldValue plaatsGeboren) {
    this.plaatsGeboren = FieldValue.from(plaatsGeboren);
    getAanvraag().setPGebA(astr(this.plaatsGeboren.getValue()));
  }

  public String getStraat() {
    return straat;
  }

  public void setStraat(String straat) {

    this.straat = straat;
    getAanvraag().setStraatA(straat);
  }

  public String getTelMobiel() {
    return telMobiel;
  }

  public void setTelMobiel(String telMobiel) {

    this.telMobiel = telMobiel;
    getAanvraag().setTelMobA(telMobiel);
  }

  public String getTelThuis() {
    return telThuis;
  }

  public void setTelThuis(String telThuis) {

    this.telThuis = telThuis;
    getAanvraag().setTelThuisA(telThuis);
  }

  public String getTelWerk() {
    return telWerk;
  }

  public void setTelWerk(String telWerk) {

    this.telWerk = telWerk;
    getAanvraag().setTelWerkA(telWerk);
  }

  public String getVoornamen() {
    return voornamen;
  }

  public void setVoornamen(String voornamen) {

    this.voornamen = voornamen;
    getAanvraag().setVoornA(voornamen);
  }

  public String getVoorvoegsel() {
    return voorvoegsel;
  }

  public void setVoorvoegsel(String voorvoegsel) {

    this.voorvoegsel = voorvoegsel;
    getAanvraag().setVoorvA(voorvoegsel);
  }

  public boolean isAfwijkendAdres() {
    return afwijkendAdres;
  }

  public void setAfwijkendAdres(boolean afwijkendAdres) {

    this.afwijkendAdres = afwijkendAdres;
    getAanvraag().setAdresAfwA(toBigDecimal(afwijkendAdres ? 1 : 0));
  }
}
