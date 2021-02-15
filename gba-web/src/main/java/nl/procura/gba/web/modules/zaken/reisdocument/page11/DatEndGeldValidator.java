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

package nl.procura.gba.web.modules.zaken.reisdocument.page11;

import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.data.validator.AbstractValidator;

import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DatEndGeldValidator extends AbstractValidator {

  private ProcuraDate dIn, dEnd;

  public DatEndGeldValidator(ProcuraDate dIn, ProcuraDate dEnd) {

    super("Foutieve datum. Deze moet tussen " + dIn.getFormatDate() + " en " + dEnd.getFormatDate() + " liggen.");

    setdIn(dIn);
    setdEnd(dEnd.addDays(1));
  }

  public ProcuraDate getdEnd() {
    return dEnd;
  }

  private void setdEnd(ProcuraDate dEnd) {
    this.dEnd = dEnd;
  }

  public ProcuraDate getdIn() {
    return dIn;
  }

  private void setdIn(ProcuraDate dIn) {
    this.dIn = dIn;
  }

  @Override
  public boolean isValid(Object value) {

    FieldValue fv = (FieldValue) value;

    return new ProcuraDate(astr(fv.getValue())).isBetween(dIn, dEnd);
  }
}
