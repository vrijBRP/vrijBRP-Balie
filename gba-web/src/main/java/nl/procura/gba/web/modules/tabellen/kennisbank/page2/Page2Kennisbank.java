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

package nl.procura.gba.web.modules.tabellen.kennisbank.page2;

import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.kennisbank.KennisbankRecord;
import nl.procura.gba.web.services.zaken.kennisbank.KennisbankRecord.KennisbankURL;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Kennisbank extends NormalPageTemplate {

  private Page2KennisbankForm    form   = null;
  private final KennisbankRecord record;
  private Table1                 table1 = null;

  public Page2Kennisbank(KennisbankRecord record) {

    super("Overzicht kennisbank record");

    this.record = record;

    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new Page2KennisbankForm(record);

      addComponent(form);

      table1 = new Table1();

      addExpandComponent(table1);
    }

    super.event(event);
  }

  public Page2KennisbankForm getForm() {
    return form;
  }

  public void setForm(Page2KennisbankForm form) {
    this.form = form;
  }

  public Table1 getTable1() {
    return table1;
  }

  public void setTable1(Table1 table1) {
    this.table1 = table1;
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  class Table1 extends GbaTable {

    @Override
    public void setColumns() {

      addColumn("Kennisbank", 100).setClassType(KennisbankButton.class);
      addColumn("Type");
      addColumn("URL");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      for (KennisbankURL url : record.getUrls()) {

        Record r = addRecord(url);

        r.addValue(new KennisbankButton(url.getUrl()));
        r.addValue(url.getDoel());
        r.addValue(url.getUrl());
      }

      super.setRecords();
    }
  }
}
