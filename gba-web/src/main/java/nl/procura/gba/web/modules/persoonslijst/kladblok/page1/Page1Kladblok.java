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

package nl.procura.gba.web.modules.persoonslijst.kladblok.page1;

import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.modules.persoonslijst.overig.listpage.PlListPage;

public class Page1Kladblok extends PlListPage {

  public Page1Kladblok(BasePLCat soort) {

    super("Inschrijving");

    CssLayout l = new CssLayout();
    l.setStyleName("kladblok");
    l.setSizeFull();

    StringBuilder sb = new StringBuilder();

    for (BasePLSet set : soort.getSets()) {

      for (BasePLRec r : set.getRecs()) {

        String line = r.getElemVal(GBAElem.KLADBLOK_REGEL).getDescr();

        if (fil(line)) {
          sb.append("<div class='kladblok_line'>" + line + "</div>");
        }
      }
    }

    l.addComponent(new Label(sb.toString(), Label.CONTENT_XHTML));
    addComponent(l);
  }
}
