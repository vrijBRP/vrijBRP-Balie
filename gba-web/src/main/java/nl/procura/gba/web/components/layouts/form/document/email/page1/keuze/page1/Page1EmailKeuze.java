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

package nl.procura.gba.web.components.layouts.form.document.email.page1.keuze.page1;

import static nl.procura.gba.web.common.misc.email.EmailAddressType.TO;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.common.misc.email.EmailAddress;
import nl.procura.gba.web.common.misc.email.EmailAddressType;
import nl.procura.gba.web.components.fields.GbaInternetAddressField;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.form.document.email.page1.EmailContainer;
import nl.procura.gba.web.components.layouts.form.document.email.page1.EmailPreviewContainer;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.Data;

public class Page1EmailKeuze extends NormalPageTemplate {

  public static final String          FIELD    = "field";
  public static final String          PERSON   = "person";
  public static final String          FUNCTION = "function";
  public static final String          NAME     = "name";
  public static final String          EMAIL    = "email";
  private final EmailAddress          emailAdres;
  private final EmailPreviewContainer container;
  private Form                        form     = null;

  public Page1EmailKeuze(EmailPreviewContainer container, EmailAddress emailAdres) {

    this.container = container;
    this.emailAdres = emailAdres;

    setSpacing(true);
    setSizeFull();

    buttonClose.setCaption("Annuleren (Esc)");
    H2 h2 = new H2("E-mailadres toevoegen");
    addButton(buttonReset);
    addButton(buttonSave);
    addButton(buttonClose);

    getButtonLayout().addComponent(h2, 0);
    getButtonLayout().setExpandRatio(h2, 1f);
    getButtonLayout().setWidth("100%");
    getButtonLayout().setExpandRatio(h2, 1f);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new Form(emailAdres);
      addExpandComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
    super.onClose();
  }

  @Override
  public void onNew() {
    form.reset();
    super.onNew();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    emailAdres.setType(form.getBean().getField());
    emailAdres.setFunction(form.getBean().getFunction());
    emailAdres.setName(form.getBean().getName());
    emailAdres.setEmail(form.getBean().getEmail());

    if (!container.getAdressen().contains(emailAdres)) {
      container.getAdressen().add(emailAdres);
    }

    container.getTable().init();

    onClose();
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  @Data
  public static class Bean implements Serializable {

    @Field(customTypeClass = ProNativeSelect.class,
        caption = "Veld",
        required = true,
        width = "120px")
    @Select(containerDataSource = OntvangerTypeContainer.class)
    private EmailAddressType field = null;

    @Field(customTypeClass = ProNativeSelect.class,
        caption = "Personen",
        width = "450px")
    @Select(itemCaptionPropertyId = EmailContainer.OMSCHRIJVING)
    private EmailAddress person = null;

    @Field(customTypeClass = GbaTextField.class,
        caption = "Functie",
        width = "200px")
    private String function = "";

    @Field(customTypeClass = GbaTextField.class,
        caption = "Naam",
        required = true,
        width = "200px")
    private String name = "";

    @Field(customTypeClass = GbaInternetAddressField.class,
        caption = "E-mailadres",
        required = true,
        width = "200px")
    @TextField(nullRepresentation = "",
        maxLength = 255)
    private String email = "";
  }

  public static class OntvangerTypeContainer extends ArrayListContainer {

    public OntvangerTypeContainer() {
      super(EmailAddressType.values());
    }
  }

  public class Form extends GbaForm<Bean> {

    public Form(EmailAddress emailOntvanger) {

      setColumnWidths("90px", "");
      setOrder(PERSON, FIELD, FUNCTION, NAME, EMAIL);

      Bean bean = new Bean();

      bean.setField(emailOntvanger.getType());
      bean.setFunction(emailOntvanger.getFunction());
      bean.setName(emailOntvanger.getName());
      bean.setEmail(emailOntvanger.getEmail());

      setBean(bean);
    }

    @Override
    public Bean getNewBean() {
      return new Bean();
    }

    @Override
    public void setBean(Object bean) {

      super.setBean(bean);

      ProNativeSelect persoonVeld = getField(PERSON, ProNativeSelect.class);

      persoonVeld.setContainerDataSource(new EmailContainer(getGebruikerAdressen(), container));

      persoonVeld.addListener((ValueChangeListener) event -> {

        Object value = event.getProperty().getValue();

        if (value != null) {
          EmailAddress ontvanger = (EmailAddress) value;
          String function = ontvanger.getFunction();
          String naam = ontvanger.getName();
          String email = ontvanger.getEmail();

          ProNativeSelect type = getField(FIELD, ProNativeSelect.class);

          if (type.getValue() == null) {
            type.setValue(TO);
          }

          getField(FUNCTION, GbaTextField.class).setValue(function);
          getField(NAME, GbaTextField.class).setValue(naam);
          getField(EMAIL, GbaInternetAddressField.class).setValue(email);
        }

        repaint();
      });
    }

    /**
     * E-mailadres van de gebruiker
     */
    private List<EmailAddress> getGebruikerAdressen() {
      List<EmailAddress> adressen = new ArrayList<>();
      Gebruiker gebruiker = Page1EmailKeuze.this.getServices().getGebruiker();
      if (fil(gebruiker.getEmail())) {
        adressen.add(new EmailAddress(EmailAddressType.TO, "Gebruiker",
            gebruiker.getNaam(), gebruiker.getEmail()));
      }

      return adressen;
    }
  }
}
