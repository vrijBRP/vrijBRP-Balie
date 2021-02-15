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

package nl.procura.diensten.gba.wk.extensions;

import static nl.procura.standard.Globalfunctions.aval;

import java.util.Comparator;

import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;

public class WKPersonWrapperSorter implements Comparator<BaseWKPerson> {

  @Override
  public int compare(BaseWKPerson p1, BaseWKPerson p2) {

    if (aval(p1.getDatum_vertrek().getCode()) < aval(p2.getDatum_vertrek().getCode())) {
      return -1;
    } else if (aval(p1.getDatum_vertrek().getCode()) > aval(p2.getDatum_vertrek().getCode())) {
      return 1;
    }

    if (aval(p1.getDatum_ingang().getCode()) < aval(p2.getDatum_ingang().getCode())) {
      return -1;
    } else if (aval(p1.getDatum_ingang().getCode()) > aval(p2.getDatum_ingang().getCode())) {
      return 1;
    }

    return 0;
  }
}
