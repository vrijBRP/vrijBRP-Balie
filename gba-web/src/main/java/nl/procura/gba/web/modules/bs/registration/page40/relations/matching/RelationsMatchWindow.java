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

package nl.procura.gba.web.modules.bs.registration.page40.relations.matching;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Label;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.page.buttons.ModalCloseButton;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.component.label.H1;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.theme.twee.Icons;

public class RelationsMatchWindow extends GbaModalWindow {

  public RelationsMatchWindow(RelationsMatchInfo matchingInfo) {
    super("Vergelijking gegevens", "850px");

    VLayout vLayout = new VLayout()
        .margin(true)
        .add(new HLayout()
            .addExpandComponent(new H1("Overzicht vergelijking van gegevens"))
            .add(new ModalCloseButton()));

    TableLayout tableLayout = new TableLayout();
    tableLayout.setColumnWidths("100px", "30px", "290px", "");
    tableLayout.addLabel("Element");
    tableLayout.addLabel("");
    tableLayout.addLabel("Gegevens inschrijver");
    tableLayout.addLabel("Gegevens op PL van de gerelateerde in BRP");

    for (RelationsMatchRecord record : matchingInfo.getRecords()) {
      tableLayout.addLabel(record.getLabel());
      tableLayout.addData(new TableImage(Icons.getIcon(record.isMatch() ? Icons.ICON_OK : Icons.ICON_ERROR)));
      tableLayout.addData(new Label(record.getValue1()));
      tableLayout.addData(new Label(appendExtraInfo(record)));
    }

    vLayout.add(tableLayout);
    setContent(vLayout);
  }

  /**
   * append extra info for clarification to the user for 'birth' fields
   * If the birth fields on the PL are currently empty they will be appended.
   */
  private String appendExtraInfo(RelationsMatchRecord record) {
    if (!record.isNameValue() && record.isMatch() && StringUtils.isBlank(record.getValue2())) {
      return "(wordt aangevuld)";
    }
    return record.getValue2();
  }
}
