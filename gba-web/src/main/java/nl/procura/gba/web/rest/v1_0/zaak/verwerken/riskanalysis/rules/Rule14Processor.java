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
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.geo.rest.domain.ngr.wfs.WfsFeature;
import nl.procura.geo.rest.domain.ngr.wfs.WfsSearchResponse;
import nl.procura.geo.rest.domain.ngr.wfs.types.FeatureType;
import nl.procura.commons.core.exceptions.ProException;

/**
 * Rule 13
 */
public class Rule14Processor extends AbstractBagRuleProcessor {

  public Rule14Processor() {
    super(RiskProfileRuleType.RULE_14);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    boolean isMatch = false;
    WfsSearchResponse response = search(relatedCase.getAddress());

    if (response != null) {
      long numberOfPersons = relatedCase.getNumberOfPersons();
      info("Aantal personen nieuw adres: {0}", numberOfPersons == 20
          ? " > 10 (voor de berekening wordt aantal van 20 personen gebruikt)"
          : numberOfPersons);
      WfsFeature feature = response.getFeatures().get(0);
      FeatureType type = feature.getType();

      if (FeatureType.VERBLIJFSOBJECT.equals(type)) {
        if (numberOfPersons < 1) {
          throw new ProException(ERROR, "Het veld 'aantal personen nieuw adres' is niet gevuld");
        }
        int surfaceSize = feature.getVerblijfsobject().getOppervlakte();
        int minimalPP = rule.getIntAttribute(X.name());
        long surfacePP = surfaceSize / numberOfPersons;

        info("Oppervlakte verblijfsobject: {0}", surfaceSize);
        info("Minimale oppervlakte pp: {0}", minimalPP);

        if (surfacePP >= minimalPP) {
          info("Werkelijke oppervlakte pp: {0}", surfacePP);
        } else {
          isMatch = true;
          warn("Werkelijke oppervlakte pp: {0}", surfacePP);
        }
      } else {
        warn("Verblijfplaats is een {0}. Geen opppervlakte beschikbaar.", type.getValue().toLowerCase());
      }
    }

    return isMatch;
  }
}
