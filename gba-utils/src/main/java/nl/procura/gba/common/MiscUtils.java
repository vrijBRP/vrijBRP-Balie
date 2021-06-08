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

import static ch.lambdaj.Lambda.join;
import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.UNKNOWN;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import nl.procura.commons.misc.application.properties.GitFileParser;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;

public class MiscUtils {

  public static final String  LOCK_ICON   = "x";
  private static final String LEGE_STRING = "";

  /**
   * Deze functie verkort een opsommingsstring van de vorm "item1, item2, item3, item4" tot de
   * meegegeven lengte. Lengte 2 meegeven zou dus de string "item1, item2,..." opleveren. De null-string
   * en de lege string leveren altijd de lege string als return op.
   *
   * @param enumString de opsommingsstring
   * @param len,       het maximaal aantal te tonen items
   * @param sepChar    het karakter waarmee de opsommingselementen gescheiden worden
   * @return de afgekorte string
   */
  public static String abbreviate(String enumString, int len, Character sepChar) {

    StringBuilder sb = new StringBuilder();
    if (len > 0) {
      String[] splits = StringUtils.split(enumString, sepChar.toString());
      if (splits != null) {
        sb.append(join(asList(splits).subList(0, Math.min(len, splits.length)), sepChar.toString()));
        if (splits.length > 0 && len < splits.length) {
          sb.append(sepChar + "...");
        }
      }
    }

    return sb.toString();
  }

  /**
   * Retourneert van een verzameling objecten een opsomming van de toString() omschrijving
   * van deze objecten. De lengte van deze opsomming wordt meegegeven via de int maxToShow.
   * De opsomming wordt in alfabetische volgorde gedaan via de sort() functie. We maken geen gebruik van
   * de abbreviate(String, int, Character) functie omdat in de toString() omschrijving ook komma's kunnen
   * voorkomen.
   */

  public static <T> String abbreviate(Collection<T> objects, int maxToShow) {
    return (objects == null) ? "" : abbreviate(join(objects), maxToShow, ',');
  }

  /**
   * Vergelijk het object met de items in de collection ipv andersom.
   */
  public static boolean contains(Object object, Collection collection) {
    if (collection != null && object != null) {
      for (Object collectionItem : collection) {
        if (object.equals(collectionItem)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Sorteert een lijstje van objecten in alfabetische volgorde via de toString() omschrijving
   * van deze objecten.
   */

  public static <T> List<T> sort(List<T> objectList) {

    objectList.sort((t1, t2) -> {
      if (!(t1 instanceof String) && t1 instanceof Comparable) {
        Comparable ct1 = (Comparable) t1;
        Comparable ct2 = (Comparable) t2;
        return ct1.compareTo(ct2);
      }

      String descrT1 = astr(t1).toLowerCase();
      String descrT2 = astr(t2).toLowerCase();
      return descrT1.compareTo(descrT2);
    });

    return objectList;
  }

  public static int getLeeftijd(String d_geb, String d_overl) {

    if (!pos(d_geb.replaceAll("\\D", LEGE_STRING))) {
      return 0;
    }

    int d_in = aval(new ProcuraDate(d_geb).setAllowedFormatExceptions(true).setForceFormatType(
        d_geb.contains("-") ? ProcuraDate.FORMATDATE_ONLY : ProcuraDate.SYSTEMDATE_ONLY).getSystemDate());
    int d_sys = aval(pos(d_overl) ? new ProcuraDate(d_overl).getSystemDate() : new ProcuraDate().getSystemDate());

    return (d_sys - d_in) / 10000;
  }

  public static String setClass(String cl, String v) {
    return "<font class='" + cl + "'>" + v + "</font>";
  }

  public static String setClass(boolean b, String v) {
    return setClass(b ? "green" : "red", v);
  }

  public static String trimNr(Object obj) {

    String value = astr(obj);

    if (fil(value)) {
      try {
        return Long.valueOf(value).toString();
      } catch (NumberFormatException e) {
        return value.replaceAll("\\D", LEGE_STRING);
      }
    }

    return value;
  }

  public static String formatPostcode(String input) {

    String new_pc = input;

    if (fil(new_pc)) {
      new_pc = new_pc.replaceAll("\\s+", LEGE_STRING);

      if (new_pc.length() == 6) {
        return new_pc.substring(0, 4) + " " + new_pc.substring(4, 6).toUpperCase();
      }
    }

    return input;
  }

  public static String trimPostcode(String value) {
    return value.replaceAll("\\s+", LEGE_STRING).toUpperCase();
  }

  public static boolean isNrEquals(String v1, String v2) {
    return fil(v1) && fil(v2) && trimNr(v1).equals(trimNr(v2));
  }

  public static boolean isDatumActueel(long d1, long d2) {

    long cur = along(getSystemDate());
    return ((cur >= d1) && ((cur <= d2) || (d2 <= 0)));
  }

  /**
   * Deze functie bekommert zich om de slashes in de padnaam: forward slashes worden vervangen door backward slashes;
   * slashes aan het begin of eind worden verwijderd en meerdere slashes na elkaar worden vervangen door een slash.
   * Eveneens worden met trim spaties en overbodige leestekens verwijderd.
   */

  public static String cleanPath(String pad) {

    if (!emp(pad)) {
      String s1 = pad.replaceAll("\\\\", "/"); // vervang alle backslashes door forward slashes
      String s2 = s1.replaceAll("^/+|/+$", LEGE_STRING); // verwijder 1 of meerdere slashes aan begin of eind
      String s3 = s2.replaceAll("/+", "/"); // vervang eventuele meerdere slashes door een enkele
      String s4 = s3.trim(); // deze trim verwijdert spaties aan begin en eind.
      return s4;
    }

    return LEGE_STRING;
  }

  /**
   * Geeft het relatieve pad van de file t.o.v. directorie dir.
   *
   * @return pad t.o.v. homedirectorie sjablonen
   */

  public static String getRelatiefPad(File dir, File file) {

    String cleanedFile = cleanPath(file.getAbsolutePath());
    String cleanedDir = cleanPath(dir.getAbsolutePath());

    if (!cleanedFile.startsWith(cleanedDir)) {
      throw new ProException(ProExceptionSeverity.WARNING, "Fout bij het opvragen van relatief pad.");
    }

    return cleanPath(cleanedFile.replaceFirst(cleanedDir, LEGE_STRING));
  }

  /**
   * Waarden krijgen lock icon als deze niet toegangelijk is.
   */
  public static String trimAllowed(String pattern) {
    if (pattern.replaceAll("[\\s\\W,]+", "").matches("^[x]+$")) {
      return "x";
    }
    return trim(pattern).trim();
  }

  /**
   * Alleen de eerste x aantal regels tonen.
   */
  public static String summarize(String content, int max) {

    StringBuilder summaryContent = new StringBuilder();
    String[] lines = content.split("\n");
    if (lines.length > max) {
      for (int i = 0; i < max; i++) {
        if (i > 0) {
          summaryContent.append("\n");
        }
        summaryContent.append(lines[i]);
      }

      summaryContent.append("... (Nog ");
      int linesLeft = lines.length - max;
      summaryContent.append(linesLeft);
      summaryContent.append(linesLeft == 1 ? " regel)" : " regels)");
    } else {
      summaryContent.append(content);
    }

    return summaryContent.toString();
  }

  @SuppressWarnings("unused")
  public static <T> T to(Object source, Class<T> targetType) { // Override
    return ((T) source);
  }

  /**
   * Kopieert de 2e parameter naar de 1e parameter
   */
  public static <T> T copy(Object source, T targetType) {

    if (source == null) {
      throw new ProException(UNKNOWN, ERROR, "Object is ongedefinieerd.");
    }

    try {
      return ReflectionUtil.deepCopyBean(targetType, source);
    } catch (Exception e) {
      throw new ProException(UNKNOWN, ERROR, "Object: " + source + " kan niet worden gekopieerd.", e);
    }
  }

  /**
   * Kopieert de 2e parameter naar een nieuwe instantie van de 1e parameter
   */
  public static <T> T copy(Object source, Class<T> targetType) {
    return ReflectionUtil.deepCopyBean(targetType, source);
  }

  @SuppressWarnings("unused")
  public static <T> List<T> copyList(Collection<?> bron, Class<T> targetType) {
    return ReflectionUtil.deepCopyBeans(targetType, bron);
  }

  public static <T> Set<T> copySet(Collection<?> source, Class<T> type) {
    return new HashSet<>(copyList(source, type));
  }

  public static <T> List<T> partition(List<T> list, int max) {
    if (list.isEmpty()) {
      return list;
    }
    if (list.size() > max) {
      return list.subList(0, max);
    }
    return list;
  }

  public static ProcuraDate convertMsToProcuraDate(String datum) {
    try {
      SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
      return new ProcuraDate(dt.parse(datum));
    } catch (ParseException e) {
      return null;
    }
  }

  public static String getCopyright() {
    return "&copy; " + new ProcuraDate().getYear() + " Procura BV";
  }

  public static String getBuildText() {
    String version = new MiscUtils().getClass().getPackage().getImplementationVersion();
    String date = GitFileParser.getProperty(GitFileParser.GIT_COMMIT_TIME);
    StringBuilder out = new StringBuilder("Versie <b>");
    if (date != null || version != null) {
      if (version != null) {
        out.append(version);
      }
      if (version != null && date != null) {
        out.append("</b> van <b>");
      }
      if (date != null) {
        out.append(GitFileParser.getDate(date, "dd-MM-yyyy"));
      }
    } else {
      out.append("Onbekend");
    }
    out.append("</b>");
    return out.toString();
  }

  public static String getVersion() {
    return defaultString(new MiscUtils().getClass().getPackage().getImplementationVersion());
  }

  public static String getBuilddate() {
    String date = GitFileParser.getProperty(GitFileParser.GIT_COMMIT_TIME);
    return date != null ? GitFileParser.getDate(date, "dd-MM-yyyy") : "";
  }
}
