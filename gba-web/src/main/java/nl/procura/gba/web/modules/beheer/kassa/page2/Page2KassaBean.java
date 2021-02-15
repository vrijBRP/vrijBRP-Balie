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

package nl.procura.gba.web.modules.beheer.kassa.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.beheer.kassa.KassaType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.reisdocumenten.SoortReisdocument;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.container.NLBooleanContainer;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2KassaBean implements Serializable {

  public static final String KASSA        = "kassa";
  public static final String KASSATYPE    = "kassaType";
  public static final String DOCUMENT     = "document";
  public static final String REISDOCUMENT = "reisdocument";
  public static final String RIJBEWIJS    = "rijbewijs";
  public static final String PRODUCT      = "anders";
  public static final String BUNDEL       = "bundel";
  public static final String PRODUCTGROEP = "productgroep";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassacode",
      required = true)
  @TextField(nullRepresentation = "")
  private String kassa = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type",
      required = true)
  @Immediate()
  private KassaType kassaType;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bundel",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate
  private Boolean bundel = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Document",
      required = true)
  @Immediate()
  private DocumentRecord document;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reisdocument",
      required = true)
  @Immediate()
  private SoortReisdocument reisdocument;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Rijbewijs",
      required = true)
  @Immediate()
  private RijbewijsAanvraagSoort rijbewijs;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Product",
      required = true,
      width = "300px")
  @TextField(nullRepresentation = "")
  private String anders = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Productgroep",
      required = true,
      width = "300px")
  @TextField(nullRepresentation = "")
  private String productgroep = "";
}
