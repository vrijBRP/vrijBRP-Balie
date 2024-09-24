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

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BezorgingGemeenteStatusType {

  ISTA_BLK("ISTA_BLK"), // Blokkering
  IKPL_ORD("IKPL_ORD"), // Bevestiging koppeling
  ISTA_INK("ISTA_INK"), // Bevestiging update/inklaring
  IANN_BEZ("IANN_BEZ"), // Annulering annulering
  IUIT_BEZ("IUIT_BEZ"); // Bevestiging uitreiking

  private final String code;

  @Override
  public String toString() {
    return code;
  }
}
