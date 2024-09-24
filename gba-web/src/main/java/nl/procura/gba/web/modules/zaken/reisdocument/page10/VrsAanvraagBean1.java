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

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Data
public class VrsAanvraagBean1 {

  static final String DOC_NR                         = "documentnummer";
  static final String AANVRAAGNUMMER                 = "aanvraagnummer";
  static final String AANVRAAGDATUM                  = "aanvraagdatum";
  static final String DOC_SOORT                      = "documentSoort";
  static final String INSTANTIE_LOCATIE              = "instantieLocatie";
  static final String ARTIKEL23B                     = "artikel23b";
  static final String NIET_PERSOONLIJK_AANWEZIG      = "nietPersoonlijkAanwezig";
  static final String AUT_VAN_VERSTREK               = "autoriteitVerstrekking";
  static final String INLEVERINSTANTIE               = "inleverinstantie";
  static final String INLEVERDATUM                   = "inleverdatum";
  static final String VERZOCHT_EINDE_GELDIGHEID      = "verzochtEindeGeldigheid";
  static final String REDEN_VINGER_AFWEZIG           = "redenvingerafwezig";
  static final String GOEDKEURING_GELAAT             = "goedkeuringGelaat";
  static final String NIET_INSTAAT_TOT_ONDERTEKENING = "nietInStaatTotOndertekening";
  static final String AUTORITEIT_VAN_AFGIFTE         = "autoriteitVanAfgifte";

  static final String BSN                  = "bsn";
  static final String NAAM                 = "naam";
  static final String GEBOORTEDATUM        = "geboortedatum";
  static final String GEBOORTEPLAATS       = "geboorteplaats";
  static final String NATIONALITEIT        = "nationaliteit";
  static final String GESLACHT             = "geslacht";
  static final String SPOED                = "spoed";
  static final String DATUM_VERSTREKKING   = "datumVerstrekking";
  static final String LENGTE               = "lengte";
  static final String PSEUDONIEM           = "pseudoniem";
  static final String PARTNER              = "partner";
  static final String VERMELDING_PARTNER   = "vermeldingPartner";
  static final String VERBLIJFSDOCUMENT    = "verblijfsdocument";
  static final String VERMELDING_TAAL      = "vermeldingTaal";
  static final String TVV                  = "tvv";
  static final String UITGEZONDERDE_LANDEN = "uitgezonderdeLanden";
  static final String GELDIG_VOOR_LANDEN   = "geldigLanden";
  static final String STAATLOOS            = "staatloos";

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Documentnummer")
  private String documentnummer;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Aanvraagnummer")
  private String aanvraagnummer;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Aanvraagdatum")
  private String aanvraagdatum;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Soort document")
  private String documentSoort;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Instantielocatie")
  private String instantieLocatie;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Artikel 23b")
  private String artikel23b;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Niet persoonlijk aanwezig")
  private String nietPersoonlijkAanwezig;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Autoriteit van verstrekking")
  private String autoriteitVerstrekking;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Inleverinstantie")
  private String inleverinstantie;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Inleverdatum")
  private String inleverdatum;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Verzocht einde geldigheid")
  private String verzochtEindeGeldigheid;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Reden vinger afwezig")
  private String redenvingerafwezig;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Goedkeuring gelaat")
  private String goedkeuringGelaat;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Niet in staat tot ondertekening")
  private String nietInStaatTotOndertekening;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Autoriteit van afgifte")
  private String autoriteitVanAfgifte;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "BSN / A-nummer")
  private String bsn;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Naam")
  private String naam;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Geboortedatum")
  private String geboortedatum;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Geboorteplaats")
  private String geboorteplaats;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Nationaliteit")
  private String nationaliteit;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Geslacht")
  private String geslacht;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Spoedaanvraag")
  private String spoed;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Datum verstrekking")
  private String datumVerstrekking;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Lengte")
  private String lengte;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Pseudoniem")
  private String pseudoniem;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Naam partner")
  private String partner;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Vermelding partner")
  private String vermeldingPartner;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Verblijfsdocument")
  private String verblijfsdocument;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Vermelding talen")
  private String vermeldingTaal;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Ter vermelding van")
  private String tvv;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Uitgezonderde landen")
  private String uitgezonderdeLanden;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Geldig voor landen")
  private String geldigLanden;

  @Field(type = TEXT_FIELD,
      width = "250px",
      caption = "Staatloos")
  private String staatloos;

}
