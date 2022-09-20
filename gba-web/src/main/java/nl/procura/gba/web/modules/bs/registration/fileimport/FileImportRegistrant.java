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

import com.vaadin.ui.Label;
import lombok.Getter;
import nl.procura.commons.misc.formats.naam.Naam;
import nl.procura.commons.misc.formats.naam.NaamBuilder;
import nl.procura.standard.ProcuraDate;

import lombok.Builder;

@Getter
@Builder
public class FileImportRegistrant {

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
