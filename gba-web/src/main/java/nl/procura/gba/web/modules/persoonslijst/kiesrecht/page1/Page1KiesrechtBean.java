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

package nl.procura.gba.web.modules.persoonslijst.kiesrecht.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1KiesrechtBean implements Serializable {

  public static final String AANDUIDINGUITGESLOTENKIESRECHT        = "aanduidingUitgeslotenKiesrecht";
  public static final String EINDDATUMUITSLUITING                  = "einddatumUitsluiting";
  public static final String AANDUIDINGEUROPEESKIESRECHT           = "aanduidingEuropeesKiesrecht";
  public static final String DATUMVERZOEKMEDEDELING                = "datumVerzoekMededeling";
  public static final String EINDDATUMUITSLUITINGEUROPEESKIESRECHT = "einddatumUitsluitingEuropeesKiesrecht";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Aanduiding uitgesloten kiesrecht")
  private String aanduidingUitgeslotenKiesrecht = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Einddatum uitsluiting")
  private String einddatumUitsluiting = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Aanduiding europees kiesrecht")
  private String aanduidingEuropeesKiesrecht = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum verzoek/mededeling")
  private String datumVerzoekMededeling = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Einddatum uitsluiting europees kiesrecht")
  private String einddatumUitsluitingEuropeesKiesrecht = "";
}
