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

package nl.procura.gbaws.requests;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

public class Logger {

  private List<String> loglines = new ArrayList<>();

  public Logger() {
  }

  public void item(String line, Object message) {
    log(">>" + line);
    log(message);
  }

  public void chapter(String s) {
    log(">" + s);
  }

  private void log(Object s) {
    getLoglines().add(astr(s));
  }

  public List<String> getLoglines() {
    return loglines;
  }

  public void setLoglines(ArrayList<String> loglines) {
    this.loglines = loglines;
  }
}
