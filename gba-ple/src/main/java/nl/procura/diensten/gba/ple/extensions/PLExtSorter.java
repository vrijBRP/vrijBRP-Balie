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

package nl.procura.diensten.gba.ple.extensions;

import static nl.procura.standard.Globalfunctions.aval;

import java.text.Collator;
import java.util.Comparator;

public class PLExtSorter implements Comparator<BasePLExt> {

  private final Collator collator = Collator.getInstance();

  @Override
  public int compare(BasePLExt pl1, BasePLExt pl2) {
    int d_geb = compared(pl1.getPersoon().getGeboorte().getGeboortedatum().getCode(),
        pl2.getPersoon().getGeboorte().getGeboortedatum().getCode());

    return ((d_geb != 0) ? d_geb
        : compared(pl1.getPersoon().getNaam().getNaamNaamgebruik(),
            pl2.getPersoon().getNaam().getNaamNaamgebruik()));
  }

  private int compared(Object pl1, Object pl2) {
    return compared(0, pl1, pl2);
  }

  private int compared(int returnValue, Object obj1, Object obj2) {
    if (returnValue != 0) {
      return returnValue;
    }

    if (obj1 instanceof Number) {
      return Integer.compare(aval(obj1), aval(obj2));
    }

    return collator.compare(obj1, obj2);
  }
}
