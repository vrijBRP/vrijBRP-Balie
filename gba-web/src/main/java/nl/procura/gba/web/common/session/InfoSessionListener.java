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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * A listener than stores the sessions in a static map
 */
public class InfoSessionListener implements HttpSessionListener {

  public InfoSessionListener() {
  }

  @SuppressWarnings("unused")
  public void onCreateSession(HttpSession httpSession) {
  }

  @SuppressWarnings("unused")
  public void onDestroySession(HttpSession httpSession) {
  }

  @Override
  public void sessionCreated(HttpSessionEvent se) {
    onCreateSession(se.getSession());
    HttpSessionStorage.add(se.getSession().getId(), se.getSession());
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
    onDestroySession(HttpSessionStorage.get(se.getSession().getId()));
    HttpSessionStorage.remove(se.getSession().getId());
  }
}
