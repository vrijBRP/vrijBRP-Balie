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

package nl.procura.gbaws.web.vaadin.module.requests.page1.periodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Periodes {

  private final List<Periode> periodes = new ArrayList<>();

  public Periodes() {

    add(new Vandaag(), new Gisteren(), new DezeWeek(), new VorigeWeek(), new DezeMaand(), new VorigeMaand(),
        new DitJaar(), new VorigJaar(), new Anders());
  }

  private void add(Periode... ps) {
    getPeriodes().addAll(Arrays.asList(ps));
  }

  public Periode getPeriode(String descr) {

    for (Periode p : getPeriodes()) {
      if (p.getDescr().equals(descr)) {
        return p;
      }
    }

    return null;
  }

  public List<Periode> getPeriodes() {
    return periodes;
  }
}
