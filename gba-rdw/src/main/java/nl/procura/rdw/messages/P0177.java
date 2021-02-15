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
import nl.procura.rdw.processen.p0177.f01.ONGELDCATTAB;
import nl.procura.rdw.processen.p0177.f01.ONGVERKLGEG;
import nl.procura.rdw.processen.p0177.f01.RYBGEG;
import nl.procura.rdw.processen.p0177.f01.RYBONDERHOUD;

/**
 * Registeren ongeldig verklaring
 */
public class P0177 extends RdwAanvraagMessage {

  public RYBONDERHOUD newF1(BigInteger rijbewijsNummer) {

    RYBONDERHOUD p0177f1 = new RYBONDERHOUD();

    RYBGEG r = new RYBGEG();
    r.setRybnr(rijbewijsNummer);
    ONGVERKLGEG ov = new ONGVERKLGEG();
    ONGELDCATTAB c = new ONGELDCATTAB();

    p0177f1.setRybgeg(r);
    p0177f1.setOngeldcattab(c);
    p0177f1.setOngverklgeg(ov);

    setRequest(new Proces(RdwProces.P0177_F1, p0177f1));

    return p0177f1;
  }
}
