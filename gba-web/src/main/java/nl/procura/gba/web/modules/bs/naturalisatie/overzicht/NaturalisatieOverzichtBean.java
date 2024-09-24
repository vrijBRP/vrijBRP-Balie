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

package nl.procura.gba.web.modules.bs.naturalisatie.overzicht;

import static nl.procura.vaadin.annotation.field.Field.FieldType.LABEL;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class NaturalisatieOverzichtBean implements Serializable {

  public static final String F_AANGEVER                  = "aangever";
  public static final String F_BEVOEGD                   = "bevoegd";
  public static final String F_OPTIE_MOGELIJK            = "optieMogelijk";
  public static final String F_BASIS_VERZOEK             = "basisVerzoek";
  public static final String F_KINDEREN_MEENATURALISEREN = "kinderenMeeNaturaliseren";
  public static final String F_VERTEGENWOORDIGER         = "vertegenwoordiger";

  public static final String F_VERKLARINGVERBLIJF          = "verklaringVerblijf";
  public static final String F_BEREIDAFLEGGENVERKLARING    = "bereidAfleggenVerklaring";
  public static final String F_BETROKKENEBEKENDMETBETALING = "betrokkeneBekendMetBetaling";
  public static final String F_BEREIDAFSTANDNATIONALITEIT  = "bereidAfstandNationaliteit";
  public static final String F_BEWIJSVANIDENTITEIT         = "bewijsVanIdentiteit";
  public static final String F_BEWIJSVANNATIONALITEIT      = "bewijsVanNationaliteit";
  public static final String F_BEWIJSNOODAANGETOOND        = "bewijsnoodAangetoond";
  public static final String F_GELDIGEVERBLIJFSVERGUNNING  = "geldigeVerblijfsvergunning";

  public static final String F_NAAMSVASTSTELLINGOFWIJZIGING = "naamsvaststellingOfWijziging";
  public static final String F_GESLACHTSNAAMVASTGESTELD     = "geslachtsnaamVastgesteld";
  public static final String F_VOORNAMENVASTGESTELD         = "voornamenVastgesteld";
  public static final String F_GESLACHTSNAAMGEWIJZIGD       = "geslachtsnaamGewijzigd";

  public static final String F_BERICHT_OMTRENT_TOELATING   = "berichtOmtrentToelating";
  public static final String F_MINDERJARIGE_KINDEREN_12    = "minderjarigeKinderen12";
  public static final String F_MINDERJARIGE_KINDEREN_16    = "minderjarigeKinderen16";
  public static final String F_ANDERE_OUDER_AKKOORD        = "andereOuderAkkoord";
  public static final String F_NAAM_ANDERE_OUDER_WETTELIJK = "naamAndereOuderWettelijk";
  public static final String F_TOELICHTING                 = "toelichting";
  public static final String F_INFORMATIE_JUSTIS           = "informatieJustis";

  public static final String F_DATUM_AANVRAAG = "datumAanvraag";

  public static final String F_EINDE_TERMIJN             = "eindeTermijn";
  public static final String F_BESLISSING                = "beslissing";
  public static final String F_ADVIES                    = "advies";
  public static final String F_TOELICHTING2              = "toelichting2";
  public static final String F_DATUM_BEVESTIGING         = "datumBevestiging";
  public static final String F_DATUM_KONINKLIJK_BESLUIT  = "datumKoninklijkBesluit";
  public static final String F_NUMMER_KONINKLIJK_BESLUIT = "nummerKoninklijkBesluit";

  public static final String F_CEREMONIE_1      = "ceremonie1";
  public static final String F_CEREMONIE_2      = "ceremonie2";
  public static final String F_CEREMONIE_3      = "ceremonie3";
  public static final String F_DATUM_UITREIKING = "datumUitreiking";
  public static final String F_DATUM_VERVAL     = "datumVerval";

  // Procedurekeuze

  @Field(type = LABEL, caption = "Aangever")
  private Object aangever;

  @Field(type = LABEL, caption = "Bevoegd tot indienen verzoek?")
  private Object bevoegd;

  @Field(type = LABEL, caption = "Optie mogelijk?")
  private Object optieMogelijk;

  // Optie / naturalisatie

  @Field(type = LABEL, caption = "Basis voor verzoek?")
  private Object basisVerzoek;

  @Field(type = LABEL, caption = "Kinderen mee-naturaliseren")
  private Object kinderenMeeNaturaliseren;

  @Field(type = LABEL, caption = "Vertegenwoordiger")
  private Object vertegenwoordiger;

  // Toetsing aan voorwaarden

  @Field(type = LABEL, caption = "Verklaring verblijf en gedrag ondertekend")
  private Object verklaringVerblijf;

  @Field(type = LABEL, caption = "Bereid tot afleggen verklaring van verbondenheid")
  private Object bereidAfleggenVerklaring;

  @Field(type = LABEL, caption = "Betrokkene bekend met betaling/ontheffing/vrijstelling")
  private Object betrokkeneBekendMetBetaling;

  @Field(type = LABEL, caption = "Bereid tot doen van afstand huidige nationaliteit")
  private Object bereidAfstandNationaliteit;

  @Field(type = LABEL, caption = "Bewijs van identiteit aanwezig")
  private Object bewijsVanIdentiteit;

  @Field(type = LABEL, caption = "Bewijs van nationaliteit aanwezig")
  private Object bewijsVanNationaliteit;

  @Field(type = LABEL, caption = "Bewijsnood aangetoond")
  private Object bewijsnoodAangetoond;

  @Field(type = LABEL, caption = "Geldige verblijfsvergunning aanwezig")
  private Object geldigeVerblijfsvergunning;

  // Naamsvaststelling/wijziging

  @Field(type = LABEL, caption = "Naamsvaststelling of wijziging nodig")
  private Object naamsvaststellingOfWijziging;

  @Field(type = LABEL, caption = "Geslachtsnaam vastgesteld als")
  private Object geslachtsnaamVastgesteld;

  @Field(type = LABEL, caption = "Voorna(a)m(en) vastgesteld als")
  private Object voornamenVastgesteld;

  @Field(type = LABEL, caption = "Geslachtsnaam gewijzigd in")
  private Object geslachtsnaamGewijzigd;

  // Behandeling - aanvullingen n.a.v. aanvraag

  @Field(type = LABEL, caption = "Bericht Omtrent Toelating opgevraagd")
  private Object berichtOmtrentToelating;

  @Field(type = LABEL, caption = "Minderjarige kinderen (12 t/m 15 jr.) akkoord")
  private Object minderjarigeKinderen12;

  @Field(type = LABEL, caption = "Minderjarige kinderen (16+) akkoord")
  private Object minderjarigeKinderen16;

  @Field(type = LABEL, caption = "Andere ouder/wettelijk vertegenwoordiger akkoord")
  private Object andereOuderAkkoord;

  @Field(type = LABEL, caption = "Andere ouder/wettelijk vertegenwoordiger")
  private Object naamAndereOuderWettelijk;

  @Field(type = LABEL, caption = "Toelichting")
  private Object toelichting;

  @Field(type = LABEL, caption = "Informatie opgevraagd bij justitie en politie")
  private Object informatieJustis;

  // Behandeling - datum aanvraag
  @Field(type = LABEL, caption = "Datum aanvraag")
  private Object datumAanvraag;

  // Behandeling - termijn / beslissing / advies
  @Field(type = LABEL, caption = "Einde termijn")
  private Object eindeTermijn;

  @Field(type = LABEL, caption = "Beslissing (optie)")
  private Object beslissing;

  @Field(type = LABEL, caption = "Advies burgemeester (naturalisatie)")
  private Object advies;

  @Field(type = LABEL, caption = "Toelichting")
  private Object toelichting2;

  @Field(type = LABEL, caption = "Datum bevestiging")
  private Object datumBevestiging;

  @Field(type = LABEL, caption = "Datum Koninklijk Besluit")
  private Object datumKoninklijkBesluit;

  @Field(type = LABEL, caption = "Nummer Koninklijk Besluit")
  private Object nummerKoninklijkBesluit;

  // Ceremonie

  @Field(type = LABEL, caption = "Ceremonie 1")
  private Object ceremonie1;

  @Field(type = LABEL, caption = "Ceremonie 2")
  private Object ceremonie2;

  @Field(type = LABEL, caption = "Ceremonie 3")
  private Object ceremonie3;

  @Field(type = LABEL, caption = "Bevestiging / KB uitgereikt op")
  private Object datumUitreiking;

  @Field(type = LABEL, caption = "Vervaldatum")
  private Object datumVerval;
}
