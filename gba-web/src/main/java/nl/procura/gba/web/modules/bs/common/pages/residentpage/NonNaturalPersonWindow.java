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

package nl.procura.gba.web.modules.bs.common.pages.residentpage;

import java.lang.annotation.ElementType;
import java.util.function.Consumer;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextField;

import lombok.Data;

class NonNaturalPersonWindow extends GbaModalWindow {

  private final Page page;

  private Form form;

  NonNaturalPersonWindow(String consentProvider, Consumer<String> consentProviderConsumer) {
    super(true, "Niet-natuurlijk persoon (Druk op escape om te sluiten)", "600px");
    page = new Page(consentProvider, consentProviderConsumer);
  }

  @Override
  public void attach() {
    super.attach();
    final MainModuleContainer mainModule = new MainModuleContainer();
    addComponent(mainModule);
    mainModule.getNavigation().addPage(page);
  }

  public final class Form extends GbaForm<Bean> {

    private static final String OMSCHRIJVING = "omschrijving";

    private Form(String toestemmingAnders) {

      setOrder(OMSCHRIJVING);
      final Bean bean = new Bean();
      bean.setOmschrijving(toestemmingAnders);
      setBean(bean);
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean {

    @Field(type = FieldType.TEXT_FIELD, caption = "Niet-natuurlijk persoon", width = "400px")
    @TextField(maxLength = 200, nullRepresentation = "")
    private String omschrijving = "";
  }

  public class Page extends NormalPageTemplate {

    private final String           consentProvider;
    private final Consumer<String> consentProviderConsumer;

    public Page(String consentProvider, Consumer<String> consentProviderConsumer) {
      this.consentProvider = consentProvider;
      this.consentProviderConsumer = consentProviderConsumer;
      setMargin(false);
    }

    @Override
    protected void initPage() {
      addButton(buttonSave, 1F);
      addButton(buttonClose);

      form = new Form(consentProvider);
      addComponent(form);
      super.initPage();
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
      super.onClose();
    }

    @Override
    public void onSave() {
      form.commit();
      consentProviderConsumer.accept(form.getBean().getOmschrijving());
      super.onSave();
      onClose();
    }
  }
}
