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

package nl.procura.gba.web.modules.bs.onderzoek;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;

import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.dialogs.ZaakConfiguratieDialog;
import nl.procura.gba.web.modules.bs.common.modules.BsModule;
import nl.procura.gba.web.modules.bs.onderzoek.aanschrijving.AanschrijvingWindow;
import nl.procura.gba.web.modules.bs.onderzoek.onderzoekstelling.OnderzoekStellingWindow;
import nl.procura.gba.web.modules.bs.onderzoek.page30.Page30Onderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.page40.Page40Onderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.processen.OnderzoekProcessen;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

@ModuleAnnotation(url = "#bs.onderzoek",
    caption = "Onderzoek",
    profielActie = ProfielActie.SELECT_ZAAK_ONDERZOEK)
public class ModuleOnderzoek extends BsModule {

  private Dossier dossier;
  private Button  buttonAanschr;
  private Button  buttonOnderzoek;

  public ModuleOnderzoek() {
  }

  public ModuleOnderzoek(Dossier dossier) {
    this.dossier = dossier;
  }

  @Override
  protected void setExtraButtons(Layout layout) {
    // Aanschrijving
    buttonAanschr = new Button("Aanschrijving / afdrukken");
    buttonAanschr.setWidth("100%");
    buttonAanschr.addListener((Button.ClickListener) event -> {

      Page30Onderzoek uitbreiding = VaadinUtils.getChild(this, Page30Onderzoek.class);
      Page40Onderzoek resultaat = VaadinUtils.getChild(this, Page40Onderzoek.class);

      if (uitbreiding != null) {
        uitbreiding.checkPage();
      }

      if (resultaat != null) {
        resultaat.checkPage();
      }

      getApplication().getParentWindow()
          .addWindow(new AanschrijvingWindow((DossierOnderzoek) dossier.getZaakDossier()));
    });

    layout.addComponent(buttonAanschr);

    //In onderzoekstelling
    buttonOnderzoek = new Button("Onderzoek / deelresultaat");
    buttonOnderzoek.setWidth("100%");
    buttonOnderzoek.addListener((Button.ClickListener) event -> {
      getApplication().getParentWindow()
          .addWindow(new OnderzoekStellingWindow((DossierOnderzoek) dossier.getZaakDossier()));
    });
    layout.addComponent(buttonOnderzoek);
  }

  @Override
  public void checkButtons() {
    DossierOnderzoek dossierOnderzoek = (DossierOnderzoek) dossier.getZaakDossier();
    buttonOnderzoek.setEnabled(!dossierOnderzoek.getBetrokkenen().isEmpty());
    buttonAanschr.setEnabled(!dossierOnderzoek.getBetrokkenen().isEmpty());
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
        dossier = (Dossier) getApplication().getServices().getOnderzoekService().getNewZaak();
      }

      if (!dossier.isStored()) {
        ZaakConfiguratieDialog.of(getApplication(), dossier, getApplication().getServices());
      }

      setProcessen(new OnderzoekProcessen(dossier, getApplication()));
    }

    super.event(event);
  }
}
