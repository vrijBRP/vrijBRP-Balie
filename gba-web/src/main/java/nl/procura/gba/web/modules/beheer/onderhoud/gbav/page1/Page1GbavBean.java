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

package nl.procura.gba.web.modules.beheer.onderhoud.gbav.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1GbavBean implements Serializable {

  public static final String ID           = "id";
  public static final String NAAM         = "naam";
  public static final String TYPE         = "type";
  public static final String VERLOOPDATUM = "verloopdatumWachtwoord";
  public static final String GEBLOKKEERD  = "geblokkeerd";

  @Field(type = FieldType.LABEL,
      caption = "Account-id")
  private String id = "";

  @Field(type = FieldType.LABEL,
      caption = "Account")
  private String naam = "";

  @Field(type = FieldType.LABEL,
      caption = "Soort")
  private String type = "";

  @Field(type = FieldType.LABEL,
      caption = "Verloopdatum wachtwoord")
  private String verloopdatumWachtwoord = "";

  @Field(type = FieldType.LABEL,
      caption = "Geblokkeerd")
  private String geblokkeerd = "";
}
