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

package nl.procura.gba.web.modules.account.ontbrekende.email;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoService;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoType;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.EmailField;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class OntbrekendeEmailDialog extends GbaModalWindow implements OntbrekendeDialog {

  private static final String EMAIL = "email";

  private final Services services;

  public OntbrekendeEmailDialog(Services services) {

    super("Ontbrekend e-mailadres", "400px");

    this.services = services;

    setClosable(false);
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();

    addComponent(mainModule);

    Module module = new Module();

    mainModule.getNavigation().addPage(module);
  }

  @Override
  public boolean isNodig() {
    return !services.getGebruikerInfoService().heeftInfo(services.getGebruiker(), GebruikerInfoType.email);
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD,
      defaultWidth = "220px")
  public static class Bean implements Serializable {

    @Field(customTypeClass = EmailField.class,
        caption = "E-mailadres",
        required = true)
    private String email = "";

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }

  public class Form extends GbaForm<Bean> {

    public Form(Bean bean) {

      setColumnWidths("90px", "");
      setOrder(EMAIL);

      setBean(bean);
    }
  }

  public class Module extends ZakenModuleTemplate {

    public Module() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {

        getPages().getNavigation().goToPage(new Page1());
      }
    }
  }

  public class Page1 extends ButtonPageTemplate {

    private Form form = null;

    public Page1() {
      setSpacing(true);
    }

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {

        buttonClose.setCaption("Overslaan (Esc)");

        addButton(buttonSave, 1f);
        addButton(buttonClose);

        setInfo("Wat is uw e-mailadres?", "");

        form = new Form(new Bean());

        addComponent(form);
      }

      super.event(event);
    }

    @Override
    public void onClose() {
      closeWindow();
    }

    @Override
    public void onSave() {

      form.commit();

      gebruikerInfoOpslaan(GebruikerInfoType.email, form.getBean().getEmail());

      getApplication().getServices().reloadGebruiker();

      new Message(getParentWindow(), "E-mailadres is opgeslagen", Message.TYPE_SUCCESS);

      closeWindow();
    }

    private void gebruikerInfoOpslaan(GebruikerInfoType type, String waarde) {

      getServices().getGebruikerService().checkEmail(getServices().getGebruiker(), waarde);

      GebruikerInfoService db = getServices().getGebruikerInfoService();

      db.save(type, getServices().getGebruiker(), waarde);
    }
  }
}
