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

package nl.procura.gba.web.modules.zaken.woningkaart.window;

import static nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class WoningObjectForm extends ReadOnlyForm {

  public WoningObjectForm(Address address) {
    setOrder(ADRES, OPMERKING, OPENBARE_RUIMTE, AON, WOONPLAATS, INA, WONINGSOORT, WONINGINDICATIE, ADRESINDICATIE,
        PPDCODE, STEMDISTRICT, PANDCODE, WIJK, DATUMINGANG, BUURT, DATUMEINDE, SUBBUURT,
        SURFACE_SIZE, STATUS, BUILDING_STATUS, PURPOSE);
    setColumnWidths("160px", "", "150px", "200px");
    setReadonlyAsText(true);
    setBean(new WoningObjectBean(address));
  }

  @Override
  public Field newField(Field field, Property property) {
    super.newField(field, property);
    if (property.is(WONINGSOORT, OPENBARE_RUIMTE, WIJK, SURFACE_SIZE)) {
      getLayout().addBreak();
    }
    return field;
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(ADRES, OPMERKING, SUBBUURT)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }
}
