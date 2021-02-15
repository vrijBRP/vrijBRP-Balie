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

package nl.procura.gba.web.services.zaken.verhuizing;

import static nl.procura.standard.Globalfunctions.*;

import java.io.Serializable;

import nl.procura.gba.common.DateTime;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class VerhuisEmigratie implements Serializable {

  private static final long serialVersionUID = 287397921102820592L;

  private FieldValue land         = new FieldValue();
  private DateTime   datumVertrek = new DateTime();
  private String     adres1       = "";
  private String     adres2       = "";
  private String     adres3       = "";
  private String     duur         = "";

  private VerhuisAanvraag aanvraag;

  public VerhuisEmigratie(VerhuisAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  public VerhuisAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VerhuisAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public String getAdres() {
    return trim(getAdres1() + ", " + getAdres2() + ", " + getAdres3() + ", " + getLand());
  }

  public String getAdres1() {
    return adres1;
  }

  public void setAdres1(String adres1) {

    this.adres1 = adres1;
    getAanvraag().setBAdr1(adres1);
  }

  public String getAdres2() {
    return adres2;
  }

  public void setAdres2(String adres2) {

    this.adres2 = adres2;
    getAanvraag().setBAdr2(adres2);
  }

  public String getAdres3() {
    return adres3;
  }

  public void setAdres3(String adres3) {

    this.adres3 = adres3;
    getAanvraag().setBAdr3(adres3);
  }

  public DateTime getDatumVertrek() {
    return datumVertrek;
  }

  public void setDatumVertrek(DateTime datumVertrek) {

    this.datumVertrek = datumVertrek;
    getAanvraag().setDVertrek(toBigDecimal(datumVertrek.getLongDate()));

    if (pos(getAanvraag().getDVertrek())) {
      getAanvraag().setDAanv(getAanvraag().getDVertrek());
    }
  }

  public String getDuur() {
    return duur;
  }

  public void setDuur(String duur) {

    this.duur = duur;
    getAanvraag().setDuur(duur);
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {

    this.land = FieldValue.from(land);
    getAanvraag().setLVertrek(this.land.getBigDecimalValue());
  }

  public String toString() {

    return "VerhuisEmigratieImpl [land=" + land + ", datumVertrek=" + datumVertrek + ", adres1=" + adres1 + ", adres2="
        + adres2 + ", adres3=" + adres3 + ", duur=" + duur + "]";
  }
}
