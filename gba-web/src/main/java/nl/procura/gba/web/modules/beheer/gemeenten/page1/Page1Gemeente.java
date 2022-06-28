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

package nl.procura.gba.web.modules.beheer.gemeenten.page1;

import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.gemeenten.page2.Page2Gemeente;
import nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page3.Page3HuwelijkLocaties;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

public class Page1Gemeente extends NormalPageTemplate {

  private final Uploader uploader = new Uploader();
  private Table          table    = null;

  public Page1Gemeente() {

    super("Overzicht van gemeenten");

    addButton(buttonNew);
    addButton(buttonDel);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      addComponent(new Fieldset("Uploaden van bestand", uploader));
      table = new Table();
      addExpandComponent(table);
      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<Gemeente>(table) {

      @Override
      public void deleteValue(Gemeente gemeente) {
        getServices().getGemeenteService().delete(gemeente);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2Gemeente(new Gemeente()));
    super.onNew();
  }

  @Override
  public void onNextPage() {
    getNavigation().goToPage(Page3HuwelijkLocaties.class);
    super.onNextPage();
  }

  public class Table extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {
      getNavigation().goToPage(new Page2Gemeente((Gemeente) record.getObject()));
      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Gemeente");
      addColumn("Cbs-code", 100);
      addColumn("Adres");
      addColumn("Postcode", 100);
      addColumn("Plaats");
    }

    @Override
    public void setRecords() {
      for (Gemeente g : getServices().getGemeenteService().getGemeenten()) {
        Record record = addRecord(g);
        record.addValue(g.getGemeente());
        record.addValue(g.getCbscode());
        record.addValue(g.getAdres());
        record.addValue(g.getPostcode());
        record.addValue(g.getPlaats());
      }

      super.setRecords();
    }
  }

  private class Uploader extends DocUploader {

    private static final String CSV = "csv";

    @Override
    public void uploadSucceeded(final SucceededEvent event) {
      if (!getFileName().toLowerCase().endsWith(CSV)) {
        addMessage("Het is geen CSV bestand", Icons.ICON_ERROR);
      } else {
        try {
          getServices().getGemeenteService().update(getFile());
          table.init();
        } catch (ProException e) {
          addMessage(e.getMessage(), Icons.ICON_ERROR);
        }

        super.uploadSucceeded(event);
      }
    }
  }
}
