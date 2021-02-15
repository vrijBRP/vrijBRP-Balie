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

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.commons.core.utils.ProNumberUtils.isNotNeg;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;

/**
 * Rule 5
 */
public class Rule5Processor extends AbstractRuleProcessor {

  public Rule5Processor() {
    super(RiskProfileRuleType.RULE_5);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    BasePLExt pl = getUtils().getPL(subject);
    BasePLRec actueel = pl.getLatestRec(GBACat.VB);

    BasePLValue aand = actueel.getElemVal(AAND_GEG_IN_ONDERZ);
    long dIn = actueel.getElemVal(DATUM_INGANG_ONDERZ).toLong();
    long dEnd = actueel.getElemVal(DATUM_EINDE_ONDERZ).toLong();
    boolean isAddressInResearch = (isNotBlank(aand.getVal()) || (isNotNeg(dIn) && !isNotNeg(dEnd)));

    if (isAddressInResearch) {
      info("Categorie 08 (verblijfplaats) staat in onderzoek met aanduiding {0} ",
          aand.getDescr());
    } else {
      info("Categorie 08 (verblijfplaats) staat niet in onderzoek");
    }
    return isAddressInResearch;
  }
}
