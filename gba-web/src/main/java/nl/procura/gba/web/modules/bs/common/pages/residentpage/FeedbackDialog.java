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

package nl.procura.gba.web.modules.bs.common.pages.residentpage;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.bs.common.pages.residentpage.ResidentPage.ResidentTable;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

class FeedbackDialog extends GbaModalWindow {

  private final ResidentTable parentTable;
  private final Relatie       relation;

  FeedbackDialog(ResidentTable parentTable, Relatie relation) {

    super("Terugmeldingen (Escape om te sluiten)", "700px");
    this.parentTable = parentTable;
    this.relation = relation;
  }

  @Override
  public void attach() {

    super.attach();

    final MainModuleContainer mainModule = new MainModuleContainer();

    addComponent(mainModule);

    final ModuleTmvWindow module = new ModuleTmvWindow();

    mainModule.getNavigation().addPage(module);
  }

  public class ModuleTmvWindow extends ZakenModuleTemplate {

    ModuleTmvWindow() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {

        addComponent(new FeedbackPage(parentTable, relation));
      }
    }
  }
}
