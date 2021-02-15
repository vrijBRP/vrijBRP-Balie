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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.verwerken;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class BsmVerwerkingWindow<T extends Zaak> extends GbaModalWindow {

  private final T zaak;

  public BsmVerwerkingWindow(T zaak) {

    super("Verwerking van een zaak (Escape om te sluiten)", "700px");

    this.zaak = zaak;
  }

  @Override
  public void attach() {

    super.attach();

    addComponent(new MainModuleContainer(false, new Module()));
  }

  protected abstract void reload();

  public class Module extends ModuleTemplate {

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {

        BsmVerwerkenPage page = new BsmVerwerkenPage(zaak) {

          @Override
          public void reloadZaak() {

            // Verwijder in de cache opgeslagen persoonslijsten
            getServices().getPersonenWsService().getOpslag().clear();

            BsmVerwerkingWindow.this.reload();
          }
        };

        getPages().getNavigation().goToPage(page);
      }

      super.event(event);
    }
  }
}
