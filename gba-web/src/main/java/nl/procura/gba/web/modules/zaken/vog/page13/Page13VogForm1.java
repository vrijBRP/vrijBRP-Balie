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

package nl.procura.gba.web.modules.zaken.vog.page13;

import static nl.procura.gba.web.modules.zaken.vog.page13.Page13VogBean1.BASIS;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;

public class Page13VogForm1 extends GbaForm<Page13VogBean1> {

  public Page13VogForm1(VogAanvraag aanvraag) {

    setCaption("Screening / functiegebieden");
    setOrder(BASIS);
    setColumnWidths(WIDTH_130, "");

    Page13VogBean1 b = new Page13VogBean1();
    b.setBasis(
        (aanvraag.getScreening().getFunctiegebieden().size() > 0) ? ScreeningsType.FUNCTIEGEBIED
            : ScreeningsType.SCREENINGSPROFIEL);
    setBean(b);
  }
}
