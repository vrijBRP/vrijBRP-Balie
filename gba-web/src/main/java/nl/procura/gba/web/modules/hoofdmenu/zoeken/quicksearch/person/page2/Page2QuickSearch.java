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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page2;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.SelectListener;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page2QuickSearch extends NormalPageTemplate {

  private final PLResultComposite result;
  private final SelectListener    quickSearchListener;
  private Page2QuickSearchTable   table;

  public Page2QuickSearch(PLResultComposite result, SelectListener snelZoekListener) {
    this.result = result;
    this.quickSearchListener = snelZoekListener;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonClose);

      setInfo("Selecteer een persoon. Pijltjetoetsen en enter kunnen hierbij gebruikt worden.");
      setResult(result);

      table = new Page2QuickSearchTable(result) {

        @Override
        public void onClick(Record record) {
          selectRecord(record);
        }
      };

      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onEnter() {

    if (table.getRecord() != null) {
      selectRecord(table.getRecord());
    }
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  public void setResult(PLResultComposite result) {

    if (result.getTotaalAantal() > result.getMaxAantal()) {

      InfoLayout info = new InfoLayout();

      info.setHeader("Meer zoekresultaten dan maximum.");
      info.setMessage(
          "Er zijn meer dan <b>%s</b> persoonslijsten gevonden. "
              + "Daarvan worden er <b>%s</b> getoond. Specifieer de zoekopdracht verder.",
          result.getTotaalAantal(), result.getMaxAantal());

      addComponent(info, getComponentIndex(getButtonLayout()) + 1);
    }
  }

  /**
   * Selecteer het record en sluit de window
   */
  private void selectRecord(Record record) {
    quickSearchListener.select((BasePLExt) record.getObject());
    onClose();
  }
}
