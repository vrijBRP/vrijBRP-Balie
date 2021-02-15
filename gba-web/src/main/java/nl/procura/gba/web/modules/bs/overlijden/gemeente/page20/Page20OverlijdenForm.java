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

package nl.procura.gba.web.modules.bs.overlijden.gemeente.page20;

import static nl.procura.gba.web.modules.bs.overlijden.gemeente.page20.Page20OverlijdenBean.DATUM_OVERLIJDEN;
import static nl.procura.gba.web.modules.bs.overlijden.gemeente.page20.Page20OverlijdenBean.TIJD_OVERLIJDEN;

import java.util.Calendar;
import java.util.Date;

import com.vaadin.ui.Field;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.GbaDatumUtils;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

public class Page20OverlijdenForm extends GbaForm<Page20OverlijdenBean> {

  private final DossierOverlijdenGemeente zaakDossier;

  public Page20OverlijdenForm(DossierOverlijdenGemeente zaakDossier) {
    this.zaakDossier = zaakDossier;
    setColumnWidths("170px", "");
    setCaptionAndOrder();
    update();
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);

    addListener(getField(DATUM_OVERLIJDEN));
    addListener(getField(TIJD_OVERLIJDEN));
  }

  @Override
  public Page20OverlijdenBean getNewBean() {
    return new Page20OverlijdenBean();
  }

  public Calendar getOverlijdenTijdstip() {
    Date date = (Date) getField(DATUM_OVERLIJDEN).getValue();
    TimeFieldValue time = (TimeFieldValue) getField(TIJD_OVERLIJDEN).getValue();
    return GbaDatumUtils.dateTimeFieldtoCalendar(date, time, "000001");
  }

  public void setCaptionAndOrder() {
  }

  public void update() {

    Page20OverlijdenBean bean = new Page20OverlijdenBean();

    // Overlijden
    bean.setPlaatsOverlijden(zaakDossier.getPlaatsOverlijden());
    bean.setDatumOverlijden(zaakDossier.getDatumOverlijden().getDate());

    DateTime tijdOverlijden = zaakDossier.getTijdOverlijden();

    if (tijdOverlijden.getLongTime() >= 0) {
      bean.setTijdOverlijden(new TimeFieldValue(tijdOverlijden.getFormatTime()));
    }

    if (OntvangenDocument.ONBEKEND != zaakDossier.getOntvangenDocument()) {
      bean.setOntvangenDocument(zaakDossier.getOntvangenDocument());
    }

    setBean(bean);
  }

  // Override please
  protected void onDatumWijziging() {
  }

  private void addListener(Field field) {

    if (field != null) {
      field.addListener((ValueChangeListener) event -> onDatumWijziging());
    }
  }
}
