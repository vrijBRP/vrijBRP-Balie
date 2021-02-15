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

package nl.procura.diensten.gba.wk.client;

import static nl.procura.standard.Globalfunctions.aval;

import java.io.Serializable;
import java.util.Comparator;

import nl.procura.diensten.woningkaart.objecten.Persoon;

public class WKPersoonComparator implements Comparator<Persoon>, Serializable {

  private static final long serialVersionUID = -7955034743910558545L;

  @Override
  public int compare(Persoon p1, Persoon p2) {

    if (aval(p1.getDatum_vertrek()) < aval(p2.getDatum_vertrek())) {
      return -1;
    } else if (aval(p1.getDatum_vertrek()) > aval(p2.getDatum_vertrek())) {
      return 1;
    }

    if (aval(p1.getDatum_ingang()) < aval(p2.getDatum_ingang())) {
      return -1;
    } else if (aval(p1.getDatum_ingang()) > aval(p2.getDatum_ingang())) {
      return 1;
    }

    return 0;
  }
}
