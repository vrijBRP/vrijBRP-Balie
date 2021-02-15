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

package nl.procura.gba.web.modules.bs.erkenning.page32;

import nl.procura.gba.web.modules.bs.common.pages.aktepage.page1.BsAktePage1;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Erkenning
 */

public class Page32Erkenning extends BsAktePage1<DossierErkenning> {

  public Page32Erkenning() {
    super("Erkenning - aktenummers");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      DossierErkenning dossier = getZaakDossier();

      for (DossierAkte akte : dossier.getDossier().getAktes()) {

        akte.setPersoon(dossier.getErkenner());
        akte.setPersoon(dossier.getMoeder());
      }
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }
}
