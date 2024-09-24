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

package nl.procura.gba.web.modules.bs.naturalisatie.page50;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.SelectListener;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class VertegenwoordigerQuickSearch extends NormalPageTemplate {

  private final BsnFieldValue  bsn;
  private final SelectListener selectListener;
  private VertegenwoordigerQuickSearchTable table;

  public VertegenwoordigerQuickSearch(BsnFieldValue bsn, SelectListener snelZoekListener) {
    this.bsn = bsn;
    this.selectListener = snelZoekListener;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      PLResultComposite result = getPersoonslijst();
      table = new VertegenwoordigerQuickSearchTable(result) {

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
  public void attach() {
    super.attach();
    VaadinUtils.resetHeight(getWindow());
    getWindow().setWidth("700px");
    getWindow().center();
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

  private void selectRecord(Record record) {
    selectListener.select(record.getObject(BasePLExt.class));
    onClose();
  }

  private PLResultComposite getPersoonslijst() {
    PLEArgs args = new PLEArgs();
    if (bsn.isCorrect()) {
      args.addNummer(bsn.getStringValue());
      return getServices().getPersonenWsService().getPersoonslijsten(args, false);
    }
    return null;
  }
}
