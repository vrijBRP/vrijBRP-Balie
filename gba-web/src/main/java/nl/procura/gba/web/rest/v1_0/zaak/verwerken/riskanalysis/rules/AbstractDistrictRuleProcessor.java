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

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.gba.jpa.personen.types.RiskProfileRuleVar;
import nl.procura.gba.web.components.containers.TabelContainer;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.standard.exceptions.ProException;

/**
 * Rule 7
 */
public abstract class AbstractDistrictRuleProcessor extends AbstractRuleProcessor {

  public AbstractDistrictRuleProcessor(RiskProfileRuleType ruleType) {
    super(ruleType);
  }

  /**
   * Check if district 1 (wijk), district 2 (buurt) or district 3 (subbuurt) matches 
   */
  protected boolean isDistrictMatch(RiskAnalysisRelatedCase relatedCase,
      RiskProfileRule rule,
      String districtString, TabelContainer container) {

    String attr = rule.getAttribute(RiskProfileRuleVar.X.name());
    String attrDistrict = container.get(attr).getStringValue();

    if (StringUtils.isBlank(attrDistrict)) {
      throw new ProException(ERROR, "Geen {0} genaamd {1} gevonden", districtString, attr);
    }

    List<BaseWKExt> addresses = getUtils().getRelocationAddress(relatedCase);

    boolean isMatch = false;
    if (INTEGER_ONE.equals(addresses.size())) {

      BaseWKExt address = addresses.get(0);
      String addressDistrict = address.getBasisWk().getWijk().getValue();

      if (GBATable.BUURT.equals(container.getTabel())) {
        addressDistrict = address.getBasisWk().getBuurt().getValue();

      } else if (GBATable.SUBBUURT.equals(container.getTabel())) {
        addressDistrict = address.getBasisWk().getSub_buurt().getValue();
      }

      String addressString = address.getAdres();
      if (StringUtils.isBlank(addressDistrict)) {
        info("{0} is niet gekoppeld aan een {1}", addressString, districtString);
      } else {
        if (attrDistrict.equals(addressDistrict)) {
          info("{0} ligt in {1}", addressString, attrDistrict);
          isMatch = true;
        } else {
          info("{0} ligt niet in {1}, maar in {2}", addressString, attrDistrict, addressDistrict);
        }
      }
    } else if (INTEGER_ZERO.equals(addresses.size())) {
      warn("Geen adressen gevonden in de gemeente op basis van het nieuwe verhuisadres");
    } else { // multiple addresses
      warn("Meerdere adressen gevonden in de gemeente op basis van het nieuwe verhuisadres");
    }

    return isMatch;
  }
}
