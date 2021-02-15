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

package examples.nl.procura.rdw.processen;

import java.math.BigInteger;

import nl.procura.gbaws.testdata.Testdata;
import nl.procura.rdw.functions.Proces;
import nl.procura.rdw.functions.RdwProcesType;
import nl.procura.rdw.functions.RdwUtils;
import nl.procura.rdw.messages.P1651;
import nl.procura.rdw.processen.p1651.f01.AANRYBKOVERZ;

public class VoorbeeldProcesP1651F1 {

  public VoorbeeldProcesP1651F1() {

    P1651 message = new P1651();

    AANRYBKOVERZ o = message.newF1(Testdata.TEST_BSN_1.toString(), Testdata.TEST_ANR_1.toString(), 1980, "Duck",
        "Donald");

    BigInteger procId = BigInteger.valueOf(message.getRequest().getProces());
    BigInteger procFunc = BigInteger.valueOf(message.getRequest().getFunctie());
    String gebrIdent = "<username>";
    String gebrWw = "<password>";
    String info = "0000010614";

    RdwUtils.init(o, procId, procFunc, gebrIdent, gebrWw, info);

    RdwUtils.toStream(message.getRequest(), System.out, false, true);

    Proces proces = RdwUtils.getRandomStream(procId, BigInteger.valueOf(0), RdwProcesType.ERROR);

    RdwUtils.toStream(proces, System.out, false, false);
  }

  public static void main(String[] args) {
    new VoorbeeldProcesP1651F1();
  }
}
