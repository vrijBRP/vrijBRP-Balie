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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page5;

import java.util.List;
import java.util.Set;

import com.vaadin.ui.Embedded;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.PrintOptieValue;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.PrinterContainer;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.theme.twee.Icons;

public class SelectedPrintOptionsPage extends NormalPageTemplate {

  private final int              MAX_DOCSTOSHOW = 3;
  private final List<PrintOptie> printopties;

  private GbaTable              table = null;
  private List<PrintOptieValue> printers;

  public SelectedPrintOptionsPage(List<PrintOptie> printopties) {
    this.printopties = printopties;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      printers = new PrinterContainer(getServices()).getPrinters();

      table = new GbaTable() {

        @Override
        public void setColumns() {

          setSelectable(true);
          setMultiSelect(true);

          addColumn("", 30).setClassType(Embedded.class);
          addColumn("ID", 50);
          addColumn("Naam");
          addColumn("Opties");
          addColumn("Aantal");
          addColumn("Gekoppelde documenten");
        }
      };

      addComponent(table);
    } else if (event.isEvent(LoadPage.class)) {

      table.getRecords().clear();

      for (PrintOptie printoptie : printopties) {

        // gekoppelde documenten wordt bijgewerkt
        Set<DocumentRecord> coupledDocs = getServices().getPrintOptieService().getPrintoptieDocumenten(
            printoptie);

        Record r = table.addRecord(printoptie);
        String docsToShow = MiscUtils.abbreviate(coupledDocs, MAX_DOCSTOSHOW);

        r.addValue(printerAanwezig(printoptie) ? "" : new TableImage(Icons.getIcon(Icons.ICON_ERROR)));
        r.addValue(printoptie.getCPrintoptie());
        r.addValue(printoptie.getOms());
        r.addValue(printoptie.getCmd());
        r.addValue(coupledDocs.size());
        r.addValue(docsToShow);
      }

      table.reloadRecords();
    }

    super.event(event);
  }

  private boolean printerAanwezig(PrintOptie printOptie) {
    return printers.contains(new FieldValue(printOptie.getPrintoptie()));
  }
}
