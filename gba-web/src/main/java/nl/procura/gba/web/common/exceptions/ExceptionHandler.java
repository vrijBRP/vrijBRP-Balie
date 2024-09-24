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

package nl.procura.gba.web.common.exceptions;

import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.FAULT;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.io.EOFException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Window;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.vaadin.component.window.Message;

public class ExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

  private ExceptionHandler() {
  }

  public static void handle(Window window, Throwable exc) {
    if (window != null) {
      // Een breakexception betekent gewoon stopppen met de code.
      if (exc instanceof Validator.InvalidValueException || exc instanceof EOFException) {
        return;
      }

      ExceptionMessage em = new ExceptionMessage(exc);
      em.map(IllegalArgumentException.class, Message.TYPE_WARNING_MESSAGE);

      if (!em.getCauses().isEmpty()) {
        // InvalidValueException is een formulier foutmelding.
        // Wordt toch al getoond.
        if (em.hasException(InvalidValueException.class)) {
          return;
        }
        if (em.getWarningType() == Message.TYPE_ERROR_MESSAGE) {
          store(window, exc, em);
        }
        // Toon melding aan gebruiker
        ExceptionCause c = em.getCauses().get(0);
        Message message = new Message(c.getTitle(), em.getHtmlCauseMsg(), em.getWarningType());
        message.setDelayMsec(em.getDelay());
        window.showNotification(message);
      }
    }
  }

  private static void store(Window window, Throwable exc, ExceptionMessage msg) {
    exc.printStackTrace();
    LOGGER.error("Error!", exc);
    if (window instanceof GbaWindow) {
      Services services = ((GbaWindow) window).getGbaApplication().getServices();
      PersonenWsService service = services.getPersonenWsService();
      service.addMessage(false, FAULT, ERROR, msg.getMsg(), msg.getCauseMsg(), exc);
    }
  }
}
