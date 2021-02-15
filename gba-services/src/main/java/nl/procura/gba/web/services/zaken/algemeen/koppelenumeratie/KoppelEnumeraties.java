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

package nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KoppelEnumeraties implements Serializable {

  private static final long serialVersionUID = -8859107592476594796L;

  private List<KoppelEnumeratie> all = new ArrayList<>();

  public KoppelEnumeraties() {
  }

  public KoppelEnumeraties(List<KoppelEnumeratie> all) {
    setAll(all);
  }

  public List<KoppelEnumeratie> getAlle() {
    return all;
  }

  public KoppelEnumeratie getKoppelElement(KoppelEnumeratie pleCategorie) {
    for (KoppelEnumeratie pE : getAlle()) {
      if (pE.equals(pleCategorie)) {
        return pE;
      }
    }

    return null;
  }

  public void setAll(List<KoppelEnumeratie> all) {
    this.all = all;
  }

  public String toString() {
    return "KoppelElementenImpl [all=" + all + "]";
  }
}
