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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.Page4ZakenBean.*;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenParameters;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.Anders;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.Vandaag;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.ZaakPeriodes;
import nl.procura.gba.web.services.applicatie.MemoryStorageService;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page4ZakenFormSimpel extends GbaForm<Page4ZakenBean> {

  Page4ZakenFormSimpel() {
    setCaption("Algemeen");
    setOrder(DATUM_INVOER, INVOER_VAN, INVOER_TM, DATUM_INGANG, INGANG_VAN, INGANG_TM, AANVRAAGNR, NR);
    setColumnWidths(WIDTH_130, "");
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(NR)) {
      Button buttonPersoon = new Button("Zoek");
      buttonPersoon.addListener((ClickListener) event -> onZoekPersoon());
      column.addComponent(buttonPersoon);
    }
  }

  @Override
  public void reset() {
    setColumnWidths(WIDTH_130, "");
    super.reset();
  }

  @Override
  public Page4ZakenBean getNewBean() {

    Page4ZakenBean bean = new Page4ZakenBean();
    MemoryStorageService memoryService = getApplication().getServices().getMemoryService();

    int periode = aval(memoryService.getAndRemoveObject(ZakenParameters.PERIODE_INVOER));
    String zaakId = astr(memoryService.getAndRemoveObject(ZakenParameters.ZAAK));
    String bsn = astr(memoryService.getAndRemoveObject(ZakenParameters.BSN));

    bean.setDatumInvoer(null);
    if (periode >= 0 && periode <= new ZaakPeriodes().getHistorischePeriodes().size()) {
      bean.setDatumInvoer(new ZaakPeriodes().getHistorischePeriodes().get(periode));
    } else if (emp(zaakId)) {
      bean.setDatumInvoer(new Vandaag());
    }

    if (fil(zaakId)) {
      bean.setAanvraagNr(zaakId);
    }

    if (fil(bsn)) {
      bean.setNr(bsn);
    }

    return bean;
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);
    for (final Field field : getFields(DATUM_INVOER, DATUM_INGANG, AANVRAAGNR, NR)) {
      if (field != null) {
        field.addListener((ValueChangeListener) event -> {
          if (getField(DATUM_INVOER) == field) {
            onChangePeriode(event.getProperty().getValue(), getField(DATUM_INGANG).getValue());
            repaint();
          }
          if (getField(DATUM_INGANG) == field) {
            onChangePeriode(getField(DATUM_INVOER).getValue(), event.getProperty().getValue());
            repaint();
          }

          onReload();
        });
      }
    }
  }

  public void onReload() {
    // Overriden please!!!
  }

  public void onZoekPersoon() {
    // Overriden please!!!
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (isPeriodeInvoerAnders() && property.is(AANVRAAGNR, NR)) {
      column.setColspan(5);
    } else if (isPeriodeInvoerAnders() && !isPeriodeIngangAnders() && property.is(AANVRAAGNR, NR, DATUM_INGANG)) {
      column.setColspan(5);
    } else if (isPeriodeIngangAnders() && !isPeriodeInvoerAnders() && property.is(AANVRAAGNR, NR, DATUM_INVOER)) {
      column.setColspan(5);
    }
  }

  private boolean isPeriodeInvoerAnders() {
    return getField(DATUM_INVOER) != null && new Anders().equals(getField(DATUM_INVOER).getValue());
  }

  private boolean isPeriodeIngangAnders() {
    return getField(DATUM_INGANG) != null && new Anders().equals(getField(DATUM_INGANG).getValue());
  }

  private void onChangePeriode(Object periodeInvoer, Object periodeIngang) {

    boolean invoerAnders = new Anders().equals(periodeInvoer);
    boolean ingangAnders = new Anders().equals(periodeIngang);

    if (invoerAnders) {

      getField(INVOER_VAN).setVisible(true);
      getField(INVOER_TM).setVisible(true);

    } else {

      getField(INVOER_VAN).setVisible(false);
      getField(INVOER_TM).setVisible(false);
    }

    if (ingangAnders) {

      getField(INGANG_VAN).setVisible(true);
      getField(INGANG_TM).setVisible(true);

    } else {

      getField(INGANG_VAN).setVisible(false);
      getField(INGANG_TM).setVisible(false);
    }

    if (invoerAnders || ingangAnders) {
      setColumnWidths(WIDTH_130, "250px", "30px", "100px", "30px", "");
    } else {
      setColumnWidths(WIDTH_130, "");
    }
  }

  private void onChangeIngangPeriode(Object periode) {

    if (new Anders().equals(periode)) {

      getField(INGANG_VAN).setVisible(true);
      getField(INGANG_TM).setVisible(true);

      setColumnWidths(WIDTH_130, "250px", "30px", "100px", "30px", "");
    } else {

      getField(INGANG_VAN).setVisible(false);
      getField(INGANG_TM).setVisible(false);

      setColumnWidths(WIDTH_130, "");
    }
  }
}
