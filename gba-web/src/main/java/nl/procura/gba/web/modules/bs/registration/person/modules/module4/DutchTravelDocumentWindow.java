/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.bs.registration.person.modules.module4;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.HLayout;

public class DutchTravelDocumentWindow extends GbaModalWindow {

  public DutchTravelDocumentWindow(DutchTravelDocumentForm form, String width, String caption, String title) {
    super(caption + " (Druk op escape om te sluiten)", width);
    addComponent(new MainModuleContainer(false, new Page(form, title)));
  }

  private static final class Page extends NormalPageTemplate {

    private final DutchTravelDocumentForm form;

    private Page(DutchTravelDocumentForm form, String title) {
      this.form = form;
      addButton(buttonSave);
      addButton(buttonClose);
      final H2 h2 = new H2(title);
      final HLayout buttonLayout = getButtonLayout();
      buttonLayout.addComponent(h2, buttonLayout.getComponentIndex(buttonSave));
      buttonLayout.setExpandRatio(h2, 1F);
      buttonLayout.setWidth("100%");
      addComponent(form);
    }

    private void onBasisregister() {
    }

    @Override
    public void onSave() {
      super.onSave();
      form.commit(this::onClose);
    }

    @Override
    public void onClose() {
      super.onClose();
      getWindow().closeWindow();
    }
  }
}
