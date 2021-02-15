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

package nl.procura.gba.web.common.misc.email;

public enum EmailAddressType {

  FROM("Van", 1),
  REPLY_TO("Antwoord naar", 2),
  TO("Aan", 3),
  CC("Cc", 4),
  BCC("Bcc", 5);

  private final String oms;
  private final int    sort;

  EmailAddressType(String oms, int sort) {
    this.oms = oms;
    this.sort = sort;
  }

  public String getOms() {
    return oms;
  }

  public int getSort() {
    return sort;
  }

  public boolean is(EmailAddressType... types) {

    for (EmailAddressType type : types) {
      if (type == this) {
        return true;
      }
    }

    return false;
  }

  @Override
  public String toString() {
    return oms;
  }
}
