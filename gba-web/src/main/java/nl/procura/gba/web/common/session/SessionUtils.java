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

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

public class SessionUtils {

  public static String getReadableTime(long time) {

    long minutesInHour = 60;
    long days = TimeUnit.MILLISECONDS.toDays(time);
    long hours = TimeUnit.MILLISECONDS.toHours(time);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(time) % minutesInHour;
    long seconds = toSeconds(time);

    StringBuilder sb = new StringBuilder();
    if (days > 0) {
      sb.append(days);
      sb.append(" days, ");
    }
    if (hours > 0) {
      sb.append(hours);
      sb.append(" hours, ");
    }
    if (minutes > 0) {
      sb.append(minutes);
      sb.append(" min, ");
    }

    if (seconds >= 0) {
      sb.append(seconds);
      sb.append(" sec");
    }

    return sb.toString().trim().replaceAll(",$", "");
  }

  public static Session getSession(HttpSession httpSession) {

    Session session = new Session();
    long lastAccessedTime = httpSession.getLastAccessedTime();
    long inactiveInterval = System.currentTimeMillis() - lastAccessedTime;
    long creationTime = httpSession.getCreationTime();
    long duration = (System.currentTimeMillis() - creationTime);
    int maxInactiveInterval = (httpSession.getMaxInactiveInterval() * 1000);

    session.setId(httpSession.getId());
    session.setCreationTime(creationTime);
    session.setDuration(duration);
    session.setDurationFormatted(getReadableTime(duration));
    session.setMaxInactiveDuration(maxInactiveInterval);
    session.setMaxInactiveDurationFormatted(getReadableTime(maxInactiveInterval));
    session.setLastAccessedTime(lastAccessedTime);
    session.setLastAccessedTimeFormatted(getReadableTime(inactiveInterval));
    session.setExpired(inactiveInterval > maxInactiveInterval);

    return session;
  }

  private static long toSeconds(long time) {
    return TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(
        TimeUnit.MILLISECONDS.toMinutes(time));
  }
}
