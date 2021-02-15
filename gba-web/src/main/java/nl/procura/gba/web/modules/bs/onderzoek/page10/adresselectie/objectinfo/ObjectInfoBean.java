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

package nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo;

import static nl.procura.standard.Globalfunctions.aval;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import org.apache.commons.lang3.builder.ToStringBuilder;

import nl.procura.diensten.gba.wk.baseWK.BaseWK;
import nl.procura.diensten.gba.wk.baseWK.BaseWKValue;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ObjectInfoBean implements Serializable {

  public static final String ADRES           = "adres";
  public static final String WONINGSOORT     = "woningsoort";
  public static final String WONINGINDICATIE = "woningindicatie";
  public static final String ADRESINDICATIE  = "adresindicatie";
  public static final String PPDCODE         = "ppdcode";
  public static final String STEMDISTRICT    = "stemdistrict";
  public static final String PANDCODE        = "pandcode";
  public static final String WIJKCODE        = "wijkcode";
  public static final String BUURTCODE       = "buurtcode";
  public static final String SUBBUURTCODE    = "subbuurtcode";
  public static final String DATUMINGANG     = "datumIngang";
  public static final String DATUMEINDE      = "datumEinde";
  public static final String OPMERKING       = "opmerking";
  public static final String OPENBARE_RUIMTE = "openbareRuimte";
  public static final String WOONPLAATS      = "woonplaats";
  public static final String AON             = "aon";
  public static final String INA             = "ina";

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
      caption = "Pandcode")
  private String pandcode = "";

  @Field(type = FieldType.LABEL,
      caption = "Wijkcode")
  private String wijkcode = "";

  @Field(type = FieldType.LABEL,
      caption = "Buurtcode")
  private String buurtcode = "";

  @Field(type = FieldType.LABEL,
      caption = "Subbuurtcode")
  private String subbuurtcode = "";

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

  public ObjectInfoBean() {
  }

  public ObjectInfoBean(BaseWKExt wk) {

    BaseWK bk = wk.getBasisWk();
    setAdres(wk.getAdres());
    setWoningsoort(get(bk.getWoning()));
    setWoningindicatie(get(bk.getWoning_indicatie()));
    setAdresindicatie(get(bk.getAdres_indicatie()));
    setPpdcode(get(bk.getPpd()));
    setStemdistrict(get(bk.getStemdistrict()));
    setPandcode(bk.getPnd().getValue());
    setWijkcode(get(bk.getWijk()));
    setBuurtcode(get(bk.getBuurt()));
    setSubbuurtcode(get(bk.getSub_buurt()));
    setDatumIngang(bk.getDatum_ingang().getDescr());
    setDatumEinde(bk.getDatum_einde().getDescr());
    setOpmerking(bk.getOpmerking().getDescr());
    setWoonplaats(bk.getWoonplaats().getDescr());
    setOpenbareRuimte(bk.getOpenbareRuimte().getDescr());
    setAon(bk.getAon().getDescr());
    setIna(bk.getIna().getDescr());
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  private String get(BaseWKValue o) {
    return (aval(o.getCode()) >= 0) ? o.getCode() + ": " + o.getDescr() : "";
  }
}
