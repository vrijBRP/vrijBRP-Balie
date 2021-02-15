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

package nl.procura.gba.web.modules.beheer.protocollering.page3;

import java.util.List;
import java.util.Map.Entry;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.protocollering.page4.Page4Protocollering;
import nl.procura.gba.web.modules.zaken.protocol.page3.FormAndTablePage3;
import nl.procura.gba.web.services.zaken.protocol.ProtocolRecord;
import nl.procura.gba.web.services.zaken.protocol.ProtocolRecordAttribuut;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page3Protocollering extends NormalPageTemplate {

  private final FormAndTablePage3 form;

  public Page3Protocollering(ProtocolRecord protocol) {

    super("Overzicht van de BRP categorieën");

    addButton(buttonPrev);

    form = new FormAndTablePage3(protocol) {

      @Override
      public void onTableClick(Record record) {

        selectRecord(record);
      }
    };

    addComponent(form);
    getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(form.getTable()));
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @SuppressWarnings("unchecked")
  private void selectRecord(Record record) {

    Entry<String, List<ProtocolRecordAttribuut>> entry = (Entry<String, List<ProtocolRecordAttribuut>>) record
        .getObject();
    getNavigation().goToPage(new Page4Protocollering(form.getProtocol(), entry.getKey(), entry.getValue()));
  }
}
