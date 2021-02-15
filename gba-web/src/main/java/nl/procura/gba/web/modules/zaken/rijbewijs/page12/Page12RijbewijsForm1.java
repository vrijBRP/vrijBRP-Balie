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

package nl.procura.gba.web.modules.zaken.rijbewijs.page12;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagReden;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType;

public class Page12RijbewijsForm1 extends RdwReadOnlyForm {

  Page12RijbewijsForm1(RijbewijsAanvraagAntwoord antwoord, String belemmering) {

    setReadonlyAsText(true);
    setReadThrough(true);
    setColumnWidths("160px", "");

    initForm();

    Page12RijbewijsBean1 b = new Page12RijbewijsBean1();

    b.setStatus(astr(RijbewijsStatusType.get(antwoord.getStat_gegevens().getStatusCode())));
    b.setSoort(astr(RijbewijsAanvraagSoort.get(antwoord.getAanvr_gegevens().getSoort())));
    b.setReden(astr(RijbewijsAanvraagReden.get(antwoord.getAanvr_gegevens().getReden())));
    b.setAanvraagNr(antwoord.getAanvr_gegevens().getAanvraag_nr());
    b.setNieuwRbwNr(antwoord.getStat_gegevens().getRBW_nr());

    String huidig = antwoord.getAanvr_gegevens().getVervangt_rijb();

    if (pos(antwoord.getRijb_gegevens().getRijb_nr())
        && antwoord.getAanvr_gegevens().getReden() != RijbewijsAanvraagReden.WEGENS_VERLIES_OF_DIEFSTAL.getCode()) {
      huidig += setClass(false, " (Dit document dient bij de uitreiking te worden ingeleverd)");
    }

    b.setHuidigRbwNr(huidig);

    b.setBelemmering(belemmering);

    setBean(b);
  }

  @Override
  public Page12RijbewijsBean1 getBean() {
    return (Page12RijbewijsBean1) super.getBean();
  }

  public void initForm() {
  }
}
