/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.registration.fileimport;

import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.ACHTERNAAM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.EMAIL;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.GEBOORTEDATUM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.GEBOORTELAND;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.GEBOORTEPLAATS;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.GESLACHT;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.HUISLETTER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.HUISNUMMER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.LAND_VAN_VORIG_ADRES;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.NATIONALITEIT;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.POSTCODE;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.STRAATNAAM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.TELEFOON;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.TOEVOEGING;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.VOORNAMEN;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.VOORVOEGSEL;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.WOONPLAATS;
import static nl.procura.standard.ProcuraDate.SYSTEMDATE_ONLY;

import nl.procura.commons.misc.formats.naam.Naam;
import nl.procura.commons.misc.formats.naam.NaamBuilder;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.standard.ProcuraDate;

import lombok.Getter;

@Getter
public class FileImportRegistrant {

  private FileImportType   type;
  private FileImportRecord record;

  public static FileImportRegistrant of(FileImportType type, FileImportRecord record) {
    FileImportRegistrant registrant = new FileImportRegistrant();
    registrant.type = type;
    registrant.record = record;
    registrant.lastname = record.getValue(ACHTERNAAM);
    registrant.prefix = record.getValue(VOORVOEGSEL);
    registrant.firstname = record.getValue(VOORNAMEN);
    registrant.gender = record.getValue(GESLACHT);
    registrant.birthDate = new ProcuraDate(record.getValue(GEBOORTEDATUM))
        .setForceFormatType(SYSTEMDATE_ONLY)
        .setAllowedFormatExceptions(true);
    registrant.birthPlace = record.getValue(GEBOORTEPLAATS);
    registrant.birthCountry = record.getValue(GEBOORTELAND);
    registrant.street = record.getValue(STRAATNAAM);
    registrant.postalcode = record.getValue(POSTCODE);
    registrant.hnr = record.getValue(HUISNUMMER);
    registrant.hnrL = record.getValue(HUISLETTER);
    registrant.hnrT = record.getValue(TOEVOEGING);
    registrant.place = record.getValue(WOONPLAATS);
    registrant.nationality = record.getValue(NATIONALITEIT);
    registrant.email = record.getValue(EMAIL);
    registrant.tel = record.getValue(TELEFOON);
    registrant.previousCountry = record.getValue(LAND_VAN_VORIG_ADRES);

    return registrant;
  }

  private String      lastname;
  private String      prefix;
  private String      firstname;
  private String      gender;
  private ProcuraDate birthDate;
  private String      birthPlace;
  private String      birthCountry;
  private String      nationality;
  private String      street;
  private String      postalcode;
  private String      hnr;
  private String      hnrL;
  private String      hnrT;
  private String      place;
  private String      email;
  private String      tel;
  private String      previousCountry;

  public String getSummary() {
    Naam naam = new NaamBuilder()
        .setGeslachtsnaam(getLastname())
        .setVoorvoegsel(getPrefix())
        .setVoornamen(getFirstname())
        .createNaam();
    return String.format("Gekozen uit bestand: %s %s, geboren op %s in %s",
        getFirstname(),
        naam.getNaamNaarNaamgebruik(),
        getBirthDate().getFormatDate(),
        getBirthCountry());
  }
}
