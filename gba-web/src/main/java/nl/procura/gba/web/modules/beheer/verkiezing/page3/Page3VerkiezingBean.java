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

package nl.procura.gba.web.modules.beheer.verkiezing.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.PosNumberField;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page3VerkiezingBean implements Serializable {

  public static final String F_ROS             = "ros";
  public static final String F_VNR             = "vnr";
  public static final String F_AAND            = "aanduiding";
  public static final String F_AANTAL          = "aantal";
  public static final String F_DATUM_VAN       = "datumVan";
  public static final String F_DATUM_TM        = "datumTm";
  public static final String F_KIESGERECHTIGDE = "kiesgerechtigde";
  public static final String F_GEMACHTIGDE     = "gemachtigde";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Opgenomen in het ROS")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean ros;

  @Field(customTypeClass = PosNumberField.class,
      width = "70px",
      caption = "Volgnummer")
  @TextField(nullRepresentation = "")
  private String vnr = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      width = "140px",
      caption = "Aanduiding in kiezersregister")
  @Immediate
  @Select(containerDataSource = StempasAanduidingContainer.class)
  private StempasAanduidingType aanduiding = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum aanduiding van",
      width = "140px")
  private Date datumVan = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum aanduiding t/m",
      width = "140px")
  private Date datumTm = null;

  @Field(customTypeClass = ProTextField.class,
      caption = "Aantal stempassen",
      readOnly = true)
  private String aantal = "";

  @Field(customTypeClass = ProTextField.class,
      caption = "Kiesgerechtigde",
      width = "600px",
      readOnly = true)
  private String kiesgerechtigde;

  @Field(customTypeClass = ProTextField.class,
      caption = "Gemachtigde",
      width = "600px",
      readOnly = true)
  private String gemachtigde;
}
