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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.page2;

import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class ZaakAttribuutContainer extends ArrayListContainer {

  public ZaakAttribuutContainer() {
    addItem(ZaakAttribuutType.MIJN_OVERHEID_WEL);
    addItem(ZaakAttribuutType.MIJN_OVERHEID_NIET);
    addItem(ZaakAttribuutType.WACHT_OP_RISICOANALYSE);
    addItem(ZaakAttribuutType.FOUT_BIJ_VERWERKING);
    addItem(ZaakAttribuutType.ANDERS);
  }
}
