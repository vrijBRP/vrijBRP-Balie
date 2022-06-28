/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister;

import java.util.Optional;

import com.vaadin.ui.Window;

import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.vaadin.functies.VaadinUtils;

public class ZakenregisterUtils {

  public static Optional<ZakenregisterAccordionTab> getZakenregisterAccordionTab(Window window) {
    return Optional.ofNullable(VaadinUtils.getChild(window, ZakenregisterAccordionTab.class));
  }
}
