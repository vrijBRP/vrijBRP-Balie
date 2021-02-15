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

package nl.procura.gba.web.modules.persoonslijst.reisdocument.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2ReisdocumentBean implements Serializable {

  public static final String REISDOCUMENT        = "reisdocument";
  public static final String NUMMER              = "nummer";
  public static final String AUTORITEIT          = "autoriteit";
  public static final String DATUMUITGIFTE       = "datumUitgifte";
  public static final String DATUMEINDE          = "datumEinde";
  public static final String INHOUDINGVERMISSING = "inhoudingVermissing";
  public static final String SIGNALERING         = "signalering";
  public static final String BUITENLANDSDOCUMENT = "buitenlandsDocument";
  public static final String LENGTE              = "lengte";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Reisdocument")
  private String reisdocument = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nummer")
  private String nummer = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Autoriteit")
  private String autoriteit = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum uitgifte")
  private String datumUitgifte = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum einde")
  private String datumEinde = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Inhouding / vermissing")
  private String inhoudingVermissing = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Signalering")
  private String signalering = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Buitenlands document")
  private String buitenlandsDocument = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Lengte")
  private String lengte = "";
}
