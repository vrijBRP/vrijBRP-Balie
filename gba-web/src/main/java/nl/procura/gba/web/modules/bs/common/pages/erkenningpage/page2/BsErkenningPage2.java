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

package nl.procura.gba.web.modules.bs.common.pages.erkenningpage.page2;

import nl.procura.gba.web.modules.bs.common.pages.erkenningpage.BsErkenningForm;
import nl.procura.gba.web.modules.bs.common.pages.erkenningpage.BsErkenningPage;
import nl.procura.gba.web.services.bs.erkenning.argumenten.ErkenningArgumenten;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BsErkenningPage2 extends BsErkenningPage {

  private Form1 form = null;

  public BsErkenningPage2(DossierGeboorte dossierGeboorte) {
    super(dossierGeboorte);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new Form1();
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onEnter() {
    zoekErkenningenAkte();
    super.onEnter();
  }

  private void zoekErkenningenAkte() {
    form.commit();

    ErkenningArgumenten zaakArgumenten = new ErkenningArgumenten();
    zaakArgumenten.setAkteVolgnr(form.getBean().getAkteVolgnr());
    fillTable(zaakArgumenten);
  }

  public class Form1 extends BsErkenningForm {

    @Override
    protected void onZoek() {
      zoekErkenningenAkte();
    }
  }
}
