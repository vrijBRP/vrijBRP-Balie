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

package nl.procura.gba.web.modules.bs.common.pages.persoonpage;

import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

public class BsPersoonForm4 extends BsPersoonForm {

  public BsPersoonForm4(DossierPersoon dossierPersoon, BsPersoonRequirement requirement) {

    super(dossierPersoon, requirement);

    setCaption("Overige");

    setOrder(BS, BS_VANAF, VERSTREK, CURATELE, NAT, VBT, TOELICHTING);

    update();
  }

  /**
   * Update alleen het overige scherm
   */
  public void updateNationaliteiten() {
    Field nationaliteiten = getField(NAT);
    nationaliteiten.setReadOnly(false);
    nationaliteiten.setValue(getNationaliteiten());
    nationaliteiten.setReadOnly(true);
    repaint();
  }
}
