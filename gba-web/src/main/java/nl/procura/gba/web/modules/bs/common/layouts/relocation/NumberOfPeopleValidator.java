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

package nl.procura.gba.web.modules.bs.common.layouts.relocation;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.data.Validator;

public class NumberOfPeopleValidator implements Validator {

  private boolean fieldIsVisible  = false;
  private boolean ignoreThisField = false;

  public boolean isIgnoreThisField() {
    return ignoreThisField;
  }

  public void setIgnoreThisField(boolean ignoreThisField) {
    this.ignoreThisField = ignoreThisField;
  }

  public boolean isFieldIsVisible() {
    return fieldIsVisible;
  }

  public void setFieldIsVisible(boolean fieldIsVisible) {
    this.fieldIsVisible = fieldIsVisible;
  }

  @Override
  public boolean isValid(Object value) {
    return ignoreThisField || fil(astr(value));
  }

  @Override
  public void validate(Object value) throws InvalidValueException {
    if (!isValid(value) && isFieldIsVisible()) {
      throw new InvalidValueException(
          "Geef het aantal personen in dan woonachtig zal zijn op dit adres na de verhuizing");
    }
  }
}
