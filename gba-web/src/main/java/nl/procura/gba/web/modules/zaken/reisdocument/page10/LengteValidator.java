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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static nl.procura.standard.Globalfunctions.along;

import com.vaadin.data.validator.AbstractValidator;

public class LengteValidator extends AbstractValidator {

  public LengteValidator() {
    this("Minimaal 40 cm, maximaal 250 cm.");
  }

  private LengteValidator(String errorMessage) {
    super(errorMessage);
  }

  @Override
  public boolean isValid(Object value) {

    long lengte = along(value);
    return ((lengte >= 40) && (lengte <= 250));
  }
}
