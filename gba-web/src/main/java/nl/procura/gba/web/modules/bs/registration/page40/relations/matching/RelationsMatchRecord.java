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

package nl.procura.gba.web.modules.bs.registration.page40.relations.matching;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

@Getter
public class RelationsMatchRecord {

  private boolean nameValue;
  private String  label;
  private String  value1;
  private String  value2;

  public RelationsMatchRecord(boolean nameValue, String label) {
    this.nameValue = nameValue;
    this.label = label;
  }

  public void set(String value1, String value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  /**
   * If the element is a 'name' element then must match completely
   * If the element is not a 'name' element then the values must match completely only if existing values are filled
   */
  public boolean isMatch() {
    if (nameValue) {
      return value1.equals(value2);
    } else {
      return StringUtils.isBlank(value2) || value1.equals(value2);
    }
  }
}
