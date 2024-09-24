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

package nl.procura.gba.web.services.zaken.reisdocumenten.bezorging;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BezorgingStatusType {

  INGEVOERD(10, "Ingevoerd in VrijBRP"),
  AANGEMELD_AMP(20, "Aangemeld bij bezorger"),
  GEKOPPELD(30, "Koppeling aan order bevestigd"),
  INGEKLAARD(40, "Inklaring bevestigd"),
  GEBLOKKEERD(50, "Blokkering doorgegeven"),
  GEANNULEERD_AMP(60, "Annulering bevestigd"),
  UITGEREIKT_AMP(70, "Uitreiking bevestigd"),
  VERWERKT(80, "Afgesloten in VrijBRP"),
  ONBEKEND(-1, "Onbekend");

  private final Integer code;
  private final String  oms;

  public boolean is(BezorgingStatusType... types) {
    return Arrays.stream(types).anyMatch(type -> this == type);
  }
}
