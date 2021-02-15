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

import static nl.procura.gba.jpa.personen.types.RiskProfileRuleVar.X;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleVar.Y;
import static nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.RelocationFilters.byPeriod;

import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;

/**
 * Rule 2
 */
public class Rule2Processor extends AbstractRuleProcessor {

  public Rule2Processor() {
    super(RiskProfileRuleType.RULE_2);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    int reps = rule.getIntAttribute(X.name()); // Number of occurrences
    int days = rule.getIntAttribute(Y.name()); // Number of days

    BasePLExt pl = getUtils().getPL(subject);
    RuleAddresses addresses = getUtils().getAddresses(relatedCase, pl);

    List<RuleAddress> filteredList = addresses.stream()
        .filter(byPeriod(Period.ofDays(days)))
        .collect(Collectors.toList());

    info("{0} keer verhuist in de laatste {1} dagen", filteredList.size(),
        String.valueOf(days));
    return filteredList.size() >= reps;
  }
}
