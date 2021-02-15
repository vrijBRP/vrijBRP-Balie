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
public class ReisdocumentOverzichtBean2 implements Serializable {

  public static final String VERMELDING_PARTNER  = "vermeldingPartner";
  public static final String VERMELDING_TITEL    = "vermeldingTitel";
  public static final String VERMELDING_LAND     = "vermeldingLand";
  public static final String PSEUDONIEM          = "pseudoniem";
  public static final String ONDERTEKENING       = "ondertekening";
  public static final String UITGEZONDERDELANDEN = "uitgezonderdeLanden";
  public static final String GELDIGVOORREIZEN    = "geldigVoorReizen";
  public static final String STAATLOOS           = "staatloos";
  public static final String TERVERVANGINGVAN    = "terVervangingVan";
  public static final String IN_BEZIT_BUITENLDOC = "inBezitBuitenlDoc";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vermelding partner")
  private String vermeldingPartner = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vermelding titel / predikaat")
  private String vermeldingTitel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vermelding land achter geboorteplaats")
  private String vermeldingLand = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Pseudoniem")
  private String pseudoniem = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Ondertekening")
  private String ondertekening = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Uitgezonderde landen")
  private String uitgezonderdeLanden = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geldig voor reizen")
  private String geldigVoorReizen = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Staatloos")
  private String staatloos = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Ter vervanging van")
  private String terVervangingVan = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "In bezit buitenl. doc.")
  private String inBezitBuitenlDoc = "";

}
