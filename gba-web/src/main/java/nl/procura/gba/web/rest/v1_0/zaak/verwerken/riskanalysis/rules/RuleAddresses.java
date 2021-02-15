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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Composite class for the RiskProfileRules
 */
public class RuleAddresses {

  private List<RuleAddress> addresses = new ArrayList<>();

  public RuleAddresses sortDescending() {
    addresses.sort(new RuleAddressesComparator(true));
    return this;
  }

  public RuleAddress add(RuleAddress address) {
    addresses.add(address);
    return address;
  }

  /**
   * Returns the RuleAddres of the case
   * @return
   */
  public Stream<RuleAddress> stream() {
    return addresses.stream();
  }

  /**
   * Compare the address based on startDate
   */
  private class RuleAddressesComparator implements Comparator<RuleAddress> {

    private boolean descending; // New To Old (defaults to TRUE)

    public RuleAddressesComparator(boolean descending) {
      this.descending = descending;
    }

    @Override
    public int compare(RuleAddress o1, RuleAddress o2) {
      if (descending) {
        return new CompareToBuilder().append(o2.getDIn(), o1.getDIn()).build();
      } else {
        return new CompareToBuilder().append(o1.getDIn(), o2.getDIn()).build();
      }
    }
  }
}
