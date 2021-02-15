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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab2.search;

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekBean;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Tab2Form extends GbaForm<ZoekBean> {

  public Tab2Form() {

    setOrder(BSN, ANR, GEBOORTEDATUM, GESLACHT, GESLACHTSNAAM, VOORVOEGSEL, VOORNAAM, TITEL, STRAATLAND, HNR, HNRL,
        HNRT, POSTCODE, GEMEENTE, ADRESIND);
    setColumnWidths(WIDTH_130, "250px", "110px", "");
  }

  @Override
  public ZoekBean getNewBean() {
    return new ZoekBean();
  }

  @Override
  public Field newField(Field field, Property property) {
    field.setWidth("200px");
    if (property.is(GEBOORTEDATUM, STRAATLAND, ADRESIND)) {
      getLayout().addBreak();
    }

    return super.newField(field, property);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(ADRESIND)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }
}
