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

package nl.procura.gba.web.modules.zaken.rijbewijs.page7;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page7.Page7RijbewijsBean1.*;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.modules.zaken.rijbewijs.page7.page7a.Page7aRijbewijs;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord.Cat_gegevens;
import nl.procura.vaadin.component.layout.Fieldset;

public class Page7RijbewijsSubpage2 extends VerticalLayout {

  public Page7RijbewijsSubpage2(final Page7Rijbewijs page7Rijbewijs, RijbewijsAanvraagAntwoord a) {

    Page7RijbewijsTable1 table1 = new Page7RijbewijsTable1(a) {

      @Override
      public void onClick(Record record) {

        page7Rijbewijs.getNavigation().goToPage(new Page7aRijbewijs((Cat_gegevens) record.getObject()));
      }
    };

    addComponent(new Page7RijbewijsForm1(a) {

      @Override
      protected void initForm() {

        setCaption("Aanvraag rijbewijskaartgegevens");

        setOrder(AANVRAAGNR, DATUMTIJDAANVRAAG, SOORT, REDEN, SPOED, BESTENDIG, VERVANGT, AUTORITEIT, LOC,
            COLLO);
      }

    });

    addComponent(new Fieldset("Rijbewijscategorieën", table1));
  }
}
