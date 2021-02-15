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

package nl.procura.gba.web.common.session;

import lombok.Data;

@Data
public class Session {

  private String  id                           = "";
  private boolean expired                      = false;
  private long    creationTime                 = -1;
  private long    lastAccessedTime             = -1;
  private String  lastAccessedTimeFormatted    = "";
  private long    maxInactiveDuration          = -1;
  private String  maxInactiveDurationFormatted = "";
  private long    duration                     = -1;
  private String  durationFormatted            = "";

  private String userName         = "";
  private String userOs           = "";
  private String userAddress      = "";
  private String userAgent        = "";
  private String userAgentVersion = "";

  public Session() {
  }
}
