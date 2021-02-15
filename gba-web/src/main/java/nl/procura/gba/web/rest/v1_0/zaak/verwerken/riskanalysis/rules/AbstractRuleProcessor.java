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

import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessor;
import nl.procura.standard.exceptions.ProExceptionSeverity;

public abstract class AbstractRuleProcessor {

  private final RiskProfileRuleType ruleType;
  private Services                  services;
  private CaseProcessor             caseProcessor;
  private RiskAnalysisRuleUtils     utils;

  public AbstractRuleProcessor(RiskProfileRuleType ruleType) {
    this.ruleType = ruleType;
  }

  public RiskProfileRuleType getRuleType() {
    return ruleType;
  }

  public abstract boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule);

  public Services getServices() {
    return services;
  }

  public void setServices(Services services) {
    this.services = services;
  }

  public CaseProcessor getCaseProcessor() {
    return caseProcessor;
  }

  public void setCaseProcessor(CaseProcessor caseProcessor) {
    this.caseProcessor = caseProcessor;
  }

  public RiskAnalysisRuleUtils getUtils() {
    if (utils == null) {
      this.utils = new RiskAnalysisRuleUtils(services);
    }
    return utils;
  }

  protected void warn(String message, Object... args) {
    log(ProExceptionSeverity.WARNING, message, args);
  }

  protected void info(String message, Object... args) {
    log(ProExceptionSeverity.INFO, message, args);
  }

  protected void log(ProExceptionSeverity severity, String message, Object... args) {
    getCaseProcessor().log(severity, message, args);
  }
}
