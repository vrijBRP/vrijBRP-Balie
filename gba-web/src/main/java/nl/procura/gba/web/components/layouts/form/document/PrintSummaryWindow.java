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

package nl.procura.gba.web.components.layouts.form.document;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.Icons;

public class PrintSummaryWindow extends ModalWindow {

  public PrintSummaryWindow(List<PrintRecord> records) {

    setCaption("Afdruksamenvatting");

    setWidth("600px");

    VerticalLayout v = new VerticalLayout();

    v.setSpacing(true);

    v.setMargin(true);

    Table table = new Table(records);

    table.setSizeFull();

    Button sluitButton = new Button("Sluiten");

    sluitButton.addListener((ClickListener) event -> ((ModalWindow) getWindow()).closeWindow());

    v.addComponent(sluitButton);

    sluitButton.focus();

    v.addComponent(new InfoLayout("", "Overzicht van de afgedrukte documenten"));

    v.addComponent(table);

    setContent(v);
  }

  public class Table extends GbaTable {

    private final List<PrintRecord> records;

    private Table(List<PrintRecord> records) {
      this.records = records;
    }

    @Override
    public void setColumns() {

      setSelectable(false);

      addColumn("", 20).setClassType(Embedded.class);
      addColumn("Document");
      addColumn("Uitgevoerd naar");
    }

    @Override
    public void setRecords() {

      for (PrintRecord documentRecord : records) {

        Record r = addRecord(documentRecord);

        switch (documentRecord.getStatus()) {
          case ERROR:
            r.addValue(new TableImage(Icons.getIcon(Icons.ICON_ERROR)));
            break;

          case PRINTED:
            r.addValue(new TableImage(Icons.getIcon(Icons.ICON_OK)));
            break;

          case DEFAULT:
          default:
            r.addValue("");
        }

        r.addValue(documentRecord.getDocument());

        if (documentRecord.getPrintActie() == null) {
          r.addValue("N.v.t. (geen printactie)");
        } else if (documentRecord.getPrintActie().getPrintOptie() == null) {
          r.addValue("Geen printeruitvoer geselecteerd");
        } else if (documentRecord.getPrintActie().getPrintOptie().getPrintType() == PrintOptieType.POPUP) {
          r.addValue("Nieuw browser scherm");
        } else {
          r.addValue(documentRecord.getPrintActie().getPrintOptie().getLocatie());
        }
      }

      super.setRecords();
    }
  }
}
