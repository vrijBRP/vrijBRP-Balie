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

package nl.procura.gba.web.modules.bs.huwelijk.page35;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.AMBTENAAR;

import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonPage;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Getuige
 */

public class Page35Huwelijk extends BsPersoonPage<DossierHuwelijk> {

  public Page35Huwelijk(DossierPersoon dossierPersoon) {

    super("Huwelijk/GPS - ambtenaar");

    setDossierPersoon(dossierPersoon);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      getDossierPersoon().setDossierPersoonType(AMBTENAAR);
    }

    super.event(event);
  }

  @Override
  public void onNew() {
    super.onNew();
    onPreviousPage();
  }

  @Override
  public void onNextPage() {

    if (checkPage()) {
      onPreviousPage();
    }
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  protected String getInfo() {
    return "Zoek de persoon, controleer de gegevens of vul deze in. Druk op Volgende (F2) om verder te gaan.";
  }
}
