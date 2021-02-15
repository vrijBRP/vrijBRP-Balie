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

package nl.procura.gba.web.common.misc;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;

import nl.procura.gba.web.services.interfaces.DatumTijdInvoer;

/**
 * Sorterings classes
 */
public class GbaSorter {

  /**
   * Sorteer op datum / tijd van invoer
   */
  public static Comparator<DatumTijdInvoer> getByDatumTijdInvoer() {

    return (o1, o2) -> {

      long dIn1 = o1.getDatumTijdInvoer().getLongDate();
      long dIn2 = o2.getDatumTijdInvoer().getLongDate();

      long tInv1 = o1.getDatumTijdInvoer().getLongTime();
      long tInv2 = o2.getDatumTijdInvoer().getLongTime();

      return ComparisonChain.start().compare(dIn1, dIn2).compare(tInv1, tInv2).result();
    };
  }
}
