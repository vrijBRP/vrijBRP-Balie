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

package nl.procura.gba.web.services.bs.algemeen.functies;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter.filter;
import static nl.procura.gba.web.services.gba.functies.Geslacht.MAN;
import static nl.procura.gba.web.services.gba.functies.Geslacht.VROUW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoonFilter;
import nl.procura.gba.web.services.gba.functies.Geslacht;

public class BsPersonenHandlerTest {

  @Test
  public void getPersonByDbIndexMustReturnTypesInDbOrder() {
    // given
    BsPersonenHandler people = new BsPersonenHandler();
    people.addPersoon(null, newPerson(5L, "A", null, PARTNER1));
    people.addPersoon(null, newPerson(1L, "B", null, PARTNER2));
    people.addPersoon(null, newPerson(4L, "C", null, AMBTENAAR));
    people.addPersoon(null, newPerson(3L, "D", null, AMBTENAAR));
    people.addPersoon(null, newPerson(6L, "E", null, AMBTENAAR));
    // when, then
    assertEquals(Long.valueOf(3), people.getPersoon(filter(0, AMBTENAAR)).getCDossPers());
    assertEquals(Long.valueOf(4), people.getPersoon(filter(1, AMBTENAAR)).getCDossPers());
    assertEquals(Long.valueOf(6), people.getPersoon(filter(2, AMBTENAAR)).getCDossPers());
    assertEquals(Long.valueOf(5), people.getPersoon(filter(0, PARTNER1)).getCDossPers());
    assertEquals(Long.valueOf(1), people.getPersoon(filter(0, PARTNER2)).getCDossPers());
    assertNull(people.getPersoon(0, OUDER));
  }

  @Test
  public void getPersonByDbIndexMustReturnTypesNotInDbOrder() {
    // given
    BsPersonenHandler people = new BsPersonenHandler();
    people.addPersoon(null, newPerson(null, "A", null, PARTNER1));
    people.addPersoon(null, newPerson(null, "B", null, PARTNER2));
    people.addPersoon(null, newPerson(null, "C", null, AMBTENAAR));
    people.addPersoon(null, newPerson(null, "D", null, AMBTENAAR));
    people.addPersoon(null, newPerson(null, "E", null, AMBTENAAR));
    // when, then
    assertEquals("C", people.getPersoon(filter(0, AMBTENAAR)).getGeslachtsnaam());
    assertEquals("D", people.getPersoon(filter(1, AMBTENAAR)).getGeslachtsnaam());
    assertEquals("E", people.getPersoon(filter(2, AMBTENAAR)).getGeslachtsnaam());
    assertEquals("A", people.getPersoon(filter(0, PARTNER1)).getGeslachtsnaam());
    assertEquals("B", people.getPersoon(filter(0, PARTNER2)).getGeslachtsnaam());
  }

  @Test
  public void getPersonWithIndex() {
    // given
    BsPersonenHandler people = new BsPersonenHandler();
    people.addPersoon(null, new DossierPersoon(OUDER));
    people.addPersoon(null, new DossierPersoon(OUDER));
    people.addPersoon(null, newPerson(null, "C", null, OUDER));
    people.addPersoon(null, newPerson(null, "D", null, OUDER));
    people.addPersoon(null, newPerson(null, "E", null, OUDER));

    // when, then
    assertEquals("C", people.getPersoon(filter(2, OUDER)).getGeslachtsnaam());
    assertEquals("D", people.getPersoon(filter(3, OUDER)).getGeslachtsnaam());
    assertEquals("E", people.getPersoon(filter(4, OUDER)).getGeslachtsnaam());
  }

  @Test
  public void getSortingOneMan() {
    // given
    BsPersonenHandler people = new BsPersonenHandler();
    people.addPersoon(null, newPerson(null, "A", MAN, OUDER));

    // when, then
    assertEquals("A", people.getPersoon(DossierPersoonFilter.filter(0, MAN, OUDER)).getGeslachtsnaam());
    assertEquals("A", people.getPersoon(DossierPersoonFilter.filter(1, MAN, OUDER)).getGeslachtsnaam());
    assertNull(people.getPersoon(DossierPersoonFilter.filter(0, VROUW, OUDER)));
    assertNull(people.getPersoon(DossierPersoonFilter.filter(1, VROUW, OUDER)));
  }

  @Test
  public void getSortingManWoman() {
    // given
    BsPersonenHandler people = new BsPersonenHandler();
    people.addPersoon(null, newPerson(null, "A", VROUW, OUDER));
    people.addPersoon(null, newPerson(null, "B", MAN, OUDER));

    // when, then
    assertEquals("A", people.getPersoon(DossierPersoonFilter.filter(0, VROUW, OUDER)).getGeslachtsnaam());
    assertEquals("A", people.getPersoon(DossierPersoonFilter.filter(1, VROUW, OUDER)).getGeslachtsnaam());
    assertEquals("B", people.getPersoon(DossierPersoonFilter.filter(0, MAN, OUDER)).getGeslachtsnaam());
    assertEquals("B", people.getPersoon(DossierPersoonFilter.filter(1, MAN, OUDER)).getGeslachtsnaam());
    assertNull(people.getPersoon(filter(2, OUDER)));
  }

  @Test
  public void getSortingWomanMan() {
    // given
    BsPersonenHandler people = new BsPersonenHandler();
    people.addPersoon(null, newPerson(null, "A", MAN, OUDER));
    people.addPersoon(null, newPerson(null, "B", VROUW, OUDER));

    // when, then
    assertEquals("A", people.getPersoon(DossierPersoonFilter.filter(0, MAN, OUDER)).getGeslachtsnaam());
    assertEquals("A", people.getPersoon(DossierPersoonFilter.filter(1, MAN, OUDER)).getGeslachtsnaam());
    assertEquals("B", people.getPersoon(DossierPersoonFilter.filter(0, VROUW, OUDER)).getGeslachtsnaam());
    assertEquals("B", people.getPersoon(DossierPersoonFilter.filter(1, VROUW, OUDER)).getGeslachtsnaam());
    assertNull(people.getPersoon(filter(2, OUDER)));
  }

  @Test
  public void getSortingTwoWomen() {
    // given
    BsPersonenHandler people = new BsPersonenHandler();
    people.addPersoon(null, newPerson(null, "A", VROUW, OUDER));
    people.addPersoon(null, newPerson(null, "B", VROUW, OUDER));

    // when, then
    assertEquals("A", people.getPersoon(DossierPersoonFilter.filter(0, MAN, OUDER)).getGeslachtsnaam());
    assertEquals("B", people.getPersoon(DossierPersoonFilter.filter(1, MAN, OUDER)).getGeslachtsnaam());
    assertEquals("A", people.getPersoon(DossierPersoonFilter.filter(0, VROUW, OUDER)).getGeslachtsnaam());
    assertEquals("B", people.getPersoon(DossierPersoonFilter.filter(1, VROUW, OUDER)).getGeslachtsnaam());
    assertNull(people.getPersoon(DossierPersoonFilter.filter(2, VROUW, OUDER)));
    assertNull(people.getPersoon(DossierPersoonFilter.filter(3, VROUW, OUDER)));
  }

  private static DossierPersoon newPerson(Long code, String name, Geslacht gender, DossierPersoonType type) {
    DossierPersoon person = new DossierPersoon(type);
    person.setCDossPers(code);
    person.setGeslachtsnaam(name);
    person.setGeslacht(gender);
    return person;
  }
}
