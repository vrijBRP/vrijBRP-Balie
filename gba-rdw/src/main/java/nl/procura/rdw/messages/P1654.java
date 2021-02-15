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
import nl.procura.rdw.processen.p1651.f08.AANRYBKOVERZ;
import nl.procura.rdw.processen.p1654.f01.AANVRRYBKGEG;
import nl.procura.rdw.processen.p1654.f01.AANVRRYBKRT;
import nl.procura.rdw.processen.p1654.f01.ADRESNATPGEG;
import nl.procura.rdw.processen.p1654.f01.NATPERSOONGEG;

/**
 * Registreren byzondere aanvraag rybewyskaart (omw)
 */
public class P1654 extends RdwAanvraagMessage {

  public P1654() {
  }

  public AANVRRYBKRT newF1(P1651 p1651) { // Als er sprake is van meerdere personen

    AANVRRYBKRT p1654f1 = new AANVRRYBKRT();
    NATPERSOONGEG n = new NATPERSOONGEG();
    ADRESNATPGEG a = new ADRESNATPGEG();
    AANVRRYBKGEG r = new AANVRRYBKGEG();

    getNatSleutelFunctie8(p1651, n);

    p1654f1.setAanvrrybkgeg(r);
    p1654f1.setAdresnatpgeg(a);
    p1654f1.setNatpersoongeg(n);

    setRequest(new Proces(RdwProces.P1654_F1, p1654f1));

    return p1654f1;
  }

  /**
   * Zet Natuurlijke persoonssleutel bij functie 8
   */
  private void getNatSleutelFunctie8(P1651 p1651, NATPERSOONGEG n) {
    Object object = p1651.getResponse().getObject();
    if (object instanceof AANRYBKOVERZ) {
      AANRYBKOVERZ p1651f8 = (AANRYBKOVERZ) object;
      n.setNatperssl(p1651f8.getNatpersoongeg().getNatperssl());
    }
  }
}
