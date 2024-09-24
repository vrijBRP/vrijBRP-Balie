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

package nl.procura.gba.web.services.bs.geboorte.naamskeuzebuitenproweb;

import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class NaamskeuzeBuitenProweb {

  private final DossierGeboorte dg;
  private FieldValue            gemeente = new FieldValue();
  private FieldValue            land     = new FieldValue();

  public NaamskeuzeBuitenProweb(DossierGeboorte dossierGeboorte) {
    this.dg = dossierGeboorte;
  }

  public String getAktenummer() {
    return dg.getAkteNk();
  }

  public void setAktenummer(String aktenummer) {
    dg.setAkteNk(aktenummer);
  }

  public String getBuitenlandsePlaats() {
    return dg.getPlaatsNk();
  }

  public void setBuitenlandsePlaats(String plaats) {
    dg.setPlaatsNk(plaats);
  }

  public DateTime getDatum() {
    return new DateTime(dg.getdInNk());
  }

  public void setDatum(DateTime dt) {
    dg.setdInNk(toBigDecimal(dt != null ? dt.getLongDate() : 0));
  }

  public FieldValue getGemeente() {
    return gemeente;
  }

  public void setGemeente(FieldValue gemeente) {
    this.gemeente = FieldValue.from(gemeente);
    dg.setcGemNk(this.gemeente.getBigDecimalValue());
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = FieldValue.from(land);
    dg.setcLandNk(this.land.getBigDecimalValue());
  }

  public NaamsPersoonType getNaamskeuzePersoon() {
    return NaamsPersoonType.get(dg.getPersonTypeNk());
  }

  public void setNaamskeuzePersoon(NaamsPersoonType type) {
    dg.setPersonTypeNk(toBigDecimal(type.getCode()));
  }

  public String getBijzonderheden() {
    return dg.getBijzNk();
  }

  public void setBijzonderheden(String bijzonderheden) {
    dg.setBijzNk(bijzonderheden);
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
    setDatum(null);
    setGemeente(new FieldValue());
    setNaamskeuzePersoon(NaamsPersoonType.ONBEKEND);
    setLand(new FieldValue());
    setBuitenlandsePlaats("");
    setBijzonderheden("");
    setGeslachtsnaam("");
    setVoorvoegsel(new FieldValue());
    setTitel(new FieldValue());
  }
}
