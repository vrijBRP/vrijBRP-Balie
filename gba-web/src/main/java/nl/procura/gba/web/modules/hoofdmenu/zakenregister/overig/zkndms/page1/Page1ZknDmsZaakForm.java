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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page1;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page1.Page1ZknDmsZaakBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1ZknDmsZaakForm extends GbaForm<Page1ZknDmsZaakBean> {

  public Page1ZknDmsZaakForm() {
    setOrder(OMSCHRIJVING, STARTDATUM, GEPLANDE_EINDDATUM, REGISTRATIEDATUM, UITERLIJKE_EINDDATUM);
    setColumnWidths(WIDTH_130, "250px", "130px", "");
    setBean(new Page1ZknDmsZaakBean());
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {
    if (property.is(OMSCHRIJVING)) {
      column.setColspan(3);
    }
    super.afterSetColumn(column, field, property);
  }
}
