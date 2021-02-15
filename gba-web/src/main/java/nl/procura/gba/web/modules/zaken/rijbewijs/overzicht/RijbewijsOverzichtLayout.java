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

package nl.procura.gba.web.modules.zaken.rijbewijs.overzicht;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsForm1;
import nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2RijbewijsTabel1;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.vaadin.component.layout.Fieldset;

public class RijbewijsOverzichtLayout extends VerticalLayout {

  public RijbewijsOverzichtLayout(RijbewijsAanvraag aanvraag) {

    setSpacing(true);

    addComponent(new Page2RijbewijsForm1(aanvraag) {

      @Override
      public void zoekRijbewijsNummer() {

        if (update()) {
          success("De zaak is bijgewerkt met het nieuwe rijbewijsnummer.");
        }
      }
    });

    addComponent(new Fieldset("Statussen toegevoegd vanuit Proweb personen", new Page2RijbewijsTabel1(aanvraag)));
  }

  @SuppressWarnings("unused")
  protected void success(String message) {
  } // Override
}
