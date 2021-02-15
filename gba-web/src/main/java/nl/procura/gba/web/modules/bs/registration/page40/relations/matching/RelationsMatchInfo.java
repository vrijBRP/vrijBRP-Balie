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

package nl.procura.gba.web.modules.bs.registration.page40.relations.matching;

import java.util.Arrays;
import java.util.List;

import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RelationsMatchInfo {

  private boolean              applicable   = true;
  private String               label        = "";
  private RelationsMatchRecord firstName    = new RelationsMatchRecord(true, "Voornamen");
  private RelationsMatchRecord lastName     = new RelationsMatchRecord(true, "Geslachtsnaam");
  private RelationsMatchRecord prefix       = new RelationsMatchRecord(true, "Voorvoegsel");
  private RelationsMatchRecord title        = new RelationsMatchRecord(true, "Titel/predikaat");
  private RelationsMatchRecord birthdate    = new RelationsMatchRecord(false, "Geboortedatum");
  private RelationsMatchRecord birthplace   = new RelationsMatchRecord(false, "Geboorteplaats");
  private RelationsMatchRecord birthcountry = new RelationsMatchRecord(false, "Geboorteland");

  public RelationsMatchInfo() {
  }

  public RelationsMatchInfo(DossierPersoon person, DossierPersoon rec) {

    Naamformats personName = person.getNaam();
    Geboorteformats personBirth = person.getGeboorte();

    Naamformats relatedName = rec.getNaam();
    Geboorteformats relatedBirth = rec.getGeboorte();

    firstName.set(personName.getVoornamen(), relatedName.getVoornamen());
    lastName.set(personName.getGeslachtsnaam(), relatedName.getGeslachtsnaam());
    prefix.set(personName.getVoorvoegsel(), relatedName.getVoorvoegsel());
    title.set(personName.getTitel(), relatedName.getTitel());
    birthdate.set(personBirth.getGeboortedatum(), relatedBirth.getGeboortedatum());
    birthplace.set(personBirth.getGeboorteplaats(), relatedBirth.getGeboorteplaats());
    birthcountry.set(personBirth.getGeboorteland(), relatedBirth.getGeboorteland());

    String nameLabel = relatedName.getNaam_naamgebruik_eerste_voornaam();
    if (isMatch()) {
      label = nameLabel + " - volledige match met de inschrijver";
    } else {
      label = nameLabel + " - geen match met de inschrijver";
    }
  }

  public List<RelationsMatchRecord> getRecords() {
    return Arrays.asList(firstName, lastName, prefix, title, birthdate, birthplace, birthcountry);
  }

  public boolean isMatch() {
    return getRecords().stream()
        .allMatch(RelationsMatchRecord::isMatch);
  }

  @Override
  public String toString() {
    return label;
  }
}
