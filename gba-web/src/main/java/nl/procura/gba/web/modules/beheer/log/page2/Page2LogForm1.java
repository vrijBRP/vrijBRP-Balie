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

package nl.procura.gba.web.modules.beheer.log.page2;

import static nl.procura.gba.web.modules.beheer.log.page2.Page2LogBean1.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.beheer.log.InLogpoging;

public class Page2LogForm1 extends ReadOnlyForm {

  public Page2LogForm1() {
    setOrder(DATUMTIJD, INLOGNAAM, GEBRUIKER, BROWSER, IPADRES);
  }

  public Page2LogForm1(InLogpoging log) {
    this();
    setBean(new Page2LogBean1(log));
  }

  @Override
  public Field newField(Field field, Property property) {

    if (property.is(INLOGNAAM)) {
      getLayout().addBreak();
    }

    return super.newField(field, property);
  }
}
