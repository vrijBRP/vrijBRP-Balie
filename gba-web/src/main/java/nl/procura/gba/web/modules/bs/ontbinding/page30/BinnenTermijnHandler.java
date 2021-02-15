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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import static nl.procura.standard.Globalfunctions.fil;

import java.util.Date;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.GbaDatumUtils;
import nl.procura.gba.web.common.misc.GbaDatumUtils.Dag;
import nl.procura.standard.ProcuraDate;

public class BinnenTermijnHandler {

  public static ProcuraDate getEinddatum(String dGewijsde) {
    return new ProcuraDate(dGewijsde).addMonths(6);
  }

  public static ProcuraDate getLaatsteDag(String dGewijsde) {

    // Einddatum
    String dEind = BinnenTermijnHandler.getEinddatum(dGewijsde).getSystemDate();

    if (fil(dEind) && fil(dGewijsde)) {

      // Zoek de laatste dag
      Date laatsteDag = null;
      for (Dag dag : GbaDatumUtils.getDagenTussen(new DateTime(dGewijsde), new DateTime(dEind))) {
        if (dag.isTelt()) {
          laatsteDag = dag.getDag();
        }
      }

      return new ProcuraDate(laatsteDag);
    }

    return null;
  }
}
