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

package nl.procura.gba.web.services.bs.algemeen.persoon;

import static nl.procura.gba.common.DateTime.TimeType.TIME_4_DIGITS;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;

public final class DossierPersoonMock {

  private DossierPersoonMock() {
  }

  public static DossierPersoon childBelow16() {
    DossierPersoon person = personWithAge(1);
    person.setVoornaam("Child");
    person.setGeslachtsnaam("Familyname");

    return person;
  }

  public static DossierPersoon adult(String firstName) {
    DossierPersoon person = personWithAge(24);
    person.setVoornaam(firstName);
    person.setGeslachtsnaam("Familyname");

    return person;
  }

  private static DossierPersoon personWithAge(int age) {
    DossierPersoon person = new DossierPersoon();
    LocalDateTime ageOne = LocalDateTime.now().minus(age, ChronoUnit.YEARS);
    int longDate = ageOne.get(ChronoField.YEAR) * 10000 + ageOne.get(ChronoField.MONTH_OF_YEAR) * 100
        + ageOne.get(ChronoField.DAY_OF_MONTH);
    person.setDatumGeboorte(new GbaDateFieldValue(new DateTime(longDate)));
    int longTime = ageOne.get(ChronoField.HOUR_OF_DAY) * 100 + ageOne.get(ChronoField.MINUTE_OF_HOUR);
    person.setTijdGeboorte(new DateTime(0, longTime, TIME_4_DIGITS));

    return person;
  }
}
