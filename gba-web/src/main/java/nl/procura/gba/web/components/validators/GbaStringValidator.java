/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.components.validators;

import java.util.function.Predicate;

import com.vaadin.data.validator.AbstractStringValidator;

public class GbaStringValidator extends AbstractStringValidator {

  private final Predicate<String> predicate;

  public GbaStringValidator(String errorMessage, Predicate<String> predicate) {
    super(errorMessage);
    this.predicate = predicate;
  }

  @Override
  protected boolean isValidString(String value) {
    return predicate.test(value);
  }
}
