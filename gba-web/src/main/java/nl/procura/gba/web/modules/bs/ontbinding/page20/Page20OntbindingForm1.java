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

package nl.procura.gba.web.modules.bs.ontbinding.page20;

import static nl.procura.gba.web.modules.bs.ontbinding.page20.Page20OntbindingBean1.*;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page20OntbindingForm1 extends GbaForm<Page20OntbindingBean1> {

  public Page20OntbindingForm1() {

    setCaption("Huwelijk/GPS sluitinggegevens");
    setColumnWidths("100px", "");
    setOrder(SOORT, DATUM, LAND, PLAATS_NL, PLAATS_BL);
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
  public Page20OntbindingBean1 getNewBean() {
    return new Page20OntbindingBean1();
  }
}
