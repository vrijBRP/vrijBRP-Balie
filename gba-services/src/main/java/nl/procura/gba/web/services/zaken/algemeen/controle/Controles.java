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

package nl.procura.gba.web.services.zaken.algemeen.controle;

import java.util.ArrayList;
import java.util.List;

/**
 * Een lijst van controles
 */
public class Controles extends ArrayList<Controle> {

  /**
   * Voegt de controle toe
   */
  public <T extends Controle> T addControle(T controle) {
    add(controle);
    return controle;
  }

  /**
   * Voegt alleen de controles toe die tot een aanpassing van de zaak hebben geleidt.
   */
  public void addGewijzigdeControles(Controles controles) {
    for (Controle controle : controles) {
      if (controle.isGewijzigd()) {
        add(controle);
      }
    }
  }

  public List<Controle> getGewijzigdeControles() {
    List<Controle> controles = new ArrayList<>();
    for (Controle controle : this) {
      if (controle.isGewijzigd()) {
        controles.add(controle);
      }
    }

    return controles;
  }
}
