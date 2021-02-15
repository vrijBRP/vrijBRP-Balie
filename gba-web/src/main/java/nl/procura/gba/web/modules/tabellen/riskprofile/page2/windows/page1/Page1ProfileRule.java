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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Label;

import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleMap;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.components.containers.Container;
import nl.procura.gba.web.components.containers.StraatContainer;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.vaadin.component.field.*;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.table.TableLayout;

import lombok.AllArgsConstructor;
import lombok.Data;

public class Page1ProfileRule extends NormalPageTemplate {

  private VLayout               variablesLayout = new VLayout();
  private List<ComponentVar>    varList         = new ArrayList<>();
  private Label                 errorLabel      = new Label();
  private RiskProfileRule       riskProfileRule;
  private Page1ProfileRuleForm1 form;

  public Page1ProfileRule(RiskProfileRule riskProfileRule) {
    super("Toevoegen / muteren risicoprofielregel");
    this.riskProfileRule = riskProfileRule;
    setSpacing(true);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new Page1ProfileRuleForm1(riskProfileRule, new FieldChangeListener<RiskProfileRuleType>() {

        @Override
        public void onChange(RiskProfileRuleType type) {
          loadVariables(type);
        }
      });
      addComponent(form);
      variablesLayout.setWidth("100%");
      addComponent(variablesLayout);
      loadVariables(riskProfileRule.getRuleType());
    }

    super.event(event);
  }

  private void loadVariables(RiskProfileRuleType type) {

    TableLayout tl = new TableLayout();
    tl.addStyleName("v-form tableform v-form-error");
    tl.setColumnWidths(GbaForm.WIDTH_130, "");

    variablesLayout.removeAllComponents();
    varList.clear();
    getWindow().setHeight(null);

    if (type != null) {
      variablesLayout.addComponent(new Fieldset("Variabelen"));
      if (type.getVariables().getVariables().isEmpty()) {
        variablesLayout.addExpandComponent(new Label("Geen variabelen"));
      } else {
        type.getVariables().getVariables().forEach(var -> {
          AbstractField component = getComponent(var);
          varList.add(new ComponentVar(var.getVariable(), var.getLabel(), component));
          tl.addLabel(var.getLabel());
          tl.addData(component);
        });

        variablesLayout.addComponent(tl);
      }
    }
  }

  private AbstractField getComponent(RiskProfileRuleMap.RuleVariable var) {
    String value = riskProfileRule.getAttributes().getProperty(var.getVariable());
    AbstractField field;
    switch (var.getType()) {
      case DATE:
        field = new ProDateField();
        field.setWidth("80px");
        field.setValue(value);
        break;
      case POSNUMBER:
        field = new PosNumberField();
        PosNumberField posNumberField = (PosNumberField) field;
        posNumberField.setNullRepresentation("");
        posNumberField.setWidth("50px");
        posNumberField.setValue(value);
        break;
      case NUMBER:
        field = new NumberField();
        NumberField numberField = (NumberField) field;
        numberField.setNullRepresentation("");
        numberField.setWidth("50px");
        numberField.setValue(value);
        break;
      case COUNTRY:
      case DISTRICT1:
      case DISTRICT2:
      case DISTRICT3:
      case STREET:
        field = new ProComboBox();
        ProComboBox comboBox = (ProComboBox) field;
        if (RiskProfileRuleMap.Type.DISTRICT1 == var.getType()) {
          comboBox.setContainerDataSource(Container.WIJK);
        } else if (RiskProfileRuleMap.Type.DISTRICT2 == var.getType()) {
          comboBox.setContainerDataSource(Container.BUURT);
        } else if (RiskProfileRuleMap.Type.DISTRICT3 == var.getType()) {
          comboBox.setContainerDataSource(Container.SUBBUURT);
        } else if (RiskProfileRuleMap.Type.COUNTRY == var.getType()) {
          comboBox.setContainerDataSource(Container.LAND);
        } else if (RiskProfileRuleMap.Type.STREET == var.getType()) {
          comboBox.setContainerDataSource(new StraatContainer());
        }
        comboBox.setValue(new FieldValue(value));
        break;
      case TEXT:
      default:
        field = new GbaTextField();
        GbaTextField textField = (GbaTextField) field;
        textField.setValue(value);
        textField.setNullRepresentation("");
        break;
    }
    field.setValidationVisible(true);
    field.setRequired(var.isRequired());
    return field;
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    Page1ProfileRuleBean1 bean = form.getBean();

    riskProfileRule.setName(bean.getName());
    riskProfileRule.setRuleType(bean.getType());
    riskProfileRule.setScore(bean.getScore());
    riskProfileRule.setVnr(bean.getVnr());
    riskProfileRule.setAttributes(toProperties(varList));

    getServices().getRiskAnalysisService().save(riskProfileRule);
    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }

  private Properties toProperties(List<ComponentVar> list) {
    if (errorLabel != null) {
      variablesLayout.removeComponent(errorLabel);
      errorLabel = null;
    }
    return list.stream()
        .collect(Collectors.toMap(ComponentVar::getVariable,
            ComponentVar::getValue, (a, b) -> b, Properties::new));
  }

  @Data
  @AllArgsConstructor
  private class ComponentVar {

    private String        variable = null;
    private String        label    = null;
    private AbstractField field    = null;

    public String getValue() {
      try {

        field.setStyleName("");
        field.validate();
        return getFieldValue();

      } catch (Exception e) {
        field.setStyleName("v-textfield-error v-select-error v-filterselect-error");

        // Update errorLabel
        if (errorLabel == null) {
          errorLabel = new Label();
          errorLabel.setStyleName("v-form-errormessage");
          variablesLayout.addComponent(errorLabel);

          if (e instanceof Validator.EmptyValueException) {
            errorLabel.setValue("Veld \"" + label + "\" is verplicht");
          } else {
            errorLabel.setValue(e.getMessage());
          }
        }
      }
      return null;
    }

    /**
     * Returns the storable value based on the type of field
     */
    private String getFieldValue() {
      if (field instanceof ProDateField) {
        return String.valueOf(field.getValue());
      } else if (field instanceof ProNativeSelect) {
        FieldValue fv = (FieldValue) field.getValue();
        return fv.getStringValue();
      } else if (field instanceof ProComboBox) {
        FieldValue fv = (FieldValue) field.getValue();
        return fv.getStringValue();
      } else if (field instanceof GbaTextField) {
        return String.valueOf(field.getValue());
      } else {
        throw new IllegalArgumentException("Unknown field: " + field);
      }
    }
  }
}
