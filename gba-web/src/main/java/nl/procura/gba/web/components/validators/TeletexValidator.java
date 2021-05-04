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

package nl.procura.gba.web.components.validators;

import java.util.Collection;

import com.vaadin.ui.TextField;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Field;

import nl.procura.commons.misc.teletex.Teletex;

public class TeletexValidator extends AbstractStringValidator {

  private String errorMessage = "";

  public TeletexValidator() {
    super("");
  }

  public static void add(Field field) {
    if (field instanceof TextField) {
      Collection<Validator> validators = field.getValidators();
      if (validators == null || validators.stream().noneMatch(TeletexValidator::isTeletextValidator)) {
        field.addValidator(new TeletexValidator());
      }
    }
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  protected boolean isValidString(String text) {
    return Teletex.fromUtf8(text.getBytes())
        .getErrors()
        .stream()
        .filter(StringUtils::isNotBlank)
        .findFirst()
        .map(error -> {
          errorMessage = StringUtils.capitalize(error) + ". Druk op pijltje-omlaag voor het diacrietenscherm.";
          return false;
        }).orElse(true);
  }

  private static boolean isTeletextValidator(com.vaadin.data.Validator validator) {
    return validator instanceof TeletexValidator;
  }
}
