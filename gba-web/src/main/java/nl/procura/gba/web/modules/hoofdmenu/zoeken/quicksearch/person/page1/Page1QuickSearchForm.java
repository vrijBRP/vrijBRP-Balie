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

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1.Page1QuickSearchBean.F_ADRES;
import static nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1.Page1QuickSearchBean.F_ANR;
import static nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1.Page1QuickSearchBean.F_BSN;
import static nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1.Page1QuickSearchBean.F_GEBOORTEDATUM;
import static nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1.Page1QuickSearchBean.F_GESLACHTSNAAM;
import static nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1.Page1QuickSearchBean.F_HNR;
import static nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1.Page1QuickSearchBean.F_POSTCODE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.ADRESSEERBAAROBJECTID;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISLETTER;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISNUMMER;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISNUMMERTOEVOEGING;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.POSTCODE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.TYPE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.WEERGAVENAAM;
import static nl.procura.geo.rest.domain.pdok.locationserver.ServiceType.SUGGEST;

import com.vaadin.ui.Field;

import javax.ws.rs.HEAD;
import nl.procura.gba.web.components.fields.BagPopupField;
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
    BagPopupField bagAddressField = getField(F_ADRES, BagPopupField.class);
    if (bagAddressField != null) {
      if (Services.getInstance().getGeoService().isGeoServiceActive()) {
        bagAddressField.setGeoRestClient(Services.getInstance().getGeoService().getGeoClient())
            .setRequestListener(value -> new LocationServerRequest()
                .setRequestorName("VrijBRP")
                .setServiceType(SUGGEST)
                .setOffset(0).setRows(10)
                .search(TYPE, "adres")
                .search(value)
                .filters(WEERGAVENAAM, ADRESSEERBAAROBJECTID,
                    POSTCODE, HUISNUMMER, HUISLETTER, HUISNUMMERTOEVOEGING));
      } else {
        bagAddressField.setVisible(false);
      }
    }
  }
}
