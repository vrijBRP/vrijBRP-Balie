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

package nl.procura.gba.web.modules.hoofdmenu.gv.page1;

import static nl.procura.gba.web.modules.hoofdmenu.gv.page1.Page1GvBean1.*;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.gv.containers.AfnemerContainer;
import nl.procura.gba.web.modules.hoofdmenu.gv.containers.ToekenningContainer;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public abstract class Page1GvForm1 extends GbaForm<Page1GvBean1> {

  private final GbaApplication application;

  public Page1GvForm1(GbaApplication application) {

    this.application = application;

    setCaption("Verzoek");
    setReadThrough(true);
    setColumnWidths(WIDTH_130, "");
    setOrder(DATUM_ONTVANGST, AFNEMER, GRONDSLAG, TOEKENNING, TOEKENNING_MOTIVERING);
    setBean(new Page1GvBean1());
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(AFNEMER)) {
      if (getApplication() != null) {
        Button button = new Button("+", (ClickListener) event -> onButtonAfnemer());
        button.addStyleName("plus-button");
        column.addComponent(button);
      }

      if (getApplication() != null) {
        String url = getApplication().getParmValue(ParameterConstant.GV_KB_URL);
        Component kb = new KennisbankButton(url);
        column.addComponent(kb);
      }
    }

    super.afterSetColumn(column, field, property);
  }

  public void enableGrondslag(boolean enable) {
    getGrondslagVeld().setVisible(enable);
    getToekenningVeld().setVisible(enable);
  }

  public void enableMotiveringToekenning(boolean enable) {

    if (enable) {
      if (!getMotiveringToekenningVeld().isVisible()) {
        getMotiveringToekenningVeld().setVisible(true);
        getMotiveringToekenningVeld().setValue("");
      }
    } else {
      getMotiveringToekenningVeld().setValue("");
      getMotiveringToekenningVeld().setVisible(false);
    }
  }

  public GbaComboBox getAfnemerVeld() {
    return (GbaComboBox) getField(AFNEMER);
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);

    getAfnemerVeld().setContainerDataSource(new AfnemerContainer(application));
    getAfnemerVeld().addListener((ValueChangeListener) this);
    getToekenningVeld().addListener((ValueChangeListener) this);
  }

  public TextField getGrondslagVeld() {
    return (TextField) getField(GRONDSLAG);
  }

  public TextArea getMotiveringToekenningVeld() {
    return (TextArea) getField(TOEKENNING_MOTIVERING);
  }

  @Override
  public Page1GvBean1 getNewBean() {
    return new Page1GvBean1();
  }

  public ProNativeSelect getToekenningVeld() {
    return (ProNativeSelect) getField(TOEKENNING);
  }

  public void setGrondslag(DocumentAfnemer da) {

    if (da == null || KoppelEnumeratieType.ONBEKEND.is(da.getGrondslagType())) {
      getBean().setGrondslag(null);
    } else {
      getBean().setGrondslag(da.getGrondslagType());
    }
  }

  public void setToekennen(boolean vb, DocumentAfnemer da) {

    ToekenningContainer toekennenContainer = new ToekenningContainer(vb, da);
    KoppelEnumeratieType type = null;

    if (toekennenContainer.getItemIds().size() > 0) {
      type = (KoppelEnumeratieType) toekennenContainer.getIdByIndex(0);
    }

    getToekenningVeld().setValue(type);
    getBean().setToekenning(type);

    getToekenningVeld().setContainerDataSource(toekennenContainer);
  }

  public void updateAfnemers() {
    DocumentAfnemer af = (DocumentAfnemer) getAfnemerVeld().getValue();
    getAfnemerVeld().setContainerDataSource(new AfnemerContainer(application));
    getAfnemerVeld().setValue(getAfnemer(af));
  }

  @Override
  public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {

    DocumentAfnemer da = (DocumentAfnemer) getAfnemerVeld().getValue();
    KoppelEnumeratieType gs = getBean().getGrondslag();
    KoppelEnumeratieType tk = (KoppelEnumeratieType) getToekenningVeld().getValue();

    if (event.getProperty() == getAfnemerVeld()) {
      da = (DocumentAfnemer) event.getProperty().getValue();
      wijzigAfnemer(da);
    } else if (event.getProperty() == getToekenningVeld()) {

      tk = (KoppelEnumeratieType) event.getProperty().getValue();
      wijzigToekenning(gs, tk);
    }

    repaint();

    super.valueChange(event);
  }

  public abstract void wijzigAfnemer(DocumentAfnemer da);

  public abstract void wijzigToekenning(KoppelEnumeratieType gs, KoppelEnumeratieType tk);

  protected abstract void onButtonAfnemer();

  private DocumentAfnemer getAfnemer(DocumentAfnemer af) {
    if (af != null) {
      for (DocumentAfnemer afnemer : application.getServices().getDocumentService().getAfnemers()) {
        if (afnemer.getCDocumentAfn().equals(af.getCDocumentAfn())) {
          return afnemer;
        }
      }
    }

    return af;
  }
}
