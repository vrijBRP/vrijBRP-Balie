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

package nl.procura.gba.web.modules.persoonslijst.overig.data;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class PlDataBean implements Serializable {

  public static final String GELDIGHEID  = "geldigheid";
  public static final String OPNAME      = "opname";
  public static final String VORIGANR    = "voriganr";
  public static final String VOLGENDANR  = "volgendanr";
  public static final String ONJUIST     = "onjuist";
  public static final String INDDOCUMENT = "inddocument";
  public static final String INONDERZOEK = "inonderzoek";
  public static final String DATUMINGANG = "datumingang";
  public static final String DATUMEINDE  = "datumeinde";
  public static final String AKTE        = "akte";
  public static final String ONTLENING   = "ontlening";
  public static final String DOCUMENT    = "document";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Ind. document")
  private String inddocument = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geldigheid")
  private String geldigheid = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Opname")
  private String opname = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vorig anr.")
  private String voriganr = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Volgend anr.")
  private String volgendanr = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Onjuist")
  private String onjuist = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "In onderzoek")
  private String inonderzoek = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum ingang")
  private String datumingang = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum einde")
  private String datumeinde = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Akte")
  private String akte = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Ontlening")
  private String ontlening = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Document")
  private String document = "";
}
