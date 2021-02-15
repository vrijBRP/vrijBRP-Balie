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

package nl.procura.gba.web.modules.bs.overlijden.buitenland.page1;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.OVERLEDENE;

import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonPage;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**

 * <p>
 * 6 Feb. 2013 8:00:00
 */

public class Page1Overlijden extends BsPersoonPage<DossierOverlijdenBuitenland> {

  public Page1Overlijden() {
    super("Overlijden buitenland - overledene");
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {
      getServices().getOverlijdenBuitenlandService().save(getDossier());
      return true;
    }

    return false;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonNext);

      setDossierPersoon(getZaakDossier().getOverledene());
      getDossierPersoon().setDossierPersoonType(OVERLEDENE);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  protected String getInfo() {
    return "Zoek de persoon, controleer de gegevens of vul deze in. " + "Druk op Volgende (F2) om verder te gaan.";
  }
}
