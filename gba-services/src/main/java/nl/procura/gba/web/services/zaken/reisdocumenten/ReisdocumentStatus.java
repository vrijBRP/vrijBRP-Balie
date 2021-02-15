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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.io.Serializable;

import nl.procura.gba.common.DateTime;

public class ReisdocumentStatus implements Serializable {

  private static final long serialVersionUID = 2643071408462385700L;

  private ReisdocumentAanvraag aanvraag;

  public ReisdocumentStatus(ReisdocumentAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public DateTime getDatumTijdAfsluiting() {
    return new DateTime(aanvraag.getDAfsl(), aanvraag.getTAfsl());
  }

  public void setDatumTijdAfsluiting(DateTime datumTijdAfsluiting) {

    aanvraag.setDAfsl(toBigDecimal(datumTijdAfsluiting.getLongDate()));
    aanvraag.setTAfsl(toBigDecimal(datumTijdAfsluiting.getLongTime()));
  }

  public DateTime getDatumTijdLevering() {
    return new DateTime(aanvraag.getDDeliv(), aanvraag.getTDeliv());
  }

  public void setDatumTijdLevering(DateTime datumTijdLevering) {
    aanvraag.setDDeliv(toBigDecimal(datumTijdLevering.getLongDate()));
    aanvraag.setTDeliv(toBigDecimal(datumTijdLevering.getLongTime()));
  }

  public String getNrNederlandsDocument() {
    return aanvraag.getNrNlDoc();
  }

  public void setNrNederlandsDocument(String nrNederlandsDocument) {
    aanvraag.setNrNlDoc(nrNederlandsDocument);
  }

  public SluitingType getStatusAfsluiting() {
    return SluitingType.get(aval(aanvraag.getStatAfsl()));
  }

  public void setStatusAfsluiting(SluitingType statusAfsluiting) {
    aanvraag.setStatAfsl(toBigDecimal(statusAfsluiting.getCode()));
  }

  public LeveringType getStatusLevering() {
    return LeveringType.get(aval(aanvraag.getStatDeliv()));
  }

  public void setStatusLevering(LeveringType statusLevering) {
    aanvraag.setStatDeliv(toBigDecimal(statusLevering.getCode()));
  }

  public boolean isUitTeReiken() {
    return getStatusLevering() == LeveringType.DOCUMENT_GOED
        && getStatusAfsluiting().getCode() <= SluitingType.AANVRAAG_NIET_AFGESLOTEN.getCode();
  }

  public String toString() {

    return "ReisdocumentStatusImpl [getDatumTijdLevering()=" + getDatumTijdLevering() + ", getDatumTijdAfsluiting()="
        + getDatumTijdAfsluiting() + ", getStatusAfsluiting()=" + getStatusAfsluiting() + ", getNrNederlandsDocument()="
        + getNrNederlandsDocument() + ", getStatusLevering()=" + getStatusLevering() + "]";
  }
}
