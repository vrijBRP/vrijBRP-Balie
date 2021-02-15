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

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

import java.util.List;

import nl.procura.diensten.gba.wk.baseWK.BaseWKValue;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;

/**
 * Rule 11
 */
public class Rule11Processor extends AbstractRuleProcessor {

  public Rule11Processor() {
    super(RiskProfileRuleType.RULE_11);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    List<BaseWKExt> addresses = getUtils().getRelocationAddress(relatedCase);

    boolean isMatch = false;
    if (INTEGER_ONE.equals(addresses.size())) {
      BaseWKExt address = addresses.get(0);
      BaseWKValue wonInd = address.getBasisWk().getWoning_indicatie();

      if (Integer.parseInt(wonInd.getCode()) != INTEGER_ZERO.intValue()) {
        warn("Adressen heeft indicatie {0}: {1}", wonInd.getCode(), wonInd.getDescr());
        isMatch = true;
      }

    } else if (INTEGER_ZERO.equals(addresses.size())) {
      warn("Geen adressen gevonden in de gemeente op basis van het nieuwe verhuisadres");
    } else { // multiple addresses
      warn("Meerdere adressen gevonden in de gemeente op basis van het nieuwe verhuisadres");
    }

    return isMatch;
  }
}
