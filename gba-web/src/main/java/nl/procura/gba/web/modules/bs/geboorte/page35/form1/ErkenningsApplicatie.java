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

package nl.procura.gba.web.modules.bs.geboorte.page35.form1;

import java.util.Arrays;

public enum ErkenningsApplicatie {

  IN_PROWEB("in deze applicatie"),
  BUITEN_PROWEB("in een andere applicatie en / of gemeente"),
  ONBEKEND("Onbekend");

  private String oms = "";

  ErkenningsApplicatie(String oms) {
    setOms(oms);
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public boolean is(ErkenningsApplicatie... types) {
    return Arrays.stream(types).anyMatch(type -> type == this);
  }

  @Override
  public String toString() {
    return getOms();
  }
}
