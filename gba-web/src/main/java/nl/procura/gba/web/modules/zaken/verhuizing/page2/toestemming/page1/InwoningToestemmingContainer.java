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

package nl.procura.gba.web.modules.zaken.verhuizing.page2.toestemming.page1;

import nl.procura.gba.web.components.containers.GbaContainer;

public class InwoningToestemmingContainer extends GbaContainer {

  public InwoningToestemmingContainer() {
    addItem(InwoningToestemming.ONBEKEND);
    addItem(InwoningToestemming.JA);
    addItem(InwoningToestemming.NEE);
  }

  public enum InwoningToestemming {

    JA(1, "Ja"),
    NEE(0, "Nee, er is geen toestemming ontvangen"),
    ONBEKEND(-1, "Nog niet aangegeven");

    private int    code  = 0;
    private String descr = "";

    InwoningToestemming(int code, String descr) {
      this.code = code;
      this.descr = descr;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public String getDescr() {
      return descr;
    }

    public void setDescr(String descr) {
      this.descr = descr;
    }

    @Override
    public String toString() {
      return descr;
    }
  }
}
