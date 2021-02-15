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
import nl.procura.rdw.processen.p0183.f01.MAATRONDERHOUD;
import nl.procura.rdw.processen.p0183.f01.NATPERSOONGEG;
import nl.procura.rdw.processen.p0183.f01.RYBGEG;
import nl.procura.rdw.processen.p0183.f01.VERBLYFRYBGEG;

/**
 * Registreren ontvangst rijbewijs algemeen
 */
public class P0183 extends RdwAanvraagMessage {

  public MAATRONDERHOUD newF1(BigInteger rijbewijsNummer, String natPersSleutel, BigInteger autorCode) {

    MAATRONDERHOUD p0183f1 = new MAATRONDERHOUD();
    RYBGEG rGeg = new RYBGEG();
    rGeg.setRybnr(rijbewijsNummer);
    p0183f1.setRybgeg(rGeg);

    if (autorCode != null) {
      VERBLYFRYBGEG vGeg = new VERBLYFRYBGEG();
      vGeg.setAutorcodeverz(autorCode);
      p0183f1.setVerblyfrybgeg(vGeg);
    }

    NATPERSOONGEG natPersoonGeg = new NATPERSOONGEG();
    natPersoonGeg.setNatperssl(natPersSleutel);
    p0183f1.setNatpersoongeg(natPersoonGeg);

    setRequest(new Proces(RdwProces.P0183_F1, p0183f1));

    return p0183f1;
  }
}
