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
import nl.procura.rdw.processen.p0178.f01.RYBGEG;
import nl.procura.rdw.processen.p0178.f01.RYBONDERHOUD;

/**
 * Registeren ontvangst rijbewijs na ongeldig verklaring
 */
public class P0178 extends RdwAanvraagMessage {

  public RYBONDERHOUD newF1(BigInteger rijbewijsNummer) {

    RYBONDERHOUD p0178f1 = new RYBONDERHOUD();
    RYBGEG rGeg = new RYBGEG();
    rGeg.setRybnr(rijbewijsNummer);
    p0178f1.setRybgeg(rGeg);
    setRequest(new Proces(RdwProces.P0178_F1, p0178f1));

    return p0178f1;
  }
}
