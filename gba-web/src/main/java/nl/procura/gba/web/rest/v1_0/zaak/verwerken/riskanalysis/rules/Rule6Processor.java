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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules;

import static nl.procura.gba.jpa.personen.types.RiskProfileRuleVar.X;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleVar.Y;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;

/**
 * Rule 6
 */
public class Rule6Processor extends AbstractRuleProcessor {

  public Rule6Processor() {
    super(RiskProfileRuleType.RULE_6);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    int startHour = rule.getIntAttribute(X.name()); // Begin hour (x)
    int endHour = rule.getIntAttribute(Y.name()); // End hour (y)

    DateTime dateTimeEntry = relatedCase.getDatumTijdInvoer();
    long hour = Long.parseLong(dateTimeEntry.getFormatTime("HH"));
    String formatHour = dateTimeEntry.getFormatTime("HH:mm");

    boolean isInBetween = hour >= startHour && hour < endHour;
    info("Verhuizing is ingevoerd om {0}", formatHour);
    return isInBetween;
  }
}
