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

package nl.procura.gba.web.modules.zaken.tmv.page1;

import java.util.List;

import nl.procura.gba.web.modules.persoonslijst.overig.PlPage;
import nl.procura.gba.web.modules.persoonslijst.overig.grid.RecordElementCombo;
import nl.procura.gba.web.modules.zaken.tmv.layouts.TmvLijstLayout;
import nl.procura.gba.web.modules.zaken.tmv.page2.Page2Tmv;

public class Page1Tmv extends PlPage {

  private TmvLijstLayout tmvLayout = null;

  public Page1Tmv(List<RecordElementCombo> list) {

    super("Terugmelding");

    addButton(buttonPrev);
    addButton(buttonNext);

    tmvLayout = new TmvLijstLayout(list);

    addExpandComponent(tmvLayout);
  }

  @Override
  public void onNextPage() {

    getNavigation().goToPage(new Page2Tmv(tmvLayout.getTmvRecords()));

    super.onNextPage();
  }
}
