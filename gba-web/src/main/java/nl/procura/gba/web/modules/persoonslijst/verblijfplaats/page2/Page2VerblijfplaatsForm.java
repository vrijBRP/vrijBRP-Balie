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

package nl.procura.gba.web.modules.persoonslijst.verblijfplaats.page2;

import static nl.procura.gba.web.modules.persoonslijst.verblijfplaats.page2.Page2VerblijfplaatsBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2VerblijfplaatsForm extends PlForm {

  public Page2VerblijfplaatsForm() {

    setOrder(ADRES, GEMEENTE, GEMEENTEDEEL, DATUMINSCHRIJVING, AANVANG, FUNCTIEADRES, AANGIFTE, VERTREK,
        VERTREKADRES, VESTIGING, WOONPLAATS, OPENBARE_RUIMTE, AON, INA);
    setColumnWidths("160px", "300px", "150px", "");
  }

  @Override
  public Page2VerblijfplaatsBean getBean() {
    return (Page2VerblijfplaatsBean) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new Page2VerblijfplaatsBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(VERTREK, WOONPLAATS)) {
      getLayout().addBreak();
    }

    if (property.is(ADRES, VERTREK)) {
      column.setColspan(3);
    }
  }
}
