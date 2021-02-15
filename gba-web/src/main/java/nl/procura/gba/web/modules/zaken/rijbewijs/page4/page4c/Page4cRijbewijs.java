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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4c;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4RijbewijsBean1.*;

import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4RijbewijsTable2;
import nl.procura.rdw.processen.p0252.f08.UITGRYBGEG;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Uitsplitsing van de maatregel
 */
public class Page4cRijbewijs extends RijbewijsPage {

  private final UITGRYBGEG c;

  Page4cRijbewijs(UITGRYBGEG c) {

    super("Uitgegeven rijbewijs");
    this.c = c;
    setMargin(true);
    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      Page4cRijbewijsForm1 rijbewijsForm1 = new Page4cRijbewijsForm1(c) {

        @Override
        protected void initForm() {
          setCaption("Rijbewijsgegevens");
          setOrder(AFGIFTE, NUMMER, AFGIFTE2, VERVANGT, VERLIES, VERLIES2, ERKEND);
        }
      };

      addComponent(rijbewijsForm1);
      addComponent(new Page4cRijbewijsForm2(c));
      addComponent(new Fieldset("Technische voorziening", new Page4RijbewijsTable2(c)));
    }

    super.event(event);
  }
}
