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

package nl.procura.gba.web.modules.beheer.aktes.page3;

import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class AkteRegistersoortContainer extends ArrayListContainer {

  public AkteRegistersoortContainer() {
    addItem(DossierAkteRegistersoort.AKTE_GEBOORTE);
    addItem(DossierAkteRegistersoort.AKTE_OVERLIJDEN);
    addItem(DossierAkteRegistersoort.AKTE_HUWELIJK);
    addItem(DossierAkteRegistersoort.AKTE_GPS);
    addItem(DossierAkteRegistersoort.AKTE_ERKENNING_NAAMSKEUZE);
  }
}
