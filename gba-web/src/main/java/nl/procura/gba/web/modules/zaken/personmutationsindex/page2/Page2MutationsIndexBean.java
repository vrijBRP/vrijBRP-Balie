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

package nl.procura.gba.web.modules.zaken.personmutationsindex.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2MutationsIndexBean implements Serializable {

  public static final String NAAM                   = "naam";
  public static final String ANR                    = "anr";
  public static final String DATUM_TIJD_MUTATIE     = "datumTijdMutatie";
  public static final String CAT                    = "cat";
  public static final String STATUS                 = "status";
  public static final String DATUM_TIJD_GOEDKEURING = "datumTijdGoedkeuring";

  @Field(type = Field.FieldType.LABEL,
      caption = "Naam")
  private String naam;

  @Field(type = Field.FieldType.LABEL,
      caption = "A-nummer")
  private String anr;

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum/tijd mutatie")
  private String datumTijdMutatie;

  @Field(type = Field.FieldType.LABEL,
      caption = "Categorie")
  private String cat;

  @Field(type = Field.FieldType.LABEL,
      caption = "Status")
  private String status;

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum/tijd goedkeuring")
  private String datumTijdGoedkeuring;
}
