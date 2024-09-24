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

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum InboxItemTypeName implements InboxEnum<String> {

  VRIJBRP_ADDRESS_RESEARCH("vrijbrp-address-research", "Verzoek adresonderzoek", false),
  VRIJBRP_CERTIFICATE_OF_CONDUCT("vrijbrp-certificate-of-conduct", "Verzoek VOG", false),
  VRIJBRP_CIVIL_RECORD("vrijbrp-civil-record", "Verzoek afschrift burgerlijke stand", false),
  VRIJBRP_LOST_TRAVEL_DOCUMENT("vrijbrp-lost-travel-document", "Verzoek vermissing reisdocument", false),
  VRIJBRP_TRAVEL_DOCUMENT("vrijbrp-travel-document-application", "Verzoek reisdocument", true),
  VRIJBRP_PAYMENT("vrijbrp-travel-document-application-update-payment", "Verzoek betaling", true),
  VRIJBRP_VOTING_CARD("vrijbrp-voting-card", "Verzoek vervangende stempas", false),
  UNKNOWN("", "Onbekend", false);

  private final String  id;
  private final String  descr;
  private final boolean automaticProcessing;

  InboxItemTypeName(String id, String descr, boolean automaticProcessing) {
    this.id = id;
    this.descr = descr;
    this.automaticProcessing = automaticProcessing;
  }

  public static InboxItemTypeName getByName(String name) {
    return Arrays.stream(values())
        .filter(status -> status.getId().equals(name))
        .findFirst()
        .orElse(UNKNOWN);
  }

  public static List<String> getAutomaticProcessingIds() {
    return Arrays.stream(InboxItemTypeName.values())
        .filter(InboxItemTypeName::isAutomaticProcessing)
        .map(InboxItemTypeName::getId)
        .collect(toList());
  }

  public static List<String> getIds() {
    return Arrays.stream(InboxItemTypeName.values())
        .map(InboxItemTypeName::getId)
        .collect(toList());
  }

  public boolean isUnknown() {
    return this == UNKNOWN;
  }

  @Override
  public String toString() {
    return descr;
  }
}
