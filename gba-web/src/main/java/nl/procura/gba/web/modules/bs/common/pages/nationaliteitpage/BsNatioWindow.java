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

package nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BsNatioWindow extends GbaModalWindow {

  private final BsNatioType            type;
  private final DossierNationaliteiten dossier;
  private final String                 message;

  public BsNatioWindow(BsNatioType type, DossierNationaliteiten dossier, String message) {

    super("Namen (Druk op escape om te sluiten)", "70%");

    this.type = type;
    this.dossier = dossier;
    this.message = message;
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();

    addComponent(mainModule);

    mainModule.getNavigation().addPage(new Module());
  }

  @SuppressWarnings("unused")
  protected void add(DossierNationaliteit natio) {
  } // Override

  @SuppressWarnings("unused")
  protected void delete(DossierNationaliteit natio) {
  } // Override

  public class Module extends ModuleTemplate {

    public Module() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {

        getPages().getNavigation().goToPage(new BsNatioPage(type, dossier, message));
      }
    }
  }
}
