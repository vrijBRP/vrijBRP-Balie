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

package nl.procura.gba.web.modules.zaken.protocol.page3;

import static nl.procura.gba.web.modules.zaken.protocol.ProtocolBean.*;

import java.util.List;
import java.util.Map.Entry;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.protocol.ProtocolBean;
import nl.procura.gba.web.modules.zaken.protocol.ProtocolForm;
import nl.procura.gba.web.services.zaken.protocol.ProtocolRecord;
import nl.procura.gba.web.services.zaken.protocol.ProtocolRecordAttribuut;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class FormAndTablePage3 extends GbaVerticalLayout {

  private ProtocolRecord protocol;
  private ProtocolForm   form;
  private GbaTable       table;

  public FormAndTablePage3(final ProtocolRecord protocol) {

    setProtocol(protocol);

    setSpacing(true);

    setForm(new ProtocolForm(new ProtocolBean(protocol), GEBRUIKER, ANUMMMER, DATUMTIJD));

    addComponent(form);

    if (protocol.getCategorieen().isEmpty()) {
      addComponent(new InfoLayout("", "Er zijn geen categorieën geprotocolleerd."));
    }

    setTable(new GbaTable() {

      @Override
      public void onClick(Record record) {
        onTableClick(record);
      }

      @Override
      public void setColumns() {

        setSelectable(true);

        addColumn("Nr", 60);
        addColumn("Categorie", 300);
        addColumn("Aantal velden geprotocolleerd");

        super.setColumns();
      }

      @Override
      public void setRecords() {

        int nr = 0;

        for (Entry<String, List<ProtocolRecordAttribuut>> categorie : protocol.getCategorieen().entrySet()) {

          nr++;

          Record r = addRecord(categorie);
          r.addValue(nr);
          r.addValue(categorie.getKey());
          r.addValue(categorie.getValue().size());
        }
      }
    });

    addExpandComponent(table);
  }

  public ProtocolForm getForm() {
    return form;
  }

  public void setForm(ProtocolForm form) {
    this.form = form;
  }

  public ProtocolRecord getProtocol() {
    return protocol;
  }

  public void setProtocol(ProtocolRecord protocol) {
    this.protocol = protocol;
  }

  public GbaTable getTable() {
    return table;
  }

  public void setTable(GbaTable table) {
    this.table = table;
  }

  @SuppressWarnings("unused")
  protected void onTableClick(Record record) {
  } // Override
}
