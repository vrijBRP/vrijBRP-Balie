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

package nl.procura.gba.web.modules.zaken.reisdocument.page24;

import nl.procura.gba.web.services.zaken.reisdocumenten.SluitingType;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class SluitingContainer extends ArrayListContainer {

  public static SluitingContainer getAll() {
    SluitingContainer sl = new SluitingContainer();
    sl.addItem(SluitingType.DOCUMENT_UITGEREIKT);
    sl.addItem(SluitingType.DOCUMENT_NIET_UITGEREIKT_ONJUIST);
    sl.addItem(SluitingType.DOCUMENT_UITGEREIKT_DOOR_ANDERE_INSTANTIE);
    sl.addItem(SluitingType.DOCUMENT_NIET_UITGEREIKT_OVERIGE_REDEN);
    return sl;
  }

  public static SluitingContainer getError() {
    SluitingContainer sl = new SluitingContainer();
    sl.addItem(SluitingType.DOCUMENT_NIET_UITGEREIKT_ONJUIST);
    sl.addItem(SluitingType.DOCUMENT_NIET_UITGEREIKT_OVERIGE_REDEN);
    return sl;
  }
}
