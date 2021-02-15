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

package nl.procura.gba.web.modules.zaken.rijbewijs.page15;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.math.BigInteger;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.rijbewijs.page15.OngeldigVerklaring.Categorie;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.table.TableLayout;

import lombok.Data;

public abstract class OngeldigVerklaringDialog extends GbaModalWindow {

  public static final String       DATUM_INLEV   = "datum";
  public static final String       REDEN         = "reden";
  public static final String       RIJB_NR_FIELD = "rijbewijsNr";
  private final OngeldigVerklaring ongeldigVerklaring;

  public OngeldigVerklaringDialog(OngeldigVerklaring ongeldigVerklaring) {
    super("Wilt u deze actie uitvoeren?", "600px");
    this.ongeldigVerklaring = ongeldigVerklaring;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page1()));
  }

  public abstract void onSend(OngeldigVerklaring ongeldigVerklaring);

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  @Data
  public class Bean implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Rijbewijsnummer")
    private BigInteger rijbewijsNr;

    @Field(type = FieldType.LABEL,
        caption = "Datum inlevering rijbewijs")
    private String datum = "";

    @Field(type = FieldType.LABEL,
        caption = "Reden ongeldigverklaring")

    private RedenOngeldigVerklaring reden;
  }

  public class Form extends GbaForm<Bean> {

    public Form(Bean bean, String... fields) {
      setCaption("Registeren ongeldigverklaren rijbewijs");
      setColumnWidths("160px", "");
      setOrder(fields);
      setBean(bean);
    }
  }

  public class Page1 extends ButtonPageTemplate {

    protected final Button buttonJa = new Button("Ja");
    private Form           form     = null;

    public Page1() {
      setSpacing(true);
    }

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {

        DateFieldValue datumInlevering = ongeldigVerklaring.getDatumInlevering();

        Bean bean = new Bean();
        bean.setRijbewijsNr(ongeldigVerklaring.getRijbewijsNummer());
        bean.setDatum(datumInlevering.getLongValue() > 0 ? datumInlevering.toString() : "Niet van toepassing");
        bean.setReden(ongeldigVerklaring.getRedenOngeldig());

        form = new Form(bean, RIJB_NR_FIELD, DATUM_INLEV, REDEN);
        addComponent(form);
        addComponent(new Fieldset("Categorieën", new CategoriesLayout()));

        buttonJa.setWidth("100%");
        buttonClose.setWidth("100%");
        buttonClose.setCaption("Nee (Esc)");

        getButtonLayout().setWidth("100%");
        addButton(buttonJa);
        addButton(buttonClose);
        getButtonLayout().setComponentAlignment(buttonJa, Alignment.MIDDLE_CENTER);
        getButtonLayout().setComponentAlignment(buttonClose, Alignment.MIDDLE_CENTER);
      }

      super.event(event);
    }

    @Override
    public void handleEvent(Button button, int keyCode) {

      if (button == buttonJa) {
        form.commit();
        onSend(ongeldigVerklaring);
        closeWindow();
      }

      if (button == buttonClose) {
        closeWindow();
      }

      super.handleEvent(button, keyCode);
    }
  }

  public class CategoriesLayout extends TableLayout {

    public CategoriesLayout() {
      setColumnWidths("60px", "");

      for (Categorie cat : ongeldigVerklaring.getCategorieen()) {
        Column label = addColumn(ColumnType.LABEL);
        label.addText(cat.getCode());
        Column data = addColumn(ColumnType.DATA);
        data.addText(cat.getDatum().getLongValue() > 0 ? "Ongeldig per " + cat.getDatum() : "Niet wijzigen");
      }
    }
  }
}
