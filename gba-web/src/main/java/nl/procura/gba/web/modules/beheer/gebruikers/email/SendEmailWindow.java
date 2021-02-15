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

package nl.procura.gba.web.modules.beheer.gebruikers.email;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.common.misc.email.Verzending;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.beheer.gebruikers.email.page1.Page1SendEmail;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class SendEmailWindow extends GbaModalWindow {

  private List<Verzending> verzendingen = new ArrayList<>();

  public SendEmailWindow(List<Verzending> verzendingen) {
    this();
    this.verzendingen = verzendingen;
  }

  private SendEmailWindow() {
    super("E-mails (Escape om te sluiten)", "800px");
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();

    addComponent(mainModule);

    mainModule.getNavigation().addPage(new ModuleEmail());
  }

  public class ModuleEmail extends ZakenModuleTemplate {

    public ModuleEmail() {
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(new Page1SendEmail(verzendingen));
      }
    }
  }
}
