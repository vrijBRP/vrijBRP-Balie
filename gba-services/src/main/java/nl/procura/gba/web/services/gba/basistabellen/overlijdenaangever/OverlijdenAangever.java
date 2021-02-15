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

package nl.procura.gba.web.services.gba.basistabellen.overlijdenaangever;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.DossOverlAangever;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.interfaces.Geldigheid;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class OverlijdenAangever extends DossOverlAangever implements Comparable<OverlijdenAangever>, Geldigheid {

  private static final long serialVersionUID = -1218181049188497309L;

  private FieldValue titel          = new FieldValue();
  private FieldValue geboorteplaats = new FieldValue();
  private FieldValue geboorteland   = new FieldValue();

  public OverlijdenAangever() {
    setVoorvoegsel("");
    setVoornamen("");
    setGeslachtsnaam("");
    setTp("");
    setDGeb(toBigDecimal(0));
    setGebPlaats("");
    setCGebLand(toBigDecimal(-1));
    setBsn(toBigDecimal(-1));
    setEmail("");
    setTelefoon("");
  }

  @Override
  public int compareTo(OverlijdenAangever a) {
    String naam1 = a.getNaam().getPred_adel_voorv_gesl_voorn().toLowerCase();
    String naam2 = getNaam().getPred_adel_voorv_gesl_voorn().toLowerCase();
    return naam2.compareTo(naam1);
  }

  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(astr(getBsn()));
  }

  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    setBsn(FieldValue.from(bsn).getBigDecimalValue());
  }

  @Override
  public DateTime getDatumEinde() {
    return new DateTime(getdEnd());
  }

  @Override
  public void setDatumEinde(DateTime dateTime) {
    setdEnd(toBigDecimal(dateTime.getLongDate()));
  }

  public GbaDateFieldValue getDatumGeboorte() {
    return new GbaDateFieldValue(getDGeb());
  }

  public void setDatumGeboorte(GbaDateFieldValue datum) {
    setDGeb(toBigDecimal(datum.getLongDate()));
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(getdIn());
  }

  @Override
  public void setDatumIngang(DateTime dateTime) {
    setdIn(toBigDecimal(dateTime.getLongDate()));
  }

  public Geboorteformats getGeboorte() {
    return new Geboorteformats().setValues(getDatumGeboorte().getFormatDate(), getGeboorteplaats().getDescription(),
        getGeboorteland().getDescription());
  }

  public FieldValue getGeboorteland() {
    return geboorteland;
  }

  public void setGeboorteland(FieldValue geboorteland) {
    this.geboorteland = FieldValue.from(geboorteland);
    setCGebLand(geboorteland.getBigDecimalValue());
  }

  public FieldValue getGeboorteplaats() {
    return geboorteplaats;
  }

  public void setGeboorteplaats(FieldValue geboorteplaats) {
    this.geboorteplaats = FieldValue.from(geboorteplaats);
    setCGebPlaats(this.geboorteplaats.getBigDecimalValue());
    setGebPlaats(this.geboorteplaats.getDescription());
  }

  @Override
  public GeldigheidStatus getGeldigheidStatus() {
    return GeldigheidStatus.get(this);
  }

  public Geslacht getGeslacht() {
    return Geslacht.get(getGesl());
  }

  public void setGeslacht(Geslacht geslacht) {
    setGesl(geslacht != null ? geslacht.getAfkorting() : "");
  }

  public Naamformats getNaam() {
    return new Naamformats(getVoornamen(), getGeslachtsnaam(), getVoorvoegsel(), getTitel().getDescription(), "E",
        null);
  }

  public String getTelefoon() {
    return getTel();
  }

  public void setTelefoon(String telefoon) {
    setTel(telefoon);
  }

  public FieldValue getTitel() {
    return titel;
  }

  public void setTitel(FieldValue titel) {
    this.titel = FieldValue.from(titel);
    setTp(this.titel.getStringValue());
  }
}
