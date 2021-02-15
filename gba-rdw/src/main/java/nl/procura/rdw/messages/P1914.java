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
import nl.procura.rdw.processen.p1914.f01.AANVRRYBZGEG;
import nl.procura.rdw.processen.p1914.f01.RYBAANVROVERZ;

/**
 * Raadplegen rijbewijsaanvragen op actuele status
 */
public class P1914 extends RdwAanvraagMessage {

  public RYBAANVROVERZ newF1(long statusCode, long gemeenteCode, BigInteger laatsteAanvraagNr) {

    RYBAANVROVERZ p1914f1 = new RYBAANVROVERZ();
    AANVRRYBZGEG rGeg = new AANVRRYBZGEG();

    rGeg.setStatcoderybk(BigInteger.valueOf(statusCode));
    rGeg.setAutorarybk(BigInteger.valueOf(gemeenteCode));

    // Als laatsteaanvraagnummer wordt meegegeven dan wordt
    // vandaar gezocht (blader)
    if (laatsteAanvraagNr != null) {
      rGeg.setAanvrnrrybl(laatsteAanvraagNr);
    }

    p1914f1.setAanvrrybzgeg(rGeg);

    setRequest(new Proces(RdwProces.P1914_F1, p1914f1));

    return p1914f1;
  }
}
