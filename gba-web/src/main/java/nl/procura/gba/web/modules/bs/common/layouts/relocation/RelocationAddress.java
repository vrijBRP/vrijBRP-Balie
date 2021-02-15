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

import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public interface RelocationAddress {

  AddressSourceType getSource();

  void setSource(AddressSourceType source);

  String getAon(); // Adresseerbaar object id

  void setAon(String aon);

  FieldValue getStreet();

  void setStreet(FieldValue street);

  String getHouseNumber();

  void setHouseNumber(String hnr);

  String getHouseNumberL();

  void setHouseNumberL(String hnrL);

  FieldValue getHouseNumberA();

  void setHouseNumberA(FieldValue hnrA);

  String getHouseNumberT();

  void setHouseNumberT(String hnrT);

  FieldValue getPostalCode();

  void setPostalCode(FieldValue pc);

  FieldValue getResidence();

  void setResidence(FieldValue residence);

  FieldValue getMunicipality();

  void setMunicipality(FieldValue municipality);

  FunctieAdres getAddressFunction();

  void setAddressFunction(FunctieAdres addressFunction);

  FieldValue getNoOfPeople();

  void setNoOfPeople(FieldValue noOfPeople);

  ConsentProvider getConsentProvider();

  void setConsentProvider(ConsentProvider value);

  Address getBagAddress();

  void setBagAddress(Address address);
}
