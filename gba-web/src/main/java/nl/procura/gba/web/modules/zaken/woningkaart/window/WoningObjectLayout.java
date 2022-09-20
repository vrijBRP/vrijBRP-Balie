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

package nl.procura.gba.web.modules.zaken.woningkaart.window;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.ui.Component;

import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressRequest;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabChangeListener;

public class WoningObjectLayout extends VLayout {

  private final Address  address;
  private GbaApplication application;
  private Address        brpAddress;
  private Address        bagAddress;

  public WoningObjectLayout(GbaApplication application, Address address) {
    this.application = application;
    this.address = address;
    if (address.getSourceType() == AddressSourceType.BRP) {
      this.brpAddress = address;
    }
    if (address.getSourceType() == AddressSourceType.BAG) {
      this.bagAddress = address;
    }

    GbaTabsheet tabsheet = new GbaTabsheet();
    tabsheet.addStyleName("vbo-zoektab");
    tabsheet.setExtraTopMargin();
    tabsheet.addTab("Woningkaart gemeentelijke BRP", (LazyTabChangeListener) () -> showBrpAddress());
    if (application.getServices().getGeoService().isGeoServiceActive()) {
      tabsheet.addTab("Landelijke BAG", (LazyTabChangeListener) () -> showBagAddress());
    }
    addExpandComponent(tabsheet);
  }

  private Component showBrpAddress() {
    if (brpAddress == null) {
      ZoekArgumenten z = new ZoekArgumenten();
      z.setPostcode(address.getPostalCode());
      z.setHuisnummer(address.getHnr());
      z.setHuisletter(address.getHnrL());
      z.setHuisnummertoevoeging(address.getHnrT());
      z.setDatum_einde("-1");

      List<Address> addresses = application.getServices().getPersonenWsService()
          .getAdres(z, false)
          .getBasisWkWrappers()
          .stream()
          .map(ProcuraInhabitantsAddress::new)
          .collect(Collectors.toList());

      if (addresses.size() == 1) {
        this.brpAddress = addresses.get(0);
      }
    }
    return setLayout(brpAddress, " is niet gevonden in de gemeentelijke BRP");
  }

  private Component showBagAddress() {
    if (bagAddress == null) {
      AddressRequest request = new AddressRequest();
      request.setPc(address.getPostalCode());
      request.setHnr(address.getHnr());
      request.setHnrL(address.getHnrL());
      request.setHnrT(address.getHnrT());

      List<Address> addresses = application.getServices()
          .getGeoService().search(request);

      if (addresses.size() == 1) {
        this.bagAddress = addresses.get(0);
      }
      if (addresses.size() > 1) {
        return setLayout(bagAddress, " is meerdere keren gevonden in de landelijke BAG");
      }
    }

    return setLayout(bagAddress, " is niet gevonden in de landelijke BAG");
  }

  private Component setLayout(Address selectedAddress, String label) {
    if (getWindow() != null) {
      getWindow().setHeight(null);
      getWindow().center();
    }
    if (selectedAddress != null) {
      return new WoningObjectForm(selectedAddress);
    } else {
      return new InfoLayout(address.getLabel() + label);
    }
  }
}
