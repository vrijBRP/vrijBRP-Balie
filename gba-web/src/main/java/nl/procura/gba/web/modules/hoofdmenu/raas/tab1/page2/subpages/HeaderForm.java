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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages;

import static nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages.Tab1RaasPage2Bean.*;

import com.vaadin.ui.Field;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class HeaderForm extends Tab1RaasPage2Form {

  public HeaderForm(DocAanvraagDto documentAanvraag) {
    super(documentAanvraag);
  }

  @Override
  protected void createFields() {
    setColumnWidths("220px", "250px", "170px", "", "150px", "100px");
    setOrder(AANVRAAGNUMMER, IND_SPOED, UPDATE_PROWEB, STATUS_AANVR, STATUS_LEV, UPDATE_PROBEV, STATUS_AFSL,
        STATUS_VERW);
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {

    if (property.is(STATUS_VERW)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
