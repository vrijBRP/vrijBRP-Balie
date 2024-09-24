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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis;

import static java.text.MessageFormat.format;
import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.java.reflection.ReflectionUtil.deepCopyBean;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.AbstractRuleProcessor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule10Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule11Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule12Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule13Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule14Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule15Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule16Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule17Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule1Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule2Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule3Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule4Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule5Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule6Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule7Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule8Processor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules.Rule9Processor;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessor;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.commons.core.exceptions.ProException;

public class RiskAnalysisProcessor extends CaseProcessor<Zaak> {

  public RiskAnalysisProcessor(Services services) {
    super(services);
  }

  @Override
  public CaseProcessingResult process(Zaak zaak) {
    Dossier dossier = (Dossier) getZaak(zaak.getZaakId(), ZaakType.RISK_ANALYSIS);
    DossierRiskAnalysis riskAnalysis = (DossierRiskAnalysis) dossier.getZaakDossier();
    Zaak relatedCase = getRelatedCase(riskAnalysis);
    boolean totalThresholdReached = false;

    long totalScore = 0;
    for (DossRiskAnalysisSubject subject : riskAnalysis.getSubjects()) {
      clearLog();
      DossierPersoon person = deepCopyBean(DossierPersoon.class, subject.getPerson());
      String name = person.getNaam().getNaam_naamgebruik_eerste_voornaam();
      String bsn = StringUtils.defaultIfBlank(person.getBurgerServiceNummer().toString(), "Geen");
      String anr = StringUtils.defaultIfBlank(person.getAnummer().toString(), "Geen");

      log(INFO, format("Persoon: {0}", name));
      log(INFO, format("BSN: {0}, a-nummer: {1}", bsn, anr));
      log(INFO, "");

      // Process rule
      long score = 0;
      int nr = 0;
      RiskProfile riskProfile = riskAnalysis.getRiskProfile();
      for (RiskProfileRule rule : riskProfile.getRules()) {
        nr++;
        AbstractRuleProcessor processor = getRuleProcessor(rule);
        if (processor != null) {
          processor.setServices(getServices());
          processor.setCaseProcessor(this);
          log(INFO, "Regel {0}: {1}", nr, rule.getName());

          if (processor.process(riskAnalysis,
              new RiskAnalysisRelatedCase(relatedCase),
              subject,
              rule)) {

            score += rule.getScore().longValue();
            log(WARNING, "Regel is van toepassing met score: {0}", rule.getScore());

          } else {
            log(INFO, "Regel is niet van toepassing");
          }

        } else {
          log(WARNING, "Regel is nog niet geïmplementeerd");
        }
        log(INFO, "");
      }

      // Log the total score per person
      BigDecimal threshold = riskProfile.getThreshold();
      totalScore += score;
      boolean subjectThresholdReacted = totalScore >= threshold.longValue();
      if (subjectThresholdReacted) {
        log(WARNING, "Totaalscore: {0}, drempel: {1}. Drempel gehaald.", totalScore, threshold);
      } else {
        log(INFO, "Totaalscore: {0}, drempel: {1}. Drempel niet gehaald.", totalScore, threshold);
      }

      // Final blankline for readability
      log(INFO, "");

      // Is the threshold reached
      if (subjectThresholdReacted) {
        totalThresholdReached = true;
      }

      // Store score
      subject.setScore(new BigDecimal(score));
      subject.setLog(getLog().toString());
      getServices().getRiskAnalysisService().save(subject);
    }

    updateRelatedCase(relatedCase, totalThresholdReached);

    //Update riskanalysis status
    ZaakStatusService statusService = getServices().getZaakStatusService();
    statusService.updateStatus(zaak, ZaakStatusType.VERWERKT, "Analyses voltooid");

    return getResult();
  }

  private AbstractRuleProcessor getRuleProcessor(RiskProfileRule rule) {

    List<AbstractRuleProcessor> ruleProcessors = new ArrayList<>();
    ruleProcessors.add(new Rule1Processor());
    ruleProcessors.add(new Rule2Processor());
    ruleProcessors.add(new Rule3Processor());
    ruleProcessors.add(new Rule4Processor());
    ruleProcessors.add(new Rule5Processor());
    ruleProcessors.add(new Rule6Processor());
    ruleProcessors.add(new Rule7Processor());
    ruleProcessors.add(new Rule8Processor());
    ruleProcessors.add(new Rule9Processor());
    ruleProcessors.add(new Rule10Processor());
    ruleProcessors.add(new Rule11Processor());
    ruleProcessors.add(new Rule12Processor());
    ruleProcessors.add(new Rule13Processor());
    ruleProcessors.add(new Rule14Processor());
    ruleProcessors.add(new Rule15Processor());
    ruleProcessors.add(new Rule16Processor());
    ruleProcessors.add(new Rule17Processor());

    return ruleProcessors.stream()
        .filter(processor -> processor.getRuleType() == rule.getRuleType())
        .findFirst()
        .orElse(null);
  }

  /**
   * Change the related case
   */
  private void updateRelatedCase(Zaak relatedCase, boolean thresholdReached) {
    getServices().getRiskAnalysisService().removeWaitForRiskAnalysis(relatedCase);

    if (thresholdReached) {
      // Threshold reached which is NOT good.
      ZaakStatusType status = relatedCase.getStatus();
      if (status.isEindStatus()) {
        log(ERROR, "Status van zaak {0} kon niet gewijzigd worden omdat status {1} een eindstatus is",
            relatedCase.getZaakId(), status.getOms());
      } else {
        ZaakService<Zaak> relatedCaseService = getServices().getZakenService().getService(relatedCase);
        String remark = "Risico analyse verwerkt. Drempel gehaald. Zie risicoanalyse.";
        relatedCaseService.updateStatus(relatedCase, status, INBEHANDELING, remark);
        log(INFO, "Status zaak {0} gewijzigd naar in behandeling", relatedCase.getZaakId());
      }
    }
  }

  /**
   * Search the related case
   */
  private Zaak getRelatedCase(DossierRiskAnalysis riskAnalysis) {
    String zaakId = riskAnalysis.getRefCaseId();
    ZaakType zaakType = ZaakType.get(riskAnalysis.getRefCaseType().longValue());
    ZaakArgumenten za = new ZaakArgumenten(new ZaakKey(zaakId, zaakType));
    return getServices().getZakenService().getStandaardZaken(za).stream().findFirst()
        .orElseThrow(() -> new ProException(ERROR, "Geen gerelateerde zaak gevonden"));
  }
}
