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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.types.RiskProfileRelatedCaseType;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;

public enum ProfileMatcher {

  BINNENGEMEENTELIJK(RiskProfileRelatedCaseType.BINNENGEMEENTELIJK, ProfileMatcher::binnengemeentelijk),
  INTERGEMEENTELIJK(RiskProfileRelatedCaseType.INTERGEMEENTELIJK, ProfileMatcher::intergemeentelijk),
  HERVESTIGING(RiskProfileRelatedCaseType.HERVESTIGING, ProfileMatcher::hervestiging),
  INSCHRIJVING(RiskProfileRelatedCaseType.INSCHRIJVING, ProfileMatcher::inschrijving);

  private final RiskProfileRelatedCaseType type;
  private final Function<Zaak, Boolean>    matchZaak;

  ProfileMatcher(RiskProfileRelatedCaseType type, Function<Zaak, Boolean> matchZaak) {
    this.type = type;
    this.matchZaak = matchZaak;
  }

  public RiskProfileRelatedCaseType type() {
    return type;
  }

  public boolean match(Zaak zaak) {
    return matchZaak.apply(zaak);
  }

  public static boolean match(Zaak zaak, Collection<RiskProfileRelatedCaseType> types) {
    Set<ProfileMatcher> matchers = getMatchers(types);
    return matchers.stream()
        .anyMatch(matcher -> matcher.match(zaak));
  }

  private static Set<ProfileMatcher> getMatchers(Collection<RiskProfileRelatedCaseType> types) {
    Set<ProfileMatcher> matchers = new HashSet<>();
    for (ProfileMatcher matcher : values()) {
      if (types.contains(matcher.type)) {
        matchers.add(matcher);
      }
    }
    return matchers;
  }

  private static Boolean binnengemeentelijk(Zaak zaak) {
    return isVerhuisType(zaak, VerhuisType.BINNENGEMEENTELIJK);
  }

  private static Boolean intergemeentelijk(Zaak zaak) {
    return isVerhuisType(zaak, VerhuisType.INTERGEMEENTELIJK);
  }

  private static Boolean inschrijving(Zaak zaak) {
    return zaak.getType().is(ZaakType.REGISTRATION);
  }

  private static Boolean hervestiging(Zaak zaak) {
    return isVerhuisType(zaak, VerhuisType.HERVESTIGING);
  }

  private static Boolean isVerhuisType(Zaak zaak, VerhuisType type) {
    if (zaak.getType().is(ZaakType.VERHUIZING)) {
      VerhuisAanvraag verhuizing = (VerhuisAanvraag) zaak;
      if (verhuizing.getTypeVerhuizing().is(type)) {
        return Boolean.TRUE;
      }
    }
    return Boolean.FALSE;
  }
}
