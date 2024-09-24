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

package nl.procura.gba.web.modules.bs.naturalisatie;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;

import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.dialogs.ZaakConfiguratieDialog;
import nl.procura.gba.web.modules.bs.common.modules.BsModule;
import nl.procura.gba.web.modules.bs.naturalisatie.aanschrijving.AanschrijvingWindow;
import nl.procura.gba.web.modules.bs.naturalisatie.page40.Page40Naturalisatie;
import nl.procura.gba.web.modules.bs.naturalisatie.page50.Page50Naturalisatie;
import nl.procura.gba.web.modules.bs.naturalisatie.page60.Page60Naturalisatie;
import nl.procura.gba.web.modules.bs.naturalisatie.processen.NaturalisatieProcessen;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

@ModuleAnnotation(url = "#bs.naturalisatie",
    caption = "Nationaliteit",
    profielActie = ProfielActie.SELECT_ZAAK_NATURALISATIE)
public class ModuleNaturalisatie extends BsModule {

  private Dossier dossier;
  private Button  buttonAanschr;

  public ModuleNaturalisatie() {
  }

  public ModuleNaturalisatie(Dossier dossier) {
    this.dossier = dossier;
  }

  @Override
  protected void setExtraButtons(Layout layout) {
    buttonAanschr = new Button("Aanschrijving / afdrukken");
    buttonAanschr.setWidth("100%");
    buttonAanschr.addListener((Button.ClickListener) event -> {

      Page40Naturalisatie toetsing = VaadinUtils.getChild(this, Page40Naturalisatie.class);
      Page50Naturalisatie behandeling = VaadinUtils.getChild(this, Page50Naturalisatie.class);
      Page60Naturalisatie ceremonie = VaadinUtils.getChild(this, Page60Naturalisatie.class);

      if (toetsing != null) {
        toetsing.checkPage();
      }

      if (behandeling != null) {
        behandeling.checkPage();
      }

      if (ceremonie != null) {
        ceremonie.checkPage();
      }

      getApplication().getParentWindow()
          .addWindow(new AanschrijvingWindow((DossierNaturalisatie) dossier.getZaakDossier()));

    });

    layout.addComponent(buttonAanschr);
  }

  @Override
  public void checkButtons() {
    // DossierNaturalisatie dossierNaturalisatie = (DossierNaturalisatie) dossier.getZaakDossier();
    // buttonAanschr.setEnabled(!dossierNaturalisatie.getBetrokkenen().isEmpty());
    super.checkButtons();
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
        dossier = (Dossier) getApplication().getServices().getNaturalisatieService().getNewZaak();
      }

      if (!dossier.isStored()) {
        ZaakConfiguratieDialog.of(getApplication(), dossier, getApplication().getServices());
      }

      setProcessen(new NaturalisatieProcessen(dossier, getApplication()));
    }

    super.event(event);
  }
}
