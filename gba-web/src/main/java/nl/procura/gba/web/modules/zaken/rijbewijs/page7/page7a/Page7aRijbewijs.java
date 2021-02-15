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

import static nl.procura.gba.web.modules.zaken.rijbewijs.page7.page7a.Page7aRijbewijsBean1.*;

import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord.Cat_gegevens;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geregistreerde aanvraag 2
 */

public class Page7aRijbewijs extends RijbewijsPage {

  private final Cat_gegevens c;

  public Page7aRijbewijs(Cat_gegevens c) {

    super("Categoriegegevens inzien");

    this.c = c;

    setMargin(true);

    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(new Page7aRijbewijsForm1(c) {

        @Override
        protected void initForm() {

          setOrder(CAT);

          super.initForm();
        }

      });

      addComponent(new Page7aRijbewijsForm1(c) {

        @Override
        protected void initForm() {

          setOrder(TOELICHTING1, STATUS1, AFGIFTE1, EINDE, BEPERKINGEN);

          setCaption("Geschikheidsverklaring");

          super.initForm();
        }

      });

      addComponent(new Page7aRijbewijsForm1(c) {

        @Override
        protected void initForm() {

          setOrder(TOELICHTING2, STATUS2, AFGIFTE2, AUTOMAAT, BEPERKING);

          setCaption("Rijvaardigheidsverklaring");

          super.initForm();
        }

      });
    }

    super.event(event);
  }
}
