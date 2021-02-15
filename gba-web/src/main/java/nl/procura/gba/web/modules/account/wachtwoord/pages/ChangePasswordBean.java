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

package nl.procura.gba.web.modules.account.wachtwoord.pages;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.annotation.layout.Position;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ChangePasswordBean implements Serializable {

  public static final String GEBRUIKER          = "gebruiker";
  public static final String DATUM              = "datum";
  public static final String HUIDIG_WW          = "huidigWw";
  public static final String NIEUW_WW           = "nieuwWw";
  public static final String HERHALING_NIEUW_WW = "herhalingNieuwWw";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruiker",
      readOnly = true)
  @Position(order = "1")
  private String gebruiker = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verloopdatum wachtwoord",
      readOnly = true)
  @Position(order = "1")
  private String datum = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Huidig wachtwoord",
      required = true)
  @TextField(secret = true)
  @Position(order = "2")
  private String huidigWw = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nieuw wachtwoord",
      required = true)
  @TextField(secret = true)
  private String nieuwWw = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Bevestig nieuw wachtwoord",
      required = true)
  @TextField(secret = true)
  private String herhalingNieuwWw = "";
}
