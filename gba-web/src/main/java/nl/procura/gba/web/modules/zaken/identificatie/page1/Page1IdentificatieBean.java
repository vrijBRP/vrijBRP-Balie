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

package nl.procura.gba.web.modules.zaken.identificatie.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.container.NLBooleanContainer;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1IdentificatieBean implements Serializable {

  public static final String NUMMER                     = "nummer";
  public static final String SOORT                      = "soort";
  public static final String REISDOCUMENT_ADMINISTRATIE = "reisdocumentAdministratie";
  public static final String RIJBEWIJS_ADMINISTRATIE    = "rijbewijsAdministratie";
  public static final String EXTERNE_APP                = "externeApp";
  public static final String VRAGEN_NIET_MOGELIJK       = "vragenNietMogelijk";
  public static final String RPS                        = "rps";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nummer",
      required = true,
      width = "250px")
  @TextField(maxLength = 40)
  private String nummer = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort document",
      required = true,
      width = "250px")
  @Select(containerDataSource = DocumentTypeContainer.class)
  @Immediate()
  private IdentificatieType soort = null;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Reisdocumentenadministratie")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean reisdocumentAdministratie = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Rijbewijzenadministratie")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean rijbewijsAdministratie = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Externe applicatie")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean externeApp = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Persoon gezien, vragen niet mogelijk")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean vragenNietMogelijk = false;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Register Paspoortsignaleringen (RPS)")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean rps = false;
}
