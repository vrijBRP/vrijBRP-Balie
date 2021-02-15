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

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.date2str;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.diensten.gba.wk.baseWK.BaseWK;
import nl.procura.diensten.gba.wk.baseWK.BaseWKValue;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class WoningObjectBean implements Serializable {

  public static final String ADRES           = "adres";
  public static final String WONINGSOORT     = "woningsoort";
  public static final String WONINGINDICATIE = "woningindicatie";
  public static final String ADRESINDICATIE  = "adresindicatie";
  public static final String PPDCODE         = "ppdcode";
  public static final String STEMDISTRICT    = "stemdistrict";
  public static final String PANDCODE        = "pandcode";
  public static final String WIJK            = "wijk";
  public static final String BUURT           = "buurt";
  public static final String SUBBUURT        = "subbuurt";
  public static final String DATUMINGANG     = "datumIngang";
  public static final String DATUMEINDE      = "datumEinde";
  public static final String OPMERKING       = "opmerking";
  public static final String OPENBARE_RUIMTE = "openbareRuimte";
  public static final String WOONPLAATS      = "woonplaats";
  public static final String AON             = "aon";
  public static final String INA             = "ina";
  public static final String SURFACE_SIZE    = "surfaceSize";
  public static final String STATUS          = "status";
  public static final String BUILDING_STATUS = "buildingStatus";
  public static final String PURPOSE         = "purpose";

  @Field(type = FieldType.LABEL,
      caption = "Adres")
  private String adres = "";

  @Field(type = FieldType.LABEL,
      caption = "Woningsoort")
  private String woningsoort = "";

  @Field(type = FieldType.LABEL,
      caption = "Woningindicatie")
  private String woningindicatie = "";

  @Field(type = FieldType.LABEL,
      caption = "Adresindicatie")
  private String adresindicatie = "";

  @Field(type = FieldType.LABEL,
      caption = "PPD-code")
  private String ppdcode = "";

  @Field(type = FieldType.LABEL,
      caption = "Stemdistrict")
  private String stemdistrict = "";

  @Field(type = FieldType.LABEL,
      caption = "ID-pand")
  private String pandcode = "";

  @Field(type = FieldType.LABEL,
      caption = "Wijk")
  private String wijk = "";

  @Field(type = FieldType.LABEL,
      caption = "Buurt")
  private String buurt = "";

  @Field(type = FieldType.LABEL,
      caption = "Subbuurt")
  private String subbuurt = "";

  @Field(type = FieldType.LABEL,
      caption = "Datum ingang")
  private String datumIngang = "";

  @Field(type = FieldType.LABEL,
      caption = "Datum einde")
  private String datumEinde = "";

  @Field(type = FieldType.LABEL,
      caption = "Opmerking")
  private String opmerking = "";

  @Field(type = FieldType.LABEL,
      caption = "Openbare ruimte")
  private String openbareRuimte = "";

  @Field(type = FieldType.LABEL,
      caption = "Woonplaats")
  private String woonplaats = "";

  @Field(type = FieldType.LABEL,
      caption = "ID-verblijfplaats (AON)")
  private String aon = "";

  @Field(type = FieldType.LABEL,
      caption = "ID-nummeraand. (INA)")
  private String ina = "";

  @Field(type = FieldType.LABEL,
      caption = "Oppervlakte")
  private String surfaceSize = "";

  @Field(type = FieldType.LABEL,
      caption = "Status")
  private String status = "";

  @Field(type = FieldType.LABEL,
      caption = "Pandstatus")
  private String buildingStatus = "";

  @Field(type = FieldType.LABEL,
      caption = "Gebruiksdoel")
  private String purpose = "";

  public WoningObjectBean() {
  }

  public WoningObjectBean(BaseWKExt wk) {
    BaseWK bk = wk.getBasisWk();
    setAdres(wk.getAdres());
    setWoningsoort(get(bk.getWoning()));
    setWoningindicatie(get(bk.getWoning_indicatie()));
    setAdresindicatie(get(bk.getAdres_indicatie()));
    setPpdcode(get(bk.getPpd()));
    setStemdistrict(get(bk.getStemdistrict()));
    setPandcode(bk.getPnd().getValue());
    setWijk(get(bk.getWijk()));
    setBuurt(get(bk.getBuurt()));
    setSubbuurt(get(bk.getSub_buurt()));
    setDatumIngang(bk.getDatum_ingang().getDescr());
    setDatumEinde(bk.getDatum_einde().getDescr());
    setOpmerking(bk.getOpmerking().getDescr());
    setWoonplaats(bk.getWoonplaats().getDescr());
    setOpenbareRuimte(bk.getOpenbareRuimte().getDescr());
    setAon(bk.getAon().getDescr());
    setIna(bk.getIna().getDescr());
  }

  public WoningObjectBean(Address address) {
    setAdres(address.getLabel());
    setWoningsoort(address.getRecidenceType());
    setWoningindicatie(address.isSuitableForLiving() ? "Voor bewoning geschikt" : "Niet voor bewoning geschikt");
    setAdresindicatie(address.getAddressIndicationName());
    setPpdcode(address.getPPD() > 0 ? String.valueOf(address.getPPD()) : "");
    setStemdistrict(address.getVotingDistrict());
    setPandcode(address.getBuildingId());
    setWijk(address.getDistrict());
    setBuurt(address.getNeighborhood());
    setSubbuurt(address.getSubNeighborhood());
    setDatumIngang(date2str(address.getStartDate()));
    setDatumEinde(date2str(address.getEndDate()));
    setOpmerking(address.getRemarks());
    setWoonplaats(address.getResidenceName());
    setOpenbareRuimte(address.getStreet());
    setAon(address.getAonId());
    setIna(address.getInaId());
    setSurfaceSize(address.getSurfaceSize() > 0 ? address.getSurfaceSize() + " m2" : "");
    setStatus(address.getStatus());
    setBuildingStatus(address.getBuildingStatus());
    setPurpose(address.getPurpose());
  }

  private String get(BaseWKValue o) {
    return (aval(o.getCode()) >= 0) ? o.getCode() + ": " + o.getDescr() : "";
  }
}
