/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1;

import static nl.procura.vaadin.annotation.field.Field.FieldType.TEXT_AREA;
import static nl.procura.vaadin.annotation.field.Field.FieldType.TEXT_FIELD;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextArea;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class BezorgingBean1 implements Serializable {

  static final String DOC_TYPE       = "docType";
  static final String AANVR_NR       = "aanvrNr";
  static final String REF_NR         = "refNr";
  static final String ORDER_REF_NR   = "orderRefNr";
  static final String NAAM           = "naam";
  static final String ADRES          = "adres";
  static final String HOOFDORDER     = "hoofdorder";
  static final String OPMERKINGEN    = "opmerkingen";
  static final String STATUS_LEV     = "statusLevering";
  static final String STATUS_AFSL    = "statusAfsluiting";
  static final String RDN_BLOKKERING = "redenBlokkering";
  static final String RDN_ANNULERING = "redenAnnulering";

  @Field(type = TEXT_FIELD,
      caption = "Document")
  private String docType = "";

  @Field(type = TEXT_FIELD,
      caption = "Aanvraagnr")
  private String aanvrNr = "";

  @Field(type = TEXT_FIELD,
      caption = "Bundel referentienr")
  private String refNr = "";

  @Field(type = TEXT_FIELD,
      caption = "Order referentienr")
  private String orderRefNr = "";

  @Field(type = TEXT_FIELD,
      caption = "Naam")
  private String naam = "";

  @Field(type = TEXT_FIELD,
      caption = "Adres")
  private String adres = "";

  @Field(type = TEXT_FIELD,
      caption = "Hoofdorder")
  private String hoofdorder = "";

  @Field(type = TEXT_AREA,
      caption = "Opmerkingen",
      height = "62px")
  @TextArea(rows = 3)
  private String opmerkingen = "";

  @Field(type = TEXT_FIELD,
      caption = "Reden blokkering")
  private String redenBlokkering = "";

  @Field(type = TEXT_FIELD,
      caption = "Reden annulering")
  private String redenAnnulering = "";

  public BezorgingBean1() {
  }
}
