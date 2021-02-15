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

import nl.procura.rdw.functions.Proces;
import nl.procura.rdw.functions.RdwAanvraagMessage;
import nl.procura.rdw.functions.RdwProces;
import nl.procura.rdw.processen.p1660.f01.AANVRRYBKGEG;
import nl.procura.rdw.processen.p1660.f01.AANVRRYBKRT;

/**
 * Raadplegen aanvraag rijbewijskaart op rijbewijsnummer
 */
public class P1660 extends RdwAanvraagMessage {

  public AANVRRYBKRT newF1(String rbwNr) {

    AANVRRYBKRT p1660f1 = new AANVRRYBKRT();

    AANVRRYBKGEG n = new AANVRRYBKGEG();

    n.setRybnrvervang(rbwNr);

    p1660f1.setAanvrrybkgeg(n);

    setRequest(new Proces(RdwProces.P1660_F1, p1660f1));

    return p1660f1;
  }
}
