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

package nl.procura.gba.web.services.interfaces.address;

/**
 * Generic interface for multiple Address implementations
 */
public interface Address {

  AddressSourceType getSourceType();

  String getAonId(); // Dutch: verblijfplaats ID

  String getInaId(); // Dutch: nummeraanduiding ID

  String getBuildingId(); // Dutch: pandaanduiding

  String getLabel();

  String getStreet();

  String getHnr();

  String getHnrL();

  String getHnrT();

  String getHnrA();

  String getPostalCode();

  String getResidenceCode();

  String getResidenceName();

  String getMunicipalityCode();

  String getMunicipalityName();

  String getForeignAddress1();

  String getForeignAddress2();

  String getForeignAddress3();

  String getCountryCode();

  String getCountryName();

  String getPurpose();

  String getBuildingStatus();

  String getStatus();

  int getSurfaceSize();

  boolean isSuitableForLiving();

  int getPPD();

  int getAddressIndicationCode();

  String getAddressIndicationName();

  String getRecidenceType();

  int getStartDate();

  int getEndDate();

  String getVotingDistrict();

  String getDistrict(); // Dutch: Wijk

  String getNeighborhood(); // Dutch: Buurt

  String getSubNeighborhood(); // Dutch: Subbuurt

  String getRemarks();
}
