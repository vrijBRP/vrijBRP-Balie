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

package nl.procura.gba.web.modules.beheer.locaties.page2;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.math.BigDecimal;

import nl.procura.gba.web.services.beheer.locatie.LocatieType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.layout.Position;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.ProNativeSelect;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2LocatiesBean implements Serializable {

  public static final String IPADRES      = "IPadres";
  public static final String LOCATIE      = "locatie";
  public static final String OMSCHRIJVING = "omschrijving";
  public static final String IPADRESSEN   = "IPadressen";
  public static final String RASCODE      = "rasCode";
  public static final String RAASCODE     = "raasCode";
  public static final String JCCGKASID    = "JCCGKASid";
  public static final String TYPE         = "type";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Uw IP-adres",
      readOnly = true)
  @Position(order = "1")
  private String IPadres = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Type locatie",
      required = true)
  @Select(containerDataSource = LocatieTypeContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private LocatieType type = LocatieType.NORMALE_LOCATIE;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Locatie",
      required = true)
  @TextField(nullRepresentation = "")
  private String locatie = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Omschrijving",
      width = "500px")
  @TextArea(nullRepresentation = "")
  private String omschrijving = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "IP adres(sen) (gescheiden door komma of door een nieuwe regel)",
      width = "500px")
  @TextField(nullRepresentation = "")
  private String IPadressen = "";

  @Field(customTypeClass = NumberField.class,
      caption = "Ras-id (rijbewijzen)",
      required = true)
  private BigDecimal rasCode = toBigDecimal(0);

  @Field(customTypeClass = NumberField.class,
      caption = "RAAS-id (reisdocumenten)",
      required = true)
  private BigDecimal raasCode = toBigDecimal(0);

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa-locatie-identificatie")
  @TextField(nullRepresentation = "")
  private String JCCGKASid = "";

  public String getIPadres() {
    return IPadres;
  }

  public void setIPadres(String IPadres) {
    this.IPadres = IPadres;
  }

  public String getIPadressen() {
    return IPadressen;
  }

  public void setIPadressen(String iPadressen) {
    IPadressen = iPadressen;
  }

  public String getJCCGKASid() {
    return JCCGKASid;
  }

  public void setJCCGKASid(String jCCGKASid) {
    JCCGKASid = jCCGKASid;
  }

  public String getLocatie() {
    return locatie;
  }

  public void setLocatie(String locatie) {
    this.locatie = locatie;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public BigDecimal getRaasCode() {
    return raasCode;
  }

  public void setRaasCode(BigDecimal raasCode) {
    this.raasCode = raasCode;
  }

  public BigDecimal getRasCode() {
    return rasCode;
  }

  public void setRasCode(BigDecimal rasCode) {
    this.rasCode = rasCode;
  }

  public LocatieType getType() {
    return type;
  }

  public void setType(LocatieType type) {
    this.type = type;
  }
}
