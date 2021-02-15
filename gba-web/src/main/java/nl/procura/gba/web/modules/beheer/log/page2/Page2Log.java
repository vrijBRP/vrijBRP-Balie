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

package nl.procura.gba.web.modules.beheer.log.page2;

import static nl.procura.standard.Globalfunctions.fil;

import java.text.MessageFormat;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.log.InLogpoging;

public class Page2Log extends NormalPageTemplate {

  public Page2Log(InLogpoging log) {

    super(MessageFormat.format("Log: {0} - {1}", log.getDatumTijd(),
        fil(log.getGebruiker().getDescription()) ? log.getGebruiker().getDescription() : "Onbekend"));

    addButton(buttonPrev);

    addComponent(new Page2LogForm1(log));
  }

  @Override
  public void onPreviousPage() {

    getNavigation().goBackToPage(getNavigation().getPreviousPage());
  }
}
