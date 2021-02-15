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

import static nl.procura.gba.web.modules.bs.riskanalysis.page1.Page1RiskAnalysisBean2.F_RELOCATION;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.riskanalysis.page1.relocationwindow.RiskAnalysisRelocationWindow;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page1RiskAnalysisForm2 extends GbaForm<Page1RiskAnalysisBean2> {

  private RiskAnalysisRelatedCase                                                              relatedCase;
  private nl.procura.gba.web.components.listeners.ValueChangeListener<RiskAnalysisRelatedCase> changeListener;

  public Page1RiskAnalysisForm2(RiskAnalysisRelatedCase relatedCase,
      nl.procura.gba.web.components.listeners.ValueChangeListener<RiskAnalysisRelatedCase> changeListener) {

    this.relatedCase = relatedCase;
    this.changeListener = changeListener;

    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(false);
    setOrder(F_RELOCATION);
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_RELOCATION)) {
      column.addComponent(new Button("Selecteer", (Button.ClickListener) event -> {
        getWindow().addWindow(new RiskAnalysisRelocationWindow(relatedCase, returnValue -> {
          changeListener.onChange(returnValue);
        }));
      }));
    }
  }

  public void setRelatedCase(RiskAnalysisRelatedCase relatedCase) {
    Page1RiskAnalysisBean2 bean = new Page1RiskAnalysisBean2();
    if (relatedCase != null) {
      bean.setRelocation(String.format("%s naar %s",
          relatedCase.getDescr(),
          relatedCase.getAddress().getAdres()));
    } else {
      bean.setRelocation("<geen verhuizing geselecteerd>");
    }
    super.setBean(bean);
  }
}
