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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1ZknDmsZaakBean implements Serializable {

  public static final String STARTDATUM           = "startdatum";
  public static final String REGISTRATIEDATUM     = "registratiedatum";
  public static final String GEPLANDE_EINDDATUM   = "geplandeEinddatum";
  public static final String UITERLIJKE_EINDDATUM = "uiterlijkeEinddatum";
  public static final String OMSCHRIJVING         = "omschrijving";

  @Field(type = FieldType.LABEL,
      caption = "Startdatum")
  private String startdatum = null;

  @Field(type = FieldType.LABEL,
      caption = "Registratiedatum")
  private String registratiedatum = null;

  @Field(type = FieldType.LABEL,
      caption = "Geplande einddatum")
  private String geplandeEinddatum = null;

  @Field(type = FieldType.LABEL,
      caption = "Uiterlijke einddatum")
  private String uiterlijkeEinddatum = null;

  @Field(type = FieldType.LABEL,
      caption = "Omschrijving")
  private String omschrijving = null;
}
