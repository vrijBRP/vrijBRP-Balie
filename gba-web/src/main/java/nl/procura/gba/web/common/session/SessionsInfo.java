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

import static nl.procura.gba.web.common.session.SessionUtils.getReadableTime;

import org.apache.commons.lang3.math.NumberUtils;

import lombok.Data;

@Data
public class SessionsInfo {

  private int    sessionsCreated              = 0;
  private long   lastCreationTime             = -1;
  private String lastCreationTimeFormatted    = "";
  private long   lastDestructionTime          = -1;
  private String lastDestructionTimeFormatted = "";

  public SessionsInfo() {
  }

  public void calculate() {
    String lastTime = String.valueOf(getLastCreationTime());
    if (NumberUtils.toLong(lastTime, 0) > 0) {
      setLastCreationTimeFormatted(getReadableTime(System.currentTimeMillis() - NumberUtils.toLong(lastTime)));
    } else {
      setLastCreationTimeFormatted("");
    }

    lastTime = String.valueOf(getLastDestructionTime());
    if (NumberUtils.toLong(lastTime, 0) > 0) {
      setLastDestructionTimeFormatted(getReadableTime(System.currentTimeMillis() - NumberUtils.toLong(lastTime)));
    } else {
      setLastDestructionTimeFormatted("");
    }
  }

  public void removeLastSession() {
    setLastCreationTime(System.currentTimeMillis());
    setLastDestructionTimeFormatted(getReadableTime(System.currentTimeMillis()));
  }

  public void setLastSession() {
    int sessions = NumberUtils.toInt(String.valueOf(getSessionsCreated()), 0);
    setSessionsCreated(sessions + 1);
    setLastCreationTime(System.currentTimeMillis());
    setLastDestructionTime(-1);
    setLastCreationTimeFormatted("");
    setLastDestructionTimeFormatted("");
  }
}
