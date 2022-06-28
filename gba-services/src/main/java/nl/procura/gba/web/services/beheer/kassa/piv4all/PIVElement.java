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

package nl.procura.gba.web.services.beheer.kassa.piv4all;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.rightPad;

import org.apache.commons.lang3.StringUtils;

import com.mysema.commons.lang.Assert;

public class PIVElement {

  private PIVElementType type;
  private String         input;

  public static PIVElement fromInput(PIVElementType type, Integer input) {
    Assert.notNull(input, "PIVFile: " + type.getLabel() + " is niet gevuld");
    return fromInput(type, String.valueOf(input));
  }

  public static PIVElement fromInput(PIVElementType type, String input) {
    Assert.notNull(input, "PIVFile: " + type.getLabel() + " is niet gevuld");
    PIVElement element = new PIVElement();
    element.type = type;
    element.input = input;
    return element;
  }

  public static PIVElement fromLine(PIVElementType type, String line) {
    PIVElement element = new PIVElement();
    element.type = type;
    element.input = line.substring(type.getStart(), type.getStart() + type.getLength());
    return element;
  }

  public PIVElementType getType() {
    return type;
  }

  public String getValue() {
    String value = StringUtils.substring(input, 0, type.getLength());
    if (PIVElementType.PIVElementAlignment.LEFT == type.getAlignment()) {
      return rightPad(value, type.getLength(), type.getDefaultValue());
    } else {
      return leftPad(value, type.getLength(), type.getDefaultValue());
    }
  }
}
