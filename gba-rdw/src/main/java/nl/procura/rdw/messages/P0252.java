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

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.math.BigInteger;

import nl.procura.rdw.functions.Proces;
import nl.procura.rdw.functions.RdwMessage;
import nl.procura.rdw.functions.RdwProces;
import nl.procura.rdw.processen.p0252.f01.NATPRYBMAATR;
import nl.procura.rdw.processen.p0252.f01.NATPZOEKGEG;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.validation.Bsn;

/**
 * Raadplegen aanvraag
 */
public class P0252 extends RdwMessage {

  public P0252() {
  }

  public NATPRYBMAATR newF1(String bsn) {

    NATPRYBMAATR o = new NATPRYBMAATR();
    NATPZOEKGEG n = new NATPZOEKGEG();
    Bsn bsn2 = new Bsn(bsn);

    if (!bsn2.isCorrect()) {
      throw new ProException(WARNING, "Deze persoon heeft geen geldig BSN. De raadpleging is niet mogelijk.");
    }

    n.setFiscnrnpa(BigInteger.valueOf(bsn2.getLongBsn()));
    o.setNatpzoekgeg(n);
    setRequest(new Proces(RdwProces.P0252_F1, o));

    return o;
  }

  public nl.procura.rdw.processen.p0252.f02.NATPRYBMAATR newF2(String bsn) {

    nl.procura.rdw.processen.p0252.f02.NATPRYBMAATR o = new nl.procura.rdw.processen.p0252.f02.NATPRYBMAATR();
    Bsn bsn2 = new Bsn(bsn);

    nl.procura.rdw.processen.p0252.f02.NATPZOEKGEG n = new nl.procura.rdw.processen.p0252.f02.NATPZOEKGEG();
    n.setFiscnrnpa(BigInteger.valueOf(bsn2.getLongBsn()));
    o.setNatpzoekgeg(n);
    setRequest(new Proces(RdwProces.P0252_F2, o));

    return o;
  }
}
