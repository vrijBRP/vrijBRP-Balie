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

package nl.procura.gba.web.services.beheer.profiel.actie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Acties implements Serializable {

  private static final long serialVersionUID = -7660549422322305190L;

  private List<Actie> all = new ArrayList<>();

  public Acties() {
  }

  public Acties(List<Actie> all) {
    setAll(all);
  }

  public Actie getActie(Actie actie) {
    for (Actie a : getAlle()) {
      if (a.equals(actie)) {
        return a;
      }
    }

    return null;
  }

  public List<Actie> getAlle() {
    return all;
  }

  public void setAll(List<Actie> all) {
    this.all = all;
  }

  @Override
  public String toString() {
    return "ActiesImpl [all=" + all + "]";
  }
}
