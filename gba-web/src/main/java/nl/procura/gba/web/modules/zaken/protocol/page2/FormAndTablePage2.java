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

package nl.procura.gba.web.modules.zaken.protocol.page2;

import java.util.List;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.protocol.ProtocolRecord;
import nl.procura.gba.web.services.zaken.protocol.ProtocolZoekopdracht;
import nl.procura.vaadin.component.layout.Fieldset;

public class FormAndTablePage2 extends VerticalLayout {

  private GbaTable table;

  public FormAndTablePage2(final ProtocolZoekopdracht zoekopdracht) {

    table = new GbaTable() {

      @Override
      public void onClick(Record record) {
        onSelectRecord((ProtocolRecord) record.getObject());
      }

      @Override
      public void setColumns() {

        setSizeFull();
        setSelectable(true);

        addColumn("Nr.", 60);
        addColumn("Datum / tijd", 200);
        addColumn("Gebruiker");
        addColumn("A-nummer");

        super.setColumns();
      }

      @Override
      public void setRecords() {

        List<ProtocolRecord> records = getApplication().getServices().getProtocolleringService().getProtocollen(
            zoekopdracht);

        int nr = records.size();

        for (ProtocolRecord p : records) {

          Record r = addRecord(p);

          r.addValue(nr);
          r.addValue(p.getDate());
          r.addValue(p.getGebruiker());
          r.addValue(p.getAnummer());

          nr--;
        }

        super.setRecords();
      }
    };

    addComponent(new Fieldset("Zoekresultaten"));
    addComponent(table);
    setExpandRatio(table, 1f);
    setSpacing(true);
  }

  public GbaTable getTable() {
    return table;
  }

  public void setTable(GbaTable table) {
    this.table = table;
  }

  @SuppressWarnings("unused")
  protected void onSelectRecord(ProtocolRecord record) {
  } // Override
}
