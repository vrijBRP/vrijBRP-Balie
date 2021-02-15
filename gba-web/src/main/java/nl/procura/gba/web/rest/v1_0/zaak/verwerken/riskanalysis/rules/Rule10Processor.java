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

import static nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService.SIGNALTYPE.ADDRESS;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.db.RiskProfileSig;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.validation.Postcode;

/**
 * Rule 10
 */
public class Rule10Processor extends AbstractRuleProcessor {

  public Rule10Processor() {
    super(RiskProfileRuleType.RULE_10);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    for (RiskProfileSig sig : getServices().getRiskAnalysisService().getSignals(ADDRESS)) {
      String varPc = Postcode.getCompact(sig.getPc());
      String varHnr = sig.getHnr().toString();
      String varHnrL = sig.getHnrL();
      String varHnrT = sig.getHnrT();
      String varHnrA = sig.getHnrA();

      // The relocation address
      RuleAddresses addresses = getUtils().getAddresses(relatedCase, null);
      RuleAddress caseAddress = addresses.stream()
          .filter(RelocationFilters.isCaseAddress(true))
          .findFirst().get();

      String pc = Postcode.getCompact(caseAddress.getAddress().getPostcode());
      String hnr = caseAddress.getAddress().getHuisnummer();
      String hnrL = caseAddress.getAddress().getHuisletter();
      String hnrT = caseAddress.getAddress().getHuisnummertoev();
      String hnrA = caseAddress.getAddress().getHuisnummeraand();

      if (eq(varPc, pc)
          && eq(varHnr, hnr)
          && eq(varHnrL, hnrL)
          && eq(varHnrT, hnrT)
          && eq(varHnrA, hnrA)) {
        info("{0} is een gemarkeerd adres", sig.getLabel());
        return true;
      }
    }

    return false;
  }

  public boolean eq(String val1, String val2) {
    String value1 = StringUtils.deleteWhitespace(val1);
    String value2 = StringUtils.deleteWhitespace(val2);
    if (StringUtils.isBlank(value1 + value2)) {
      return true;
    }
    return value1.equalsIgnoreCase(value2);
  }
}
