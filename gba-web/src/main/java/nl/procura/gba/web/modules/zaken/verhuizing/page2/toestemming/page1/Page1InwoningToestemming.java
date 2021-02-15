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

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisToestemminggever.AangifteStatus;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisToestemminggever.ToestemmingStatus;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Inwoning toestemming
 */
public class Page1InwoningToestemming extends ButtonPageTemplate {

  private final VerhuisAanvraag         aanvraag;
  private Page1InwoningToestemmingForm1 form1 = null;

  public Page1InwoningToestemming(VerhuisAanvraag aanvraag) {

    addButton(buttonSave);
    addButton(buttonClose);
    getButtonLayout().setWidth("100%");
    getButtonLayout().setExpandRatio(buttonSave, 1f);

    setSpacing(true);

    this.aanvraag = aanvraag;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Page1InwoningToestemmingForm1(aanvraag);

      addComponent(form1);
    }

    super.event(event);
  }

  @Override
  public void onClose() {

    getWindow().closeWindow();

    super.onClose();
  }

  @Override
  public void onSave() {

    form1.commit();

    switch (form1.getBean().getToestemming()) {
      case JA:
        aanvraag.getToestemminggever().setToestemmingStatus(ToestemmingStatus.JA);
        break;

      case NEE:
        aanvraag.getToestemminggever().setToestemmingStatus(ToestemmingStatus.NEE);
        break;

      case ONBEKEND:
        aanvraag.getToestemminggever().setToestemmingStatus(ToestemmingStatus.NIET_INGEVULD);
        break;

      default:
        break;
    }

    switch (form1.getBean().getAangifte()) {
      case JA:
        aanvraag.getToestemminggever().setAangifteStatus(AangifteStatus.GEACCEPTEERD);
        break;

      case NEE:
        aanvraag.getToestemminggever().setAangifteStatus(AangifteStatus.NIET_GEACCEPTEERD);
        break;

      case ONBEKEND:
        aanvraag.getToestemminggever().setAangifteStatus(AangifteStatus.NIET_INGEVULD);
        break;

      default:
        break;
    }

    getServices().getVerhuizingService().saveToestemming(aanvraag);

    super.onSave();

    onClose();
  }
}
