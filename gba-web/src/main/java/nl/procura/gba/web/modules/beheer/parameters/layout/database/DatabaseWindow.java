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

package nl.procura.gba.web.modules.beheer.parameters.layout.database;

import java.io.File;
import java.util.UUID;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.beheer.parameters.layout.database.page1.Page1Database;
import nl.procura.gba.web.modules.beheer.parameters.layout.database.page2.Page2Database;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class DatabaseWindow extends GbaModalWindow {

  private final File propertiesFile;

  public DatabaseWindow() {
    this(null);
  }

  public DatabaseWindow(File propertiesFile) {
    super("Databases", "700px");
    this.propertiesFile = propertiesFile;
  }

  public static File getNewFile() {
    return new File(GbaConfig.getPath().getConfigDir(), "db." + UUID.randomUUID().toString() + ".properties");
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();
    addComponent(mainModule);
    mainModule.getNavigation().addPage(new Module());
  }

  public class Module extends ModuleTemplate {

    public Module() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {
        if (propertiesFile == null) {
          getPages().getNavigation().goToPage(new Page1Database());
        } else {
          getPages().getNavigation().goToPage(new Page2Database(propertiesFile));
        }
      }

      super.event(event);
    }
  }
}
