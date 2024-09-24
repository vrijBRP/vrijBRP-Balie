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

package nl.procura.gba.web.modules.bs.lv.page30;

import static nl.procura.gba.web.modules.bs.lv.page30.Page30LvBean2.AKTE_JAAR;
import static nl.procura.gba.web.modules.bs.lv.page30.Page30LvBean2.AKTE_NUMMER;
import static nl.procura.gba.web.modules.bs.lv.page30.Page30LvBean2.AKTE_PLAATS;
import static nl.procura.gba.web.modules.bs.lv.page30.Page30LvBean2.HUIDIG_BRP_AKTE_NUMMER;
import static nl.procura.gba.web.modules.bs.lv.page30.Page30LvBean2.NIEUW_BRP_AKTE_NUMMER;

import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.lv.page30.Page30Lv.GeboorteGegevens;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page30LvForm2 extends GbaForm<Page30LvBean2> {

  private final GeboorteGegevens geboorteGegevens;

  public Page30LvForm2(GeboorteGegevens geboorteGegevens) {
    this.geboorteGegevens = geboorteGegevens;
    setCaption("Geboorteaktegegevens");
    setColumnWidths("220px", "");
    setOrder(AKTE_NUMMER, HUIDIG_BRP_AKTE_NUMMER, NIEUW_BRP_AKTE_NUMMER, AKTE_PLAATS, AKTE_JAAR);
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(AKTE_NUMMER)) {
      column.addComponent(new Label("(max. 7 tekens lang)"));
    }

    if (property.is(HUIDIG_BRP_AKTE_NUMMER)) {
      column.addComponent(new Label("(max. 7 tekens lang)"));
    }

    if (property.is(NIEUW_BRP_AKTE_NUMMER)) {
      column.addComponent(new Label("(7 posities, 3e letter behorende bij de soort)"));
    }

    if (property.is(AKTE_PLAATS) && geboorteGegevens.getPlaats() != null) {
      column.addComponent(new Label("(" + geboorteGegevens.getPlaats() + " is de geboorteplaats)"));
    }

    if (property.is(AKTE_JAAR) && geboorteGegevens.getJaar() != null) {
      column.addComponent(new Label("(" + geboorteGegevens.getJaar() + " is het geboortejaar)"));
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page30LvBean2 getNewBean() {
    return new Page30LvBean2();
  }
}
