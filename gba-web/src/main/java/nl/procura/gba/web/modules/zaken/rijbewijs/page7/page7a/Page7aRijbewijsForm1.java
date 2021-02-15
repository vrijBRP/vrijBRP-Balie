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

package nl.procura.gba.web.modules.zaken.rijbewijs.page7.page7a;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord.Cat_gegevens;

public class Page7aRijbewijsForm1 extends RdwReadOnlyForm {

  Page7aRijbewijsForm1(Cat_gegevens c) {

    setReadonlyAsText(true);
    initForm();

    Page7aRijbewijsBean1 b = new Page7aRijbewijsBean1();

    b.setCat(c.getCategorie());
    b.setMelding(c.getMelding());

    b.setToelichting1(c.getGesch_gegevens().getToelichting());
    b.setStatus1(c.getGesch_gegevens().getStatus());
    b.setAfgifte1(c.getGesch_gegevens().getDatum_afgifte());
    b.setEinde(c.getGesch_gegevens().getDatum_einde());
    b.setBeperkingen(c.getGesch_gegevens().getBeperking());

    b.setToelichting2(c.getRijv_gegevens().getToelichting());
    b.setStatus2(c.getRijv_gegevens().getStatus());
    b.setAfgifte2(c.getRijv_gegevens().getDatum_afgifte());
    b.setAutomaat(c.getRijv_gegevens().getAutomaat());
    b.setBeperking(c.getRijv_gegevens().getBeperking());

    setBean(b);
  }

  protected void initForm() {
  }
}
