/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.bs.overlijden.buitenland;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.modules.bs.common.modules.BsModule;
import nl.procura.gba.web.modules.bs.overlijden.buitenland.processen.OverlijdenProcessen;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

@ModuleAnnotation(url = "#" + ZaakFragment.FR_OVERL_BUITENLAND,
    caption = "In Buitenland",
    profielActie = ProfielActie.SELECT_ZAAK_OVERLIJDEN)
public class ModuleOverlijdenBuitenland extends BsModule {

  private Dossier dossier = null;

  public ModuleOverlijdenBuitenland() {
  }

  public ModuleOverlijdenBuitenland(Dossier dossier) {
    this.dossier = dossier;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      Dossier selectedDossier = getApplication().getServices().getMemoryService().getAndRemoveByClass(
          Dossier.class);

      if (selectedDossier != null) {
        dossier = selectedDossier;
      }

      if (dossier == null) {
        dossier = (Dossier) getApplication().getServices().getOverlijdenBuitenlandService().getNewZaak();
      }

      setProcessen(new OverlijdenProcessen(dossier, getApplication()));
    }

    super.event(event);
  }
}
