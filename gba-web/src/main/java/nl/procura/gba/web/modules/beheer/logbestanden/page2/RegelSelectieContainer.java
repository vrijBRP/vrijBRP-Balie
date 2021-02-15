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

package nl.procura.gba.web.modules.beheer.logbestanden.page2;

import nl.procura.vaadin.component.container.ArrayListContainer;

public class RegelSelectieContainer extends ArrayListContainer {

  public RegelSelectieContainer() {
    addItems(RegelSelectie.values(), false);
  }

  public enum RegelSelectie {

    LAATSTE1000("Laatste 1000", 1000),
    LAATSTE500("Laatste 500", 500),
    LAATSTE100("Laatste 100", 100);

    private String oms = "";
    private int    value;

    RegelSelectie(String oms, int value) {
      setOms(oms);
      setValue(value);
    }

    public String getOms() {
      return oms;
    }

    public void setOms(String oms) {
      this.oms = oms;
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return getOms();
    }
  }
}
