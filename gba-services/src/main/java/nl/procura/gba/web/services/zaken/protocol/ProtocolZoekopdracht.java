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

package nl.procura.gba.web.services.zaken.protocol;

import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class ProtocolZoekopdracht {

  private UsrFieldValue       gebruiker;
  private FieldValue          nummer;
  private ZaakPeriode         periode;
  private ProtocolleringGroep groep;

  private long   waarde       = 0;
  private String omschrijving = "";
  private long   aantal       = 0;

  public long getAantal() {
    return aantal;
  }

  public void setAantal(long aantal) {
    this.aantal = aantal;
  }

  public FieldValue getAnummer() {
    return nummer;
  }

  public UsrFieldValue getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(UsrFieldValue gebruiker) {
    this.gebruiker = gebruiker;
  }

  public ProtocolleringGroep getGroep() {
    return groep;
  }

  public void setGroep(ProtocolleringGroep groep) {
    this.groep = groep;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public ZaakPeriode getPeriode() {
    return periode;
  }

  public void setPeriode(ZaakPeriode periode) {
    this.periode = periode;
  }

  public long getWaarde() {
    return waarde;
  }

  public void setWaarde(long waarde) {
    this.waarde = waarde;
  }

  public void setNummer(FieldValue nummer) {
    this.nummer = nummer;
  }
}
