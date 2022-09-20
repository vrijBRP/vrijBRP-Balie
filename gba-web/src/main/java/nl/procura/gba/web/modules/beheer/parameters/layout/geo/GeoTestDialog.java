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

package nl.procura.gba.web.modules.beheer.parameters.layout.geo;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.Data;

public abstract class GeoTestDialog extends GbaModalWindow {

  private static final String POSTCODE  = "postcode";
  private static final String HNR       = "hnr";
  private static final String RESULTAAT = "resultaat";

  public GeoTestDialog() {
    super("GEO / BAG koppeling testen (Escape om te sluiten)", "700px");
  }

  @Override
  public void attach() {
    super.attach();
    MainModuleContainer mainModule = new MainModuleContainer();
    addComponent(mainModule);
    Module module = new Module();
    mainModule.getNavigation().addPage(module);
  }

  public abstract String onTest(String pc, String hnr);

  @Data
  @FormFieldFactoryBean(
      accessType = ElementType.FIELD,
      defaultWidth = "100px")
  public static class Bean implements Serializable {

    @Field(customTypeClass = PostalcodeField.class,
        caption = "Postcode",
        required = true)
    private FieldValue postcode;

    @Field(customTypeClass = NumberField.class,
        caption = "Huisnummer",
        required = true)
    private String hnr = "";

    @Field(type = Field.FieldType.LABEL,
        caption = "Resultaat",
        required = true)
    private String resultaat = "";
  }

  public class Form1 extends GbaForm<Bean> {

    public Form1(Bean bean) {
      setCaption("Zoekargumenten");
      setColumnWidths("90px", "");
      setOrder(POSTCODE, HNR);
      setBean(bean);
    }
  }

  public class Form2 extends GbaForm<Bean> {

    public Form2(Bean bean) {
      setCaption("Resultaat");
      setColumnWidths("90px", "");
      setOrder(RESULTAAT);
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

    private Form1 form1 = null;
    private Form2 form2 = null;

    public Page1() {
      setSpacing(true);
    }

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {
        addButton(buttonSearch, 1f);
        addButton(buttonClose);

        Bean bean = new Bean();
        form1 = new Form1(bean);
        addComponent(form1);

        form2 = new Form2(new Bean());
        addComponent(form2);
      }

      super.event(event);
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
    }

    @Override
    public void onEnter() {
      onSearch();
    }

    @Override
    public void onSearch() {

      form1.commit();

      Bean bean = new Bean();
      String postcode = form1.getBean().getPostcode().getStringValue();
      String hnr = form1.getBean().getHnr();
      try {
        bean.setResultaat(MiscUtils.setClass(true, onTest(postcode, hnr)));
      } catch (RuntimeException e) {
        bean.setResultaat(MiscUtils.setClass(false, e.getMessage()));
      }

      form2.setBean(bean);
    }
  }
}
