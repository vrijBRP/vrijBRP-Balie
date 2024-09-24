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

package nl.procura.gba.web.modules.bs.lv;

import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.dialogs.ZaakConfiguratieDialog;
import nl.procura.gba.web.modules.bs.common.modules.BsModule;
import nl.procura.gba.web.modules.bs.lv.processen.LvProcessen;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

@ModuleAnnotation(url = "#bs.lv",
    caption = "Latere vermelding",
    profielActie = ProfielActie.SELECT_ZAAK_AFSTAMMING)
public class ModuleLv extends BsModule {

  private Dossier dossier;

  public ModuleLv() {
  }

  public ModuleLv(Dossier dossier) {
    this.dossier = dossier;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      Dossier selectedDossier = getApplication().getServices()
          .getMemoryService()
          .getAndRemoveByClass(Dossier.class);

      if (selectedDossier != null) {
        dossier = selectedDossier;
      }

      if (dossier == null) {
        dossier = (Dossier) getApplication().getServices().getLvService().getNewZaak();
      }

      if (!dossier.isStored()) {
        ZaakConfiguratieDialog.of(getApplication(), dossier, getApplication().getServices());
      }

      setProcessen(new LvProcessen(dossier, getApplication()));
    }

    super.event(event);
  }
}
