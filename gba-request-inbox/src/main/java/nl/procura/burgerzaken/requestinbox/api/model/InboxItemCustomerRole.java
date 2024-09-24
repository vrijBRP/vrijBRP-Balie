/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.burgerzaken.requestinbox.api.model;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum InboxItemCustomerRole implements InboxEnum<String> {

  INITIATOR("initiator", "Aanvrager"),
  CO_INITIATOR("co_initiator", "Mede-aanvrager"),
  INTERESTED_PARTY("interested_party", "Betrokkene"),
  UNKNOWN("", "Onbekend");

  private final String id;
  private final String descr;

  InboxItemCustomerRole(String id, String descr) {
    this.id = id;
    this.descr = descr;
  }

  public boolean isUnknown() {
    return this == UNKNOWN;
  }

  public static InboxItemCustomerRole getByName(String name) {
    return Arrays.stream(values())
        .filter(status -> status.getId().equals(name))
        .findFirst().orElse(UNKNOWN);
  }

  @Override
  public String toString() {
    return descr;
  }
}
