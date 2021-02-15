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

package nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page4;

import static nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page4.Page4HuwelijksLocatieOptiesBean.*;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatieOptieType;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page4HuwelijksLocatieForm extends GbaForm<Page4HuwelijksLocatieOptiesBean> {

  public Page4HuwelijksLocatieForm(HuwelijksLocatieOptie optie) {

    setCaption("Locatie optie");
    setOrder(OPTIE, VERPLICHT, TYPE, MIN, MAX, VNR, OMS, ALIAS);
    setColumnWidths(WIDTH_130, "");

    Page4HuwelijksLocatieOptiesBean bean = new Page4HuwelijksLocatieOptiesBean();
    bean.setOptie(optie.getHuwelijksLocatieOptie());
    bean.setType(optie.getOptieType());
    bean.setVerplicht(optie.isVerplichteOptie());
    bean.setVnr(optie.getVnr());
    bean.setMin(optie.getMin());
    bean.setMax(optie.getMax());
    bean.setAlias(StringUtils.join(optie.getAliassen(), ", "));

    setBean(bean);
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);

    getField(TYPE).addListener((ValueChangeListener) event -> check(event.getProperty().getValue()));
    check(getField(TYPE).getValue());
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(MIN, MAX)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  private void check(Object optie) {

    boolean isNumber = false;

    if (optie instanceof HuwelijksLocatieOptieType) {
      isNumber = (optie == HuwelijksLocatieOptieType.NUMBER);
    }

    getField(MIN).setVisible(isNumber);
    getField(MAX).setVisible(isNumber);

    repaint();
  }
}
