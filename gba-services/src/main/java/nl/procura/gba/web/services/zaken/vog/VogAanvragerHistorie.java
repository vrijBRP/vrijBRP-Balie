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

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.gba.jpa.personen.db.VogHist;
import nl.procura.gba.jpa.personen.db.VogHistPK;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class VogAanvragerHistorie extends VogHist {

  private static final long serialVersionUID = -4175868994648255585L;

  private long       cVogAanvr       = -1;
  private long       volgCode        = -1;
  private String     geslachtsnaam   = "";
  private String     voorvoegsel     = "";
  private String     voornamen       = "";
  private FieldValue gemeenteGeboren = new FieldValue();
  private FieldValue landGeboren     = new FieldValue();
  private Geslacht   geslacht        = Geslacht.ONBEKEND;

  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(astr(getBsnA()));
  }

  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    setBsnA(FieldValue.from(bsn).getBigDecimalValue());
  }

  public long getCVogAanvr() {
    return cVogAanvr;
  }

  public void setCVogAanvr(long cVogAanvr) {
    this.cVogAanvr = cVogAanvr;
    getId().setCVogAanvr(cVogAanvr);
  }

  public GbaDateFieldValue getDatumGeboorte() {
    return new GbaDateFieldValue(getDGebA());
  }

  public void setDatumGeboorte(GbaDateFieldValue datumGeboorte) {
    setDGebA(toBigDecimal(datumGeboorte.getLongDate()));
  }

  public FieldValue getGemeenteGeboren() {
    return gemeenteGeboren;
  }

  public void setGemeenteGeboren(FieldValue gemeenteGeboren) {
    this.gemeenteGeboren = FieldValue.from(gemeenteGeboren);
    setPGebA(this.gemeenteGeboren.getStringValue());
  }

  public Geslacht getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(Geslacht geslacht) {

    this.geslacht = geslacht;
    setGeslA(geslacht.getAfkorting());
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {

    this.geslachtsnaam = geslachtsnaam;
    setNaamA(geslachtsnaam);
  }

  @Override
  public VogHistPK getId() {

    if (super.getId() == null) {
      setId(new VogHistPK());
    }

    return super.getId();
  }

  public FieldValue getLandGeboren() {
    return landGeboren;
  }

  public void setLandGeboren(FieldValue landGeboren) {
    this.landGeboren = FieldValue.from(landGeboren);
    setLGebA(this.landGeboren.getBigDecimalValue());
  }

  public long getVolgCode() {
    return volgCode;
  }

  public void setVolgCode(long volgCode) {
    this.volgCode = volgCode;
    getId().setVHist(volgCode);
  }

  public String getVoornamen() {
    return voornamen;
  }

  public void setVoornamen(String voornamen) {

    this.voornamen = voornamen;
    setVoornA(voornamen);
  }

  public String getVoorvoegsel() {
    return voorvoegsel;
  }

  public void setVoorvoegsel(String voorvoegsel) {

    this.voorvoegsel = voorvoegsel;
    setVoorvA(voorvoegsel);
  }
}
