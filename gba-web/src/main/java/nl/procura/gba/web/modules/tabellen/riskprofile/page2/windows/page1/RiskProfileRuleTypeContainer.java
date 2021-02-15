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

package nl.procura.gba.web.modules.tabellen.riskprofile.page2.windows.page1;

import static java.text.MessageFormat.format;
import static nl.procura.gba.jpa.personen.types.RiskProfileRuleType.UNKNOWN;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.jpa.personen.types.RiskProfileRuleType;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class RiskProfileRuleTypeContainer extends IndexedContainer implements ProcuraContainer {

  public RiskProfileRuleTypeContainer() {
    addContainerProperty(OMSCHRIJVING, String.class, "");
    int nr = 0;
    for (RiskProfileRuleType value : RiskProfileRuleType.values()) {
      if (!UNKNOWN.equals(value)) {
        nr++;
        Item item = addItem(value);
        item.getItemProperty(OMSCHRIJVING).setValue(format("{0}. {1}", nr, value.getDescr()));
      }
    }
  }
}
