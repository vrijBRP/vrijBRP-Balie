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

package nl.procura.gba.web.modules.zaken.rijbewijs.page14;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RYB_PV_NR;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.math.BigInteger;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.rdw.messages.P0252;
import nl.procura.rdw.processen.p0252.f08.NATPERSOONGEG;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.Data;

public abstract class RijbewijsOntvangstDialog extends GbaModalWindow {

  static final String PROCESVERBAAL_FIELD = "procesVerbaal";
  static final String AUTHCODE_FIELD      = "autoriteitscode";
  static final String RIJB_NR_FIELD       = "rijbewijsNr";
  static final String NAT_PERS_SL_FIELD   = "natPersSleutel";

  private final String   caption;
  private final String[] fields;
  private final P0252    p0252;

  RijbewijsOntvangstDialog(P0252 p0252, String caption, String width, String... fields) {
    super("Wilt u deze actie uitvoeren?", width);
    this.p0252 = p0252;
    this.caption = caption;
    this.fields = fields;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page1()));
  }

  public abstract void onSend(Bean bean);

  BigInteger getRijbewijsNummer() {
    NATPRYBMAATR a = (NATPRYBMAATR) p0252.getResponse().getObject();
    return a.getUitgrybtab()
        .getUitgrybgeg()
        .stream()
        .findFirst()
        .map(c -> c.getRybgeg().getRybnr())
        .orElse(BigInteger.valueOf(-1));
  }

  String getNatPersSleutel() {
    NATPRYBMAATR a = (NATPRYBMAATR) p0252.getResponse().getObject();
    return a.getNatpersoontab().getNatpersoongeg().stream().findFirst().map(NATPERSOONGEG::getNatperssl).orElse("");
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD,
      defaultWidth = "200px")
  @Data
  public class Bean implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Rijbewijsnummer")
    private String rijbewijsNr = "";

    @Field(type = FieldType.LABEL,
        caption = "CRB-sleutel")
    private String natPersSleutel = "";

    @Field(customTypeClass = GbaTextField.class,
        caption = "Proces-verbaal")
    @TextField(maxLength = 16)
    private String procesVerbaal = "";

    @Field(customTypeClass = NumberField.class,
        caption = "Verzonden naar",
        required = true,
        width = "50px")
    @TextField(maxLength = 4)
    private String autoriteitscode = "";
  }

  public class Form extends GbaForm<Bean> {

    public Form(Bean bean, String... fields) {
      setColumnWidths("110px", "");
      setOrder(fields);
      setBean(bean);
    }
  }

  public class Page1 extends ButtonPageTemplate {

    final Button buttonSend = new Button("Registeren");
    private Form form       = null;

    Page1() {
      setSpacing(true);
    }

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {
        String standaardPvNr = getApplication().getServices().getParameterService().getSysteemParm(RYB_PV_NR, false);

        Bean bean = new Bean();
        bean.setRijbewijsNr(astr(getRijbewijsNummer()));
        bean.setNatPersSleutel(getNatPersSleutel());

        StringBuilder out = new StringBuilder();
        out.append("<hr/>");
        if (fields != null && asList(fields).contains(PROCESVERBAAL_FIELD) && fil(standaardPvNr)) {
          out.append("Het veld <b>nummer proces-verbaal</b> is gevuld met de standaardwaarde. ");
          bean.setProcesVerbaal(standaardPvNr);
        }

        out.append("Druk op <b>registeren</b> om het bericht te versturen.</b>.");

        String info = out.toString();
        setInfo(caption, info);

        if (isForm()) {
          form = new Form(bean, fields);
          addComponent(form);
        }

        buttonSend.setWidth("100%");
        buttonClose.setWidth("100%");
        buttonClose.setCaption("Annuleren (Esc)");

        getButtonLayout().setWidth("100%");
        addButton(buttonSend);
        addButton(buttonClose);
        getButtonLayout().setComponentAlignment(buttonSend, Alignment.MIDDLE_CENTER);
        getButtonLayout().setComponentAlignment(buttonClose, Alignment.MIDDLE_CENTER);
      }

      super.event(event);
    }

    private boolean isForm() {
      return fields != null && fields.length > 0;
    }

    @Override
    public void handleEvent(Button button, int keyCode) {

      if (button == buttonSend) {
        if (isForm()) {
          form.commit();
        }

        onSend(form.getBean());
        closeWindow();
      }

      if (button == buttonClose) {
        closeWindow();
      }

      super.handleEvent(button, keyCode);
    }
  }
}
