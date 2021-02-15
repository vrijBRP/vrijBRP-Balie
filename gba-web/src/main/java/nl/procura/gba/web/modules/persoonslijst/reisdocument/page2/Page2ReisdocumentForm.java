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

package nl.procura.gba.web.modules.persoonslijst.reisdocument.page2;

import static nl.procura.gba.web.modules.persoonslijst.reisdocument.page2.Page2ReisdocumentBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2ReisdocumentForm extends PlForm {

  public Page2ReisdocumentForm() {
    setOrder(REISDOCUMENT, NUMMER, AUTORITEIT, DATUMUITGIFTE, DATUMEINDE, INHOUDINGVERMISSING, SIGNALERING,
        BUITENLANDSDOCUMENT, LENGTE);
  }

  @Override
  public Page2ReisdocumentBean getBean() {
    return (Page2ReisdocumentBean) super.getBean();
  }

  @Override
  public Object getNewBean() {
    return new Page2ReisdocumentBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(SIGNALERING)) {
      getLayout().addBreak();
    }
  }
}
