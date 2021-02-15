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

package nl.procura.gba.web.modules.zaken.woningkaart.window;

import com.vaadin.ui.Alignment;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class WoningObjectWindow extends GbaModalWindow {

  private Address address;

  public WoningObjectWindow(Address address) {
    super("Informatie verblijfsobject (Escape om te sluiten)", "900px");
    this.address = address;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(true, new Page()));
  }

  public class Page extends NormalPageTemplate {

    public Page() {
      setMargin(false);
      addButton(BUTTON_CLOSE);
      getButtonLayout().setWidth("100%");
      getButtonLayout().addComponent(new H2(address.getLabel()), 0);
      getButtonLayout().setComponentAlignment(BUTTON_CLOSE, Alignment.MIDDLE_RIGHT);
    }

    @Override
    public void event(PageEvent event) {
      if (event.isEvent(InitPage.class)) {
        addExpandComponent(new WoningObjectLayout(getApplication(), address));
      }
      super.event(event);
    }
  }
}
