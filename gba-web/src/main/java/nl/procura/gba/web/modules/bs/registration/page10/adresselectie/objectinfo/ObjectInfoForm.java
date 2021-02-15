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

package nl.procura.gba.web.modules.bs.registration.page10.adresselectie.objectinfo;

import static nl.procura.gba.web.modules.bs.registration.page10.adresselectie.objectinfo.ObjectInfoBean.*;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class ObjectInfoForm extends GbaForm<ObjectInfoBean> {

  public ObjectInfoForm(final BaseWKExt adres) {

    setOrder(ADRES, OPMERKING, OPENBARE_RUIMTE, AON, WOONPLAATS, INA, WONINGSOORT, WONINGINDICATIE, ADRESINDICATIE,
        PPDCODE, STEMDISTRICT, PANDCODE, WIJKCODE, BUURTCODE, SUBBUURTCODE, DATUMINGANG, DATUMEINDE);

    setColumnWidths("160px", "", "150px", "260px");
    setReadonlyAsText(true);
    setBean(new ObjectInfoBean(adres));
  }

  @Override
  public Field newField(final Field field, final Property property) {
    super.newField(field, property);
    if (property.is(WONINGSOORT, OPENBARE_RUIMTE, WIJKCODE)) {
      getLayout().addBreak();
    }

    return field;
  }

  @Override
  public void setColumn(final TableLayout.Column column, final Field field, final Property property) {
    if (property.is(ADRES, OPMERKING, DATUMEINDE)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
