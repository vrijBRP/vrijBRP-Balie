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
import java.util.Optional;

import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

/**
 * Rule 17
 */
public class Rule17Processor extends AbstractRuleProcessor {

  public Rule17Processor() {
    super(RiskProfileRuleType.RULE_17);
  }

  @Override
  public boolean process(
      DossierRiskAnalysis riskAnalysis,
      RiskAnalysisRelatedCase relatedCase,
      DossRiskAnalysisSubject subject,
      RiskProfileRule rule) {

    if (relatedCase.isRelocation()) {
      return getAnrHoofdbewoner(relatedCase.getRelocation())
          .map(anr -> checkAnumbers(relatedCase, anr))
          .orElse(false);
    }
    return false;
  }

  private Boolean checkAnumbers(RiskAnalysisRelatedCase relatedCase, Anr anr) {
    List<BaseWKExt> addresses = getUtils().getRelocationAddress(relatedCase, true);
    if (INTEGER_ONE.equals(addresses.size())) {
      BaseWKExt address = addresses.get(0);
      if (address.getBasisWk()
          .getPersonen()
          .stream()
          .filter(BaseWKPerson::isCurrentResident)
          .map(person -> new Anr(person.getAnummer().getValue()))
          .filter(Anr::isCorrect)
          .noneMatch(anrPerson -> anrPerson.getLongAnummer().equals(anr.getLongAnummer()))) {
        String bron = relatedCase.getRelocation().getBron();
        if (ZaakUtils.PROWEB_PERSONEN.equals(bron)) {
          info("Hoofdbewoner is niet ingeschreven op het nieuwe adres, maar zaak is opgevoerd "
              + "via de balie applicatie");
          return false;
        } else {
          info("Hoofdbewoner is niet ingeschreven op het nieuwe adres en zaak is niet opgevoerd "
              + "via de balie applicatie, maar via " + bron);
          return true;
        }
      }
    } else if (INTEGER_ZERO.equals(addresses.size())) {
      warn("Geen adressen gevonden in de gemeente op basis van het nieuwe verhuisadres");
    } else { // multiple addresses
      warn("Meerdere adressen gevonden in de gemeente op basis van het nieuwe verhuisadres");
    }
    return false;
  }

  /*
  Finds Bsn and retrieve Anumber
  */
  private Optional<Anr> getAnrHoofdbewoner(VerhuisAanvraag relocation) {
    Bsn bsnHoofdbewoner = new Bsn(relocation.getHoofdbewoner().getBurgerServiceNummer().getStringValue());
    if (bsnHoofdbewoner.isCorrect()) {
      Anr anr = new Anr(getServices().getPersonenWsService()
          .getPersoonslijst(bsnHoofdbewoner.getDefaultBsn())
          .getPersoon().getAnr().getVal());
      if (anr.isCorrect()) {
        return Optional.of(anr);
      }
    }
    return Optional.empty();
  }
}
