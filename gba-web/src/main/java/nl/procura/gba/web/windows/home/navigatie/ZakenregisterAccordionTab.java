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

package nl.procura.gba.web.windows.home.navigatie;

import com.vaadin.ui.Component;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.navigation.GbaAccordionTab;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.ModuleZakenregister;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.tree.ZaakTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2.Page2ProfielenMap;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.functies.VaadinUtils;

public class ZakenregisterAccordionTab extends GbaAccordionTab {

  private static final long serialVersionUID = -4743356125380961247L;

  private Page2ProfielenMap profielenMap = null;

  private Table table;

  public ZakenregisterAccordionTab(GbaApplication application) {

    super("Zakenregister", application);

    if (application.isProfielActie(ProfielActie.SELECT_HOOFD_ZAKENREGISTER)) {

      table = new Table();

      addComponent(table);

      profielenMap = new Page2ProfielenMap();
    }
  }

  public Page2ProfielenMap getProfielenMap() {
    return profielenMap;
  }

  public void recountTree() {
    table.recount();
  }

  /**
   * Herlaad de zaken uit de zakenservices
   */
  public void reloadTree() {
    table.reloadTree();
  }

  /**
   * Herlaad en zet focus op eerst
   */
  public void resetTable() {
    table.reset();
  }

  public class Table extends ZaakTable {

    private static final long serialVersionUID = 2301804397594476005L;

    @Override
    public void setNewComponent(Component component) {

      MainModuleContainer mainModule = VaadinUtils.getChild(getWindow(), MainModuleContainer.class);
      mainModule.getNavigation().getPages().clear();
      mainModule.getNavigation().addPage(new ModuleZakenregister((PageLayout) component));

      super.setNewComponent(component);
    }
  }
}
