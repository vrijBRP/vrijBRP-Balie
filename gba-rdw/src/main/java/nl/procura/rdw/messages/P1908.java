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

package nl.procura.rdw.messages;

import java.math.BigInteger;

import nl.procura.rdw.functions.Proces;
import nl.procura.rdw.functions.RdwAanvraagMessage;
import nl.procura.rdw.functions.RdwProces;
import nl.procura.rdw.processen.p1908.f01.AANVRRYBKRT;
import nl.procura.rdw.processen.p1908.f01.NATPERSOONGEG;
import nl.procura.validation.Bsn;

/**
 * Controle belemmeringen uitreiken rijbewijskaart
 */
public class P1908 extends RdwAanvraagMessage {

  public AANVRRYBKRT newF1(String bsn) {

    AANVRRYBKRT p1908f1 = new AANVRRYBKRT();

    NATPERSOONGEG n = new NATPERSOONGEG();

    Bsn bsn2 = new Bsn(bsn);

    n.setBurgservicenr(BigInteger.valueOf(bsn2.getLongBsn()));

    p1908f1.setNatpersoongeg(n);

    setRequest(new Proces(RdwProces.P1908_F1, p1908f1));

    return p1908f1;
  }
}
