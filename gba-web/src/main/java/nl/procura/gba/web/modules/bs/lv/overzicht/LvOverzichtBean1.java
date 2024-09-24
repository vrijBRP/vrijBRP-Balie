/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.bs.lv.overzicht;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Data
public class LvOverzichtBean1 implements Serializable {

  // Soort
  public static final String SOORT    = "soort";
  public static final String DATUM_LV = "datumLv";

  // Geboorteaktegegevens
  public static final String AKTE_NUMMER            = "akteNummer";
  public static final String HUIDIG_BRP_AKTE_NUMMER = "huidigBrpAkteNummer";
  public static final String NIEUW_BRP_AKTE_NUMMER  = "nieuwBrpAkteNummer";
  public static final String AKTE_JAAR              = "akteJaar";
  public static final String AKTE_PLAATS            = "aktePlaats";

  @Field(type = FieldType.LABEL, caption = "Soort verbintenis")
  private Object soortVerbintenis = null;

  @Field(type = FieldType.LABEL, caption = "Soort latere vermelding")
  private Object soort = null;

  @Field(type = FieldType.LABEL, caption = "Datum latere vermelding")
  private Object datumLv = null;

  @Field(type = FieldType.LABEL, caption = "Aktenummer")
  private Object akteNummer = "";

  @Field(type = FieldType.LABEL, caption = "Huidig BRP-aktenummer")
  private Object huidigBrpAkteNummer = "";

  @Field(type = FieldType.LABEL, caption = "Nieuw BRP-aktenummer")
  private Object nieuwBrpAkteNummer = "";

  @Field(type = FieldType.LABEL, caption = "Jaar")
  private Object akteJaar = "";

  @Field(type = FieldType.LABEL, caption = "Aktegemeente")
  private Object aktePlaats = null;

  @Field(type = FieldType.LABEL, caption = "Uitspraak door")
  private Object uitspraak = null;

  @Field(type = FieldType.LABEL, caption = "Datum uitspraak")
  private Object datumUitspraak = null;

  @Field(type = FieldType.LABEL, caption = "Datum kracht van gewijsde")
  private Object datumGewijsde = null;

  @Field(type = FieldType.LABEL, caption = "Datum verzoekschrift")
  private Object datumVerzoekschrift = null;

  @Field(type = FieldType.LABEL, caption = "Document")
  private Object document = null;

  @Field(type = FieldType.LABEL, caption = "Nummer")
  private Object nummer = null;

  @Field(type = FieldType.LABEL, caption = "Datum")
  private Object datum = null;

  @Field(type = FieldType.LABEL, caption = "Plaats")
  private Object plaats = null;

  @Field(type = FieldType.LABEL, caption = "Door")
  private Object door = null;

  @Field(type = FieldType.LABEL, caption = "Tweede document")
  private Object tweedeDocument = false;

  @Field(type = FieldType.LABEL, caption = "Omschrijving document")
  private Object tweedeDocumentOms = null;

  @Field(type = FieldType.LABEL, caption = "Datum document")
  private Object tweedeDocumentDatum = null;

  @Field(type = FieldType.LABEL, caption = "Plaats document")
  private Object tweedeDocumentPlaats = null;

  @Field(type = FieldType.LABEL, caption = "Betreft deze ouder")
  private Object betreftOuder = null;

  @Field(type = FieldType.LABEL, caption = "Ouderschap ontkend van")
  private Object ouderschapOntkend = null;

  @Field(type = FieldType.LABEL, caption = "Ouderschap vastgesteld van")
  private Object ouderschapVastgesteld = null;

  @Field(type = FieldType.LABEL, caption = "Erkenning gedaan door")
  private Object erkenningDoor = null;

  @Field(type = FieldType.LABEL, caption = "Toestemming gegeven door")
  private Object toestemmingDoor = null;

  @Field(type = FieldType.LABEL, caption = "Toegepast recht van")
  private Object toegepastRecht = null;

  @Field(type = FieldType.LABEL, caption = "Gezag")
  private Object gezag = null;

  @Field(type = FieldType.LABEL, caption = "Geslachtsnaam ouder vastgesteld als")
  private Object geslOuderVastgesteld = null;

  @Field(type = FieldType.LABEL, caption = "Geslachtsnaam ouder gewijzigd in")
  private Object geslOuderGewijzigd = null;

  @Field(type = FieldType.LABEL, caption = "Voornamen ouder vastgesteld als")
  private Object voornamenOuderVastgesteldAls = null;

  @Field(type = FieldType.LABEL, caption = "Keuze of vaststelling geslachtnaam kind")
  private Object keuzeGeslachtsnaam = null;

  @Field(type = FieldType.LABEL, caption = "Geslachtsnaam kind is")
  private Object geslachtsnaamIs = null;

  @Field(type = FieldType.LABEL, caption = "Geslachtsnaam vastgesteld als")
  private Object geslachtsnaamVastgesteldAls = null;

  @Field(type = FieldType.LABEL, caption = "Geslachtsnaam gewijzigd in")
  private Object geslachtsnaamGewijzigdIn = null;

  @Field(type = FieldType.LABEL, caption = "Geslachtsnaam kind gewijzigd in")
  private Object geslachtsnaamKindGewijzigdIn = null;

  @Field(type = FieldType.LABEL, caption = "Voornamen vastgesteld als")
  private Object voornamenVastgesteldAls = null;

  @Field(type = FieldType.LABEL, caption = "Voornamen gewijzigd in")
  private Object voornamenGewijzigdIn = null;

  @Field(type = FieldType.LABEL, caption = "Geslacht gewijzigd in")
  private Object geslachtsaand = null;

  @Field(type = FieldType.LABEL, caption = "Gekozen recht")
  private Object gekozenRecht = null;

  @Field(type = FieldType.LABEL, caption = "Dag van wijziging")
  private Object dagVanWijziging = null;

  @Field(type = FieldType.LABEL, caption = "Familierechtelijke betrekking met deze ouder blijft in stand")
  private Object famRecht = null;

  @Field(type = FieldType.LABEL, caption = "Wat verbeterd is")
  private Object verbeterd = null;

  @Field(type = FieldType.LABEL, caption = "Adoptiefouders")
  private Object adoptiefouders = null;

  @Field(type = FieldType.LABEL, caption = "Ouders")
  private Object ouders = null;

  public LvOverzichtBean1() {
  }
}
