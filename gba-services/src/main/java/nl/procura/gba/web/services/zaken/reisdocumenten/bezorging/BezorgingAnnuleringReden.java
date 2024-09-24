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

package nl.procura.gba.web.services.zaken.reisdocumenten.bezorging;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BezorgingAnnuleringReden {

  DEUR_DICHT("1.1", "Deur dicht: klant niet aanwezig"),
  DEUR_OPEN("1.2", "Deur open: klant niet aanwezig"),
  VERZET_AFSRPK("1.3", "Klant verzet afspraak terwijl deze is ingepland"),
  REMBOURS("2", "ivm rembours"),
  ADRES_ONJUIST("3.1", "Adresgegevens onjuist"),
  ADRES_NIET_VIND("3.2", "Adres niet te vinden"),
  VERK_BEST("4", "Verkeerde bestelling / onjuiste hardware"),
  UITGEVOERD("5", "Reeds uitgevoerd"),
  GEEN_CONTACT("6", "Geen contact met klant"),
  TEL_NR_ONJUIST("7", "Onjuist telefoonnummer"),
  OPDRACHTGVR("8", "In opdracht van opdrachtgever"),
  OVERIG("9", "Overig"),
  FOUT_AMP("10", "Fout AMP"),
  BEST_NIET_AANW("11", "Bestelling/aanvraag is niet aanwezig/ingepakt"),
  NIET_OP_VOORR("13", "Product niet op voorraad"),
  NEPBEST("14", "Nepbestelling / Testorder"),
  PARKEER("15", "Parkeerstand"),
  AFSPR_NIET_UITV("16", "Afspraak niet kunnen uitvoeren op afgesproken dag"),
  GEEN_RET_ART("20", "Geen retourartikel"),
  IMEI_INCORR("21", "Imei/serienummer onjuist"),
  PROBL_VERH("22", "Probleem verholpen"),
  SCHADE_NIET_ACC("23", "Vanwege schade niet geaccpteerd"),
  KLANT_NIETS_WETEN("25", "Klant weet van niets"),
  KLANT_ZELF_OP("29", "Klant stuurt het product zelf op"),
  AANVR_NIET_AFH("30", "Aanvraag kan niet worden afgehandeld omdat andere bestuurders niet kunnen/ willen tekenen"),
  GEEN_INTERESSE("31", "Klant heeft geen interesse"),
  PER_ONGELUK("32", "Klant heeft per ongeluk besteld"),
  DUBBEL_BEST("33", "Klant heeft dubbel besteld"),
  REEDS_GEANNULEERD("34", "Reeds geannuleerd bij opdrachtgever"),
  NUMMERBEHOUD_ONMOGELIJK("35", "Nummerbehoud niet mogelijk"),
  TE_LANG("36", "Te lang geduurd"),
  VERKORT_TIJDVAK_WEIGERING("37", "Klant wil een verkort tijdvak maar wil er niet voor betalen"),
  GEEN_IN_ONTVANGST("38", "Klant ziet geen mogelijkheid tot persoonlijk in ontvangst nemen"),
  VOLDOENDE_MEDICATIE("39", "Klant heeft nog voldoende medicatie"),
  CONTRACT_ABSENT("40", "Contract niet aanwezig"),
  ID_ABSENT("41", "Identiteitsbewijs niet aanwezig"),
  BANKPAS_ABSENT("42", "Bankpas niet aanwezig"),
  INITIALEN_ONJUIST("44.0", "Initialen staan niet juist op de bankpas"),
  INITIALEN_VERKEERD("44.1", "Initialen verkeerd ingevuld"),
  ACHTERNAAM_VERKEERD("44.2", "Achternaam verkeerd ingevuld"),
  GEBOORTEDATUM_VERKEERD("44.4", "Geboortedatum verkeerd ingevuld"),
  ID_TYPE_VERKEERD("44.5", "Type identiteitsbewijs verkeerd ingevuld"),
  DOCNUMMER_VERKEERD("44.6", "Documentnummer identitetisbewijs verkeerd ingevuld"),
  VERVALDATUM_VERKEERD("44.7", "Vervaldatum identiteitsbewijs verkeerd ingevuld"),
  REKENING_TYPEFOUT("44.8", "Rekeningnummer bevat typefout(en)"),
  REKENING_VERKEERD("44.9", "Verkeerd rekeningnummer ingevuld"),
  ID_VERLOPEN("45", "Identiteitsbewijs is niet meer geldig"),
  MACHTIGING_ABSENT("46", "Machtiging niet aanwezig of niet correct"),
  FRAUDE_VERMOEDEN("47", "Vermoeden fraude"),
  VOORLICHTING_ONJUIST("48", "Onjuist voorgelicht door opdrachtgever"),
  CONTRACTANT_MINDERJARIG("50", "Contractant < 18 jaar"),
  PINTERMINAL_STORING("51", "Storing pinterminal"),
  PIN_GEEN_SALDO("53", "Pin foutcode 51, 61, 77: geen saldo"),
  KVK_UITTREKSEL_ABSENT("55", "KvK uittreksel niet aanwezig of niet correct"),
  KVK_NUMMER_ABSENT("55.1", "KvK nummer niet gevonden"),
  BEDRIJFSNAAM_MISMATCH("55.2", "Bedrijfsnaam komt niet overeen met uittreksel"),
  TEKENBEVOEGD_ABSENT("55.3", "Contractant staat niet vermeld als tekenbevoegd op uittreksel"),
  ZAKELIJKE_REKENING("57", "Klant heeft een zakelijke rekening opgegeven"),
  GEGEVENS_WEIGERING("59", "Klant wil niet dat er gegevens worden vastgelegd"),
  MEER_DAN_1_PENWIJZIGING("61", "Meer dan 1 penwijziging niet toegestaan"),
  GEEN_VERPLICHT_PIN("62", "Klant wil geen verplicht pintransactie"),
  GEEN_GELDIG_ID("63", "Geen toegestaan identiteitsbewijs"),
  BETERE_AANBIEDING("64", "Betere aanbieding bij concurrent"),
  ANDER_PRODUCT("65", "Ander product bij opdrachtgever gekozen"),
  CREDITCHECK_NG("72", "Creditcheck afgekeurd"),
  ID_READER_FAIL("73", "ID reader keurt ID af"),
  NUMMERBEHOUD_NG("74", "Nummerbehoud afgewezen"),
  BESCHADIGD("76", "Beschadigd product"),
  VERKEERDE_PERSOON("77", "Uitgegeven aan verkeerd persoon"),
  VERMIST("78", "Vermist - onderzoek AMP"),
  GESTOLEN("79", "Pas gestolen"),
  ONBEKEND("", "Onbekend");

  private final String code;
  private final String oms;

  public static String getOms(String code, String oms) {
    return defaultIfBlank(oms, get(code).getOms());
  }

  public static BezorgingAnnuleringReden get(String code) {
    return Arrays.stream(values())
        .filter(type -> type.getCode().equals(code))
        .findFirst()
        .orElse(ONBEKEND);
  }
}
