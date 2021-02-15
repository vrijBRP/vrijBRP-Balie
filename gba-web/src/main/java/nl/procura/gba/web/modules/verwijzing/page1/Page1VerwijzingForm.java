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

package nl.procura.gba.web.modules.verwijzing.page1;

import static nl.procura.gba.web.modules.verwijzing.page1.Page1VerwijzingBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1VerwijzingForm extends PlForm {

  public Page1VerwijzingForm() {

    setOrder(BSN, ANR, GESLACHTSNAAM, VOORVOEGSEL, VOORNAAM, GESLACHT, TITEL, GEBOREN, VERTROKKEN, ADRES, POSTCODE,
        LOCATIE, STATUS);
    setColumnWidths(WIDTH_130, "", "130px", "");
  }

  @Override
  public Object getNewBean() {
    return new Page1VerwijzingBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(VERTROKKEN)) {
      getLayout().addBreak();
    }

    if (property.is(STATUS)) {
      column.setColspan(3);
      field.addStyleName(GbaWebTheme.TEXT.RED);
    }
  }
}
