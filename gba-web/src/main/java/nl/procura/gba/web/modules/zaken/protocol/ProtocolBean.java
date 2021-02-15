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

package nl.procura.gba.web.modules.zaken.protocol;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.services.zaken.protocol.ProtocolRecord;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ProtocolBean implements Serializable {

  public static final String GEBRUIKER = "gebruiker";
  public static final String ANUMMMER  = "anummer";
  public static final String CATEGORIE = "categorie";
  public static final String DATUMTIJD = "datumtijd";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruiker",
      readOnly = true)
  private String gebruiker = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "A-nummer",
      readOnly = true)
  private AnrFieldValue anummer;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum/tijd",
      readOnly = true)
  private DateTime datumtijd = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Categorie",
      readOnly = true)
  private String categorie = "";

  public ProtocolBean(ProtocolRecord protocol) {
    this(protocol, "");
  }

  public ProtocolBean(ProtocolRecord protocol, String categorie) {

    setGebruiker(protocol.getGebruiker());
    setAnummer(protocol.getAnummer());
    setDatumtijd(protocol.getDate());
    setCategorie(categorie);
  }
}
