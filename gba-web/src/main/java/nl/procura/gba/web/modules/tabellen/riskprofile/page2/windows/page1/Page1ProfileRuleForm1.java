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

package nl.procura.gba.web.modules.tabellen.riskprofile.page2.windows.page1;

import static nl.procura.gba.web.modules.tabellen.riskprofile.page2.windows.page1.Page1ProfileRuleBean1.*;

import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;

public class Page1ProfileRuleForm1 extends GbaForm<Page1ProfileRuleBean1> {

  private FieldChangeListener<RiskProfileRuleType> changeTypeListener;

  public Page1ProfileRuleForm1(RiskProfileRule rule, FieldChangeListener<RiskProfileRuleType> changeTypeListener) {

    this.changeTypeListener = changeTypeListener;
    setCaption("Profielregel");
    setOrder(F_NAME, F_TYPE, F_SCORE, F_VNR);
    setColumnWidths(WIDTH_130, "");

    Page1ProfileRuleBean1 bean = new Page1ProfileRuleBean1();
    bean.setName(rule.getName());
    bean.setType(rule.getRuleType());
    bean.setScore(rule.getScore());
    bean.setVnr(rule.getVnr());

    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    getField(F_TYPE).addListener(changeTypeListener);
    super.afterSetBean();
  }
}
