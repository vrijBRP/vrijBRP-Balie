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

import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord;

public class Page7RijbewijsSubpage1 extends VerticalLayout {

  public Page7RijbewijsSubpage1(RijbewijsAanvraagAntwoord a) {

    addComponent(new Page7RijbewijsForm1(a) {

      @Override
      protected void initForm() {

        setCaption("Status aanvraag");

        setOrder(STATUS, DATUMTIJDSTATUS, GEMREF, RDWNR);
      }

    });

    addComponent(new Page7RijbewijsForm1(a) {

      @Override
      protected void initForm() {

        setCaption("Huidige rijbewijsgegevens");

        setOrder(RIJBEWIJSNUMMER, AFGIFTE, VERLIESDIEFSTAL, DEND);
      }

    });

    addComponent(new Page7RijbewijsForm1(a) {

      @Override
      protected void initForm() {

        setCaption("Persoonsgegevens");

        setOrder(CRBSLEUTEL, BSN, ANR, NAAM, VOORNAMEN, VOORVOEGSEL, GEBOREN, GESLACHT, NAAMGEBRUIK, BURGSTAAT,
            PARTNER);
      }
    });

    addComponent(new Page7RijbewijsForm1(a) {

      @Override
      protected void initForm() {

        setCaption("Adresgegevens");

        setOrder(ADRES, PCWOONPLAATS);
      }

    });
  }
}
