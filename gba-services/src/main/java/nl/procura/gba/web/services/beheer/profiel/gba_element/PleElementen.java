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

package nl.procura.gba.web.services.beheer.profiel.gba_element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PleElementen implements Serializable {

  private static final long serialVersionUID = 8011657757108210981L;

  private List<PleElement> alle = new ArrayList<>();

  public PleElementen() {
  }

  public PleElementen(List<PleElement> all) {
    setAlle(all);
  }

  public List<PleElement> getAlle() {
    return alle;
  }

  public void setAlle(List<PleElement> all) {
    this.alle = all;
  }

  public PleElement getPleElement(PleElement pleElement) {
    for (PleElement pE : getAlle()) {
      if (pE.equals(pleElement)) {
        return pE;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    return "GbaElementenImpl [all=" + alle + "]";
  }
}
