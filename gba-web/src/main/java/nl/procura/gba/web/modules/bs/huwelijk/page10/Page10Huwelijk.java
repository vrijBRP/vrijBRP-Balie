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

package nl.procura.gba.web.modules.bs.huwelijk.page10;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER2;

import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsContactpersoonPage;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Partner 1
 */

public class Page10Huwelijk extends BsContactpersoonPage<DossierHuwelijk> {

  public Page10Huwelijk() {

    super("Huwelijk/GPS - partner 2");
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

      getProcessen().updateStatus();

      getServices().getHuwelijkService().save(getDossier());

      return true;
    }

    return false;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setDossierPersoon(getZaakDossier().getPartner2());

      getDossierPersoon().setDossierPersoonType(PARTNER2);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }
}
