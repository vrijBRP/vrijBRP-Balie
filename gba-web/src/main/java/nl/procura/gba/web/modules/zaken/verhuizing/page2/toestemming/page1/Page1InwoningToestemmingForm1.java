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

package nl.procura.gba.web.modules.zaken.verhuizing.page2.toestemming.page1;

import static nl.procura.gba.web.modules.zaken.verhuizing.page2.toestemming.page1.Page1InwoningToestemmingBean1.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.zaken.verhuizing.page2.toestemming.page1.InwoningAangifteContainer.InwoningAangifte;
import nl.procura.gba.web.modules.zaken.verhuizing.page2.toestemming.page1.InwoningToestemmingContainer.InwoningToestemming;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;

public class Page1InwoningToestemmingForm1 extends GbaForm<Page1InwoningToestemmingBean1> {

  public Page1InwoningToestemmingForm1(VerhuisAanvraag aanvraag) {

    setCaption("Toestemming / Aangifte");
    setOrder(TOESTEMMING, AANGIFTE, AANGIFTETEKST);
    setColumnWidths(WIDTH_130, "");

    Page1InwoningToestemmingBean1 b = new Page1InwoningToestemmingBean1();

    switch (aanvraag.getToestemminggever().getToestemmingStatus()) {
      case JA:
        b.setToestemming(InwoningToestemming.JA);
        break;

      case NEE:
        b.setToestemming(InwoningToestemming.NEE);
        break;

      default:
        b.setToestemming(InwoningToestemming.ONBEKEND);
        break;
    }

    switch (aanvraag.getToestemminggever().getAangifteStatus()) {
      case GEACCEPTEERD:
      case GEACCEPTEERD_ZONDER_TOESTEMMING:
        b.setAangifte(InwoningAangifte.JA);
        break;

      case NIET_GEACCEPTEERD:
        b.setAangifte(InwoningAangifte.NEE);
        break;

      default:
        b.setAangifte(InwoningAangifte.ONBEKEND);
        break;
    }

    setBean(b);

    updateAangifte();
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);
    getField(TOESTEMMING).addListener((ValueChangeListener) event -> updateAangifte());
  }

  private void updateAangifte() {

    switch ((InwoningToestemming) getField(TOESTEMMING).getValue()) {

      case JA:
        getField(AANGIFTETEKST).setVisible(true);
        getField(AANGIFTE).setVisible(false);
        getField(AANGIFTE).setValue(InwoningAangifte.JA);
        break;

      default:
        getField(AANGIFTETEKST).setVisible(false);
        getField(AANGIFTE).setVisible(true);
        break;
    }

    repaint();
  }
}
