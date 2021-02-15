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

package nl.procura.gba.common;

import static nl.procura.standard.Globalfunctions.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import nl.procura.standard.ProcuraDate;

public class DateTime implements Comparable<DateTime> {

  private static final String            DATE_TIME_FORMAT = "yyyyMMddHHmmss";
  private static final String            DATE_FORMAT      = "yyyyMMdd";
  private static final String            TIME_FORMAT      = "HHmmss";
  private static final DateTimeFormatter DATE_FORMATTER   = DateTimeFormatter.ofPattern(DATE_FORMAT);
  private static final DateTimeFormatter TIME_FORMATTER   = DateTimeFormatter.ofPattern(TIME_FORMAT);
  private long                           date             = -1;
  private long                           time             = -1;
  private TimeType                       timeType         = TimeType.TIME_6_DIGITS;

  public DateTime() {

    setLongDate(along(getSystemDate()));
    setLongTime(along(getSystemTime()));
  }

  public DateTime(String date) {
    setLongDate(date != null ? along(new ProcuraDate(date).getSystemDate()) : -1);
  }

  public DateTime(Date date) {
    setLongDate(date != null ? along(new ProcuraDate(date).getSystemDate()) : -1);
  }

  public DateTime(BigDecimal bd) {

    if (bd != null) {
      setLongDate(along(bd));
    }
  }

  public DateTime(BigDecimal bd, BigDecimal bt) {
    this(bd, bt, TimeType.TIME_6_DIGITS);
  }

  public DateTime(BigDecimal bd, BigDecimal bt, TimeType timeType) {

    setTimeType(timeType);

    if (bd != null) {
      setLongDate(along(bd));
    }

    if (bt != null) {
      setLongTime(along(bt));
    }
  }

  public DateTime(long date) {
    setLongDate(date);
  }

  public DateTime(long date, long time) {
    this(date, time, TimeType.TIME_6_DIGITS);
  }

  public DateTime(long date, long time, TimeType timeType) {

    setTimeType(timeType);
    setLongDate(date);
    setLongTime(time);
  }

  /**
   * Controle op tijden met slechts uren en minuten
   */
  private long checkDigits(long bt) {

    if (timeType == TimeType.TIME_4_DIGITS) {
      if (astr(bt).length() == 1) { // 01 = 00:01:00
        return along(pad_right(astr(bt), "0", 3));
      } else if (astr(bt).length() == 2) { // 11 = 00:11:00
        return along(pad_right(astr(bt), "0", 4));
      } else if (astr(bt).length() == 3) { // 801 = 08:01:00
        return along(pad_right(astr(bt), "0", 5));
      } else if (astr(bt).length() == 4) { // 1640 = 16:40:00
        return along(pad_right(astr(bt), "0", 6));
      }
    }

    return bt;
  }

  @Override
  public int compareTo(DateTime a) {

    long d1 = getLongDate();
    long d2 = a.getLongDate();

    long t1 = getLongTime();
    long t2 = a.getLongTime();

    if (d1 > d2) {
      return 1;
    } else if (d1 < d2) {
      return -1;
    } else {
      return (t1 > t2) ? 1 : -1;
    }
  }

  @Override
  public String toString() {
    return getFormatDate() + (pos(getLongTime()) ? (" om " + getFormatTime("HH:mm")) : "");
  }

  public String getFormatDate() {
    return date2str(getLongDate());
  }

  public Date getDateOld() {
    return pos(getLongDate()) ? new ProcuraDate(astr(getLongDate())).getDateFormat() : null;
  }

  public Date getDate() {
    try {
      if (pos(getLongDate())) {
        if (pos(getLongDateTime())) {
          return new SimpleDateFormat(DATE_TIME_FORMAT).parse(astr(getLongDateTime()));
        }

        return new SimpleDateFormat(DATE_FORMAT).parse(astr(getLongDate()));
      }
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    return null;
  }

  public Calendar getCalendar() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getDate());
    return cal;
  }

  public String getFormatTime() {
    return time2str(astr(getLongTime()));
  }

  public String getFormatTime(String format) {
    return time2str(astr(getLongTime()), format);
  }

  public long getLongDateTime() {
    return along(getLongDate() + "" + String.format("%06d", getLongTime()));
  }

  public long getLongDate() {
    return date;
  }

  public int getIntDate() {
    return (int) date;
  }

  public int getIntTime() {
    return (int) time;
  }

  public void setLongDate(long date) {
    this.date = date;
  }

  public String getStringDate() {
    return astr(date);
  }

  public long getLongTime() {
    return time;
  }

  public void setLongTime(long time) {
    this.time = checkDigits(time);
  }

  public TimeType getTimeType() {
    return timeType;
  }

  public void setTimeType(TimeType timeType) {
    this.timeType = timeType;
  }

  public enum TimeType {
    TIME_4_DIGITS,
    TIME_6_DIGITS
  }

  public static DateTime of(LocalDate date) {
    return new DateTime(Integer.parseInt(date.format(DATE_FORMATTER)));
  }

  public static DateTime of(LocalTime date) {
    DateTime dateTime = new DateTime();
    dateTime.setLongTime(Integer.parseInt(date.format(TIME_FORMATTER)));
    return dateTime;
  }

  public static DateTime of(LocalDateTime localDateTime) {
    DateTime dateTime = new DateTime();
    dateTime.setLongDate(Integer.parseInt(localDateTime.format(DATE_FORMATTER)));
    dateTime.setLongTime(Integer.parseInt(localDateTime.format(TIME_FORMATTER)));
    return dateTime;
  }
}
