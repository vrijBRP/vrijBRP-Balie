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

/**
 * Rule 15
 */
public class Rule15Processor extends AbstractRuleProcessor {

  public Rule15Processor() {
    super(RiskProfileRuleType.RULE_15);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    BasePLExt pl = getUtils().getPL(subject);
    if (pl.getGezag().staatOnderCuratele()) {
      info("Persoon staat onder curatele");
      return true;
    }
    return false;
  }
}
