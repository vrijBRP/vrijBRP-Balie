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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class PresentievraagResultBean implements Serializable {

  public static final String ZAAK_TIJDSTIP     = "zaakTijdstip";
  public static final String ZAAK_GEBRUIKER    = "zaakGebruiker";
  public static final String ZAAK_LOCATIE      = "zaakLocatie";
  public static final String ZAAK_OMSCHRIJVING = "zaakOmschrijving";
  public static final String ZAAK_TOELICHTING  = "zaakToelichting";

  public static final String VRAAG        = "vraag";
  public static final String VERWERKING   = "verwerking";
  public static final String RESULTAAT    = "resultaat";
  public static final String RESULTAAT_PN = "resultaatPn";

  public static final String VR_VOORNAMEN         = "vraagVoornamen";
  public static final String VR_VOORVOEGSEL       = "vraagVoorvoegsel";
  public static final String VR_GESLACHTSNAAM     = "vraagGeslachtsnaam";
  public static final String VR_GEBOORTEDATUM     = "vraagGeboortedatum";
  public static final String VR_GEBOORTEPLAATS    = "vraagGeboorteplaats";
  public static final String VR_GEBOORTELAND      = "vraagGeboorteland";
  public static final String VR_GESLACHT          = "vraagGeslachtsaanduiding";
  public static final String VR_DATUM_AANVANG_BL  = "vraagDatumAanvangAdresBuitenland";
  public static final String VR_GEMEENTE          = "vraagGemeenteInschrijving";
  public static final String VR_BUITENLAND_PERSNR = "vraagBuitenlandsPersoonsnummer";
  public static final String VR_NATIONALITEIT     = "vraagNationaliteit";

  public static final String VOLGNR                     = "volgnr";
  public static final String REGISTRATIE                = "registratie";
  public static final String SCORE                      = "score";
  public static final String BSN                        = "bsn";
  public static final String VOORNAMEN                  = "voornamen";
  public static final String VOORVOEGSEL                = "voorvoegsel";
  public static final String GESLACHTSNAAM              = "geslachtsnaam";
  public static final String ADELIJKETITEL              = "adelijketitel";
  public static final String GEBOREN                    = "geboren";
  public static final String GESLACHT                   = "geslacht";
  public static final String NATIONALITEIT              = "nationaliteit";
  public static final String BUITENLANDS_PERSOONSNUMMER = "buitenlandsPersoonsnummer";
  public static final String GEMEENTEVANINSCHRIJVING    = "gemeenteVanInschrijving";
  public static final String DATUMVERTREKNL             = "datumVertrekNL";
  public static final String DATUMOVERLIJDEN            = "datumOverlijden";
  public static final String INDICATIEGEHEIM            = "indicatieGeheim";
  public static final String OPSCHORTING                = "opschorting";
  public static final String FUNCTIEADRES               = "functieAdres";
  public static final String GEMEENTEDEEL               = "gemeenteDeel";
  public static final String ADRES                      = "adres";
  public static final String LAND_VAN_VERTREK           = "landVanVertrek";
  public static final String ADRES_BUITENLAND           = "adresBuitenland";
  public static final String LOCATIE                    = "locatie";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Tijdstip")
  private String zaakTijdstip = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruiker")
  private String zaakGebruiker = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Locatie")
  private String zaakLocatie = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Heeft betrekking op zaak")
  private String zaakOmschrijving = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toelichting")
  private String zaakToelichting = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vraag")
  private String vraag = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Bericht")
  private String verwerking = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Resultaat")
  private String resultaat = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Resultaat persoonsnummer")
  private String resultaatPn = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Voornamen")
  private String vraagVoornamen = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Voorvoegsel")
  private String vraagVoorvoegsel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geslachtsnaam")
  private String vraagGeslachtsnaam = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboortedatum")
  private String vraagGeboortedatum = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboorteplaats")
  private String vraagGeboorteplaats = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboorteland")
  private String vraagGeboorteland = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geslachtsaanduiding")
  private String vraagGeslachtsaanduiding = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum aanvang adres buitenland")
  private String vraagDatumAanvangAdresBuitenland = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gemeente van inschrijving")
  private String vraagGemeenteInschrijving = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Buitenlands persoonsnummer")
  private String vraagBuitenlandsPersoonsnummer = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "EU-document nationaliteit")
  private String vraagNationaliteit = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Volgnr")
  private String volgnr = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Registratie")
  private String registratie = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Score")
  private String score = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "BSN")
  private String bsn = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Voornamen")
  private String voornamen = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Voorvoegsel")
  private String voorvoegsel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geslachtsnaam")
  private String geslachtsnaam = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adelijke titel")
  private String adelijketitel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboren")
  private String geboren = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geslacht")
  private String geslacht = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nationaliteit")
  private String nationaliteit = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Buitenlands persoonsnummer")
  private String buitenlandsPersoonsnummer = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gemeente van inschrijving")
  private String gemeenteVanInschrijving = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum vertrek NL")
  private String datumVertrekNL = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum overlijden")
  private String datumOverlijden = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Indicatie geheim")
  private String indicatieGeheim = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Opschorting")
  private String opschorting = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Functieadres")
  private String functieAdres = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gemeentedeel")
  private String gemeenteDeel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres")
  private String adres = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Adres buitenland")
  private String adresBuitenland = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Locatie")
  private String locatie = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Land van vertrek")
  private String landVanVertrek = "";

  public void setScore(String score) {
    this.score = "<b>" + score + "</b>";
  }
}
