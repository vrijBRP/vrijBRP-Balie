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

package nl.procura.gba.web.modules.bs.common.pages.naamskeuzepage.page2;

import nl.procura.gba.web.modules.bs.common.pages.naamskeuzepage.BsNaamskeuzeForm;
import nl.procura.gba.web.modules.bs.common.pages.naamskeuzepage.BsNaamskeuzePage;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.naamskeuze.argumenten.NaamskeuzeArgumenten;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BsNaamskeuzePage2 extends BsNaamskeuzePage {

  private BsNaamskeuzeForm form = null;

  public BsNaamskeuzePage2(DossierGeboorte dossierGeboorte) {
    super(dossierGeboorte);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new BsNaamskeuzeForm(this::zoekNaamskeuzesAkte);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onEnter() {
    zoekNaamskeuzesAkte();
    super.onEnter();
  }

  private void zoekNaamskeuzesAkte() {
    form.commit();
    NaamskeuzeArgumenten zaakArgumenten = new NaamskeuzeArgumenten();
    zaakArgumenten.setAkteVolgnr(form.getBean().getAkteVolgnr());
    fillTable(zaakArgumenten);
  }
}
