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

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;

/**
 * Rule 4
 */
public class Rule4Processor extends AbstractRuleProcessor {

  public Rule4Processor() {
    super(RiskProfileRuleType.RULE_4);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    BasePLExt pl = getUtils().getPL(subject);
    RuleAddresses addresses = getUtils().getAddresses(relatedCase, pl);

    // The relocation address
    RuleAddress caseAddress = addresses.stream()
        .filter(RelocationFilters.isCaseAddress(true))
        .findFirst().get();

    // The last address before the relocation
    RuleAddress prevAddress = addresses.stream()
        .filter(RelocationFilters.isCaseAddress(false))
        .findFirst().orElse(null);

    if (caseAddress == null || prevAddress == null) {
      // If any of the two address is null no comparison needed
      return false;
    }

    FunctieAdres caseUsage = caseAddress.getAddressUsage();
    FunctieAdres prevUsage = prevAddress.getAddressUsage();
    boolean isAddressUsageChanged = (caseUsage != prevUsage);

    if (isAddressUsageChanged) {
      info("Functie gewijzigd van {0} naar {1}", prevUsage.getOms(), caseUsage.getOms());
    } else {
      info("Functie adres blijft {0}", caseUsage.getOms());
    }
    return isAddressUsageChanged;
  }
}
