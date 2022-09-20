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

import static nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekBean.F_ADRES;
import static nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekBean.F_BSN;
import static nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekBean.F_GEBOORTEDATUM;
import static nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekBean.F_GESLACHTSNAAM;
import static nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekBean.F_HNR;
import static nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekBean.F_POSTCODE;
import static nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekBean.F_TYPE;
import static nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekBean.F_VOORNAMEN;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.ADRESSEERBAAROBJECTID;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISLETTER;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISNUMMER;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISNUMMERTOEVOEGING;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.POSTCODE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.TYPE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.WEERGAVENAAM;
import static nl.procura.geo.rest.domain.pdok.locationserver.ServiceType.SUGGEST;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.fields.BagSuggestionBox;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerRequest;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class BsZoekForm extends GbaForm<BsZoekBean> {

  private final DossierPersoon dossierPersoon;

  public BsZoekForm(DossierPersoon dossierPersoon) {
    this.dossierPersoon = dossierPersoon;

    setOrder(F_TYPE, F_BSN, F_GESLACHTSNAAM, F_GEBOORTEDATUM, F_VOORNAMEN, F_POSTCODE, F_HNR, F_ADRES);
    setColumnWidths(WIDTH_130, "250px", WIDTH_130, "");
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
    String a5 = b.getGeslachtsnaam();
    String a6 = b.getVoornamen();
    Address a7 = b.getAdres();

    if (emp(a1) && emp(a2) && emp(a3) && emp(a4) && emp(a5) && emp(a6) && a7 == null) {
      throw new ProException(SELECT, WARNING, "Geef een zoekargument in.");

    } else if ((emp(a3) && !emp(a4)) || (!emp(a3) && emp(a4))) {
      throw new ProException(SELECT, WARNING, "De combinatie postcode + hnr. is verplicht");
    }
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);
    getField(F_BSN).focus();
  }

  @Override
  public BsZoekBean getNewBean() {
    BsZoekBean bean = new BsZoekBean();
    bean.setType(dossierPersoon.getDossierPersoonType().getDescr());
    return bean;
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(F_TYPE)) {
      column.setColspan(3);
    }
    if (property.is(F_HNR)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    BagSuggestionBox suggestionBox = getField(BsZoekBean.F_ADRES, BagSuggestionBox.class);
    if (suggestionBox != null) {
      if (Services.getInstance().getGeoService().isGeoServiceActive()) {
        suggestionBox.setGeoRestClient(Services.getInstance().getGeoService().getGeoClient())
            .setRequestListener(value -> new LocationServerRequest()
                .setRequestorName("BRP-suggestionbox")
                .setServiceType(SUGGEST)
                .setOffset(0).setRows(10)
                .search(TYPE, "adres")
                .search(value)
                .filters(WEERGAVENAAM, ADRESSEERBAAROBJECTID,
                    POSTCODE, HUISNUMMER, HUISLETTER, HUISNUMMERTOEVOEGING));
      } else {
        suggestionBox.setVisible(false);
      }
    }
  }
}
