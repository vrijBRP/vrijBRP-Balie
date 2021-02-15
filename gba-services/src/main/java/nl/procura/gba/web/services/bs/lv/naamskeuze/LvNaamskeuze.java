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

package nl.procura.gba.web.services.bs.lv.naamskeuze;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAktePersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.lv.Lv;
import nl.procura.gba.web.services.bs.lv.LvRechtsfeitAkte;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class LvNaamskeuze implements Lv {

  private final LvRechtsfeitAkte rechtsfeitAkte     = new LvRechtsfeitAkte();
  private FieldValue             gemeente           = new FieldValue();
  private FieldValue             land               = new FieldValue();
  private String                 buitenlandsePlaats = "";
  private DossierAkte            akte               = new DossierAkte();
  private DossierPersoon         moeder             = new DossierPersoon();
  private DossierPersoon         partner            = new DossierPersoon();
  private FieldValue             titel              = new FieldValue();
  private String                 voorvoegsel        = "";
  private String                 geslachtsnaam      = "";
  private NaamskeuzeType         naamskeuzeType     = NaamskeuzeType.ONBEKEND;
  private DateTime               datum              = null;
  private DossierAktePersoon     kind               = null;

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

  public DossierPersoon getPartner() {
    return partner;
  }

  public void setPartner(DossierPersoon partner) {
    this.partner = partner;
  }

  public NaamskeuzeType getNaamskeuzeType() {
    return naamskeuzeType;
  }

  public void setNaamskeuzeType(NaamskeuzeType naamskeuzeType) {
    this.naamskeuzeType = naamskeuzeType;
  }

  public boolean isBestaandKind() {
    return getNaamskeuzeType().is(NaamskeuzeType.NAAMSKEUZE_BESTAAND_KIND);
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

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = land;
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

  public String getPlaatsNaamskeuze() {
    if (Landelijk.isNederland(getLand())) {
      return astr(getGemeente());
    }

    return trim(getBuitenlandsePlaats() + ", " + getLand());
  }

  public FieldValue getTitel() {
    return titel;
  }

  public void setTitel(FieldValue titel) {
    this.titel = titel;
  }

  public String getVoorvoegsel() {
    return voorvoegsel;
  }

  public void setVoorvoegsel(String voorvoegsel) {
    this.voorvoegsel = voorvoegsel;
  }
}
