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

package nl.procura.gba.web.modules.zaken.rijbewijs.errorpage;

import static nl.procura.gba.web.modules.zaken.rijbewijs.errorpage.RijbewijsErrorBean1.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.rdw.functions.ProcesMelding;

class RijbewijsErrorForm1 extends RdwReadOnlyForm {

  RijbewijsErrorForm1(ProcesMelding m) {

    setCaption("Foutmelding");

    setOrder(PROCES, SYSTEEM, MELDING, SOORTMELDING, MELDINGVAR);

    setColumnWidths(WIDTH_130, "");

    RijbewijsErrorBean1 b = new RijbewijsErrorBean1();

    b.setProces(m.getProces().p + " - " + m.getProces().f);
    b.setSysteem(m.getSysteem());
    b.setMelding(m.getNr() + ": " + m.getMeldingKort());
    b.setSoortMelding(astr(m.getMeldingSoort()));
    b.setMeldingVar(fil(m.getMeldingVar()) ? m.getMeldingVar() : "Niet van toepassing");

    setBean(b);
  }
}
