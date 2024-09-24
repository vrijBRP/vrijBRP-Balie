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

import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.F_AANT_PERS;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.F_BAG_ADDRESS;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.F_HNR;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.F_HNR_A;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.F_HNR_L;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.F_HNR_T;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.F_PC;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.F_SOURCE;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.F_STRAAT;
import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean1.F_WPL;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BAG;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BRP;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.ADRESSEERBAAROBJECTID;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.GEMEENTE_CODE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISLETTER;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISNUMMER;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISNUMMERTOEVOEGING;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.POSTCODE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.TYPE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.WEERGAVENAAM;
import static nl.procura.geo.rest.domain.pdok.locationserver.ServiceType.SUGGEST;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.common.misc.SelectListener;
import nl.procura.gba.web.components.fields.BagSuggestionBox;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.types.OnderzoekAddress;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.adres.BewonerWindow;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoWindow;
import nl.procura.gba.web.modules.bs.registration.page10.adresselectie.selectie.AddressSelectionWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerRequest;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.window.Message;

public class AdresForm1 extends BagAdresForm<AdresBean1> {

  private final OnderzoekAdres                 adres;
  private final SelectListener<DossierPersoon> listener;

  public AdresForm1(OnderzoekAdres adres, SelectListener<DossierPersoon> listener) {
    this.adres = adres;
    this.listener = listener;
  }

  @Override
  public void attach() {
    if (getBean() == null) {
      AdresBean1 bean = new AdresBean1();
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
      }

      bean.setAantalPersonen(adres.getAantalPersonen());
      setBean(bean);
    }

    super.attach();
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
                .filters(WEERGAVENAAM, ADRESSEERBAAROBJECTID,
                    POSTCODE, HUISNUMMER, HUISLETTER, HUISNUMMERTOEVOEGING));
      }
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

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_HNR, F_HNR_L, F_HNR_T, F_HNR_A, F_WPL)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  public void toonBewoners() {
    checkAddress(selectedAddress -> {
      ProcuraInhabitantsAddress brpAddress = getBrpAddress(selectedAddress);
      if (brpAddress != null) {
        getApplication().getParentWindow()
            .addWindow(new BewonerWindow(brpAddress, listener));
      } else {
        new Message(getWindow(),
            "Adres kan niet gevonden worden in de lokale BRP.",
            Message.TYPE_WARNING_MESSAGE);
      }
    });
  }

  public void toonObjectInfo() {
    checkAddress(selectedAddress -> {
      ProcuraInhabitantsAddress brpAddress = getBrpAddress(selectedAddress);
      if (brpAddress != null) {
        getApplication().getParentWindow()
            .addWindow(new ObjectInfoWindow(brpAddress));
      } else {
        new Message(getWindow(),
            "Adres kan niet gevonden worden in de lokale BRP.",
            Message.TYPE_WARNING_MESSAGE);
      }
    });
  }

  public boolean checkAddress() {
    return checkAddress(address -> new Message(getWindow(), "Dit is een bestaand adres.", Message.TYPE_INFO));
  }

  public boolean checkAddress(Consumer<Address> callback) {
    repaint();
    commit();

    AdresBean1 bean = getBean();
    AddressSourceType type = bean.getSource();

    return Optional.ofNullable(type.is(BAG)
        ? getBagAddress(bean.getBagAddress())
        : findBrpAddress(bean))
        .map(address -> {
          callback.accept(address);
          return true;
        }).orElse(false);
  }

  private Address findBrpAddress(AdresBean1 bean) {
    ZoekArgumenten z = new ZoekArgumenten();
    z.setStraatnaam(astr(bean.getStraat()));
    z.setPostcode(astr(bean.getPc().getValue()));
    z.setHuisnummer(bean.getHnr());
    z.setHuisletter(bean.getHnrL());
    z.setHuisnummertoevoeging(bean.getHnrT());
    z.setHuisnummeraanduiding(astr(bean.getHnrA()));
    z.setDatum_einde("-1");

    if (!z.isGevuld()) {
      throw new ProException(ENTRY, INFO, "Geen adres ingegeven.");
    }

    List<Address> vAddress = getServices().getPersonenWsService()
        .getAdres(z, false)
        .getBasisWkWrappers()
        .stream()
        .map(wk1 -> completeLocalAddress(new ProcuraInhabitantsAddress(wk1)))
        .collect(Collectors.toList());

    if (vAddress.isEmpty()) {
      throw new ProException(WARNING, "Het ingegeven adres kan niet worden gevonden in de gemeentelijke BRP");

    } else if (vAddress.size() == 1) {
      return vAddress.get(0);

    } else {
      // Open window
      if (getApplication() != null) {
        getApplication().getMainWindow().addWindow(new AddressSelectionWindow(vAddress, selAddress -> {
          ProcuraInhabitantsAddress mapAddress = (ProcuraInhabitantsAddress) selAddress;
          ZoekArgumenten uz = new ZoekArgumenten();
          uz.setCode_object(astr(mapAddress.getInternalId()));
          uz.setDatum_einde(astr(mapAddress.getEndDate()));
          uz.setVolgcode_einde(astr(mapAddress.getEndDateNumber()));

          getServices().getPersonenWsService()
              .getAdres(uz, false)
              .getBasisWkWrappers()
              .forEach(wk -> completeLocalAddress(new ProcuraInhabitantsAddress(wk)));
        }));
      }
    }
    return null;
  }

  private Address completeLocalAddress(Address address) {
    Field streetField = getField(F_STRAAT);
    if (streetField != null) {
      streetField.setValue(new FieldValue(address.getStreet()));
      getField(F_HNR).setValue(address.getHnr());
      getField(F_HNR_L).setValue(address.getHnrL());
      getField(F_HNR_T).setValue(address.getHnrT());
      getField(F_HNR_A).setValue(new FieldValue(address.getHnrA()));
      getField(F_PC).setValue(new FieldValue(address.getPostalCode()));
      getField(F_WPL).setValue(new FieldValue(address.getResidenceName()));
    }
    return address;
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
      columns.addAll(Arrays.asList(F_STRAAT, F_HNR, F_HNR_L, F_HNR_T, F_HNR_A, F_PC, F_WPL));
    }
    if (adres.getAantalPersonen() != null) {
      columns.add(F_AANT_PERS);
    }
    setOrder(columns.toArray(new String[0]));
  }
}
