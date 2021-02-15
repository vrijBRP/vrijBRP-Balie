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

package nl.procura.gba.web.services.bs.riskanalysis;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static nl.procura.gba.jpa.personen.types.RiskProfileRelatedCaseType.*;
import static nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisServiceTest.givenRelocationDossier;
import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import nl.procura.gba.jpa.personen.db.RiskProfileZaakType;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;

public class ProfileMatcherTest {

  @Test
  public void match() {
    VerhuisAanvraag zaak = givenRelocationDossier();
    assertTrue(ProfileMatcher.match(zaak, singleton(BINNENGEMEENTELIJK)));
    assertTrue(ProfileMatcher.match(zaak, new HashSet<>(asList(BINNENGEMEENTELIJK, HERVESTIGING))));
    assertFalse(ProfileMatcher.match(zaak, singleton(INTERGEMEENTELIJK)));
    assertFalse(ProfileMatcher.match(zaak, emptyList()));
    assertFalse(ProfileMatcher.match(zaak, new HashSet<>(asList(INTERGEMEENTELIJK, HERVESTIGING, INSCHRIJVING))));
  }

  @Test
  public void mustContainAllZaakTypes() {
    RiskProfileZaakType[] expected = RiskProfileZaakType.values();
    ProfileMatcher[] actual = ProfileMatcher.values();
    assertEquals(expected.length, actual.length);
    for (RiskProfileZaakType type : expected) {
      assertTrue(String.format("Expected RiskProfileZaakType %s in ProfileMatcher", type), containsType(actual, type));
    }
  }

  private static boolean containsType(ProfileMatcher[] matchers, RiskProfileZaakType type) {
    for (ProfileMatcher matcher : matchers) {
      if (matcher.type().type() == type) {
        return true;
      }
    }
    return false;
  }
}
