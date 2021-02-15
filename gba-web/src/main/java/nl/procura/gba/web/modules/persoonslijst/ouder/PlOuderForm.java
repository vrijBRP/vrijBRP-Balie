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

package nl.procura.gba.web.modules.persoonslijst.ouder;

import static nl.procura.gba.web.modules.persoonslijst.ouder.PlOuderBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class PlOuderForm extends PlForm {

  public PlOuderForm() {

    setOrder(BSN, ANR, GESLACHTSNAAM, VOORVOEGSEL, TITEL, VOORNAAM, GEBOREN, GESLACHT, AFSTAMMING, ADRES, STATUS);
    setColumnWidths(WIDTH_130, "300px", "130px", "");
  }

  @Override
  public Object getNewBean() {
    return new PlOuderBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (!property.is(BSN, ANR, VOORVOEGSEL, TITEL)) {
      column.setColspan(3);
    }

    if (property.is(GESLACHTSNAAM, GEBOREN, ADRES)) {
      getLayout().addBreak();
    }

    if (property.is(STATUS)) {
      field.setStyleName(GbaWebTheme.TEXT.RED);
    }
  }
}
