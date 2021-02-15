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

package nl.procura.gba.web.modules.tabellen.riskprofile.page2;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRelatedCaseType;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.tabellen.riskprofile.page2.windows.ProfileRuleWindow;
import nl.procura.vaadin.component.layout.Fieldset;

public class Page2RiskProfile extends NormalPageTemplate {

  protected final Button buttonAddRule = new Button("Toevoegen");
  protected final Button buttonRemRule = new Button("Verwijderen");

  private RiskProfile                     riskProfile;
  private Set<RiskProfileRelatedCaseType> types;
  private Page2RiskProfileForm            form;
  private Table1                          table;

  public Page2RiskProfile(RiskProfile riskProfile, Set<RiskProfileRelatedCaseType> types) {
    super("Toevoegen / muteren risicoprofiel");
    this.riskProfile = riskProfile;
    this.types = types;
    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  @Override
  protected void initPage() {
    super.initPage();
    form = new Page2RiskProfileForm(riskProfile, types);
    table = new Table1();

    OptieLayout ol = new OptieLayout();
    ol.getRight().setWidth("200px");
    ol.getRight().setCaption("Opties");
    ol.getRight().addButton(buttonAddRule, this);
    ol.getRight().addButton(buttonRemRule, this);
    ol.getLeft().addComponent(new Fieldset("Profielregels"));
    ol.getLeft().addExpandComponent(table);

    buttonAddRule.addListener((Button.ClickListener) e -> {
      RiskProfileRule rule = new RiskProfileRule();
      rule.setRiskProfile(riskProfile);
      getWindow().addWindow(new ProfileRuleWindow(rule, () -> table.init()));
    });

    buttonRemRule.addListener((Button.ClickListener) e -> {
      new DeleteProcedure<RiskProfileRule>(table) {

        @Override
        public void deleteValue(RiskProfileRule rule) {
          getServices().getRiskAnalysisService().delete(rule);
          table.init();
        }
      };
    });

    addComponent(form);
    addExpandComponent(ol);
  }

  @Override
  public void onNew() {
    form.reset();
    riskProfile = new RiskProfile();
    types = new HashSet<>();
    table.init();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    Page2RiskProfileBean bean = form.getBean();
    riskProfile.setName(bean.getName());
    riskProfile.setThreshold(toBigDecimal(bean.getThreshold()));

    getServices().getRiskAnalysisService().save(riskProfile, bean.getRelatedCaseTypes());
    successMessage("De gegevens zijn opgeslagen.");
    table.init();

    super.onSave();
  }

  private void checkProfileButtons() {
    buttonAddRule.setEnabled(riskProfile.isStored());
    buttonRemRule.setEnabled(riskProfile.isStored());
  }

  class Table1 extends GbaTable {

    @Override
    public void setColumns() {
      setSelectable(true);
      addColumn("Nr", 50);
      addColumn("Naam");
      addColumn("Type");
      addColumn("Score", 100);
      addColumn("Volgorde", 100);

      super.setColumns();
    }

    @Override
    public void onDoubleClick(Record record) {
      getWindow().addWindow(new ProfileRuleWindow(record.getObject(RiskProfileRule.class), () -> table.init()));
    }

    @Override
    public void setRecords() {
      int nr = 0;
      for (RiskProfileRule rule : riskProfile.getRules()) {
        Record r = addRecord(rule);
        r.addValue(++nr);
        r.addValue(rule.getName());
        r.addValue(rule.getRuleType());
        r.addValue(rule.getScore());
        r.addValue(astr(rule.getVnr().intValue() > 0 ? rule.getVnr() : "N.v.t."));
      }

      checkProfileButtons();

      super.setRecords();
    }
  }
}
