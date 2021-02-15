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

package nl.procura.gba.web.modules.persoonslijst.inschrijving.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1InschrijvingBean implements Serializable {

  public static final String INDICATIEGEHEIM  = "indicatieGeheim";
  public static final String BLOKKERING       = "blokkering";
  public static final String OPSCHORTING      = "opschorting";
  public static final String REDEN            = "reden";
  public static final String INSCHRIJVINGGBA  = "inschrijvingGBA";
  public static final String PKGEMEENTE       = "pkGemeente";
  public static final String PKCONVERSIE      = "pkConversie";
  public static final String VERSIENUMMER     = "versienummer";
  public static final String DATUMTIJDSTEMPEL = "datumtijdStempel";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Indicatie geheim")
  private String indicatieGeheim = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Blokkering")
  private String blokkering = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Opschorting")
  private String opschorting = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Reden")
  private String reden = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Inschrijving BRP")
  private String inschrijvingGBA = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "PK-gemeente")
  private String pkGemeente = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "PK-conversie")
  private String pkConversie = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Versienummer")
  private String versienummer = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum/tijd stempel")
  private String datumtijdStempel = "";
}
