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

package nl.procura.gba.web.rest.v2.model.zaken.geboorte;

import nl.procura.gba.web.rest.v2.model.base.GbaRestGeslacht;

import lombok.Data;

@Data
public class GbaRestKind {

  private String          voornamen;
  private GbaRestGeslacht geslacht;
  private Integer         geboortedatum;
  /**
   * Time formatted like HHmmss. E.g. 34500 for 03:45:00. Birth time is usually given with 00 for seconds.
   */
  private Integer         geboortetijd;
}
