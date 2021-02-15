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

package nl.procura.gba.web.services.bs.lv.erkenning;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAktePersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType;
import nl.procura.gba.web.services.bs.lv.Lv;
import nl.procura.gba.web.services.bs.lv.LvRechtsfeitAkte;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class LvErkenning implements Lv {

  private final LvRechtsfeitAkte      rechtsfeitAkte       = new LvRechtsfeitAkte();
  private FieldValue                  gemeente             = new FieldValue();
  private FieldValue                  landErkenning        = new FieldValue();
  private String                      buitenlandsePlaats   = "";
  private DossierAkte                 akte                 = new DossierAkte();
  private DossierPersoon              moeder               = new DossierPersoon();
  private DossierPersoon              erkenner             = new DossierPersoon();
  private DossierPersoon              partner1             = new DossierPersoon();
  private DossierPersoon              partner2             = new DossierPersoon();
  private String                      rechtbank            = "";
  private FieldValue                  afstammingsrecht     = new FieldValue();
  private FieldValue                  titel                = new FieldValue();
  private String                      voorvoegsel          = "";
  private String                      geslachtsnaam        = "";
  private ErkenningsType              erkenningsType       = ErkenningsType.ONBEKEND;
  private ToestemminggeverType        toestemminggeverType = ToestemminggeverType.ONBEKEND;
  private NaamskeuzeVanToepassingType naamskeuzeType       = NaamskeuzeVanToepassingType.ONBEKEND;
  private DateTime                    datum                = null;
  private DossierAktePersoon          kind                 = null;

  public FieldValue getAfstammingsrecht() {
    return afstammingsrecht;
  }

  public void setAfstammingsrecht(FieldValue afstammingsrecht) {
    this.afstammingsrecht = afstammingsrecht;
  }

  public DossierAkte getAkte() {
    return akte;
  }

  public void setAkte(DossierAkte akte) {
    this.akte = akte;
  }

  public String getBuitenlandsePlaats() {
    return buitenlandsePlaats;
  }

  public void setBuitenlandsePlaats(String buitenlandsePlaats) {
    this.buitenlandsePlaats = buitenlandsePlaats;
  }

  public DateTime getDatum() {
    return datum;
  }

  public void setDatum(DateTime datum) {
    this.datum = datum;
  }

  public DossierPersoon getErkenner() {
    return erkenner;
  }

  public void setErkenner(DossierPersoon erkenner) {
    this.erkenner = erkenner;
  }

  public ErkenningsType getErkenningsType() {
    return erkenningsType;
  }

  public void setErkenningsType(ErkenningsType erkenningsType) {
    this.erkenningsType = erkenningsType;
  }

  public LvRechtsfeitAkte getGeboorteAkte() {
    return rechtsfeitAkte;
  }

  public FieldValue getGemeente() {
    return gemeente;
  }

  public void setGemeente(FieldValue gemeente) {
    this.gemeente = gemeente;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public DossierAktePersoon getKind() {
    return kind;
  }

  public void setKind(DossierAktePersoon kind) {
    this.kind = kind;
  }

  public FieldValue getLandErkenning() {
    return landErkenning;
  }

  public void setLandErkenning(FieldValue landErkenning) {
    this.landErkenning = landErkenning;
  }

  public DossierPersoon getMoeder() {
    return moeder;
  }

  public void setMoeder(DossierPersoon moeder) {
    this.moeder = moeder;
  }

  public String getNaam() {
    return trim(getTitel() + " " + getVoorvoegsel() + " " + getGeslachtsnaam());
  }

  public NaamskeuzeVanToepassingType getNaamskeuzeType() {
    return naamskeuzeType;
  }

  public void setNaamskeuzeType(NaamskeuzeVanToepassingType naamskeuzeType) {
    this.naamskeuzeType = naamskeuzeType;
  }

  public DossierPersoon getPartner1() {
    return partner1;
  }

  public void setPartner1(DossierPersoon partner1) {
    this.partner1 = partner1;
  }

  public DossierPersoon getPartner2() {
    return partner2;
  }

  public void setPartner2(DossierPersoon partner2) {
    this.partner2 = partner2;
  }

  public String getPlaatsErkenning() {

    if (Landelijk.isNederland(getLandErkenning())) {
      return astr(getGemeente());
    }

    return trim(getBuitenlandsePlaats() + ", " + getLandErkenning());
  }

  public String getRechtbank() {
    return rechtbank;
  }

  public void setRechtbank(String rechtbank) {
    this.rechtbank = astr(rechtbank);
  }

  public FieldValue getTitel() {
    return titel;
  }

  public void setTitel(FieldValue titel) {
    this.titel = titel;
  }

  public String getToestemming() {
    return getToestemminggeverType().getOms() + " " + astr(getRechtbank());
  }

  public ToestemminggeverType getToestemminggeverType() {
    return toestemminggeverType;
  }

  public void setToestemminggeverType(ToestemminggeverType toestemminggeverType) {
    this.toestemminggeverType = toestemminggeverType;
  }

  public String getVoorvoegsel() {
    return voorvoegsel;
  }

  public void setVoorvoegsel(String voorvoegsel) {
    this.voorvoegsel = voorvoegsel;
  }

  public boolean isNaamskeuze() {
    return NaamskeuzeVanToepassingType.JA.equals(getNaamskeuzeType());
  }
}
