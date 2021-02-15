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
import nl.procura.rdw.processen.p1653.f01.*;

/**
 * Naam        : ONDERHOUD AANVRAAG RYBEWYSKAART EN STATUS
 * Omschrijving: Bericht voor het raadplegen van en het onderhoud op een aanvraag voor
 * een rijbewijskaart, de bijbehorende laatste status en alle categorieën.
 */
public class P1653 extends RdwAanvraagMessage {

  public P1653() {
  }

  public AANVRRYBKRT newF1(P1651 p1651) { // Als er sprake is van meerdere personen

    AANVRRYBKRT p1653f1 = new AANVRRYBKRT();
    RYBGEG g = new RYBGEG();
    NATPERSOONGEG n = new NATPERSOONGEG();
    ADRESNATPGEG a = new ADRESNATPGEG();
    AANVRRYBKGEG r = new AANVRRYBKGEG();

    AANRYBKOVERZ p1651f8 = (AANRYBKOVERZ) p1651.getResponse().getObject();

    n.setNatperssl(p1651f8.getNatpersoongeg().getNatperssl());

    p1653f1.setAanvrrybkgeg(r);
    p1653f1.setAdresnatpgeg(a);
    p1653f1.setNatpersoongeg(n);
    p1653f1.setRybgeg(g);

    setRequest(new Proces(RdwProces.P1653_F1, p1653f1));

    return p1653f1;
  }
}
