/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.kassa.page2;

import static java.util.Collections.singletonList;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.modules.zaken.kassa.page1.Page1Kassa;
import nl.procura.gba.web.services.beheer.kassa.KassaProduct;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

/**
 * Kassa
 */
public class Page2Kassa extends ButtonPageTemplate {

  private Page2KassaForm1 form1 = null;
  private Page2KassaForm2 form2 = null;

  public Page2Kassa() {

    addButton(buttonPrev);
    addButton(buttonSave);
    getButtonLayout().setWidth("100%");
    getButtonLayout().setExpandRatio(buttonPrev, 1f);

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Page2KassaForm1(getPl());
      form2 = new Page2KassaForm2();

      addComponent(form1);
      addComponent(form2);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {

    getNavigation().removeOtherPages();
    getNavigation().addPage(Page1Kassa.class);
  }

  @Override
  public void onSave() {

    form2.commit();

    KassaProduct kp = form2.getKassaProduct();

    getServices().getKassaService().addToWinkelwagen("", singletonList(kp));

    new Message(getWindow(), "Kassaproduct is opgeslagen.", Message.TYPE_SUCCESS);

    onPreviousPage();
  }
}
