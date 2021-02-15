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

package nl.procura.gba.web.modules.bs.omzetting.page60;

import static nl.procura.gba.web.modules.bs.omzetting.page60.Page60OmzettingBean1.NATIOP1;
import static nl.procura.gba.web.modules.bs.omzetting.page60.Page60OmzettingBean1.NATIOP2;

import java.util.List;

import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page60OmzettingForm1 extends GbaForm<Page60OmzettingBean1> {

  public Page60OmzettingForm1(Page60OmzettingBean1 bean) {

    setCaption("Naam na sluiting verbintenis");
    setColumnWidths("150px", "");

    init();
    setBean(bean);
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(NATIOP1)) {

      List<DossierNationaliteit> nationaliteiten = getBean().getP1().getNationaliteiten();

      column.addComponent(
          new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.NAMENRECHT, nationaliteiten));
    }

    if (property.is(NATIOP2)) {

      List<DossierNationaliteit> nationaliteiten = getBean().getP2().getNationaliteiten();

      column.addComponent(
          new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.NAMENRECHT, nationaliteiten));
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page60OmzettingBean1 getNewBean() {
    return new Page60OmzettingBean1();
  }

  protected void init() {
  }
}
