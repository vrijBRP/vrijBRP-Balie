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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.gwt.ace.AceMode;
import org.vaadin.aceeditor.gwt.ace.AceTheme;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.dao.SelDao;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.zaken.selectie.Selectie;
import nl.procura.gba.web.services.zaken.selectie.SelectieColumn;
import nl.procura.standard.Globalfunctions;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.Data;

public class Page4SelectiesZaken3 extends NormalPageTemplate {

  protected final Button buttonCheck = new Button("Controleer");

  private final Selectie     selectie;
  private Form               form;
  private final AceEditor    editor = new AceEditor();
  private SelectieInfoLayout info;

  public static final String ID  = "id";
  public static final String SEL = "sel";
  public static final String OMS = "oms";

  public Page4SelectiesZaken3(Selectie selectie) {
    super("Zakenregister - selecties");
    this.selectie = selectie;
    setSizeFull();
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonPrev.setWidth("120px");
      buttonCheck.setWidth("120px");
      buttonSave.setWidth("120px");

      addButton(buttonPrev);
      addButton(buttonCheck);
      addButton(buttonSave);

      Bean bean = new Bean();
      bean.setSel(selectie.getSelectie());
      bean.setOms(selectie.getOmschrijving());
      bean.setId(selectie.getId());

      form = new Form();
      form.setBean(bean);
      addComponent(form);

      editor.setStyleName("config-xml");
      editor.setSizeFull();
      editor.setHeight("400px");
      editor.setMode(AceMode.pgsql);
      editor.setTheme(AceTheme.crimson_editor);
      editor.setValue(selectie.getStatement());

      info = new SelectieInfoLayout(null);
      addComponent(info);

      OptieLayout ol = new OptieLayout();
      ol.setSizeFull();
      ol.setWidth("920px");
      ol.getRight().setWidth("200px");
      ol.getRight().setCaption("Informatie");
      ol.getLeft().addComponent(new Fieldset("Database selectie"));
      ol.getLeft().addExpandComponent(editor);
      ol.getRight().addComponent(new InfoLayout(getKolommenMessage()));

      editor.setWidth("705px");
      editor.setHeight(getApplication().isDynamicHeight() ? "99.5%" : "400px");

      addComponent(ol);
      setExpandRatio(ol, 1.0F);
    }

    super.event(event);
  }

  private String getKolommenMessage() {
    StringBuilder message = new StringBuilder("Dit zijn de toegestane kolommen:");
    message.append("<ul>");
    for (SelectieColumn sc : SelectieColumn.values()) {
      message.append("<li>");
      message.append(sc.getName());
      message.append("</li>");
    }
    message.append("</ul>");
    return message.toString();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonCheck) {
      onCheck();
    }
    super.handleEvent(button, keyCode);
  }

  private void onCheck() {
    try {
      SelDao.Result result = SelDao.getFromStatement(Globalfunctions.astr(editor.getValue()));
      for (String column : result.getColumns()) {
        SelectieColumn selectieColumn = SelectieColumn.getByName(column);
        if (selectieColumn == null) {
          throw new ProException(WARNING, "Kolom {0} is verkeerd. Mogelijke kolommen zijn {1}.", column,
              StringUtils.join(SelectieColumn.values(), ", ").toLowerCase());
        }
      }
      updateInfoLayout(null);
      successMessage("De selectie op de database is correct");

    } catch (Exception e) {
      updateInfoLayout(e);
    }
  }

  private void updateInfoLayout(Exception e) {
    SelectieInfoLayout newInfo = new SelectieInfoLayout(e);
    replaceComponent(info, newInfo);
    info = newInfo;
  }

  @Override
  public void onSave() {

    form.commit();

    selectie.setSelectie(form.getBean().getSel());
    selectie.setOmschrijving(form.getBean().getOms());
    selectie.setStatement(Globalfunctions.astr(editor.getValue()));
    selectie.setId(form.getBean().getId());

    getServices().getSelectieService().save(selectie);
    successMessage("De gegevens zijn opgeslagen");

    super.onSave();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Bean implements Serializable {

    @Field(customTypeClass = GbaTextField.class,
        caption = "Id",
        required = true,
        width = "150px")
    @TextField(nullRepresentation = "",
        maxLength = 100)
    private String id = "";

    @Field(customTypeClass = GbaTextField.class,
        caption = "Selectie",
        required = true,
        width = "600px")
    @TextField(nullRepresentation = "",
        maxLength = 255)
    private String sel = "";

    @Field(customTypeClass = ProTextArea.class,
        caption = "Omschrijving",
        width = "600px")
    @TextArea(rows = 3,
        nullRepresentation = "")
    private String oms = "";
  }

  public class Form extends GbaForm<Bean> {

    public Form() {
      setCaption("Selectie");
      setOrder(SEL, OMS, ID);
      setColumnWidths("90px", "");
    }
  }
}
