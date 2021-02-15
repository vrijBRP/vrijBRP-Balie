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

package nl.procura.gba.web.modules.zaken.verhuizing.page13;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.modules.bs.common.layouts.relocation.RelocationAddress;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class MigrationAddress implements RelocationAddress {

  private final VerhuisAanvraag zaak;
  private AddressSourceType     addressSource = AddressSourceType.UNKNOWN;

  public MigrationAddress(VerhuisAanvraag zaak) {
    this.zaak = zaak;
  }

  @Override
  public AddressSourceType getSource() {
    return addressSource;
  }

  @Override
  public void setSource(AddressSourceType addressSource) {
    this.addressSource = addressSource;
  }

  @Override
  public String getAon() {
    return "";
  }

  @Override
  public void setAon(String aon) {
  }

  @Override
  public FieldValue getStreet() {
    return new FieldValue();
  }

  @Override
  public void setStreet(FieldValue street) {
    zaak.getNieuwAdres().setStraat(street);
  }

  @Override
  public String getHouseNumber() {
    return "";
  }

  @Override
  public void setHouseNumber(String hnr) {
    zaak.getNieuwAdres().setHnr(along(hnr));
  }

  @Override
  public FieldValue getHouseNumberA() {
    return new FieldValue();
  }

  @Override
  public void setHouseNumberA(FieldValue hnrA) {
    if (hnrA != null) {
      zaak.getNieuwAdres().setHnrA(astr(hnrA.getStringValue()));
    } else {
      zaak.getNieuwAdres().setHnrA("");
    }
  }

  @Override
  public String getHouseNumberL() {
    return "";
  }

  @Override
  public void setHouseNumberL(String hnrL) {
    zaak.getNieuwAdres().setHnrL(hnrL);
  }

  @Override
  public String getHouseNumberT() {
    return "";
  }

  @Override
  public void setHouseNumberT(String hnrT) {
    zaak.getNieuwAdres().setHnrT(hnrT);
  }

  @Override
  public FieldValue getPostalCode() {
    return new FieldValue();
  }

  @Override
  public void setPostalCode(FieldValue pc) {
    zaak.getNieuwAdres().setPc(pc);
  }

  @Override
  public FieldValue getResidence() {
    return new FieldValue();
  }

  @Override
  public void setResidence(FieldValue residence) {
    zaak.getNieuwAdres().setWoonplaats(residence);
  }

  @Override
  public FieldValue getMunicipality() {
    return new FieldValue();
  }

  @Override
  public void setMunicipality(FieldValue municipality) {
    zaak.getNieuwAdres().setGemeente(municipality);
  }

  @Override
  public FieldValue getNoOfPeople() {
    return new FieldValue("");
  }

  @Override
  public void setNoOfPeople(FieldValue noOfPeople) {
    zaak.getNieuwAdres().setAantalPersonen((int) noOfPeople.getLongValue());
  }

  @Override
  public FunctieAdres getAddressFunction() {
    return FunctieAdres.WOONADRES;
  }

  @Override
  public void setAddressFunction(FunctieAdres addressFunction) {
    zaak.getNieuwAdres().setFunctieAdres(addressFunction);
  }

  @Override
  public ConsentProvider getConsentProvider() {
    return ConsentProvider.notDeclared();
  }

  @Override
  public void setConsentProvider(ConsentProvider value) {

    // Defaults
    zaak.getHoofdbewoner().setBurgerServiceNummer(new BsnFieldValue());
    zaak.getToestemminggever().setBurgerServiceNummer(new BsnFieldValue());
    zaak.setSprakeVanInwoning(false);
    zaak.getToestemminggever().setAnders(value.getOtherConsentProvider());

    Relatie toestemmingGever = value.getBrpConsentProvider();

    if (toestemmingGever != null) {
      BsnFieldValue bsnToestemmingever = new BsnFieldValue(toestemmingGever.getPl()
          .getPersoon()
          .getBsn()
          .getVal());

      zaak.getToestemminggever().setBurgerServiceNummer(bsnToestemmingever);
      zaak.getToestemminggever().setPersoon(new DocumentPL(toestemmingGever.getPl()));
      zaak.setSprakeVanInwoning(pos(zaak.getToestemminggever()
          .getBurgerServiceNummer()
          .getValue()));

      // Hoofdbewoner is toestemminggever
      zaak.getHoofdbewoner().setBurgerServiceNummer(bsnToestemmingever);
      zaak.getHoofdbewoner().setPersoon(new DocumentPL(toestemmingGever.getPl()));
    }
  }

  @Override
  public Address getBagAddress() {
    return null;
  }

  @Override
  public void setBagAddress(Address address) {
  }
}
