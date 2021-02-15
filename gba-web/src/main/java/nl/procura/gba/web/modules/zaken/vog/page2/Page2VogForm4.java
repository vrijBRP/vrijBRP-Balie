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

package nl.procura.gba.web.modules.zaken.vog.page2;

import static nl.procura.gba.web.modules.zaken.vog.page2.Page2VogBean4.*;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogAanvraagScreening;

public class Page2VogForm4 extends Page2VogForm {

  public Page2VogForm4(VogAanvraag aanvraag) {

    setCaption("Screening");
    setOrder(SCREENING, PROFIEL, FUNCTIEGEBIED, OMSTANDIGHEDEN);
    setColumnWidths(WIDTH_130, "");

    Page2VogBean4 b = new Page2VogBean4();

    VogAanvraagScreening s = aanvraag.getScreening();

    if (s.getProfiel() != null && pos(s.getProfiel().getVogProfTab())) {
      b.setProfiel(s.getProfiel().getVogProfTab() + " - " + s.getProfiel().getOms());
    } else {
      b.setProfiel("Niet van toepassing");
    }

    b.setOmstandigheden(opm(s.getOmstandighedenTekst()));

    String screenbasis = "";

    if (s.isGebruikProfiel()) {
      screenbasis = "Op basis van een screeningsprofiel";
    }

    if (s.isGebruikFunctiegebied()) {
      screenbasis = "Op basis van functiegebieden";
    }

    if (pos(s.getFunctiegebieden().size())) {
      b.setFunctiegebied(s.getFunctiegebiedenSamenvatting());
    } else {
      b.setFunctiegebied("Niet van toepassing");
    }

    b.setScreening(screenbasis);

    setBean(b);
  }
}
