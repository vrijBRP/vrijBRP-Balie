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

package nl.procura.gba.web.modules.zaken.kassa.page3;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.kassa.KassaProduct;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Kassa
 */
public class Page3Kassa extends NormalPageTemplate {

  private Page3KassaTable table = null;

  public Page3Kassa(String title) {
    super(fil(title) ? title : "");
    addButton(buttonPrev);
    addButton(buttonSave);
    addButton(buttonClose);
    getButtonLayout().setWidth("100%");
    getButtonLayout().setExpandRatio(buttonSave, 1f);
    buttonSave.setCaption("Vervang (F9)");
    setInfo("Bundels",
        "2 of meer producten zijn te combineren tot een bundel.</br>Selecteer een bundel en druk op <b>vervang</b>.");

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      table = new Page3KassaTable();
      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {
    if (!table.isSelectedRecords()) {
      throw new ProException(ProExceptionSeverity.WARNING, "Select een record");
    }

    getServices().getKassaService().vervangBundel(table.getSelectedRecord().getObject(KassaProduct.class));
    onPreviousPage();
  }
}
