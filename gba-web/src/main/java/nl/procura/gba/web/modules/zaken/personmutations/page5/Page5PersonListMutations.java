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

package nl.procura.gba.web.modules.zaken.personmutations.page5;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.zaken.common.ZakenOverzichtModalPage;
import nl.procura.gba.web.modules.zaken.personmutations.overview.PersonMutationOverviewBuilder;
import nl.procura.gba.web.modules.zaken.personmutations.page3.Page3PersonListMutations;
import nl.procura.gba.web.services.beheer.personmutations.PersonListMutation;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class Page5PersonListMutations extends ZakenOverzichtModalPage<PersonListMutation> {

  public Page5PersonListMutations(PersonListMutation mutation) {
    super(mutation, "");
    addButton(buttonPrev, 1f);
    addButton(buttonClose);
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonAanpassen);
    addOptieButton(buttonFiat);
    addOptieButton(buttonVerwerken);
  }

  @Override
  protected void addTabs(ZaakTabsheet<PersonListMutation> tabsheet) {
    PersonMutationOverviewBuilder.addTab(tabsheet, getZaak());
  }

  @Override
  protected void goToZaak() {
    // Open page
    Page3PersonListMutations page = new Page3PersonListMutations(getZaak());
    if (getWindow().isModal()) {
      getNavigation().goToPage(page);
    } else {
      getApplication().getParentWindow().addWindow(new WindowPersonListMutations(page));
    }
  }

  public static class WindowPersonListMutations extends GbaModalWindow {

    private final Page3PersonListMutations page;

    public WindowPersonListMutations(Page3PersonListMutations page) {
      super("Mutaties van de persoonslijst (Escape om te sluiten)", "1400px");
      this.page = page;
    }

    public WindowPersonListMutations(Page3PersonListMutations page, Runnable closeListener) {
      this(page);
      addListener((CloseListener) closeEvent -> closeListener.run());
    }

    @Override
    public void attach() {
      super.attach();
      addComponent(new MainModuleContainer(false, page));
    }
  }
}
