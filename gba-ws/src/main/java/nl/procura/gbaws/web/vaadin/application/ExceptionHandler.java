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

package nl.procura.gbaws.web.vaadin.application;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Window;

import nl.procura.vaadin.component.application.exception.ExceptionCause;
import nl.procura.vaadin.component.application.exception.ExceptionMessage;
import nl.procura.vaadin.component.window.Message;

public class ExceptionHandler {

  public static void handle(Window w, Throwable e) {

    if (w != null) {

      // Een breakexception betekent gewoon stopppen met de code.
      if (e instanceof Validator.InvalidValueException) {
        return;
      }

      ExceptionMessage m = new ExceptionMessage(e);

      m.map(IllegalArgumentException.class, Message.TYPE_WARNING_MESSAGE);

      if (m.getCauses().size() > 0) {

        // InvalidValueException is een formulier foutmelding.
        // Wordt toch al getoond.

        if (m.hasException(InvalidValueException.class)) {
          return;
        }

        if (m.getWarningType() == Message.TYPE_ERROR_MESSAGE) {

          // Toon stacktrace als er sprake is van een melding met hoogste niveau
          e.printStackTrace();
        }

        // Toon melding aan gebruiker
        ExceptionCause c = m.getCauses().get(0);
        Message message = new Message(c.getTitle(), m.getHtmlCauseMsg(), m.getWarningType());
        message.setDelayMsec(m.getDelay());
        w.showNotification(message);
      }
    }
  }
}
