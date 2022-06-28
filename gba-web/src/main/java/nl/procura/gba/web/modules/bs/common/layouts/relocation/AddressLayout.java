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

package nl.procura.gba.web.modules.bs.common.layouts.relocation;

import static nl.procura.gba.web.modules.bs.common.layouts.relocation.MunicipalAddressBean.*;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BAG;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BRP;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import nl.procura.vaadin.component.field.ProComboBox;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Validator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;

import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.registration.page10.adresselectie.adres.BewonerWindow;
import nl.procura.gba.web.modules.bs.registration.page10.adresselectie.selectie.AddressSelectionWindow;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressRequest;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class AddressLayout extends VLayout {

  private final OptieLayout          optionLayout;
  private final MunicipalAddressForm addressForm;

  private final int INT_ZERO = 0;
  private final int INT_ONE  = 1;

  private final RelocationAddress address;
  private AddressInfoLayout       localAddressInfoLayout;
  private Services                services;
  private String                  confirmedAddress = "";

  public AddressLayout(RelocationAddress address, Services services) {
    this.address = address;
    this.services = services;
    this.address.setBagAddress(getBagAddress(address.getAon()));
    this.addressForm = new MunicipalAddressForm(address, services);
    this.optionLayout = new OptieLayout();

    addComponent(getAdresLayout(addressForm));
  }

  public boolean isSaved() {
    return checkAddress(true, true, vh -> {
      Address address = vh.getAddress();
      this.address.setAon(address.getAonId());
      this.address.setStreet(new FieldValue(address.getStreet()));
      this.address.setHouseNumber(address.getHnr());
      this.address.setHouseNumberL(address.getHnrL());
      this.address.setHouseNumberT(address.getHnrT());
      this.address.setHouseNumberA(new FieldValue(address.getHnrA()));
      this.address.setPostalCode(new FieldValue(address.getPostalCode()));
      this.address.setResidence(new FieldValue(address.getResidenceName()));
      this.address.setMunicipality(new FieldValue(address.getMunicipalityCode(), address.getMunicipalityName()));

      MunicipalAddressBean addressBean = addressForm.getBean();
      this.address.setAddressFunction(addressBean.getAddressFunction());
      this.address.setSource(addressBean.getSource());
      this.address.setNoOfPeople(addressBean.getNoOfPeople());
      this.address.setConsentProvider(addressBean.getConsentProvider());
    });
  }

  private Layout getAdresLayout(MunicipalAddressForm addressForm) {
    Button resetButton = new Button("Reset adres");
    Button checkAddressButton = new Button("Controleer adres");
    Button showResidentsButton = new Button("Toon bewoners");

    optionLayout.getLeft().addComponent(addressForm);
    optionLayout.getLeft().addComponent(new AddressInfoLayout());
    optionLayout.getRight().addButton(checkAddressButton, event -> checkAddress());
    optionLayout.getRight().addButton(showResidentsButton, event -> showResidents());
    optionLayout.getRight().addButton(new Button("Object informatie"), event -> showObjectInfo());
    optionLayout.getRight().addButton(resetButton, event -> resetAddress());
    optionLayout.getRight().setWidth("200px");

    return new Fieldset("Adresgegevens binnen de gemeente", optionLayout);
  }

  private void checkAddress() {
    checkAddress(false, true, vh -> {
      new Message(getWindow(), "Dit is een bestaand adres.", Message.TYPE_INFO);
    });
  }

  private void resetAddress() {
    addressForm.reset();
  }

  private boolean checkAddress(boolean definitiveAddress, boolean checkSuitability, Consumer<VerhuisAdres> callback) {
    VerhuisAdres relocationAddress = getAddresses(definitiveAddress);
    removeAddressLayout();
    if (relocationAddress != null) {
      VerhuisAdres brpAddress = getBrpAddress(relocationAddress);
      if (brpAddress != null) {
        completeLocalAddress(brpAddress.getAddress());
        setAddressInfoLayout(brpAddress);
      } else {
        setAddressInfoLayout(relocationAddress);
      }
      return checkSuitability(checkSuitability,
          relocationAddress,
          callback);
    }
    return false;
  }

  private void showResidents() {
    checkAddress(false, false, vh -> {
      VerhuisAdres brpAddress = getBrpAddress(vh);
      getApplication().getMainWindow().addWindow(
          new BewonerWindow(brpAddress != null ? brpAddress : vh,
              addressForm.getBean().getConsentProvider(),
              addressForm::setConsentProvider));
    });
  }

  private void showObjectInfo() {
    checkAddress(false, false, vh -> {
      getApplication().getMainWindow().addWindow(new WoningObjectWindow(vh.getAddress()));
    });
  }

  private void removeAddressLayout() {
    localAddressInfoLayout = new AddressInfoLayout().removeAddress();
    VaadinUtils.addComponent(optionLayout.getLeft(), localAddressInfoLayout);
  }

  private void setAddressInfoLayout(VerhuisAdres address) {
    String extraMessage = "<hr/>Druk op Toon bewoners voor meer informatie over de bewoners.";
    localAddressInfoLayout = new AddressInfoLayout().set(address, extraMessage);
    VaadinUtils.addComponent(optionLayout.getLeft(), localAddressInfoLayout);
  }

  private VerhuisAdres getAddresses(boolean definitiveAddress) {
    Field aantalVeld = addressForm.getField(MunicipalAddressBean.F_NO_OF_PEOPLE);
    for (Validator v : aantalVeld.getValidators()) {
      if (v instanceof NumberOfPeopleValidator) {
        ((NumberOfPeopleValidator) v).setFieldIsVisible(aantalVeld.isVisible());
        ((NumberOfPeopleValidator) v).setIgnoreThisField(!definitiveAddress);
      }
    }

    addressForm.repaint();
    addressForm.commit();

    MunicipalAddressBean bean = addressForm.getBean();
    AddressSourceType type = bean.getSource();

    if (BAG == type) {
      return findBagAddress(bean);
    } else {
      return findBrpAddress(bean);
    }
  }

  /**
   * Get a specific address in the BRP
   */
  private VerhuisAdres getBrpAddress(VerhuisAdres address) {
    if (address.getAddress().getSourceType().is(BRP)) {
      return address;
    }

    if (emp(address.getAddress().getPostalCode())) {
      throw new ProException(ENTRY, WARNING, "Dit adres heeft geen postcode. Dit is nodig bij het zoeken.");
    }

    if (emp(address.getAddress().getHnr())) {
      throw new ProException(ENTRY, WARNING, "Dit adres heeft geen huisnummer. Dit is nodig bij het zoeken.");
    }

    ZoekArgumenten z = new ZoekArgumenten();
    z.setPostcode(address.getAddress().getPostalCode());
    z.setHuisnummer(address.getAddress().getHnr());
    z.setHuisletter(address.getAddress().getHnrL());
    z.setHuisnummertoevoeging(address.getAddress().getHnrT());
    z.setHuisnummeraanduiding(address.getAddress().getHnrA());
    z.setDatum_einde("-1");

    List<VerhuisAdres> adr = services.getPersonenWsService()
        .getAdres(z, true)
        .getBasisWkWrappers()
        .stream()
        .map(VerhuisAdres::new)
        .collect(Collectors.toList());

    if (adr.size() == INT_ONE) {
      return adr.get(0);
    }
    return null;
  }

  /**
   * Get a specific address in the BAG
   */
  private Address getBagAddress(String aon) {
    if (services.getGeoService().isGeoServiceActive()) {
      if (StringUtils.isNotBlank(aon)) {
        AddressRequest request = new AddressRequest().setId(aon);
        Optional<Address> results = services.getGeoService().search(request).stream().findFirst();
        if (results.isPresent()) {
          return results.get();
        }
      }
    }
    return null;
  }

  private VerhuisAdres findBrpAddress(MunicipalAddressBean bean) {
    ZoekArgumenten z = new ZoekArgumenten();
    z.setStraatnaam(astr(bean.getStreet()));
    z.setPostcode(astr(bean.getPostalCode().getValue()));
    z.setHuisnummer(bean.getHouseNumber());
    z.setHuisletter(bean.getHouseNumberL());
    z.setHuisnummertoevoeging(bean.getHouseNumberT());
    z.setHuisnummeraanduiding(astr(bean.getHouseNumberA()));
    z.setDatum_einde("-1");

    if (!z.isGevuld()) {
      throw new ProException(ENTRY, INFO, "Geen adres ingegeven.");
    }

    List<VerhuisAdres> vAddress = services.getPersonenWsService()
        .getAdres(z, false)
        .getBasisWkWrappers()
        .stream()
        .map(VerhuisAdres::new)
        .collect(Collectors.toList());

    if (vAddress.isEmpty()) {
      throw new ProException(WARNING, "Het ingegeven adres kan niet worden gevonden in de gemeentelijke BRP");
    }

    if (vAddress.size() == INT_ONE) {
      return vAddress.get(INT_ZERO);
    }

    if (getApplication() != null) {
      getApplication().getMainWindow().addWindow(new AddressSelectionWindow(vAddress, selAddress -> {

        // Search specific address based on the code key
        ZoekArgumenten uz = new ZoekArgumenten();
        ProcuraInhabitantsAddress mapAddress = (ProcuraInhabitantsAddress) selAddress.getAddress();
        uz.setCode_object(astr(mapAddress.getInternalId()));
        uz.setDatum_einde(astr(mapAddress.getEndDate()));
        uz.setVolgcode_einde(astr(mapAddress.getEndDateNumber()));

        services.getPersonenWsService()
            .getAdres(uz, false)
            .getBasisWkWrappers()
            .stream()
            .map(VerhuisAdres::new)
            .collect(Collectors.toList())
            .forEach(address -> {
              completeLocalAddress(address.getAddress());
              setAddressInfoLayout(address);
            });
      }));
    }
    return null;
  }

  private void completeLocalAddress(Address address) {
    Field streetField = addressForm.getField(F_STREET);
    if (streetField != null) {
      streetField.setValue(new FieldValue(address.getStreet()));
      addressForm.getField(F_HOUSE_NUMBER).setValue(address.getHnr());
      addressForm.getField(F_HOUSE_NUMBER_L).setValue(address.getHnrL());
      addressForm.getField(F_HOUSE_NUMBER_T).setValue(address.getHnrT());
      addressForm.getField(F_HOUSE_NUMBER_A).setValue(new FieldValue(address.getHnrA()));
      addressForm.getField(F_POSTAL_CODE).setValue(new FieldValue(address.getPostalCode()));
      addressForm.getField(F_RESIDENCE).setValue(new FieldValue(address.getResidenceName()));

    }
  }

  /**
   * Find BAG addresses
   */
  private VerhuisAdres findBagAddress(MunicipalAddressBean bean) {
    if (bean.getBagAddress() != null) {
      return services.getGeoService()
          .search(new AddressRequest().setId(bean.getBagAddress().getAonId()))
          .stream()
          .map(address -> new VerhuisAdres(address, services.getGebruiker()))
          .findFirst().orElse(null);
    }
    return null;
  }

  private boolean checkSuitability(boolean checkSuitability,
      VerhuisAdres address,
      Consumer<VerhuisAdres> callback) {

    if (address != null) {
      int ppd = aval(services.getGebruiker().getParameters().get(ParameterConstant.GEBR_PPD).getValue());
      if ((ppd >= INT_ZERO) && (ppd != address.getAddress().getPPD())) {
        throw new ProException(SELECT, WARNING, "Dit adres valt niet onder uw beheer.");
      }
      if (checkSuitability && !address.isSuitableForLiving()) {
        if (address.getAddressLabel().equals(confirmedAddress)) {
          callback.accept(address);
        } else {
          ConfirmDialog confirmDialog = getConfirmDialog(address, callback);
          getApplication().getMainWindow().addWindow(confirmDialog);
          return false;
        }
      } else {
        confirmedAddress = address.getAddressLabel();
        callback.accept(address);
      }
    }

    return true;
  }

  private ConfirmDialog getConfirmDialog(VerhuisAdres address, Consumer<VerhuisAdres> callback) {
    StringBuilder msg = new StringBuilder();
    msg.append(address.getAddressLabel() + " is niet geschikt voor bewoning. ");
    String purpose = address.getAddress().getPurpose();
    if (StringUtils.isNotBlank(purpose)) {
      msg.append("<br/>Het heeft <b>" + purpose + "</b> als gebruiksdoel.");
    }
    msg.append("<hr/>Wilt u toch dit adres gebruiken?");
    return new ConfirmDialog("Weet u dit zeker?",
        msg.toString(),
        "500px",
        ProcuraTheme.ICOON_24.WARNING) {

      @Override
      public void buttonYes() {
        super.buttonYes();
        callback.accept(address);
        confirmedAddress = address.getAddressLabel();
      }
    };
  }
}
