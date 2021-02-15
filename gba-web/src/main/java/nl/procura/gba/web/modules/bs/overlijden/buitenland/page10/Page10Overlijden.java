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

package nl.procura.gba.web.modules.bs.overlijden.buitenland.page10;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.gba.web.modules.bs.overlijden.PageBsOverlijdenGerelateerde;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page10Overlijden extends PageBsOverlijdenGerelateerde<DossierOverlijdenBuitenland> {

  public Page10Overlijden() {
    super("Overlijden buitenland - gerelateerden");
  }

  @Override
  public boolean checkPage() {

    for (DossierPersoon persoon : getPersonen()) {
      if (isMeerderjarig(persoon)) {
        if (persoon.getDossierPersoonType().is(DossierPersoonType.KIND)) {
          throw new ProException(WARNING, "Eén of meerdere kinderen is niet minderjarig");
        }
      }
    }

    super.checkPage();

    getServices().getOverlijdenBuitenlandService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      init(getZaakDossier().getOverledene());
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
