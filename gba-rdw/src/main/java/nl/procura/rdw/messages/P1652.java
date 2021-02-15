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
import nl.procura.rdw.functions.Voorletters;
import nl.procura.rdw.processen.p1651.f09.AANRYBKOVERZ;
import nl.procura.rdw.processen.p1652.f01.NATPOPNAAM;
import nl.procura.rdw.processen.p1652.f01.PERSZOEKGEG;

/**
 * Naam        : ZOEK NATUURLYK PERSOON OP NAAM EN GEBOORTEDATUM
 * Omschrijving: Bericht voor het raadplegen van persoonsgegevens op naam en
 * geboortedatum.
 */
public class P1652 extends RdwMessage {

  public P1652() {
  }

  public void newF1(P1651 p1651) { // Als er sprake is van meerdere personen

    NATPOPNAAM p1652f1 = new NATPOPNAAM();

    AANRYBKOVERZ p1651f8 = (AANRYBKOVERZ) p1651.getResponse().getObject();

    String gesl = p1651f8.getNatpersoongeg().getGeslnaamnatp();
    String voorl = p1651f8.getNatpersoongeg().getVoorletnatp();
    BigInteger geb = p1651f8.getNatpersoongeg().getGebdatnatp();

    p1652f1.setPerszoekgeg(new PERSZOEKGEG());
    p1652f1.getPerszoekgeg().setGeslnaamnpa(gesl);
    p1652f1.getPerszoekgeg().setVoorletnpa(Voorletters.getVoorletters(voorl, true));
    p1652f1.getPerszoekgeg().setGebdatnpa(geb);

    setRequest(new Proces(RdwProces.P1652_F1, p1652f1));
  }
}
