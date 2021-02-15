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

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.validator.AbstractStringValidator;

public class VervangendReisdocumentValidator extends AbstractStringValidator {

  private static final String ERROR_STRING = "Het nummer moet precies 9 tekens lang zijn";

  public VervangendReisdocumentValidator() {
    super(ERROR_STRING);
  }

  @Override
  protected boolean isValidString(String value) {
    return StringUtils.isNotEmpty(value) && value.length() == 9;
  }
}
