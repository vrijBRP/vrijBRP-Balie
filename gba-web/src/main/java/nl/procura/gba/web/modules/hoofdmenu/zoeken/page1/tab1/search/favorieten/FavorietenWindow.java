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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab1.search.favorieten;

import static nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorieType.FAVORIETEN;
import static nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorieType.HISTORIE;

import java.util.function.Consumer;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class FavorietenWindow extends GbaModalWindow {

  private final Consumer<String> nrConsumer;

  public FavorietenWindow() {
    this(null);
  }

  public FavorietenWindow(Consumer<String> nrConsumer) {
    super("Favorieten / historie (Escape om te sluiten)", "400px");
    this.nrConsumer = nrConsumer;
    addStyleName("favWindow");
    setIcon(null);
  }

  @Override
  public void attach() {

    super.attach();

    GbaTabsheet tabSheet = new GbaTabsheet();
    tabSheet.setSizeFull();
    tabSheet.addStyleName("favtab");
    tabSheet.setNoBorderTop();

    tabSheet.addTab(new ModuleFavorieten(), "Favorieten", null);
    tabSheet.addTab(new ModuleHistorie(), "Historie", null);

    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(false);
    layout.addComponent(tabSheet);

    setContent(layout);
  }

  public class ModuleFavorieten extends ZakenModuleTemplate {

    public ModuleFavorieten() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {
      super.event(event);
      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(new FavorietenPage(FAVORIETEN, nrConsumer));
      }
    }
  }

  public class ModuleHistorie extends ZakenModuleTemplate {

    public ModuleHistorie() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {
      super.event(event);
      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(new FavorietenPage(HISTORIE, nrConsumer));
      }
    }
  }
}
