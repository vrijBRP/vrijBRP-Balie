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

package nl.procura.gba.web.modules.zaken.reisdocument.overzicht;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ReisdocumentOverzichtBean1 implements Serializable {

  public static final String REISDOCUMENT       = "reisdocument";
  public static final String AANVRAAGNUMMER     = "aanvraagnummer";
  public static final String LENGTE             = "lengte";
  public static final String TOESTEMMING        = "toestemming";
  public static final String SPOED              = "spoed";
  public static final String JEUGDTARIEF        = "jeugdtarief";
  public static final String DOCUMENTOUDERVOOGD = "documentOuderVoogd";
  public static final String REDENNIETAANWEZIG  = "redenNietAanwezig";
  public static final String GELDIGHEID         = "geldigheid";
  public static final String SIGNALERING        = "signalering";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Reisdocument")
  private String reisdocument = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Aanvraagnummer")
  private String aanvraagnummer = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Lengte persoon")
  private String lengte = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toestemming(en)")
  private String toestemming = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Spoed")
  private String spoed = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Jeugdtarief")
  private String jeugdtarief = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Reden niet aanwezig")
  private String redenNietAanwezig = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geldigheid")
  private String geldigheid = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Signalering")
  private String signalering = "";
}
