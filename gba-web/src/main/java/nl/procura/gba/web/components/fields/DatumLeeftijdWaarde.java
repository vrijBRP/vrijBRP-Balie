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

package nl.procura.gba.web.components.fields;

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.standard.ProcuraDate;

public class DatumLeeftijdWaarde implements Comparable<DatumLeeftijdWaarde> {

  private final String datum;
  private final int    leeftijd;

  public DatumLeeftijdWaarde(Geboorte geboorte) {
    this.datum = geboorte.getDatum();
    this.leeftijd = geboorte.getLeeftijd();
  }

  @Override
  public int compareTo(DatumLeeftijdWaarde o) {
    return new ProcuraDate(datum).diffInDays(o.datum) > 0 ? 1 : -1;
  }

  @Override
  public String toString() {
    return pos(leeftijd) ? datum + " (" + leeftijd + ")" : datum;
  }
}
