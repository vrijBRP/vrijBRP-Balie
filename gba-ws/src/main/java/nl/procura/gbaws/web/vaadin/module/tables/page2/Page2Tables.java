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

package nl.procura.gbaws.web.vaadin.module.tables.page2;

import nl.procura.gbaws.db.handlers.LandTabDao.LandTable;
import nl.procura.gbaws.web.vaadin.layouts.DefaultPageLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;

@SuppressWarnings("serial")
public class Page2Tables extends DefaultPageLayout {

  private final LandTable  landTable;
  private Page2TablesTable table = null;

  public Page2Tables(LandTable landTable) {
    super(landTable.getDescription());
    this.landTable = landTable;
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);

      table = new Page2TablesTable(landTable);
      addExpandComponent(table);

      getButtonLayout().addComponent(new IndexedTableFilterLayout(table));
    }

    super.event(event);
  }
}
