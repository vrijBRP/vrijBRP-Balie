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

package nl.procura.gba.web.services.beheer.profiel.veld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Velden implements Serializable {

  private static final long serialVersionUID = -9192163198468083799L;
  private List<Veld>        all              = new ArrayList<>();

  public Velden() {
  }

  public Velden(List<Veld> all) {
    setAll(all);
  }

  public List<Veld> getAlle() {
    return all;
  }

  public Veld getVeld(Veld veld) {
    for (Veld v : getAlle()) {
      if (v.equals(veld)) {
        return v;
      }
    }

    return null;
  }

  public void setAll(List<Veld> all) {
    this.all = all;
  }

  @Override
  public String toString() {
    return "VeldsImpl [all=" + all + "]";
  }
}
