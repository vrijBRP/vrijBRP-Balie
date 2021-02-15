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

package nl.procura.gba.web.services.beheer.bag;

import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;

public abstract class AbstractAddress implements Address {

  protected String            aonId                 = "";
  protected String            inaId                 = "";
  protected String            buildingId            = "";
  protected String            street                = "";
  protected String            hnr                   = "";
  protected String            hnrL                  = "";
  protected String            hnrT                  = "";
  protected String            hnrA                  = "";
  protected String            foreignAddres1        = "";
  protected String            foreignAddres2        = "";
  protected String            foreignAddres3        = "";
  protected String            pc                    = "";
  protected String            recidenceCode         = "";
  protected String            recidenceName         = "";
  protected String            municipalityCode      = "";
  protected String            municipalityName      = "";
  protected String            countryCode           = "";
  protected String            countryName           = "";
  protected String            label                 = "";
  protected String            purpose               = "";
  protected String            status                = "";
  protected String            buildingStatus        = "";
  protected boolean           suitableForLiving     = true;
  protected int               surfaceSize           = -1;
  protected int               ppd                   = -1;
  protected int               addressIndicationCode = -1;
  protected String            addressIndicationName = "";
  protected int               startDate             = -1;
  protected int               endDate               = -1;
  protected String            recidenceType         = "";
  protected AddressSourceType sourceType;
  protected String            votingdistrict        = "";
  protected String            district              = "";
  protected String            neighborhood          = "";
  protected String            subNeighborhood       = "";
  protected String            remarks               = "";

  @Override
  public String getAonId() {
    return aonId;
  }

  @Override
  public String getInaId() {
    return inaId;
  }

  @Override
  public String getBuildingId() {
    return buildingId;
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public String getStreet() {
    return street;
  }

  @Override
  public String getHnr() {
    return hnr;
  }

  @Override
  public String getHnrL() {
    return hnrL;
  }

  @Override
  public String getHnrT() {
    return hnrT;
  }

  @Override
  public String getHnrA() {
    return hnrA;
  }

  @Override
  public String getPostalCode() {
    return pc;
  }

  @Override
  public String getResidenceCode() {
    return recidenceCode;
  }

  @Override
  public String getResidenceName() {
    return recidenceName;
  }

  @Override
  public String getMunicipalityCode() {
    return municipalityCode;
  }

  @Override
  public String getMunicipalityName() {
    return municipalityName;
  }

  @Override
  public String getForeignAddress1() {
    return foreignAddres1;
  }

  @Override
  public String getForeignAddress2() {
    return foreignAddres2;
  }

  @Override
  public String getForeignAddress3() {
    return foreignAddres3;
  }

  @Override
  public String getCountryCode() {
    return countryCode;
  }

  @Override
  public String getCountryName() {
    return countryName;
  }

  @Override
  public String getPurpose() {
    return purpose;
  }

  @Override
  public String getBuildingStatus() {
    return buildingStatus;
  }

  @Override
  public String getStatus() {
    return status;
  }

  @Override
  public int getSurfaceSize() {
    return surfaceSize;
  }

  @Override
  public boolean isSuitableForLiving() {
    return suitableForLiving;
  }

  @Override
  public int getPPD() {
    return ppd;
  }

  @Override
  public int getAddressIndicationCode() {
    return addressIndicationCode;
  }

  @Override
  public String getAddressIndicationName() {
    return addressIndicationName;
  }

  @Override
  public String getRecidenceType() {
    return recidenceType;
  }

  @Override
  public int getStartDate() {
    return startDate;
  }

  @Override
  public int getEndDate() {
    return endDate;
  }

  @Override
  public String getVotingDistrict() {
    return votingdistrict;
  }

  @Override
  public String getDistrict() {
    return district;
  }

  @Override
  public String getNeighborhood() {
    return neighborhood;
  }

  @Override
  public String getSubNeighborhood() {
    return subNeighborhood;
  }

  @Override
  public AddressSourceType getSourceType() {
    return sourceType;
  }

  @Override
  public String getRemarks() {
    return remarks;
  }
}
