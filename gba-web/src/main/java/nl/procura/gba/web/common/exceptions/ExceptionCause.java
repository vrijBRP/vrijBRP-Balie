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

package nl.procura.gba.web.common.exceptions;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionType.UNKNOWN;

import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.window.Message;

public class ExceptionCause {

  private String    title       = "Er is een fout opgetreden";
  private int       warningType = Message.TYPE_ERROR_MESSAGE;
  private String    msg         = "";
  private Throwable throwable;

  public ExceptionCause(Throwable t) {

    setThrowable(t);

    if (t == null || t instanceof NullPointerException) {
      setMsg("Ongedefinieerde waarde");
    } else {
      setMsg(astr(t.getMessage()).replaceAll("\\.$", ""));
    }

    if (t instanceof ProException) {

      ProException ge = (ProException) t;

      String title = ge.getSeverity().getDescr();

      if (ge.getType() != UNKNOWN) {
        title += ": " + ge.getType().getDescr().toLowerCase();
      }

      setTitle(title);

      switch (ge.getSeverity()) {

        case ERROR:
          setWarningType(Message.TYPE_ERROR_MESSAGE);
          break;

        case WARNING:
          setWarningType(Message.TYPE_WARNING_MESSAGE);
          break;

        default:
          setWarningType(Message.TYPE_INFO);
          break;
      }
    }
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getWarningType() {
    return warningType;
  }

  public void setWarningType(int warningType) {
    this.warningType = warningType;
  }
}
