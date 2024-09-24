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

package nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb;

import static nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType.ERKENNER;
import static nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType.ERKENNER_MOEDER;
import static nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType.MOEDER;
import static nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType.MOEDER_ERKENNER;

import nl.procura.gba.web.components.containers.GbaContainer;

public class NaamskeuzePersoonErkContainer extends GbaContainer {

  public NaamskeuzePersoonErkContainer() {
    addItems(MOEDER, ERKENNER, MOEDER_ERKENNER, ERKENNER_MOEDER);
  }
}
