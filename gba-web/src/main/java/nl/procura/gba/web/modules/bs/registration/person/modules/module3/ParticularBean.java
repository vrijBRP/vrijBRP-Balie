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

package nl.procura.gba.web.modules.bs.registration.person.modules.module3;

import java.lang.annotation.ElementType;

import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;
import nl.procura.gba.web.components.containers.BurgerlijkeStaatContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.zaken.rijbewijs.NaamgebruikType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.container.NLBooleanContainer;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ParticularBean {

  public static final String F_MARITAL_STATUS  = "maritalStatus";
  public static final String F_CONFIDENTIALITY = "confidentiality";
  public static final String F_USE_OF_NAME     = "useOfName";
  public static final String F_BEFORE_IN_NL    = "beforeInNl";
  public static final String F_BEFORE_LIVE     = "beforeLive";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Burgerlijke staat",
      width = "320px",
      required = true)
  @Select(containerDataSource = BurgerlijkeStaatContainer.class)
  @Immediate
  private BurgerlijkeStaatType maritalStatus;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naamgebruik",
      width = "320px",
      required = true)
  @Select
  @Immediate
  private NaamgebruikType useOfName;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verstrekkingsbeperking",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean confidentiality;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Eerder in Nederland gewoond?",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean beforeInNl;

  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting",
      required = true)
  @TextArea(rows = 5,
      columns = 50,
      nullRepresentation = "",
      maxLength = 200)
  private String beforeLive = "";

}
