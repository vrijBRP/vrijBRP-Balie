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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.page2;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.CheckListener;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.SelectListener;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2QuickSearch extends NormalPageTemplate {

  private final List<Address>  addresses;
  private final SelectListener selectListener;
  private CheckListener        checkListener;
  private GbaTable             table = null;

  public Page2QuickSearch(List<Address> addresses,
      SelectListener selectListener,
      CheckListener checkListener) {

    this.addresses = addresses;
    this.selectListener = selectListener;
    this.checkListener = checkListener;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInfo("Selecteer een adres. Pijltjetoetsen en enter kunnen hierbij gebruikt worden.");

      if (checkListener != null) {
        buttonNew.setCaption("Toevoegen (F7)");
        addButton(buttonPrev);
        addButton(buttonNew);
        addButton(buttonDel, 1f);
        addButton(buttonClose);
        table = new Page2QuickSearchTableMarked(addresses, selectListener, checkListener);

      } else {

        addButton(buttonPrev, 1f);
        addButton(buttonClose);
        table = new Page2QuickSearchTable(addresses, selectListener);
      }
      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onNew() {
    for (Address address : table.getSelectedValues(Address.class)) {
      if (!checkListener.isMarked(address)) {
        selectListener.select(address);
      }
    }
    table.init();
    super.onNew();
  }

  @Override
  public void onDelete() {
    for (Address address : table.getSelectedValues(Address.class)) {
      if (checkListener.isMarked(address)) {
        selectListener.select(address);
      }
    }
    table.init();
    super.onDelete();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onEnter() {
    if (table.getRecord() != null) {
      table.onDoubleClick(table.getRecord());
    }
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
