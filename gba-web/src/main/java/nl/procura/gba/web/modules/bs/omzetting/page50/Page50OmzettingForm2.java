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

package nl.procura.gba.web.modules.bs.omzetting.page50;

import static nl.procura.gba.web.modules.bs.omzetting.page50.Page50OmzettingBean2.*;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page50OmzettingForm2 extends GbaForm<Page50OmzettingBean2> {

  public Page50OmzettingForm2() {

    setCaption("GPS sluitingsgegevens");
    setColumnWidths("190px", "");
    setOrder(DATUM, LAND, PLAATS_NL, PLAATS_BL);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();

    if (getField(LAND) != null) {
      getField(LAND).addListener((ValueChangeListener) event -> changeLand());
      changeLand();
    }
  }

  public void changeLand() {

    FieldValue fv = (FieldValue) getField(LAND).getValue();

    if (fv == null || !pos(fv.getValue()) || Landelijk.isNederland(fv)) {
      getField(PLAATS_NL).setVisible(true);
      getField(PLAATS_BL).setVisible(false);
    } else {

      getField(PLAATS_NL).setVisible(false);
      getField(PLAATS_BL).setVisible(true);
    }

    repaint();
  }

  @Override
  public Page50OmzettingBean2 getNewBean() {
    return new Page50OmzettingBean2();
  }
}
