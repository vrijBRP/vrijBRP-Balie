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

package nl.procura.gba.web.modules.bs.onderzoek.page1;

import static nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType.ANDERE_GEMEENTE;
import static nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType.BUITENLAND;
import static nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType.IN_GEMEENTE_BRIEF;
import static nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType.IN_GEMEENTE_WOON;
import static nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType.ONBEKEND;

import nl.procura.gba.web.components.containers.GbaContainer;

public class VermoedAdresContainer extends GbaContainer {

  public VermoedAdresContainer() {
    addItems(ONBEKEND, IN_GEMEENTE_WOON, IN_GEMEENTE_BRIEF, ANDERE_GEMEENTE, BUITENLAND);
  }
}
