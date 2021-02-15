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

package nl.procura.gba.web.modules.bs.common.pages.zoekpage;

import static nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekBean.*;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class BsZoekForm extends GbaForm<BsZoekBean> {

  private final DossierPersoon dossierPersoon;

  public BsZoekForm(DossierPersoon dossierPersoon) {
    this.dossierPersoon = dossierPersoon;

    setOrder(TYPE, BSN, GEBOORTEDATUM, POSTCODE, HNR);
    setColumnWidths(WIDTH_130, "");
    setBean(getNewBean());
  }

  @Override
  public void commit() throws SourceException, InvalidValueException {

    super.commit();

    BsZoekBean b = getBean();

    String a1 = b.getBsn();
    String a2 = b.getGeboortedatum().getStringValue();
    String a3 = b.getPostcode();
    String a4 = b.getHnr();

    if (emp(a1) && emp(a2) && emp(a3) && emp(a4)) {
      throw new ProException(SELECT, WARNING, "Geef een zoekargument in.");

    } else if ((emp(a3) && !emp(a4)) || (!emp(a3) && emp(a4))) {
      throw new ProException(SELECT, WARNING, "De combinatie postcode + hnr. is verplicht");
    }
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);
    getField(BSN).focus();
  }

  @Override
  public BsZoekBean getNewBean() {
    BsZoekBean bean = new BsZoekBean();
    bean.setType(dossierPersoon.getDossierPersoonType().getDescr());
    return bean;
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(HNR)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }
}
