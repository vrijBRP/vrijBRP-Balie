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

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import java.math.BigInteger;

import nl.procura.rdw.functions.Proces;
import nl.procura.rdw.functions.RdwMessage;
import nl.procura.rdw.functions.RdwProces;
import nl.procura.rdw.functions.Voorletters;
import nl.procura.rdw.processen.p1651.f01.AANRYBKOVERZ;
import nl.procura.rdw.processen.p1651.f01.NATPERSOONGEG;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

/**
 * Raadplegen persoonsgeg op A-nr, Sofi-nr of ryb-nr
 */
public class P1651 extends RdwMessage {

  public P1651() {
  }

  /**
   * Raadplegen persoonsgeg op A-nr, Sofi-nr of ryb-nr
   */
  public AANRYBKOVERZ newF1(String bsn, String anr, long dGeb, String gesl, String voorn) {

    AANRYBKOVERZ o = new AANRYBKOVERZ();

    NATPERSOONGEG n = new NATPERSOONGEG();

    Bsn bsn2 = new Bsn(bsn);
    Anr anr2 = new Anr(anr);
    String voorl = Voorletters.getVoorletters(voorn, false);

    if (bsn2.isCorrect()) {
      n.setFiscnrnatp(BigInteger.valueOf(bsn2.getLongBsn()));
    }

    if (anr2.isCorrect()) {
      n.setGbanrnatp(BigInteger.valueOf(anr2.getLongAnummer()));
    }

    if (aval(dGeb) >= 0) {
      n.setGebdatnatp(BigInteger.valueOf(dGeb));
    }

    if (fil(voorl)) {
      n.setVoorletnatp(voorl);
    }

    n.setGeslnaamnatp(gesl);
    n.setVoornaamnatp(voorn.split("\\s+")[0]);

    o.setNatpersoongeg(n);

    setRequest(new Proces(RdwProces.P1651_F1, o));

    return o;
  }

  /**
   * Opvragen persoons-, soort aanvraag- en categorie-gegevens op Natuurlijk
   * Persoonssleutel
   */
  public void newF2(String natperssl) {

    nl.procura.rdw.processen.p1651.f02.AANRYBKOVERZ o = new nl.procura.rdw.processen.p1651.f02.AANRYBKOVERZ();
    nl.procura.rdw.processen.p1651.f02.NATPERSOONGEG n = new nl.procura.rdw.processen.p1651.f02.NATPERSOONGEG();

    n.setNatperssl(natperssl);

    o.setNatpersoongeg(n);

    setRequest(new Proces(RdwProces.P1651_F2, o));
  }
}
