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

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RISKANALYSIS_RELOCATION_IND;
import static org.apache.commons.lang3.StringUtils.isBlank;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.db.RiskProfileSig;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.services.zaken.indicaties.IndicatiesService;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;

/**
 * Rule 1
 */
public class Rule1Processor extends AbstractRuleProcessor {

  public Rule1Processor() {
    super(RiskProfileRuleType.RULE_1);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    IndicatiesService indService = getServices().getIndicatiesService();
    String relocationInd = indService.getParm(RISKANALYSIS_RELOCATION_IND);

    if (isBlank(relocationInd)) {
      log(ProExceptionSeverity.ERROR,
          "De parameter risicoanalyse => verhuizingindicatie " +
              "in Procura Burgerzaken is niet gezet.");
    }

    BasePLExt pl = getUtils().getPL(subject);
    RiskAnalysisService service = getServices().getRiskAnalysisService();
    return service.getSignal(service.buildBsnSignal(pl))
        .filter(RiskProfileSig::isEnabled)
        .isPresent();
  }
}
