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

import java.util.function.Function;

import org.apache.commons.lang3.builder.EqualsBuilder;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.validation.Postcode;

/**
 * Rule 16
 */
public class Rule16Processor extends AbstractRuleProcessor {

  public Rule16Processor() {
    super(RiskProfileRuleType.RULE_16);
  }

  @Override
  public boolean process(DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    BasePLExt pl = getUtils().getPL(subject);

    String pc = Postcode.getCompact(relatedCase.getAddress().getPostcode());
    String hnr = String.valueOf(relatedCase.getAddress().getHuisnummer());
    String hnrL = relatedCase.getAddress().getHuisletter();
    String hnrT = relatedCase.getAddress().getHuisnummertoev();

    boolean isTheSameAddress = new EqualsBuilder()
        .append(sanitize(Postcode.getCompact(get(pl, Adres::getPostcode))), sanitize(pc))
        .append(sanitize(get(pl, Adres::getHuisnummer)), sanitize(hnr))
        .append(sanitize(get(pl, Adres::getHuisletter)), sanitize(hnrL))
        .append(sanitize(get(pl, Adres::getHuisnummertoev)), sanitize(hnrT))
        .build();

    if (isTheSameAddress) {
      info("Persoon is al ingeschreven op adres " + relatedCase.getAddress().getAdres_pc_wpl_gem());
      return true;
    }
    return false;
  }

  private String sanitize(String value) {
    return value != null ? value.trim().toLowerCase() : null;
  }

  private String get(BasePLExt pl, Function<Adres, BasePLElem> function) {
    return function.apply(pl.getVerblijfplaats().getAdres()).getValue().getVal();
  }
}
