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

package nl.procura.gba.web.modules.zaken.protocol.page4;

import static nl.procura.gba.web.modules.zaken.protocol.ProtocolBean.*;

import java.util.List;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.protocol.ProtocolBean;
import nl.procura.gba.web.modules.zaken.protocol.ProtocolForm;
import nl.procura.gba.web.services.zaken.protocol.ProtocolRecord;
import nl.procura.gba.web.services.zaken.protocol.ProtocolRecordAttribuut;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class FormAndTablePage4 extends VerticalLayout {

  public FormAndTablePage4(ProtocolRecord protocol, String categorie, final List<ProtocolRecordAttribuut> list) {

    setSpacing(true);

    ProtocolForm form = new ProtocolForm(new ProtocolBean(protocol, categorie), GEBRUIKER, ANUMMMER, DATUMTIJD,
        CATEGORIE);

    addComponent(form);

    if (list.isEmpty()) {
      addComponent(new InfoLayout("", "Er zijn geen elementen geprotocolleerd."));
    }

    GbaTable table = new GbaTable() {

      @Override
      public void setColumns() {

        addColumn("Nr", 50);
        addColumn("Veld");

        super.setColumns();
      }

      @Override
      public void setRecords() {

        int i = 0;
        for (ProtocolRecordAttribuut attr : list) {

          i++;
          Record r = addRecord(attr);
          r.addValue(i);
          r.addValue(attr.getVeld());
        }
      }
    };

    addComponent(table);
  }
}
