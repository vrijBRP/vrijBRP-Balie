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

package nl.procura.gba.web.modules.beheer.parameters.layout.email.properties;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.EmailField;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class EmailTestDialog extends GbaModalWindow {

  private static final String EMAIL_AFZENDERTEST     = "emailAfzenderTest";
  private static final String EMAIL_ONTVANGERTEST    = "emailOntvangerTest";
  private static final String EMAIL_BEANTWOORDERTEST = "emailBeantwoorderTest";

  public EmailTestDialog() {
    super("E-mail testen (Escape om te sluiten)", "400px");
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();

    addComponent(mainModule);

    Module module = new Module();

    mainModule.getNavigation().addPage(module);
  }

  public abstract void onSend(String adres, String afzender, String beantwoorder);

  @FormFieldFactoryBean(accessType = ElementType.FIELD,
      defaultWidth = "220px")
  public static class Bean implements Serializable {

    @Field(customTypeClass = EmailField.class,
        caption = "Ontvanger",
        required = true)
    private String emailOntvangerTest = "";

    @Field(customTypeClass = EmailField.class,
        caption = "Afzender",
        required = true)
    private String emailAfzenderTest = "";

    @Field(customTypeClass = EmailField.class,
        caption = "Antwoord naar",
        required = true)
    private String emailBeantwoorderTest = "";

    public String getEmailAfzenderTest() {
      return emailAfzenderTest;
    }

    public void setEmailAfzenderTest(String emailAfzenderTest) {
      this.emailAfzenderTest = emailAfzenderTest;
    }

    public String getEmailBeantwoorderTest() {
      return emailBeantwoorderTest;
    }

    public void setEmailBeantwoorderTest(String emailBeantwoorderTest) {
      this.emailBeantwoorderTest = emailBeantwoorderTest;
    }

    public String getEmailOntvangerTest() {
      return emailOntvangerTest;
    }

    public void setEmailOntvangerTest(String emailOntvangerTest) {
      this.emailOntvangerTest = emailOntvangerTest;
    }
  }

  public class Form extends GbaForm<Bean> {

    public Form(Bean bean) {

      setColumnWidths("90px", "");
      setOrder(EMAIL_ONTVANGERTEST, EMAIL_AFZENDERTEST, EMAIL_BEANTWOORDERTEST);

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

    protected final Button buttonSend = new Button("Verzenden");
    private Form           form       = null;

    public Page1() {
      setSpacing(true);
    }

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {

        addButton(buttonSend, 1f);
        addButton(buttonClose);

        setInfo("Testen e-mail", "");

        Bean bean = new Bean();

        bean.setEmailOntvangerTest(
            getServices().getParameterService().getParm(ParameterConstant.EMAIL_ONTVANGER_TEST));
        bean.setEmailAfzenderTest(
            getServices().getParameterService().getParm(ParameterConstant.EMAIL_AFZENDER_TEST));
        bean.setEmailBeantwoorderTest(
            getServices().getParameterService().getParm(ParameterConstant.EMAIL_BEANTWOORDER_TEST));

        form = new Form(bean);

        addComponent(form);
      }

      super.event(event);
    }

    @Override
    public void handleEvent(Button button, int keyCode) {

      if (button == buttonSend) {

        form.commit();

        String ontvanger = form.getBean().getEmailOntvangerTest();
        String afzender = form.getBean().getEmailAfzenderTest();
        String beantwoorder = form.getBean().getEmailBeantwoorderTest();

        getServices().getParameterService().saveParameter(ParameterConstant.EMAIL_ONTVANGER_TEST, ontvanger, 0,
            0);
        getServices().getParameterService().saveParameter(ParameterConstant.EMAIL_AFZENDER_TEST, afzender, 0, 0);
        getServices().getParameterService().saveParameter(ParameterConstant.EMAIL_BEANTWOORDER_TEST, beantwoorder,
            0, 0);

        getApplication().getServices().reloadGebruiker();

        onSend(ontvanger, afzender, beantwoorder);
      }

      if (button == buttonClose) {
        closeWindow();
      }

      super.handleEvent(button, keyCode);
    }
  }
}
