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

package nl.procura.gba.web.services.bs.naturalisatie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class NaturalisatieDossierNummerTest {

  @Test
  public void mustReturnCorrectDossierNummer() {

    String yearDigit = String.valueOf(LocalDate.now().getYear()).substring(3);

    NaturalisatieDossierNummer n1 = NaturalisatieDossierNummer.ofValue("268." + yearDigit + ".12345");
    assertEquals("268." + yearDigit + ".12345", n1.getValue());
    assertFalse(n1.isValid());

    NaturalisatieDossierNummer n2 = NaturalisatieDossierNummer.ofValue("0268." + yearDigit + ".00005");
    assertEquals("0268." + yearDigit + ".00005", n2.getValue());
    assertEquals("0268." + yearDigit + ".00006", n2.getNextNumber().getValue());
    assertTrue(n2.isValid());

    NaturalisatieDossierNummer n4 = NaturalisatieDossierNummer.ofValue("");
    assertEquals("", n4.getValue());
    assertFalse(n4.isValid());

    NaturalisatieDossierNummer n5 = NaturalisatieDossierNummer.ofGemeente(268);
    assertEquals("0268." + yearDigit + ".00001", n5.getValue());
    assertTrue(n5.isValid());
    assertEquals("0268." + yearDigit + ".00002", n5.getNextNumber().getValue());

    NaturalisatieDossierNummer n6 = NaturalisatieDossierNummer.ofGemeente(1234);
    assertEquals("1234." + yearDigit + ".00001", n6.getValue());
    assertTrue(n6.isValid());
    assertEquals("1234." + yearDigit + ".00002", n6.getNextNumber().getValue());
    assertEquals("1234." + yearDigit + ".00003", n6.getNextNumber().getNextNumber().getValue());
  }
}
