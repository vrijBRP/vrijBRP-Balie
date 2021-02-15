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

package nl.procura.gba.web.modules.bs.geboorte.page41;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.geboorte.BsPageGeboorte;
import nl.procura.gba.web.modules.bs.geboorte.page40.Page40Geboorte;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geboorte
 */

public class Page41Geboorte<T extends DossierGeboorte> extends BsPageGeboorte<T> {

  protected final Button buttonZoek = new Button("Zoek persoon (F3)");

  private BasePLExt pl = null;

  private Page41GeboorteForm1 form1 = null;

  public Page41Geboorte(BasePLExt pl) {

    super("Geboorte - eerder kind moeder");

    setPl(pl);

    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Page41GeboorteForm1(getPl());

      OptieLayout ol = new OptieLayout();
      ol.getLeft().addComponent(form1);

      ol.getRight().setWidth("150px");
      ol.getRight().setCaption("Opties");

      ol.getRight().addButton(buttonZoek, this);

      addComponent(ol);
    }

    super.event(event);
  }

  @Override
  public BasePLExt getPl() {
    return pl;
  }

  public void setPl(BasePLExt pl) {
    this.pl = pl;
  }

  @Override
  public void onPreviousPage() {

    getNavigation().goBackToPage(Page40Geboorte.class);

    super.onPreviousPage();
  }
}
