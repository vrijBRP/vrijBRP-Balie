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

package nl.procura.gba.web.modules.zaken.rijbewijs.page5.page5a;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusRijvaardigheid;
import nl.procura.rdw.processen.p1651.f08.CATAANRYBGEG;

public class Page5aRijbewijsForm1 extends RdwReadOnlyForm {

  Page5aRijbewijsForm1(CATAANRYBGEG c) {

    setReadonlyAsText(true);
    initForm();

    Page5aRijbewijsBean1 b = new Page5aRijbewijsBean1();

    b.setCat(c.getRybcategorie());
    b.setMelding(c.getCatmelding());
    b.setToelichting1(c.getGeschverklgeg().getToelblgverkl());

    String status1 = c.getGeschverklgeg().getStatusgverkl();

    if (fil(status1)) {
      status1 += ": " + RijbewijsStatusRijvaardigheid.get(c.getGeschverklgeg().getStatusgverkl()).getOms();
    }

    b.setStatus1(status1);
    b.setAfgifte1(date2str(astr(c.getGeschverklgeg().getAfgdatgverkl())));
    b.setEinde(date2str(astr(c.getGeschverklgeg().getEdatgverkl())));
    b.setBeperkingen(c.getGeschverklgeg().getBeperkgverkl());

    b.setToelichting2(c.getGeschverklgeg().getToelblgverkl());

    String status2 = c.getRyvverklgeg().getStatusrverkl();

    if (fil(status2)) {
      status2 += ": " + RijbewijsStatusRijvaardigheid.get(c.getRyvverklgeg().getStatusrverkl()).getOms();
    }

    b.setStatus2(status2);
    b.setAfgifte2(date2str(astr(c.getRyvverklgeg().getAfgdatrverkl())));
    b.setAutomaat(c.getRyvverklgeg().getAutindrverkl());
    b.setBeperking(c.getRyvverklgeg().getCatbeperkind());

    setBean(b);
  }

  protected void initForm() {
  }
}
