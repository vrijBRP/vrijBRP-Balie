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

package nl.procura.gba.web.modules.persoonslijst.overlijden.page1;

import static nl.procura.gba.web.modules.persoonslijst.overlijden.page1.Page1OverlijdenBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1OverlijdenForm extends PlForm {

  public Page1OverlijdenForm() {
    setOrder(DATUM, PLAATS, LAND);
  }

  @Override
  public Object getNewBean() {
    return new Page1OverlijdenBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
  }
}
