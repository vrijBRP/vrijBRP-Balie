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

package nl.procura.gba.web.modules.bs.overlijden.levenloos.page1;

import static java.util.Arrays.asList;

import java.util.List;

import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsContactpersoonPage;
import nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekTab;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page20.Page20Levenloos;
import nl.procura.gba.web.modules.zaken.overlijden.page1.BsZoekPageOverlijden;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Levenloos extends BsContactpersoonPage<DossierLevenloos> {

  public Page1Levenloos() {
    super("Levenloos geboren kind - aangever");
  }

  @Override
  public void addButtons() {

    super.addButtons();

    getOptieLayout().getRight().addComponent(new Ruler());
    getOptieLayout().getRight().addButton(buttonIden, this);
    getOptieLayout().getRight().addButton(buttonContact, this);
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {

      getServices().getLevenloosService().save(getDossier());

      return true;
    }

    return false;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonPrev.setEnabled(false);

      setDossierPersoon(getZaakDossier().getAangever());
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    getNavigation().goToPage(Page20Levenloos.class);
  }

  @Override
  protected List<BsZoekTab> getExtraTabs() {
    return asList(new BsZoekTab(new BsZoekPageOverlijden(getDossierPersoon()), "Overzicht van aangevers"));
  }
}
