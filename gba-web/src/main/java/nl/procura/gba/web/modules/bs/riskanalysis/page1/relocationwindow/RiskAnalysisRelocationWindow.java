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

package nl.procura.gba.web.modules.bs.riskanalysis.page1.relocationwindow;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.components.listeners.ValueChangeListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.table.TableLayout;

import lombok.Data;

public class RiskAnalysisRelocationWindow extends GbaModalWindow {

  public static final String CASE_ID = "caseId";
  public static final String TYPE    = "type";
  public static final String ADDRESS = "address";

  private ValueChangeListener     changeListener;
  private RiskAnalysisRelatedCase relatedCase;

  public RiskAnalysisRelocationWindow(RiskAnalysisRelatedCase relatedCase,
      ValueChangeListener<RiskAnalysisRelatedCase> changeListener) {
    super("Zoek de verhuizing (Escape om te sluiten)", "475px");
    this.relatedCase = relatedCase;
    this.changeListener = changeListener;
    addComponent(new MainModuleContainer(false, new Page()));
  }

  @Override
  protected void close() {
    changeListener.onChange(relatedCase);
    super.close();
  }

  public class Page extends NormalPageTemplate {

    private Form form;

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {
        addButton(buttonSave);
        form = new Form();
        addComponent(form);
      }

      super.event(event);
    }

    @Override
    public void onSave() {
      form.commit();
      onClose();
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
    }

    public class Form extends GbaForm<Bean> {

      public Form() {
        setCaption("Verhuizing");
        setColumnWidths("100px", "");
        setOrder(CASE_ID, TYPE, ADDRESS);
        setReadonlyAsText(false);
        setBean();
      }

      public void setBean() {
        Bean bean = new Bean();
        if (relatedCase != null) {
          bean.setCaseId(relatedCase.getZaakId());
          bean.setType(relatedCase.getDescr());
          bean.setAddress(relatedCase.getAddress().getAdres());
        }
        setBean(bean);
      }

      @Override
      public void afterSetColumn(TableLayout.Column column, com.vaadin.ui.Field field, Property property) {
        if (property.is(CASE_ID)) {
          column.addComponent(new Button("Zoek", (Button.ClickListener) event -> {
            getWindow().setHeight(null);
            form.commit();
          }));
        }
        super.afterSetColumn(column, field, property);
      }

      @Override
      public Bean getNewBean() {
        return new Bean();
      }

      @Override
      public void afterSetBean() {
        super.afterSetBean();
        getField(CASE_ID).addValidator(new CaseValidator());
      }
    }

    public class CaseValidator extends AbstractStringValidator {

      private String  value       = "";
      private boolean returnValue = false;

      public CaseValidator() {
        super("Geen verhuizing met dit zaak-id");
      }

      @Override
      public boolean isValid(Object value) {
        return super.isValid(value);
      }

      @Override
      protected boolean isValidString(String value) {
        boolean returnValue = true;
        if (Objects.equals(value, this.value)) {
          returnValue = this.returnValue;
        } else {
          if (StringUtils.isNotBlank(value.trim())) {
            relatedCase = null;
            ZaakArgumenten za = new ZaakArgumenten();
            za.addZaakKey(new ZaakKey(value));
            List<Zaak> zaken = Services.getInstance().getZakenService().getVolledigeZaken(za);
            if (zaken.isEmpty()) {
              setErrorMessage("Geen verhuizing gevonden met zaak-id: " + value.trim());
              returnValue = false;
            } else {
              relatedCase = zaken.stream().findFirst().map(z -> new RiskAnalysisRelatedCase(z)).orElse(relatedCase);
            }
          }
        }

        this.value = value;
        this.returnValue = returnValue;

        if (relatedCase != null) {
          form.getField(TYPE).setValue(relatedCase.getDescr());
          form.getField(ADDRESS).setValue(relatedCase.getAddress().getAdres());
        } else {
          form.getField(TYPE).setValue("");
          form.getField(ADDRESS).setValue("");
        }

        return returnValue;
      }
    }

    @Data
    @FormFieldFactoryBean(accessType = ElementType.FIELD)
    public class Bean implements Serializable {

      @Field(customTypeClass = GbaTextField.class,
          caption = "Zaak-ID",
          width = "180px")
      @Immediate
      @TextField(nullRepresentation = "")
      private String caseId;

      @Field(customTypeClass = GbaTextField.class,
          caption = "Type verhuizing",
          readOnly = true)
      @TextField(nullRepresentation = "")
      private String type = "";

      @Field(customTypeClass = GbaTextField.class,
          caption = "Adres",
          readOnly = true)
      @TextField(nullRepresentation = "")
      private String address = "";
    }
  }
}
