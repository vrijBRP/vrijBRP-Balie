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

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.io.Serializable;

import nl.procura.gba.common.DateTime;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class VerhuisHerVestiging implements Serializable {

  private static final long serialVersionUID = -9181091599749682162L;

  private FieldValue land              = new FieldValue();
  private DateTime   datumHervestiging = new DateTime();
  private String     duur              = "";
  private String     rechtsfeiten      = "";

  private VerhuisAanvraag aanvraag;

  public VerhuisHerVestiging(VerhuisAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  public VerhuisAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VerhuisAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public DateTime getDatumHervestiging() {
    return datumHervestiging;
  }

  public void setDatumHervestiging(DateTime datumHervestiging) {
    this.datumHervestiging = datumHervestiging;
    getAanvraag().setDVestiging(toBigDecimal(datumHervestiging.getLongDate()));
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
    getAanvraag().setLVestiging(this.land.getBigDecimalValue());
  }

  public String getRechtsfeiten() {
    return rechtsfeiten;
  }

  public void setRechtsfeiten(String rechtsfeiten) {
    this.rechtsfeiten = rechtsfeiten;
    getAanvraag().setRechtsfeiten(rechtsfeiten);
  }

  public String toString() {

    return "VerhuisHerVestigingImpl [land=" + land + ", datumHervestiging=" + datumHervestiging + ", duur=" + duur
        + ", rechtsfeiten=" + rechtsfeiten + "]";
  }
}
