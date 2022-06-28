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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1;

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1.Page1QuickSearchBean.*;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.*;
import static nl.procura.geo.rest.domain.pdok.locationserver.ServiceType.SUGGEST;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.fields.BagSuggestionBox;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerRequest;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page1QuickSearchForm extends GbaForm<Page1QuickSearchBean> {

  public Page1QuickSearchForm() {
    setOrder(F_BSN, F_GESLACHTSNAAM, F_ANR, F_GEBOORTEDATUM, F_POSTCODE, F_HNR, F_ADRES);
    setColumnWidths(WIDTH_130, "200px", "110px", "");
    setBean(new Page1QuickSearchBean());
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_ADRES)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    BagSuggestionBox suggestionBox = getField(F_ADRES, BagSuggestionBox.class);
    if (suggestionBox != null) {
      if (Services.getInstance().getGeoService().isGeoServiceActive()) {
        suggestionBox.setGeoRestClient(Services.getInstance().getGeoService().getGeoClient())
            .setRequestListener(value -> new LocationServerRequest()
                .setRequestorName("BRP-suggestionbox")
                .setServiceType(SUGGEST)
                .setOffset(0).setRows(10)
                .search(TYPE, "adres")
                .search(value)
                .filters(WEERGAVENAAM, ADRESSEERBAAR_OBJECT_ID,
                    POSTCODE, HUISNUMMER, HUISLETTER, HUISNUMMERTOEVOEGING));
      } else {
        suggestionBox.setVisible(false);
      }
    }
  }
}
