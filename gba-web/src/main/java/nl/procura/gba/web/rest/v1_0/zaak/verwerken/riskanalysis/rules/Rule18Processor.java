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

import org.apache.commons.lang3.BooleanUtils;

import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.geo.rest.domain.baglv.adresV2.AdresResponseV2;
import nl.procura.geo.rest.domain.baglv.adresV2.AdresV2;
import nl.procura.geo.rest.domain.baglv.adresV2.LVBagResponseV2;

/**
 * Rule 17
 */
public class Rule18Processor extends AbstractBagRuleProcessor {

  public Rule18Processor() {
    super(RiskProfileRuleType.RULE_18);
  }

  @Override
  public boolean process(
      DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    LVBagResponseV2 response = searchBagLV(relatedCase.getAddress());
    if (response != null) {
      AdresResponseV2 adresResponse = response.getAdresResponse();
      if (adresResponse != null) {
        if (adresResponse.getAdressen().size() == 1) {
          for (AdresV2 adres : adresResponse.getAdressen()) {
            boolean isNevenadres = BooleanUtils.isTrue(adres.getIndicatieNevenadres());
            if (isNevenadres) {
              info(relatedCase.getAddress().getAdres_pc_wpl_gem() + " is een nevenadres");
              return true;
            } else {
              return false;
            }
          }
        }
      }
    }

    return false;
  }
}
