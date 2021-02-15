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

package nl.procura.gba.web.modules.zaken.riskanalysis.page2;

import static nl.procura.vaadin.component.container.ProcuraContainer.OMSCHRIJVING;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.AbstractValidator;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.riskanalysis.page1.RiskProfileContainer;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.riskanalysis.page1.Page1RiskAnalysis;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.ProcuraContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

import lombok.Data;

public class Page2RiskAnalysis extends ZakenPage {

  public static final String F_PROFILE           = "profile";
  public static final String F_RELATED_CASE_TYPE = "relatedCaseType";
  public static final String F_RELATED_CASE      = "relatedCase";

  private Form form;

  public Page2RiskAnalysis() {
    super("Risicoanalyse - nieuw");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonSave);

      form = new Form();
      addComponent(new Fieldset("Risicoanalyse", form));
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    super.onPreviousPage();
  }

  @Override
  public void onSave() {
    form.commit();

    RiskAnalysisService service = getServices().getRiskAnalysisService();
    RiskAnalysisRelatedCase relatedCase = form.getBean().getRelatedCase();
    Dossier dossier = service.getNewZaak(form.getBean().getProfile(), relatedCase);
    service.save(dossier);

    successMessage("Gegevens zijn opgeslagen");

    getNavigation().goToPage(new Page1RiskAnalysis());
    getNavigation().removeOtherPages();
  }

  /**
   * Bean
   */
  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Bean implements Serializable {

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Uit te voeren profiel",
        required = true)
    @Select(itemCaptionPropertyId = OMSCHRIJVING,
        containerDataSource = RiskProfileContainer.class)
    private RiskProfile profile;

    @Field(type = Field.FieldType.LABEL,
        caption = "Toepassen op",
        readOnly = true,
        width = "400px")
    private String relatedCaseType = "";

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Koppelen aan zaak",
        required = true)
    @Select(itemCaptionPropertyId = OMSCHRIJVING)
    private RiskAnalysisRelatedCase relatedCase;
  }

  /**
   * Form
   */
  public class Form extends GbaForm<Bean> {

    public Form() {
      setColumnWidths(WIDTH_130, "");
      setReadonlyAsText(false);
      setOrder(F_PROFILE, F_RELATED_CASE_TYPE, F_RELATED_CASE);
    }

    @Override
    public void afterSetBean() {
      getField(F_PROFILE).addListener((ValueChangeListener) event -> {
        String types = "";
        RiskProfile profile = (RiskProfile) event.getProperty().getValue();
        List<RiskAnalysisRelatedCase> cases = new ArrayList<>();
        if (profile != null) {
          RiskAnalysisService service = getApplication().getServices().getRiskAnalysisService();
          types = service.getTypesDescription(profile);
          BasePLExt pl = getApplication().getServices().getPersonenWsService().getHuidige();
          cases.addAll(service.getApplicableCases(profile, pl));
          if (cases.isEmpty()) {
            new Message(getWindow(), "Geen zaken gevonden met de correcte status", Message.TYPE_WARNING_MESSAGE);
          }
        }

        setTypes(types);
        getField(F_RELATED_CASE, ProNativeSelect.class).setContainerDataSource(new CaseContainer(cases));
        getField(F_RELATED_CASE).addValidator(new CaseValidator());
      });
      super.afterSetBean();
    }

    private void setTypes(String types) {
      com.vaadin.ui.Field field = getField(F_RELATED_CASE_TYPE);
      field.setReadOnly(false);
      field.setValue(types);
      field.setReadOnly(true);
    }

    @Override
    public Bean getNewBean() {
      return new Bean();
    }

  }

  /**
   * Validator to prevent double
   */
  public class CaseValidator extends AbstractValidator {

    public CaseValidator() {
      super("Deze zaak is al gekoppeld aan een risicoanalyse");
    }

    @Override
    public boolean isValid(Object value) {
      if (value instanceof RiskAnalysisRelatedCase) {
        RiskAnalysisRelatedCase relatedCase = (RiskAnalysisRelatedCase) value;
        return !getApplication().getServices().getRiskAnalysisService().hasRiskAnalysisCase(relatedCase);
      }
      return true;
    }
  }

  /**
   * Container which consists of relocation cases
   */
  public class CaseContainer extends IndexedContainer implements ProcuraContainer {

    public CaseContainer(List<RiskAnalysisRelatedCase> relatedCases) {
      addContainerProperty(OMSCHRIJVING, String.class, "");
      removeAllItems();

      for (RiskAnalysisRelatedCase riskAnalysisRelatedCase : relatedCases) {
        Item item = addItem(riskAnalysisRelatedCase);
        String descr = ZaakUtils.getTypeEnOmschrijving(riskAnalysisRelatedCase.getZaak());
        if (getApplication().getServices().getRiskAnalysisService().hasRiskAnalysisCase(riskAnalysisRelatedCase)) {
          descr = "Heeft al een risicoanalyse: " + descr;
        }
        item.getItemProperty(OMSCHRIJVING).setValue(StringUtils.capitalize(descr.toLowerCase()));
      }
    }
  }
}
