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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SluitingType {

  AANVRAAG_NIET_AFGESLOTEN(0, "Aanvraag niet afgesloten"),
  DOCUMENT_UITGEREIKT(1, "Document uitgereikt"),
  DOCUMENT_NIET_UITGEREIKT_ONJUIST(2, "Document niet uitgereikt (onjuist)"),
  DOCUMENT_UITGEREIKT_DOOR_ANDERE_INSTANTIE(3, "Document uitgereikt door andere instantie"),
  DOCUMENT_NIET_UITGEREIKT_OVERIGE_REDEN(4, "Document niet uitgereikt, overige reden"),
  DOCUMENT_NIET_OPGEHAALD(5, "Document niet opgehaald"),
  ONBEKEND(-1, "Onbekend");

  private final int    code;
  private final String oms;

  public static SluitingType get(int code) {
    return Arrays.stream(values())
        .filter(var -> var.getCode() == code)
        .findFirst().orElse(ONBEKEND);

  }

  public boolean is(SluitingType... types) {
    return Arrays.stream(types).anyMatch(type -> this == type);
  }

  @Override
  public String toString() {
    return getCode() >= 0 ? (getCode() + ": " + getOms()) : getOms();
  }

}
