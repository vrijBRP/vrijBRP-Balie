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

package nl.procura.gbaws.web.vaadin.module.tables.page3;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gbaws.web.vaadin.module.MainModuleContainer;
import nl.procura.gbaws.web.vaadin.module.ModuleTemplate;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ModuleTablesUpdateWindow extends ModalWindow {

  public ModuleTablesUpdateWindow() {
    setCaption("Updaten (Druk op escape om te sluiten)");
    setWidth("400px");
    setHeight("400px");
  }

  @Override
  public void attach() {

    super.attach();

    VerticalLayout layout = new VerticalLayout();

    layout.setMargin(false);

    MainModuleContainer mainModule = new MainModuleContainer();

    layout.addComponent(mainModule);

    mainModule.getNavigation().addPage(new Module());

    setContent(layout);
  }

  public class Module extends ModuleTemplate {

    public Module() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {

        getPages().getNavigation().goToPage(new Page3Tables());
      }
    }
  }
}
