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

package nl.procura.gba.web.modules.beheer.onderhoud.rdw.page2;

import static nl.procura.gba.web.modules.beheer.onderhoud.rdw.page2.Page2RdwBean.*;

import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2RdwForm extends GbaForm<Page2RdwBean> {

  public Page2RdwForm(String gebruikersnaam, String datum) {

    setColumnWidths("120px", "");
    setOrder(ACCOUNT_ID, WACHTWOORD, DATUM);
    setBean(new Page2RdwBean(gebruikersnaam, datum));
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(DATUM)) {
      column.addComponent(new Label("(De datum waarop het wachtwoord was gewijzigd)"));
    }

    super.afterSetColumn(column, field, property);
  }
}
