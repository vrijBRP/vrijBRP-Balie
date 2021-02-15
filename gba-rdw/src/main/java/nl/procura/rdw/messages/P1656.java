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
import nl.procura.rdw.functions.RdwMessage;
import nl.procura.rdw.functions.RdwProces;
import nl.procura.rdw.processen.p1656.f01.AANVRRYBKGEG;
import nl.procura.rdw.processen.p1656.f01.AANVRRYBKRT;
import nl.procura.rdw.processen.p1656.f01.STATRYBKGEG;

/**
 * Muteren status aanvraagbericht
 */
public class P1656 extends RdwMessage {

  public P1656() {
  }

  private AANVRRYBKRT setUp(long statusCode, long userId) {

    AANVRRYBKRT p1656f1 = new AANVRRYBKRT();
    STATRYBKGEG s = new STATRYBKGEG();
    AANVRRYBKGEG a = new AANVRRYBKGEG();

    p1656f1.setAanvrrybkgeg(a);
    p1656f1.setStatrybkgeg(s);

    setRequest(new Proces(RdwProces.P1656_F1, p1656f1));

    String id = "WEB " + userId;
    p1656f1.getStatrybkgeg().setGemrefsrybk(id);
    p1656f1.getStatrybkgeg().setStatcoderybk(BigInteger.valueOf(statusCode));

    return p1656f1;
  }

  /**
   * Antwoord op basis van eerste aanvraag (1653)
   */
  public AANVRRYBKRT newF1(long statusCode, Boolean spoed, long userId, P1653 p1653) {

    nl.procura.rdw.processen.p1653.f02.AANVRRYBKRT antwoord = (nl.procura.rdw.processen.p1653.f02.AANVRRYBKRT) p1653
        .getResponse().getObject();

    AANVRRYBKRT p1656f1 = setUp(statusCode, userId);

    p1656f1.getAanvrrybkgeg().setAanvrnrrybk(antwoord.getAanvrrybkgeg().getAanvrnrrybk());
    p1656f1.getAanvrrybkgeg().setAutorarybk(antwoord.getAanvrrybkgeg().getAutorarybk());
    p1656f1.getAanvrrybkgeg().setGemlocarybk(antwoord.getAanvrrybkgeg().getGemlocarybk());

    if (spoed != null) {
      p1656f1.getAanvrrybkgeg().setSpoedafhind(spoed ? "J" : "N");
    }

    String id = "WEB " + userId;
    p1656f1.getStatrybkgeg().setGemrefsrybk(id);
    p1656f1.getStatrybkgeg().setStatcoderybk(BigInteger.valueOf(statusCode));

    return p1656f1;
  }

  /**
   * Antwoord op basis van eerste aanvraag (1654) (Omwisseling)
   */
  public AANVRRYBKRT newF1(long statusCode, Boolean spoed, long userId, P1654 p1654) {

    nl.procura.rdw.processen.p1654.f02.AANVRRYBKRT antwoord = (nl.procura.rdw.processen.p1654.f02.AANVRRYBKRT) p1654
        .getResponse().getObject();

    AANVRRYBKRT p1656f1 = setUp(statusCode, userId);

    p1656f1.getAanvrrybkgeg().setAanvrnrrybk(antwoord.getAanvrrybkgeg().getAanvrnrrybk());
    p1656f1.getAanvrrybkgeg().setAutorarybk(antwoord.getAanvrrybkgeg().getAutorarybk());
    p1656f1.getAanvrrybkgeg().setGemlocarybk(antwoord.getAanvrrybkgeg().getGemlocarybk());

    if (spoed != null) {
      p1656f1.getAanvrrybkgeg().setSpoedafhind(spoed ? "J" : "N");
    }

    return p1656f1;
  }

  /**
   * Antwoord op basis van laatste aanvraag (1658)
   */
  public AANVRRYBKRT newF1(long statusCode, Boolean spoed, long userId, P1658 p1658) {

    nl.procura.rdw.processen.p1658.f02.AANVRRYBKRT antwoord = (nl.procura.rdw.processen.p1658.f02.AANVRRYBKRT) p1658
        .getResponse().getObject();

    AANVRRYBKRT p1656f1 = setUp(statusCode, userId);

    p1656f1.getAanvrrybkgeg().setAanvrnrrybk(antwoord.getAanvrrybkgeg().getAanvrnrrybk());
    p1656f1.getAanvrrybkgeg().setAutorarybk(antwoord.getAanvrrybkgeg().getAutorarybk());
    p1656f1.getAanvrrybkgeg().setGemlocarybk(antwoord.getAanvrrybkgeg().getGemlocarybk());

    if (spoed != null) {
      p1656f1.getAanvrrybkgeg().setSpoedafhind(spoed ? "J" : "N");
    }

    return p1656f1;
  }
}
