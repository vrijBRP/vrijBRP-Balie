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

import java.util.ArrayList;
import java.util.List;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.window.Message;

public class ExceptionMessage {

  private List<ExceptionCause> causes = new ArrayList<>();

  public ExceptionMessage(Throwable t) {
    Throwable firstCause = getCause(t);
    getCauses().add(new ExceptionCause(firstCause));

    Throwable ct = firstCause.getCause();
    int i = 0;
    while (ct != null && i < 5) { // De laatste 5 oorzaken
      getCauses().add(new ExceptionCause(ct));
      ct = ct.getCause();
    }
  }

  /**
   * Zoek de eerste oorzaak van de Exception. Als er een GBAExeption in voorkomt dan is dat de eerste.
   */
  private static Throwable getCause(Throwable t) {
    ProException ProException = getException(t, ProException.class);

    if (ProException != null) {
      return ProException;
    }

    return t;
  }

  /**
   * Zoek exception van een bepaald type
   */
  @SuppressWarnings("unchecked")
  private static <T> T getException(Throwable e, Class<T> T) {

    if (e == null) {
      return null;
    }

    if (T.isAssignableFrom(e.getClass())) {
      return (T) e;
    }

    return e.getCause() != null ? getException(e.getCause(), T) : null;
  }

  /**
   * Geeft melding terug.
   * Als er sprake is van een error dan meer tonen
   */
  public String getCauseMsg() {

    String msg = "";
    if (getWarningType() == Message.TYPE_ERROR_MESSAGE) {
      for (ExceptionCause ec : getCauses(false)) {
        msg += ec.getMsg() + "\n";
      }
    }

    return msg;
  }

  public List<ExceptionCause> getCauses() {
    return causes;
  }

  public void setCauses(List<ExceptionCause> causes) {
    this.causes = causes;
  }

  /**
   * Geeft de zwaarste melding als type
   */
  public int getDelay() {
    switch (getWarningType()) {
      case Message.TYPE_INFO:
        return 1500;

      default:
        return 2000;
    }
  }

  /**
   * Geeft melding terug.
   * Als er sprake is van een error dan meer tonen
   */
  public String getHtmlCauseMsg() {
    StringBuilder msg = new StringBuilder(getMsg());
    if (getWarningType() == Message.TYPE_ERROR_MESSAGE) {
      List<ExceptionCause> l = getCauses(false);

      if (!l.isEmpty()) {
        msg.append("<hr/>");

        for (ExceptionCause ec : l) {
          msg.append("<p>");
          msg.append(ec.getMsg());
          msg.append("</p><br/>");
        }
      }

      msg.append("<hr/><p>Zie log voor meer informatie.</p>");
    } else {
      List<ExceptionCause> l = getCauses(false);

      if (!l.isEmpty()) {
        msg.append(".<br/>");

        for (ExceptionCause ec : l) {
          msg.append(ec.getMsg());
          msg.append(".<br/>");
        }
      }
    }

    return msg.toString();
  }

  /**
   * Geeft melding terug.
   * Als er sprake is van een error dan meer tonen
   */
  public String getMsg() {
    for (ExceptionCause ec : getCauses(true)) {
      return ec.getMsg();
    }
    return "";
  }

  /**
   * Geeft de zwaarste melding als type
   */
  public int getWarningType() {

    int warningType = Message.TYPE_INFO;

    for (ExceptionCause ec : getCauses()) {

      if (ec.getWarningType() == Message.TYPE_ERROR_MESSAGE) {
        warningType = Message.TYPE_ERROR_MESSAGE;
      }

      if (ec.getWarningType() == Message.TYPE_WARNING_MESSAGE && warningType != Message.TYPE_ERROR_MESSAGE) {
        warningType = Message.TYPE_WARNING_MESSAGE;
      }

      if (ec.getWarningType() == Message.TYPE_INFO && warningType != Message.TYPE_WARNING_MESSAGE
          && warningType != Message.TYPE_ERROR_MESSAGE) {
        warningType = Message.TYPE_INFO;
      }
    }

    return warningType;
  }

  public boolean hasException(Class<?> t) {
    for (ExceptionCause c : getCauses()) {
      if (t.isAssignableFrom(c.getThrowable().getClass())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Voor bepaalde type exceptions een bepaald waarschuwings type hanteren.
   */
  public void map(Class<?> c, int typeWarningMessage) {

    for (ExceptionCause ec : causes) {
      if (c.isAssignableFrom(ec.getThrowable().getClass())) {
        ec.setWarningType(typeWarningMessage);
      }
    }
  }

  private List<ExceptionCause> getCauses(boolean first) {
    List<ExceptionCause> l = new ArrayList<>();
    if (causes.size() > 0) {
      for (int i = 0; i < causes.size(); i++) {
        if ((first && i == 0) || !first && i > 0) {
          l.add(causes.get(i));
        }
      }
    }

    return l;
  }
}
