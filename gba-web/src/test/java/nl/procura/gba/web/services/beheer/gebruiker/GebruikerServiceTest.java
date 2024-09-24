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

package nl.procura.gba.web.services.beheer.gebruiker;

import static nl.procura.gba.web.services.interfaces.GeldigheidStatus.ACTUEEL;
import static nl.procura.gba.web.services.interfaces.GeldigheidStatus.BEEINDIGD;
import static nl.procura.gba.web.services.interfaces.GeldigheidStatus.NOG_NIET_ACTUEEL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.procura.gba.common.DateTime;
import nl.procura.standard.ProcuraDate;

public class GebruikerServiceTest {

  private GebruikerService service;

  @Before
  public void setUp() {
    service = new GebruikerService();
  }

  @Test
  public void generateWachtwoordMustReturnNumbersAndLowercaseLetters() {
    // when
    String password = service.generateWachtwoord();
    // then
    assertEquals(6, password.length());
    assertTrue(password.matches(".*[0-9].*"));
    assertTrue(password.matches(".*[a-z].*"));
  }

  // Actueel
  @Test
  public void gebruikerMustBeActive1() {
    // given
    Gebruiker gebruiker = new Gebruiker();
    // when
    gebruiker.setDatumIngang(new DateTime(-1));
    gebruiker.setDatumEinde(new DateTime(-1));
    // then
    assertTrue(gebruiker.getGeldigheidStatus().is(ACTUEEL));
    assertFalse(gebruiker.getGeldigheidStatus().is(BEEINDIGD));
    assertFalse(gebruiker.getGeldigheidStatus().is(NOG_NIET_ACTUEEL));
  }

  // Beëindigd
  @Test
  public void gebruikerMustBeActive2() {
    // given
    Gebruiker gebruiker = new Gebruiker();
    // when
    gebruiker.setDatumIngang(new DateTime(-1));
    gebruiker.setDatumEinde(new DateTime(new ProcuraDate().getSystemDate()));
    // then
    assertFalse(gebruiker.getGeldigheidStatus().is(ACTUEEL));
    assertTrue(gebruiker.getGeldigheidStatus().is(BEEINDIGD));
    assertFalse(gebruiker.getGeldigheidStatus().is(NOG_NIET_ACTUEEL));
  }

  // Nog niet actueel
  @Test
  public void gebruikerMustBeActive3() {
    // given
    Gebruiker gebruiker = new Gebruiker();
    // when
    gebruiker.setDatumIngang(new DateTime(new ProcuraDate().addDays(1).getSystemDate()));
    gebruiker.setDatumEinde(new DateTime(-1));
    // then
    assertFalse(gebruiker.getGeldigheidStatus().is(ACTUEEL));
    assertFalse(gebruiker.getGeldigheidStatus().is(BEEINDIGD));
    assertTrue(gebruiker.getGeldigheidStatus().is(NOG_NIET_ACTUEEL));
    assertTrue(gebruiker.getGeldigheidStatus().is(NOG_NIET_ACTUEEL, BEEINDIGD));
  }

  // Nog niet actueel
  @Test
  public void gebruikerMustBeActive4() {
    // given
    Gebruiker gebruiker = new Gebruiker();
    // when
    gebruiker.setDatumIngang(new DateTime(new ProcuraDate().addDays(1).getSystemDate()));
    gebruiker.setDatumEinde(new DateTime(new ProcuraDate().addDays(2).getSystemDate()));
    // then
    assertFalse(gebruiker.getGeldigheidStatus().is(ACTUEEL));
    assertFalse(gebruiker.getGeldigheidStatus().is(BEEINDIGD));
    assertTrue(gebruiker.getGeldigheidStatus().is(NOG_NIET_ACTUEEL));
    assertTrue(gebruiker.getGeldigheidStatus().is(NOG_NIET_ACTUEEL, BEEINDIGD));
  }
}
