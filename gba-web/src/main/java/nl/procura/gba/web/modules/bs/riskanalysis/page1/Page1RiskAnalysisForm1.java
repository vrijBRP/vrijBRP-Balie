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

package nl.procura.gba.web.modules.bs.riskanalysis.page1;

import static nl.procura.gba.web.modules.bs.riskanalysis.page1.Page1RiskAnalysisBean1.PROFILE;

import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page1RiskAnalysisForm1 extends GbaForm<Page1RiskAnalysisBean1> {

  private ValueChangeListener changeListener;

  public Page1RiskAnalysisForm1(ValueChangeListener changeListener) {
    this.changeListener = changeListener;
    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(false);
    setOrder(PROFILE);
  }

  @Override
  public void afterSetBean() {
    getField(PROFILE).addListener(changeListener);
    super.afterSetBean();
  }

  @Override
  public Page1RiskAnalysisBean1 getNewBean() {
    return new Page1RiskAnalysisBean1();
  }
}
