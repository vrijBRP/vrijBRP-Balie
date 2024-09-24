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

package nl.procura.gba.web.modules.bs.onderzoek.adreslayout;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_BAG_ADDRESS;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_GEMEENTE;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_HNR;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_HNR_A;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_HNR_L;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_HNR_T;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_PC;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_POSTBUS;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_SOURCE;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_STRAAT;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.F_WPL;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BAG;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BRP;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.ADRESSEERBAAROBJECTID;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISLETTER;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISNUMMER;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISNUMMERTOEVOEGING;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.POSTCODE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.TYPE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.WEERGAVENAAM;
import static nl.procura.geo.rest.domain.pdok.locationserver.ServiceType.SUGGEST;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.fields.BagPopupField;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.types.OnderzoekAddress;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.GemeenteService;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerRequest;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class AdresForm2 extends BagAdresForm<AdresBean2> {

  private final OnderzoekAdres adres;

  public AdresForm2(OnderzoekAdres adres) {
    this.adres = adres;
  }

  @Override
  public void attach() {
    if (getBean() == null) {
      AdresBean2 bean = new AdresBean2();
      bean.setSource(getDefaultAddressSource(adres.getSource()));
      getFields(bean.getSource());

      if (BAG.is(bean.getSource())) {
        bean.setBagAddress(getBagAddress(new OnderzoekAddress(adres)));

      } else {
        bean.setStraat(adres.getAdres());
        bean.setHnr(adres.getHnr());
        bean.setHnrL(adres.getHnrL());
        bean.setHnrT(adres.getHnrT());
        bean.setHnrA(adres.getHnrA());
        bean.setPc(adres.getPc());
        bean.setWoonplaats(adres.getPlaats());

        if (pos(adres.getGemeente().getStringValue())) {
          bean.setGemeente(adres.getGemeente());
        }
      }

      setBean(bean);
    }

    super.attach();
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();

    Field gemeente = getField(F_GEMEENTE);
    if (gemeente != null) {
      gemeente.addListener((ValueChangeListener) event -> {
        setPostAdres((FieldValue) event.getProperty().getValue());
      });
    }

    Field source = getField(F_SOURCE);
    if (source != null) {
      source.addListener(new FieldChangeListener<AddressSourceType>() {

        @Override
        public void onChange(AddressSourceType value) {
          setFields(value);
        }
      });
      BagPopupField bagAddressField = getField(F_BAG_ADDRESS, BagPopupField.class);
      if (bagAddressField != null) {
        bagAddressField.setGeoRestClient(Services.getInstance().getGeoService().getGeoClient())
            .setRequestListener(value -> new LocationServerRequest()
                .setRequestorName("VrijBRP")
                .setServiceType(SUGGEST)
                .setOffset(0).setRows(10)
                .search(TYPE, "adres")
                .search(value)
                .filters(WEERGAVENAAM, ADRESSEERBAAROBJECTID,
                    POSTCODE, HUISNUMMER, HUISLETTER, HUISNUMMERTOEVOEGING));
      }
    }

    setPostAdres(getBean().getGemeente());
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {

    if (property.is(F_HNR, F_HNR_L, F_HNR_T, F_HNR_A)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  public boolean checkAddress(Consumer<Address> callback) {
    repaint();
    commit();

    AdresBean2 bean = getBean();
    AddressSourceType type = bean.getSource();

    if (BAG.is(type)) {
      return ofNullable(getBagAddress(bean.getBagAddress()))
          .map(address -> {
            callback.accept(address);
            return true;
          }).orElse(false);
    }
    return false;
  }

  private void setPostAdres(FieldValue value) {
    Field field = getField(F_POSTBUS);
    if (field != null) {
      field.setVisible(false);

      if (value != null) {
        GemeenteService gemService = Services.getInstance().getGemeenteService();
        Gemeente gemeente = gemService.getGemeente(value);

        field.setReadOnly(false);
        if (fil(gemeente.getAdres())) {
          field.setVisible(true);
          field.setCaption("(Post)adres gemeente " + gemeente.getGemeente());
          field.setValue(gemeente.getAdres() + " " + gemeente.getPc() + " " + gemeente.getPlaats());
        }

        field.setReadOnly(true);
      }
      repaint();
    }
  }

  private void setFields(AddressSourceType type) {
    if (AddressSourceType.UNKNOWN.is(type)) {
      type = getDefaultAddressSource(type);
    }
    if (isGeoServiceActive() && AddressSourceType.BAG == type) {
      showBagFields();

    } else {
      showBrpFields();
    }
  }

  private void showBagFields() {
    getFields(BAG);
    getBean().setSource(AddressSourceType.BAG);
    setBean(getBean());
  }

  private void showBrpFields() {
    getFields(BRP);
    getBean().setSource(AddressSourceType.BRP);
    setBean(getBean());
  }

  private void getFields(AddressSourceType type) {
    List<String> columns = new ArrayList<>();
    if (isGeoServiceActive()) {
      columns.add(F_SOURCE);
      if (type == BAG) {
        columns.add(F_BAG_ADDRESS);
      }
    }
    if (type == BRP) {
      columns.addAll(Arrays.asList(F_STRAAT, F_HNR, F_HNR_L, F_HNR_T, F_HNR_A, F_PC, F_WPL, F_GEMEENTE, F_POSTBUS));
    }
    setOrder(columns.toArray(new String[0]));
  }
}
