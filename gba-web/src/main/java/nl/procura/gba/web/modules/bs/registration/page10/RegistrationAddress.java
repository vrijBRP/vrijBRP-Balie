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

package nl.procura.gba.web.modules.bs.registration.page10;

import static java.lang.Long.parseLong;

import nl.procura.gba.web.modules.bs.common.layouts.relocation.RelocationAddress;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class RegistrationAddress implements RelocationAddress {

  private final DossierRegistration zaakDossier;
  private Address                   address;

  public RegistrationAddress(final DossierRegistration zaakDossier) {
    this.zaakDossier = zaakDossier;
  }

  @Override
  public AddressSourceType getSource() {
    return AddressSourceType.valueOfCode(zaakDossier.getAddressSource());
  }

  @Override
  public void setSource(AddressSourceType source) {
    zaakDossier.setAddressSource(source.getCode());
  }

  @Override
  public String getAon() {
    return zaakDossier.getAon();
  }

  @Override
  public void setAon(String aon) {
    zaakDossier.setAon(aon);
  }

  @Override
  public FieldValue getStreet() {
    return new FieldValue(zaakDossier.getStreet());
  }

  @Override
  public void setStreet(final FieldValue street) {
    zaakDossier.setStreet(street.getStringValue());
  }

  @Override
  public String getHouseNumber() {
    if (zaakDossier.getHouseNumber() != null) {
      return zaakDossier.getHouseNumber().toString();
    }
    return null;
  }

  @Override
  public void setHouseNumber(final String hnr) {
    zaakDossier.setHouseNumber(parseLong(hnr));
  }

  @Override
  public FieldValue getHouseNumberA() {
    return new FieldValue(zaakDossier.getHouseNumberA());
  }

  @Override
  public void setHouseNumberA(final FieldValue hnrA) {
    if (hnrA != null) {
      zaakDossier.setHouseNumberA(hnrA.getStringValue());
    }
  }

  @Override
  public String getHouseNumberL() {
    return zaakDossier.getHouseNumberL();
  }

  @Override
  public void setHouseNumberL(final String hnrL) {
    zaakDossier.setHouseNumberL(hnrL);
  }

  @Override
  public String getHouseNumberT() {
    return zaakDossier.getHouseNumberT();
  }

  @Override
  public void setHouseNumberT(final String hnrT) {
    zaakDossier.setHouseNumberT(hnrT);
  }

  @Override
  public FieldValue getPostalCode() {
    return new FieldValue(zaakDossier.getPostalCode());
  }

  @Override
  public void setPostalCode(final FieldValue pc) {
    zaakDossier.setPostalCode(pc.getStringValue());
  }

  @Override
  public FieldValue getResidence() {
    return new FieldValue(zaakDossier.getResidence());
  }

  @Override
  public void setResidence(final FieldValue residence) {
    zaakDossier.setResidence(residence.getStringValue());
  }

  @Override
  public FieldValue getMunicipality() {
    return new FieldValue();
  }

  @Override
  public void setMunicipality(FieldValue municipality) {
    // Not applicable
  }

  @Override
  public FieldValue getNoOfPeople() {
    if (zaakDossier.getNoOfPeople() > 0) {
      return new FieldValue(zaakDossier.getNoOfPeople());
    }
    return new FieldValue("");
  }

  @Override
  public void setNoOfPeople(FieldValue noOfPeople) {
    zaakDossier.setNoOfPeople(noOfPeople.getLongValue());
  }

  @Override
  public FunctieAdres getAddressFunction() {
    FunctieAdres addFunc = FunctieAdres.get(zaakDossier.getAddressFunction());
    if (addFunc.equals(FunctieAdres.ONBEKEND)) {
      addFunc = FunctieAdres.WOONADRES;
    }
    return addFunc;
  }

  @Override
  public void setAddressFunction(FunctieAdres addressFunction) {
    zaakDossier.setAddressFunction(addressFunction.getCode());
  }

  @Override
  public ConsentProvider getConsentProvider() {
    return zaakDossier.getConsent();
  }

  @Override
  public void setConsentProvider(ConsentProvider value) {
    zaakDossier.setConsent(value);
  }

  @Override
  public Address getBagAddress() {
    return address;
  }

  @Override
  public void setBagAddress(Address address) {
    this.address = address;
  }
}
