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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.Period;
import java.util.function.Predicate;

/**
 * Filters which can be applied to a stream of RuleAddress
 */
public class RelocationFilters {

  /**
   * Filter by period
   */
  public static Predicate<? super RuleAddress> byPeriod(Period period) {
    return (Predicate<RuleAddress>) address -> {
      long dIn = Long.valueOf(now().minus(period).format(ofPattern("yyyyMMdd")));
      return address.getDIn() >= dIn;
    };
  }

  /**
   * Filter the stream by countrycodes
   */
  public static Predicate<? super RuleAddress> byCountryCode(int countryCode) {
    return (Predicate<RuleAddress>) address -> address.getCountryCode() == countryCode;
  }

  /**
   * Is this the relocation case?
   */
  public static Predicate<? super RuleAddress> isCaseAddress(boolean isTrue) {
    return (Predicate<RuleAddress>) address -> isTrue == address.isCaseAddress();
  }
}
