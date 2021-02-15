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

package nl.procura.gba.web.modules.zaken.riskanalysis.window;

import static java.text.MessageFormat.format;
import static nl.procura.gba.web.services.zaken.algemeen.ZaakUtils.getTypeEnOmschrijving;
import static nl.procura.vaadin.component.container.ProcuraContainer.OMSCHRIJVING;
import static org.apache.commons.lang3.StringUtils.capitalize;

import java.lang.annotation.ElementType;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
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

/**

 *
 * Window for adding a riskprofile to a related case
 */
public class NewRiskAnalysisWindow extends GbaModalWindow {

  public static final String F_RELATED_CASE = "relatedCase";
  public static final String F_PROFILE      = "profile";

  private final RiskAnalysisRelatedCase relatedCase;
  private final ChangeListener          changeListener;
  private Form                          form;

  public NewRiskAnalysisWindow(RiskAnalysisRelatedCase relatedCase, ChangeListener changeListener) {
    super(false, "", "600px");
    this.relatedCase = relatedCase;
    this.changeListener = changeListener;
    setCaption(capitalize((format("Risicoanalyse toevoegen aan: {0}",
        getTypeEnOmschrijving(relatedCase.getZaak()))).toLowerCase()));
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page()));
  }

  private class Page extends NormalPageTemplate {

    @Override
    public void event(PageEvent event) {
      super.event(event);

      if (event.isEvent(InitPage.class)) {
        addButton(buttonSave, 1f);
        addButton(BUTTON_CLOSE);

        form = new Form();
        addComponent(new Fieldset("Risicoanalyse", form));
      }
    }

    @Override
    public void onSave() {
      form.commit();
      form.getField(F_PROFILE).commit(); // Field could be readonly and then is not commmitted.

      RiskAnalysisService service = getServices().getRiskAnalysisService();
      RiskProfile profile = form.getBean().getProfile();
      Dossier dossier = service.getNewZaak(profile, relatedCase);
      service.save(dossier);
      new Message(getParentWindow(), "De gegevens zijn opgeslagen", Message.TYPE_SUCCESS);
      changeListener.onChange();
      onClose();
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Bean {

    @Field(type = Field.FieldType.LABEL,
        caption = "Betreffende zaak",
        width = "350px")
    private String relatedCase;

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Risicoprofiel",
        width = "350px",
        required = true)
    @Select(itemCaptionPropertyId = OMSCHRIJVING,
        nullSelectionAllowed = false)
    private RiskProfile profile;
  }

  public class Form extends GbaForm<Bean> {

    public Form() {
      setColumnWidths("110px", "");
      setOrder(F_RELATED_CASE, F_PROFILE);
      Bean bean = new Bean();
      bean.setRelatedCase(ZaakUtils.getTypeEnOmschrijving(relatedCase.getZaak()));
      setBean(bean);
    }

    @Override
    public void afterSetBean() {
      getField(F_PROFILE, ProNativeSelect.class).setContainerDataSource(new ProfilesContainer());
      super.afterSetBean();
    }

    /**
     * Container which consists of the riskprofiles
     */
    private class ProfilesContainer extends IndexedContainer implements ProcuraContainer {

      public ProfilesContainer() {
        addContainerProperty(OMSCHRIJVING, String.class, "");
        RiskAnalysisService service = Services.getInstance().getRiskAnalysisService();
        service.getApplicableRiskProfile(relatedCase.getZaak())
            .ifPresent(riskProfile -> {
              Item item = addItem(riskProfile);
              item.getItemProperty(OMSCHRIJVING).setValue(riskProfile.getName());
            });
      }
    }
  }
}
