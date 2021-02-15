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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class HttpSessionStorage {

  private static final SessionsInfo             info     = new SessionsInfo();
  private static final Map<String, HttpSession> sessions = new LinkedHashMap<>();

  public static synchronized void add(String id, HttpSession session) {
    sessions.put(id, session);
    info.setLastSession();
  }

  public static synchronized HttpSession get(String id) {
    return sessions.get(id);
  }

  public static synchronized List<HttpSession> getHttpSessions() {
    return new ArrayList(sessions.values());
  }

  public static synchronized SessionsInfo getInfo() {
    info.calculate();
    return info;
  }

  public static synchronized void remove(String id) {
    if (sessions.size() == 1) {
      info.removeLastSession();
    }

    info.calculate();
    sessions.remove(id);
  }
}
