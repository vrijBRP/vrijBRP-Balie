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

package nl.procura.gba.web.services.gba.basistabellen.belanghebbende;

import static java.text.MessageFormat.format;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.Belangh;
import nl.procura.gba.web.services.interfaces.Geldigheid;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.standard.NaturalComparator;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Belanghebbende extends Belangh implements Comparable<Belanghebbende>, Geldigheid {

  private FieldValue land = new FieldValue();

  @Override
  public int compareTo(Belanghebbende o) {
    return NaturalComparator.compareTo(getNaam() + getTavNaam(), o.getNaam() + o.getTavNaam());
  }

  public BelanghebbendeType getBelanghebbendeType() {
    return BelanghebbendeType.getByWaarde(getType());
  }

  public void setBelanghebbendeType(BelanghebbendeType type) {
    setType(type.getWaarde());
  }

  @Override
  public DateTime getDatumEinde() {
    return new DateTime(getdEnd());
  }

  @Override
  public void setDatumEinde(DateTime dateTime) {
    setdEnd(toBigDecimal(dateTime.getLongDate()));
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(getdIn());
  }

  @Override
  public void setDatumIngang(DateTime dateTime) {
    setdIn(toBigDecimal(dateTime.getLongDate()));
  }

  @Override
  public GeldigheidStatus getGeldigheidStatus() {
    return GeldigheidStatus.get(this);
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue waarde) {
    this.land = FieldValue.from(waarde);
    setcLand(land.getBigDecimalValue());
  }

  public String getPostcode() {
    return getPc();
  }

  public void setPostcode(String postcode) {
    setPc(postcode);
  }

  public String getTav() {
    return getTavAanhef() + " " + getTavVoorl() + " " + getTavNaam();
  }

  public String getTelefoon() {
    return getTel();
  }

  public void setTelefoon(String telefoon) {
    setTel(telefoon);
  }

  public FieldValue getTerAttentieVanAanhef() {
    return new FieldValue(getTavAanhef());
  }

  public void setTerAttentieVanAanhef(FieldValue tav) {
    setTavAanhef(FieldValue.from(tav).getStringValue());
  }

  public String toString() {
    return format("{0}, {1} {2} {3}", getNaam(), getTavAanhef(), getTavVoorl(), getTavNaam());
  }
}
