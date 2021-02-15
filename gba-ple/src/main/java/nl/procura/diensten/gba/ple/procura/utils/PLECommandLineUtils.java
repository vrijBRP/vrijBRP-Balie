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

package nl.procura.diensten.gba.ple.procura.utils;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import com.martiansoftware.jsap.CommandLineTokenizer;

import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;

public class PLECommandLineUtils {

  public static String convert(PLEArgs args) {

    StringBuilder catBuffer = new StringBuilder();
    StringBuilder xtraBuffer = new StringBuilder();

    add(xtraBuffer, "a", args.getAnummers());
    add(xtraBuffer, "n", args.getGeslachtsnaam());
    add(xtraBuffer, "v", args.getVoornaam());
    add(xtraBuffer, "w", args.getVoorvoegsel());
    add(xtraBuffer, "b", args.getBsn());
    add(xtraBuffer, "t", args.getTitel());
    add(xtraBuffer, "d", args.getGeboortedatum());
    add(xtraBuffer, "g", args.getGemeentedeel());
    add(xtraBuffer, "gg", args.getGemeente());
    add(xtraBuffer, "s", args.getStraat());
    add(xtraBuffer, "h", args.getHuisnummer());
    add(xtraBuffer, "hl", args.getHuisletter());
    add(xtraBuffer, "ht", args.getHuisnummertoevoeging());
    add(xtraBuffer, "ha", args.getAanduiding());
    add(xtraBuffer, "p", args.getPostcode());
    add(xtraBuffer, "gs", args.getGeslacht());
    add(xtraBuffer, "go", !args.isShowSuspended());
    add(xtraBuffer, "mut", !args.isShowMutations());

    add(xtraBuffer, "afn", args.isSearchIndications());
    add(xtraBuffer, "rafn", args.getReasonForIndications());

    add(xtraBuffer, "actueel", !args.isShowHistory());
    add(xtraBuffer, "adr", args.isSearchOnAddress());
    add(xtraBuffer, "arch", args.isShowArchives());
    add(xtraBuffer, "r", args.isSearchRelations());
    add(xtraBuffer, "afg", args.isShowRemoved());
    add(xtraBuffer, "db", astr(args.getDatasource().getCode()));
    add(xtraBuffer, "mf", astr(args.getMaxFindCount()));
    add(xtraBuffer, "ct", args.getCustomTemplate());
    add(xtraBuffer, "verw", args.hasCat(VERW));

    addCat(catBuffer, 1, args.hasCat(PERSOON));
    addCat(catBuffer, 2, args.hasCat(OUDER_1));
    addCat(catBuffer, 3, args.hasCat(OUDER_2));
    addCat(catBuffer, 4, args.hasCat(NATIO));
    addCat(catBuffer, 5, args.hasCat(HUW_GPS));
    addCat(catBuffer, 6, args.hasCat(OVERL));
    addCat(catBuffer, 7, args.hasCat(INSCHR));
    addCat(catBuffer, 8, args.hasCat(VB));
    addCat(catBuffer, 9, args.hasCat(KINDEREN));
    addCat(catBuffer, 10, args.hasCat(VB));
    addCat(catBuffer, 11, args.hasCat(GEZAG));
    addCat(catBuffer, 12, args.hasCat(REISDOC));
    addCat(catBuffer, 13, args.hasCat(KIESR));
    addCat(catBuffer, 14, args.hasCat(AFNEMERS));
    addCat(catBuffer, 30, args.hasCat(DIV));
    addCat(catBuffer, 32, args.hasCat(WK));
    addCat(catBuffer, 33, args.hasCat(KLADBLOK));
    addCat(catBuffer, 34, args.hasCat(LOK_AF_IND));

    StringBuilder command = new StringBuilder("-ple ");
    command.append(trim(catBuffer.toString()));
    command.append(" ");
    command.append(trim(xtraBuffer.toString()));
    return trim(command.toString());
  }

  public static PLEArgs convert(String commandLine) {

    PLEArgs argumenten = new PLEArgs();
    String[] args = CommandLineTokenizer.tokenize(commandLine);

    argumenten.addNummer(getArgs(args, "a"));
    argumenten.setGeslachtsnaam(getArg(args, "n"));
    argumenten.setVoornaam(getArg(args, "v"));
    argumenten.setVoorvoegsel(getArg(args, "w"));
    argumenten.addNummer(getArgs(args, "b"));
    argumenten.setTitel(getArg(args, "t"));
    argumenten.setGeboortedatum(getArg(args, "d"));
    argumenten.setGemeentedeel(getArg(args, "g"));
    argumenten.setGemeente(getArg(args, "gg"));
    argumenten.setStraat(getArg(args, "s"));
    argumenten.setHuisnummer(getArg(args, "h"));
    argumenten.setHuisletter(getArg(args, "hl"));
    argumenten.setHuisnummertoevoeging(getArg(args, "ht"));
    argumenten.setAanduiding(getArg(args, "ha"));
    argumenten.setPostcode(getArg(args, "p"));
    argumenten.setGeslacht(getArg(args, "gs"));
    argumenten.setShowSuspended(!hasArg(args, "go"));
    argumenten.setShowMutations(!hasArg(args, "mut"));

    argumenten.setSearchIndications(hasArg(args, "afn"));
    argumenten.setReasonForIndications(getArg(args, "rafn"));

    argumenten.setSearchRelations(hasArg(args, "r"));
    argumenten.setShowHistory(!hasArg(args, "actueel"));
    argumenten.setSearchOnAddress(hasArg(args, "adr"));
    argumenten.setShowArchives(hasArg(args, "arch"));
    argumenten.setShowRemoved(hasArg(args, "afg"));
    argumenten.setDatasource(PLEDatasource.get(aval(getArg(args, "db"))));
    argumenten.setMaxFindCount(aval(getArg(args, "mf")));
    argumenten.setCustomTemplate(getArg(args, "ct"));

    argumenten.setCat(VERW, hasArg(args, "verw"));
    argumenten.setCat(PERSOON, hasArgs(args, "c", 1));
    argumenten.setCat(OUDER_1, hasArgs(args, "c", 2));
    argumenten.setCat(OUDER_2, hasArgs(args, "c", 3));
    argumenten.setCat(NATIO, hasArgs(args, "c", 4));
    argumenten.setCat(HUW_GPS, hasArgs(args, "c", 5));
    argumenten.setCat(OVERL, hasArgs(args, "c", 6));
    argumenten.setCat(INSCHR, hasArgs(args, "c", 7));
    argumenten.setCat(VB, hasArgs(args, "c", 8));
    argumenten.setCat(KINDEREN, hasArgs(args, "c", 9));
    argumenten.setCat(VBTITEL, hasArgs(args, "c", 10));
    argumenten.setCat(GEZAG, hasArgs(args, "c", 11));
    argumenten.setCat(REISDOC, hasArgs(args, "c", 12));
    argumenten.setCat(KIESR, hasArgs(args, "c", 13));
    argumenten.setCat(AFNEMERS, hasArgs(args, "c", 14));
    argumenten.setCat(DIV, hasArgs(args, "c", 30));
    argumenten.setCat(WK, hasArgs(args, "c", 32));
    argumenten.setCat(KLADBLOK, hasArgs(args, "c", 33));
    argumenten.setCat(LOK_AF_IND, hasArgs(args, "c", 34));

    return argumenten;
  }

  public static int getReturnTypeCode(String commandLine) {
    String[] args = CommandLineTokenizer.tokenize(commandLine);
    return aval(getArg(args, "type"));
  }

  private static String getArg(String[] args, String s) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-" + s)) {
        return args[i + 1];
      }
    }

    return "";
  }

  private static boolean hasArg(String[] args, String s) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-" + s)) {
        return true;
      }
    }

    return false;
  }

  private static boolean hasArgs(String[] args, String s, int i) {
    String vals = getArg(args, s);
    if (emp(vals)) {
      return true;
    }

    for (String val : vals.split(",")) {
      if (aval(val) == i) {
        return true;
      }
    }

    return false;
  }

  private static String[] getArgs(String[] args, String s) {
    return getArg(args, s).split(",");
  }

  private static void addCat(StringBuilder buffer, int cat_nr, boolean b) {

    if (!b) {
      return;
    }

    if (emp(buffer.toString())) {
      buffer.append(" -c ");
    }

    buffer.append(cat_nr);
    buffer.append(",");
  }

  private static void add(StringBuilder buffer, String a, String s) {

    if (fil(s)) {
      buffer.append(" -");
      buffer.append(a);
      buffer.append(" \"");
      buffer.append(trim(s));
      buffer.append("\"");
    }
  }

  private static void add(StringBuilder buffer, String a, List<String> arr) {

    StringBuilder v = new StringBuilder();

    for (String anr : arr) {
      v.append(anr).append(",");
    }

    add(buffer, a, v.toString());
  }

  private static void add(StringBuilder buffer, String a, boolean b) {

    if (b) {
      buffer.append(" -");
      buffer.append(a);
    }
  }
}
