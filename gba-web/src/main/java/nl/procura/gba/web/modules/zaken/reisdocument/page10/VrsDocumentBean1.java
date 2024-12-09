/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static nl.procura.vaadin.annotation.field.Field.FieldType.TEXT_FIELD;

import java.lang.annotation.ElementType;
import lombok.Data;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Data
public class VrsDocumentBean1 {

  static final String DOC_NR           = "documentnummer";
  static final String DOC_AFGIFTE      = "documentAfgifte";
  static final String DOC_GELDIG_TOT   = "documentGeldigTot";
  static final String DOC_SOORT        = "documentSoort";
  static final String BSN              = "bsn";
  static final String NAAM             = "naam";
  static final String GEBOORTEDATUM    = "geboortedatum";
  static final String GEBOORTEPLAATS   = "geboorteplaats";
  static final String NATIONALITEIT    = "nationaliteit";
  static final String GESLACHT         = "geslacht";
  static final String LENGTE           = "lengte";
  static final String STAATLOOS        = "staatloos";
  static final String AANVRAAGNUMMER   = "aanvraagnummer";
  static final String AUT_VAN_VERSTREK = "autoriteitVerstrekking";

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Documentnummer")
  private String documentnummer;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Documentafgifte")
  private String documentAfgifte;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Geldig tot")
  private String documentGeldigTot;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Soort document")
  private String documentSoort;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Burgerservicenummer")
  private String bsn;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Naam")
  private String naam;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Geboortedatum")
  private String geboortedatum;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Geboorteplaats")
  private String geboorteplaats;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Nationaliteit")
  private String nationaliteit;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Geslacht")
  private String geslacht;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Lengte")
  private String lengte;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Staatloos")
  private String staatloos;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Aanvraagnummer")
  private String aanvraagnummer;

  @Field(type = TEXT_FIELD,
      width = "300px",
      caption = "Autoriteit van verstrekking")
  private String autoriteitVerstrekking;
}
