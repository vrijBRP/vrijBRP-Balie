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

package nl.procura.gba.web.modules.bs.riskanalysis.page1;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.web.services.Services;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class RiskProfileContainer extends IndexedContainer implements ProcuraContainer {

  public RiskProfileContainer() {
    List<RiskProfile> profiles = Services.getInstance().getRiskAnalysisService().getRiskProfiles();
    addContainerProperty(OMSCHRIJVING, String.class, "");
    for (RiskProfile profile : profiles) {
      Item item = addItem(profile);
      item.getItemProperty(OMSCHRIJVING).setValue(profile.getName());
    }
  }
}
