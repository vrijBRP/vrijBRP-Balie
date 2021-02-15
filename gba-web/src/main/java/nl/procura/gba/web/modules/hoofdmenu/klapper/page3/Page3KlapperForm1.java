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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page3;

import static nl.procura.gba.web.modules.hoofdmenu.klapper.page3.Page3KlapperBean1.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.modules.beheer.aktes.page3.AkteInvoerTypeContainer;
import nl.procura.gba.web.modules.beheer.aktes.page3.AkteRegistersoortContainer;
import nl.procura.gba.web.services.bs.algemeen.akte.*;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public abstract class Page3KlapperForm1 extends Page3KlapperForm<Page3KlapperBean1> implements ClickListener {

  private final Label          label       = new Label();
  private final Button         checkButton = new Button("Check");
  private final GbaApplication application;
  private Button               resetButton = new Button("Reset");

  public Page3KlapperForm1(DossierAkte dossierAkte, boolean muteerbaar, GbaApplication application) {

    super(dossierAkte, muteerbaar);

    this.application = application;

    setColumnWidths(WIDTH_130, "");
    setCaption("Klapper");
    setOrder(DATUM, SOORT, INVOERTYPE, DEEL, VNR);

    resetButton.setWidth("100px");
    checkButton.setWidth("100px");

    Page3KlapperBean1 bean = new Page3KlapperBean1();
    bean.setInvoerType(dossierAkte.getInvoerType());
    bean.setDatum(new GbaDateFieldValue(dossierAkte.getDatumIngang()));
    bean.setSoort(dossierAkte.getAkteRegistersoort());
    bean.setDeel(getDeel(dossierAkte));
    bean.setVnr(astr(dossierAkte.getVnr()));

    if (!muteerbaar) { // Voorloopnullen toevoegen
      bean.setVnr(pad_left(bean.getVnr(), "0", 4));
    }

    setBean(bean);

    if (isMuteerbaar() && dossierAkte.isDossierAkte()) {
      getField(SOORT).setReadOnly(true);
      getField(INVOERTYPE).setReadOnly(true);
    }
  }

  @Override
  public void afterSetBean() {

    getDatumVeld().addListener((ValueChangeListener) this);

    getInvoerVeld().setDataSource(new AkteInvoerTypeContainer(getInvoerType()));
    getInvoerVeld().addListener((ValueChangeListener) this);

    getSoortVeld().setDataSource(new AkteRegistersoortContainer());
    getSoortVeld().addListener((ValueChangeListener) this);

    getDeelVeld().setDataSource(new DeelContainer(getSoort()));
    getDeelVeld().addListener((ValueChangeListener) this);

    checkButton.addListener((ClickListener) this);
    resetButton.addListener((ClickListener) this);

    setLabelsEnButtons();

    super.afterSetBean();
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (isMuteerbaar()) {
      if (property.is(DEEL)) {
        column.addComponent(label);
      }

      if (property.is(VNR)) {
        column.addComponent(checkButton);
        column.addComponent(resetButton);
      }
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public void buttonClick(ClickEvent event) {

    if (event.getButton() == checkButton) {
      checkVolgNummer();
    }
    if (event.getButton() == resetButton) {
      setNieuwVolgNummer();
    }
  }

  public abstract void checkVolgNummer();

  public Button getButton() {
    return resetButton;
  }

  public void setButton(Button button) {
    this.resetButton = button;
  }

  public long getDatum() {
    return along(new ProcuraDate(astr(getDatumVeld().getValue())).getSystemDate());
  }

  public GbaDateField getDatumVeld() {
    return getField(DATUM, GbaDateField.class);
  }

  public DossierAkteDeel getDeel() {
    return (DossierAkteDeel) getDeelVeld().getValue();
  }

  public GbaNativeSelect getDeelVeld() {
    return getField(DEEL, GbaNativeSelect.class);
  }

  public DossierAkteInvoerType getInvoerType() {
    return (DossierAkteInvoerType) getInvoerVeld().getValue();
  }

  public GbaNativeSelect getInvoerVeld() {
    return getField(INVOERTYPE, GbaNativeSelect.class);
  }

  public long getNummer() {
    return along(getField(VNR).getValue());
  }

  public TextField getNummerVeld() {
    return getField(VNR, TextField.class);
  }

  public DossierAkteRegistersoort getSoort() {
    return (DossierAkteRegistersoort) getSoortVeld().getValue();
  }

  public GbaNativeSelect getSoortVeld() {
    return getField(SOORT, GbaNativeSelect.class);
  }

  public abstract void resetForms();

  @Override
  public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {

    if (event.getProperty() == getInvoerVeld()) {
      resetForms();
    } else if (event.getProperty() == getSoortVeld()) {
      resetForms();
      getDeelVeld().setDataSource(new DeelContainer(getSoort()));
    }

    setLabelsEnButtons();
    setNieuwVolgNummer();

    super.valueChange(event);
  }

  private String getAkteVolgnr() {

    DossierAkteDeel deel = getDeel();
    long datum = getDatum();

    if (deel != null && datum > 0) {
      return astr(getApplication().getServices().getAkteService().getAkteVolgnummer(-1, datum, deel));
    }

    return "";
  }

  private DossierAkteDeel getDeel(DossierAkte akte) {
    return application.getServices().getAkteService().getAkteRegisterDeel(akte);
  }

  private void setLabelsEnButtons() {

    if (isMuteerbaar()) {

      DossierAkteRegistersoort soort = getSoort();
      DossierAkteDeel deel = getDeel();

      if (soort != null && deel != null) {

        long min = deel.getMin().longValue();
        long max = deel.getMax().longValue();

        label.setValue(String.format("(Bereik : %d - %d)", min, max));
      } else {

        label.setValue("");
      }
    }

    resetButton.setVisible(isMuteerbaar());
  }

  private void setNieuwVolgNummer() {

    if (isMuteerbaar()) {
      getNummerVeld().setValue("");
      getNummerVeld().setValue(getAkteVolgnr());
    }
  }

  public class DeelContainer extends ArrayListContainer {

    public DeelContainer(DossierAkteRegistersoort soort) {

      if (soort != null) {

        AkteService aktes = application.getServices().getAkteService();
        List<DossierAkteDeel> registerDelen = aktes.getAkteRegisterDelen(soort.getCode());

        if (registerDelen != null) {
          addItems(registerDelen);
        }
      }
    }
  }
}
