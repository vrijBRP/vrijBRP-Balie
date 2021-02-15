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

package nl.procura.gba.web.modules.zaken.vog.overzicht;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.modules.zaken.vog.VogHeaderForm;
import nl.procura.gba.web.modules.zaken.vog.page2.*;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;

public class VogOverzichtLayout extends VerticalLayout {

  public VogOverzichtLayout(final VogAanvraag aanvraag) {

    setSpacing(true);

    addComponent(new VogHeaderForm(aanvraag));
    addComponent(new Page2VogForm1(aanvraag));
    addComponent(new Page2VogForm2(aanvraag));
    addComponent(new Page2VogForm3(aanvraag));
    addComponent(new Page2VogForm4(aanvraag));
    addComponent(new Page2VogTable(aanvraag));
    addComponent(new Page2VogForm5(aanvraag));
  }
}
