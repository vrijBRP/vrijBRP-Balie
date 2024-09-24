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

package nl.procura.gba.web.functions.exceptions;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.AUTHENTICATION;
import static nl.procura.commons.core.exceptions.ProExceptionType.CONFIG;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.procura.gba.web.common.exceptions.ExceptionMessage;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.window.Message;

public class ExceptionTest {

  @Test
  public void cascadingException1() {

    NullPointerException a = new NullPointerException();
    ProException b = new ProException(CONFIG, WARNING, "b", a);
    ProException c = new ProException(CONFIG, INFO, "b", b);

    ExceptionMessage m = new ExceptionMessage(c);

    assertEquals(m.getWarningType(), Message.TYPE_ERROR_MESSAGE);
    assertEquals(m.getDelay(), 2000);
  }

  @Test
  public void cascadingException2() {

    ProException a = new ProException(AUTHENTICATION, WARNING, "a");
    ProException b = new ProException(CONFIG, WARNING, "b", a);
    ProException c = new ProException(CONFIG, INFO, "b", b);

    ExceptionMessage m = new ExceptionMessage(c);

    assertEquals(m.getWarningType(), Message.TYPE_WARNING_MESSAGE);
    assertEquals(m.getDelay(), 2000);
  }

  @Test
  public void infoException() {

    ProException a = new ProException(AUTHENTICATION, INFO, "a");

    ExceptionMessage m = new ExceptionMessage(a);

    assertEquals(m.getWarningType(), Message.TYPE_INFO);
    assertEquals(m.getDelay(), 1500);
  }
}
