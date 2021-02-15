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

package nl.procura.diensten.gba.wk.utils;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.emp;

import com.martiansoftware.jsap.CommandLineTokenizer;

import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;

public class CommandLine2Arguments {

  private ZoekArgumenten zoekArgumenten = new ZoekArgumenten();
  private String[]       args;

  public CommandLine2Arguments(String commandLine) {

    setArgs(CommandLineTokenizer.tokenize(commandLine));

    getArgumenten().setCode_straat(getArg("s"));
    getArgumenten().setStraatnaam(getArg("S"));
    getArgumenten().setHuisnummer(getArg("h"));
    getArgumenten().setHuisletter(getArg("H"));
    getArgumenten().setHuisnummeraanduiding(getArg("a"));
    getArgumenten().setHuisnummertoevoeging(getArg("t"));
    getArgumenten().setCode_gemeentedeel(getArg("g"));
    getArgumenten().setGemeentedeel(getArg("G"));
    getArgumenten().setCode_lokatie(getArg("l"));
    getArgumenten().setLokatie(getArg("L"));
    getArgumenten().setPostcode(getArg("p"));
    getArgumenten().setCode_object(getArg("co"));
    getArgumenten().setVolgcode_einde(getArg("ve"));
    getArgumenten().setDatum_einde(getArg("de"));
    getArgumenten().setGeen_bewoners(hasArg("B"));
    getArgumenten().setExtra_pl_gegevens(hasArg("ep"));
  }

  public boolean hasArg(String s) {

    for (int i = 0; i < getArgs().length; i++) {
      if (args[i].equals("-" + s)) {
        return true;
      }
    }

    return false;
  }

  public boolean hasArgs(String s, int i) {

    String val = getArg(s);

    if (emp(val)) {
      return true;
    }

    for (String x : val.split(",")) {
      if (aval(x) == i) {
        return true;
      }
    }

    return false;
  }

  public String getArg(String s) {

    for (int i = 0; i < getArgs().length; i++) {
      if (args[i].equals("-" + s)) {
        return args[i + 1];
      }
    }

    return "";
  }

  public String[] getArgs(String s) {
    return getArg(s).split(",");
  }

  public String[] getArgs() {
    return args;
  }

  public void setArgs(String[] args) {
    this.args = args;
  }

  public ZoekArgumenten getArgumenten() {
    return zoekArgumenten;
  }

  public void setZoekArgumenten(ZoekArgumenten zoekArgumenten) {
    this.zoekArgumenten = zoekArgumenten;
  }
}
