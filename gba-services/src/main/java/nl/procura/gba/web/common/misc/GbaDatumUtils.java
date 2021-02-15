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

package nl.procura.gba.web.common.misc;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.LocalDate;

import com.google.common.collect.ComparisonChain;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameter;
import de.jollyday.ManagerParameters;
import de.jollyday.util.CalendarUtil;

public class GbaDatumUtils {

  public GbaDatumUtils() {
  }

  public static Calendar dateTimeFieldtoCalendar(Date date, TimeFieldValue time, String timeIfEmpty) {
    return date == null ? null : toCalendar(toDateTime(date, time, timeIfEmpty));
  }

  public static List<Dag> getDagenTussen(DateTime date1, DateTime date2) {

    List<Dag> dagen = new ArrayList<>();

    Calendar dagStart = Calendar.getInstance();
    Calendar dagEind = Calendar.getInstance();

    if (date1.getDate().before(date2.getDate())) {
      dagStart.setTime(date1.getDate());
      dagEind.setTime(date2.getDate());
    } else {
      dagStart.setTime(date2.getDate());
      dagEind.setTime(date1.getDate());
    }

    dagStart.add(Calendar.DAY_OF_MONTH, 1);

    do {

      if (isFeestdag(dagStart)) {
        dagen.add(new Dag(dagStart, getFeestdag(dagStart)));
      } else if (isZaterdag(dagStart)) {
        dagen.add(new Dag(dagStart, false, "Zaterdag"));
      } else if (isZondag(dagStart)) {
        dagen.add(new Dag(dagStart, false, "Zondag"));
      } else {
        dagen.add(new Dag(dagStart, true, "Werkdag"));
      }

      dagStart.add(Calendar.DAY_OF_MONTH, 1);
    } while (dagStart.getTimeInMillis() <= dagEind.getTimeInMillis());

    return dagen;
  }

  /**
   * Converteer de duur in .ms naar een String
   */
  public static String getDuration(long millis) {

    if (millis <= 0) {
      return "Onbekend";
    }

    long days = TimeUnit.MILLISECONDS.toDays(millis);
    millis -= TimeUnit.DAYS.toMillis(days);

    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    millis -= TimeUnit.HOURS.toMillis(hours);

    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
    millis -= TimeUnit.MINUTES.toMillis(minutes);

    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

    StringBuilder sb = new StringBuilder(64);

    if (days > 0) {
      sb.append(days);
      sb.append(" dag(en) ");
      return (sb.toString());
    } else if (hours > 0) {
      sb.append(hours);
      sb.append(" uur ");
      sb.append(minutes);
      sb.append(" min. ");
      return (sb.toString());
    }

    if (minutes > 0) {
      sb.append(minutes);
      sb.append(" min. ");
      sb.append(seconds);
      sb.append(" sec.");
    } else {
      if (millis > 2000) {
        sb.append(seconds);
        sb.append(" sec.");
      } else {
        sb.append(millis);
        sb.append(" ms.");
      }
    }

    return (sb.toString());
  }

  public static boolean isMinimaalAantalUur(Calendar beginTijdstip, Calendar eindTijdstip, int uren) {
    return isMinimaalAantalUur(beginTijdstip, eindTijdstip, uren, false);
  }

  public static Calendar addWerkdagen(Calendar beginDatum, int werkdagen) {
    Calendar tmpDate = (Calendar) beginDatum.clone();
    while (werkdagen > 0) {
      tmpDate.add(Calendar.DAY_OF_MONTH, 1);
      if (!isFeestdag(tmpDate) && !isZaterdag(tmpDate) && !isZondag(tmpDate)) {
        werkdagen--;
      }
    }

    return tmpDate;
  }

  public static boolean isMinimaalAantalWerkdagen(Calendar beginDatum, Calendar eindDatum, int werkdagen) {
    Calendar tmpDate = (Calendar) beginDatum.clone();
    while (werkdagen > 0) {
      tmpDate.add(Calendar.DAY_OF_MONTH, 1);
      if (!isFeestdag(tmpDate) && !isZaterdag(tmpDate) && !isZondag(tmpDate)) {
        werkdagen--;
      }
    }

    return eindDatum.after(tmpDate);
  }

  public static boolean isNieuwer(DateTime huidig, DateTime nieuw) {
    ComparisonChain huidige = ComparisonChain.start().compare(nieuw.getLongDate(), huidig.getLongDate());
    return huidige.compare(nieuw.getLongTime(), huidig.getLongTime()).result() > 0;
  }

  public static Calendar toBeginDag(Calendar tijdstip) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(tijdstip.getTime());
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal;
  }

  public static Calendar toEindeDag(Calendar tijdstip) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(tijdstip.getTime());
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 0);
    return cal;
  }

  private static Holiday getFeestdag(Calendar calendarDate) {

    CalendarUtil calendarUtil = new CalendarUtil();
    LocalDate tmpDate = calendarUtil.create(calendarDate);

    for (Holiday h : getHolidayManager().getHolidays(tmpDate.getYear(), "")) {
      if (tmpDate.equals(h.getDate())) {
        return h;
      }
    }

    return null;
  }

  private static HolidayManager getHolidayManager() {
    ManagerParameter parameter = ManagerParameters.create("procura");
    return HolidayManager.getInstance(parameter);
  }

  private static boolean isFeestdag(Calendar calendarDag) {
    return getHolidayManager().isHoliday(calendarDag, "");
  }

  private static boolean isMinimaalAantalUur(Calendar beginTijdstip, Calendar eindTijdstip, int uren,
      boolean opWerkdagen) {

    Calendar tmpDate = (Calendar) beginTijdstip.clone();
    tmpDate.add(Calendar.HOUR_OF_DAY, uren);
    if (opWerkdagen) {
      while (!isWerkdag(tmpDate)) {
        tmpDate.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
      }
    }

    return eindTijdstip.after(tmpDate);
  }

  private static boolean isWerkdag(Calendar calendarDag) {
    return (!isFeestdag(calendarDag) && !isZaterdag(calendarDag) && !isZondag(calendarDag));
  }

  private static boolean isZaterdag(Calendar calendarDag) {
    return (calendarDag.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
  }

  private static boolean isZondag(Calendar calendarDag) {
    return (calendarDag.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
  }

  /*
   * Van DateTime naar Calendar
   */
  private static Calendar toCalendar(DateTime dateTimeDatum) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dateTimeDatum.getDate());
    return calendar;
  }

  private static DateTime toDateTime(Date date, TimeFieldValue time, String tijdIndienLeeg) {

    if (emp(astr(time))) {
      time = new TimeFieldValue(tijdIndienLeeg);
    }

    return new DateTime((new DateTime(date)).getLongDate(), along(time.getValue()), TimeType.TIME_4_DIGITS);
  }

  public static class Dag {

    private Date    dag;
    private boolean telt;
    private String  oms;

    private Dag(Calendar dag, boolean telt, String oms) {
      this.dag = dag.getTime();
      this.telt = telt;
      this.oms = oms;
    }

    private Dag(Calendar dag, Holiday feestdag) {
      this.dag = dag.getTime();
      this.telt = (feestdag == null);
      this.oms = (feestdag == null) ? "" : feestdag.getDescription();
    }

    public Date getDag() {
      return dag;
    }

    public void setDag(Date dag) {
      this.dag = dag;
    }

    public String getOms() {
      return oms;
    }

    public void setOms(String oms) {
      this.oms = oms;
    }

    public boolean isTelt() {
      return telt;
    }

    public void setTelt(boolean telt) {
      this.telt = telt;
    }
  }
}
