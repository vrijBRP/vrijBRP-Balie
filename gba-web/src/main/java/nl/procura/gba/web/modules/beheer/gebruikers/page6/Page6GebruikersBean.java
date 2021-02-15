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

package nl.procura.gba.web.modules.beheer.gebruikers.page6;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.beheer.gebruikers.page5.GebruikerInfoTypeContainer;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page6GebruikersBean implements Serializable {

  public static final String STANDAARDOPTIE   = "standaardoptie";
  public static final String TERM             = "term";
  public static final String OMSCHRIJVING     = "omschrijving";
  public static final String WAARDE           = "waarde";
  public static final String AANGEPASTEWAARDE = "gebruikerWaarde";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Standaardopties",
      width = "400px")
  @Select(containerDataSource = GebruikerInfoTypeContainer.class)
  @Immediate()
  private GebruikerInfoType standaardoptie = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Term",
      width = "400px",
      required = true)
  @TextField(nullRepresentation = "")
  private String term = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Omschrijving",
      width = "400px",
      required = true)
  @TextField(nullRepresentation = "")
  private String omschrijving = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Algemene waarde",
      width = "400px")
  @TextArea(nullRepresentation = "",
      rows = 3)
  private String waarde = "";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Aangepaste waarde",
      width = "400px")
  @TextArea(nullRepresentation = "",
      rows = 3)
  private String gebruikerWaarde = "";

}
