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
import nl.procura.rdw.processen.p0182.f01.MAATRONDERHOUD;
import nl.procura.rdw.processen.p0182.f01.NATPERSOONGEG;
import nl.procura.rdw.processen.p0182.f01.RYBGEG;

/**
 * Registreren ontvangst rijbewijs algemeen
 */
public class P0182 extends RdwAanvraagMessage {

  public MAATRONDERHOUD newF1(BigInteger rijbewijsNummer, String natPersSleutel) {

    MAATRONDERHOUD p0182f1 = new MAATRONDERHOUD();
    RYBGEG r = new RYBGEG();
    r.setRybnr(rijbewijsNummer);

    NATPERSOONGEG n = new NATPERSOONGEG();
    n.setNatperssl(natPersSleutel);

    p0182f1.setRybgeg(r);
    p0182f1.setNatpersoongeg(n);
    setRequest(new Proces(RdwProces.P0182_F1, p0182f1));

    return p0182f1;
  }
}
