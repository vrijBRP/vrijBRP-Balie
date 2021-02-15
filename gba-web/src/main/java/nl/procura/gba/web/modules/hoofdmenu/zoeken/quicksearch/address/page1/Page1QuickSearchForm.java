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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.page1;

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.page1.Page1QuickSearchBean.*;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEO_SEARCH_DEFAULT;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BAG;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BRP;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.*;
import static nl.procura.geo.rest.domain.pdok.locationserver.ServiceType.SUGGEST;
import static nl.procura.standard.Globalfunctions.isTru;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.fields.BagSuggestionBox;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerRequest;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page1QuickSearchForm extends GbaForm<Page1QuickSearchBean> {

  private Services services;

  Page1QuickSearchForm(Services services) {
    this.services = services;
    setColumnWidths(WIDTH_130, "");
    setOrder(F_STREET, F_HOUSE_NUMBER, F_HOUSE_NUMBER_L, F_HOUSE_NUMBER_T,
        F_HOUSE_NUMBER_A, F_POSTAL_CODE, F_RESIDENCE);

    Page1QuickSearchBean bean = new Page1QuickSearchBean();
    setBean(bean);
    setFields(BAG);
  }

  @Override
  public void reset() {
    super.reset();
    setFields(getDefaultAddressSource());
  }

  @Override
  public void setColumn(final TableLayout.Column column, final Field field, final Property property) {
    if (property.is(F_HOUSE_NUMBER, F_HOUSE_NUMBER_L, F_HOUSE_NUMBER_T, F_HOUSE_NUMBER_A, F_RESIDENCE)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    Field source = getField(F_SOURCE);
    if (source != null) {
      source.addListener(new FieldChangeListener<AddressSourceType>() {

        @Override
        public void onChange(AddressSourceType value) {
          setFields(value);
        }
      });
      BagSuggestionBox suggestionBox = getField(F_BAG_ADDRESS, BagSuggestionBox.class);
      if (suggestionBox != null) {
        String gemCode = Services.getInstance().getGebruiker().getGemeenteCode();
        suggestionBox.setGeoRestClient(Services.getInstance().getGeoService().getGeoClient())
            .setRequestListener(value -> new LocationServerRequest()
                .setRequestorName("BRP-suggestionbox")
                .setServiceType(SUGGEST)
                .setOffset(0).setRows(10)
                .search(TYPE, "adres")
                .search(GEMEENTE_CODE, gemCode)
                .search(value)
                .filters(WEERGAVENAAM, ADRESSEERBAAR_OBJECT_ID,
                    POSTCODE, HUISNUMMER, HUISLETTER, HUISNUMMERTOEVOEGING));
      }
    }
  }

  private void setFields(AddressSourceType type) {
    if (AddressSourceType.UNKNOWN.is(type)) {
      type = getDefaultAddressSource();
    }
    if (isGeoServiceActive() && AddressSourceType.BAG == type) {
      showBagFields();
    } else {
      showBrpFields();
    }
  }

  private void showBagFields() {
    setOrder(F_SOURCE, F_BAG_ADDRESS);
    getBean().setSource(AddressSourceType.BAG);
    setBean(getBean());
  }

  private void showBrpFields() {
    if (isGeoServiceActive()) {
      setOrder(F_SOURCE, F_STREET, F_HOUSE_NUMBER, F_HOUSE_NUMBER_L, F_HOUSE_NUMBER_T,
          F_HOUSE_NUMBER_A, F_POSTAL_CODE, F_RESIDENCE);
    } else {
      setOrder(F_STREET, F_HOUSE_NUMBER, F_HOUSE_NUMBER_L, F_HOUSE_NUMBER_T,
          F_HOUSE_NUMBER_A, F_POSTAL_CODE, F_RESIDENCE);
    }

    getBean().setSource(AddressSourceType.BRP);
    setBean(getBean());
  }

  private boolean isGeoServiceActive() {
    return services.getGeoService().isGeoServiceActive();
  }

  private AddressSourceType getDefaultAddressSource() {
    boolean isDefaultBag = isTru(services.getParameterService().getParm(GEO_SEARCH_DEFAULT));
    return isGeoServiceActive() && isDefaultBag ? BAG : BRP;
  }
}
