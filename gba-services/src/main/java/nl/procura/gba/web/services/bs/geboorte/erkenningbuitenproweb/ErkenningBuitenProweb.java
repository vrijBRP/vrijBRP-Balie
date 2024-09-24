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

package nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb;

import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.standard.Globalfunctions.trim;

import java.math.BigDecimal;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class ErkenningBuitenProweb {

  private final DossierGeboorte dg;
  private FieldValue            gemeente       = new FieldValue();
  private FieldValue            landAfstamming = new FieldValue();
  private FieldValue            landErkenning  = new FieldValue();

  public ErkenningBuitenProweb(DossierGeboorte dossierGeboorte) {
    this.dg = dossierGeboorte;
  }

  public String getAktenummer() {
    return dg.getAkteErkenning();
  }

  public void setAktenummer(String aktenummer) {
    dg.setAkteErkenning(aktenummer);
  }

  public String getBuitenlandsePlaats() {
    return dg.getPlaatsErk();
  }

  public void setBuitenlandsePlaats(String plaats) {
    dg.setPlaatsErk(plaats);
  }

  public DateTime getDatumErkenning() {
    return new DateTime(dg.getdInErkenning());
  }

  public void setDatumErkenning(DateTime dt) {
    dg.setdInErkenning(toBigDecimal(dt != null ? dt.getLongDate() : 0));
  }

  public FieldValue getGemeente() {
    return gemeente;
  }

  public void setGemeente(FieldValue gemeente) {
    this.gemeente = FieldValue.from(gemeente);
    dg.setcGemErk(this.gemeente.getBigDecimalValue());
  }

  public FieldValue getLandAfstamming() {
    return landAfstamming;
  }

  public void setLandAfstamming(FieldValue land) {
    this.landAfstamming = FieldValue.from(land);
    dg.setcLandAfstamRechtErk(this.landAfstamming.getBigDecimalValue());
  }

  public FieldValue getLandErkenning() {
    return landErkenning;
  }

  public void setLandErkenning(FieldValue land) {
    this.landErkenning = FieldValue.from(land);
    dg.setcLandErk(this.landErkenning.getBigDecimalValue());
  }

  public NaamsPersoonType getNaamskeuzePersoon() {

    if (NaamskeuzeVanToepassingType.JA.equals(getNaamskeuzeType())) {
      return NaamsPersoonType.ERKENNER;
    }

    return NaamsPersoonType.get(dg.gettNaamskeuzeErkenning());
  }

  public void setNaamskeuzePersoon(NaamsPersoonType type) {
    dg.settNaamskeuzeErkenning(toBigDecimal(type.getCode()));
  }

  public NaamskeuzeVanToepassingType getNaamskeuzeType() {
    return NaamskeuzeVanToepassingType.get(dg.getbNaamskeuzeErkenning());
  }

  public void setNaamskeuzeType(NaamskeuzeVanToepassingType type) {
    dg.setbNaamskeuzeErkenning(toBigDecimal(type.getCode()));
  }

  public String getRechtbank() {
    return dg.getRechtbankErk();
  }

  public void setRechtbank(String rechtbank) {
    dg.setRechtbankErk(rechtbank);
  }

  public ToestemminggeverType getToestemminggeverType() {
    return ToestemminggeverType.get(dg.getToestIdErkenning());
  }

  public void setToestemminggeverType(ToestemminggeverType toestemminggever) {
    dg.setToestIdErkenning(
        toestemminggever != null ? toestemminggever.getCode() : ToestemminggeverType.ONBEKEND.getCode());
  }

  public boolean isVerklaringGezag() {
    return dg.getVerklaringGezagErk().intValue() > 0;
  }

  public void setVerklaringGezag(boolean verklaringGezag) {
    dg.setVerklaringGezagErk(BigDecimal.valueOf(verklaringGezag ? 1L : 0L));
  }

  public String getGeslachtsnaam() {
    return dg.getKeuzeNaamGesl();
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    dg.setKeuzeNaamGesl(geslachtsnaam);
  }

  public String getVoorvoegsel() {
    return dg.getKeuzeVoorvoegsel();
  }

  public void setVoorvoegsel(FieldValue voorvoegsel) {
    dg.setKeuzeVoorvoegsel(voorvoegsel.getStringValue());
  }

  public FieldValue getTitel() {
    return dg.getKeuzeTitel();
  }

  public void setTitel(FieldValue titel) {
    dg.setKeuzeTitel(titel);
  }

  public String getNaam() {
    return trim(getTitel() + " " + getVoorvoegsel() + " " + getGeslachtsnaam());
  }

  public void reset() {
    setAktenummer("");
    setDatumErkenning(null);
    setGemeente(new FieldValue());
    setToestemminggeverType(ToestemminggeverType.ONBEKEND);
    setVerklaringGezag(false);
    setRechtbank("");
    setNaamskeuzeType(NaamskeuzeVanToepassingType.NEE);
    setNaamskeuzePersoon(NaamsPersoonType.ONBEKEND);
    setLandAfstamming(new FieldValue());
    setLandErkenning(new FieldValue());
    setBuitenlandsePlaats("");
  }
}
