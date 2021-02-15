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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.SpoedType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.PosNumberField;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page10ReisdocumentBean1 implements Serializable {

  public static final String AANVRAAGNUMMER    = "aanvraagnr";
  public static final String REISDOCUMENT      = "reisdocument";
  public static final String LENGTE            = "lengte";
  public static final String LENGTENVT         = "lengteNvt";
  public static final String SPOED             = "spoed";
  public static final String JEUGDTARIEF       = "jeugdtarief";
  public static final String REDENNIETAANWEZIG = "redenNietAanwezig";
  public static final String AFHAAL_LOCATIE    = "afhaalLocatie";

  @Field(type = FieldType.LABEL,
      caption = "Aanvraagnummer")
  private String aanvraagnr = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reisdocument",
      required = true)
  @Immediate()
  private ReisdocumentType reisdocument = ReisdocumentType.ONBEKEND;

  @Field(customTypeClass = PosNumberField.class,
      caption = "Lengte (cm)",
      width = "40px",
      required = true,
      validators = { LengteValidator.class })
  @TextField(maxLength = 3)
  private String lengte = "";

  @Field(type = FieldType.LABEL,
      caption = "Lengte (cm)",
      readOnly = true,
      visible = false)
  private String lengteNvt = "Niet van toepassing";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Spoed")
  @Select(containerDataSource = SpoedContainer.class,
      nullSelectionAllowed = false)
  private SpoedType spoed = SpoedType.NEE;

  @Field(type = FieldType.LABEL,
      caption = "Jeugdtarief")
  private String jeugdtarief = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Reden niet aanwezig",
      width = "500px")
  @TextField(maxLength = 255)
  private String redenNietAanwezig = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afhaallocatie",
      required = true)
  private Locatie afhaalLocatie = null;

  public Locatie getAfhaalLocatie() {
    return afhaalLocatie;
  }

  public void setAfhaalLocatie(Locatie afhaalLocatie) {
    this.afhaalLocatie = afhaalLocatie;
  }

  public String getJeugdtarief() {
    return jeugdtarief;
  }

  public void setJeugdtarief(String jeugdtarief) {
    this.jeugdtarief = jeugdtarief;
  }

  public String getLengte() {
    return lengte;
  }

  public void setLengte(String lengte) {
    this.lengte = lengte;
  }

  public String getLengteNvt() {
    return lengteNvt;
  }

  public void setLengteNvt(String lengteNvt) {
    this.lengteNvt = lengteNvt;
  }

  public String getRedenNietAanwezig() {
    return redenNietAanwezig;
  }

  public void setRedenNietAanwezig(String redenNietAanwezig) {
    this.redenNietAanwezig = redenNietAanwezig;
  }

  public ReisdocumentType getReisdocument() {
    return reisdocument;
  }

  public void setReisdocument(ReisdocumentType reisdocument) {
    this.reisdocument = reisdocument;
  }

  public SpoedType getSpoed() {
    return spoed;
  }

  public void setSpoed(SpoedType spoed) {
    this.spoed = spoed;
  }

  public String getAanvraagnr() {
    return aanvraagnr;
  }

  public void setAanvraagnr(String aanvraagnr) {
    this.aanvraagnr = aanvraagnr;
  }
}
