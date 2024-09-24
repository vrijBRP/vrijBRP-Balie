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

package nl.procura.gba.web.modules.bs.lv.page30;

import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.data.validator.AbstractStringValidator;

public class BrpAkteValidator extends AbstractStringValidator {

  private final String letter;

  public BrpAkteValidator(String letter) {
    super("Incorrecte waarde");
    this.letter = letter;
  }

  @Override
  protected boolean isValidString(String value) {
    if (value.length() == 7) {
      String ingevoerdeLetter = value.substring(2, 3);
      return astr(letter).equals(astr(ingevoerdeLetter));
    }
    return false;
  }
}
