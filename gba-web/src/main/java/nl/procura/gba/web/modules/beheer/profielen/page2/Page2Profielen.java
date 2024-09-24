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

package nl.procura.gba.web.modules.beheer.profielen.page2;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.beheer.profielen.ModuleProfielPageTemplate;
import nl.procura.gba.web.modules.beheer.profielen.page10.IndicatiesTab;
import nl.procura.gba.web.modules.beheer.profielen.page11.Page11Profielen;
import nl.procura.gba.web.modules.beheer.profielen.page12.ZaakConfiguratiesTab;
import nl.procura.gba.web.modules.beheer.profielen.page3.Page3Profielen;
import nl.procura.gba.web.modules.beheer.profielen.page4.Page4Profielen;
import nl.procura.gba.web.modules.beheer.profielen.page5.Page5Profielen;
import nl.procura.gba.web.modules.beheer.profielen.page6.Page6Profielen;
import nl.procura.gba.web.modules.beheer.profielen.page8.Page8Profielen;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.commons.core.exceptions.ProException;

public class Page2Profielen extends ModuleProfielPageTemplate {

  private final Button buttonGebruikers        = new Button("Gebruikers koppelen");
  private final Button buttonParameters        = new Button("Parameters instellen");
  private final Button buttonActies            = new Button("Acties instellen");
  private final Button buttonVeld              = new Button("Velden instellen");
  private final Button buttonGbaElement        = new Button("BRP-elementen instellen");
  private final Button buttonGbaCatHist        = new Button("BRP-categorie historie");
  private final Button buttonIndicaties        = new Button("Indicaties");
  private final Button buttonZaakConfiguraties = new Button("Zaak configuraties");

  private final Page2ProfielenForm form;
  private Profiel                  profiel;

  public Page2Profielen(Profiel profiel) {

    super("Toevoegen / muteren profielen");
    setProfiel(profiel);

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
    addButton(buttonGbaElement);

    form = new Page2ProfielenForm(profiel);

    OptieLayout ol = new OptieLayout();
    ol.getLeft().addComponent(form);

    ol.getRight().setWidth("200px");
    ol.getRight().setCaption("Opties");

    ol.getRight().addButton(buttonGebruikers, this);
    ol.getRight().addButton(buttonParameters, this);
    ol.getRight().addButton(buttonActies, this);
    ol.getRight().addButton(buttonVeld, this);
    ol.getRight().addButton(buttonGbaElement, this);
    ol.getRight().addButton(buttonGbaCatHist, this);
    ol.getRight().addButton(buttonIndicaties, this);
    ol.getRight().addButton(buttonZaakConfiguraties, this);

    checkButtons();
    addComponent(ol);
  }

  public Profiel getProfiel() {
    return profiel;
  }

  public void setProfiel(Profiel profiel) {
    this.profiel = profiel;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonGebruikers) {
      getNavigation().goToPage(new Page3Profielen(getProfiel()));
    } else if (button == buttonParameters) {
      getNavigation().goToPage(new Page11Profielen(getProfiel()));
    } else if (button == buttonActies) {
      getNavigation().goToPage(new Page4Profielen(getProfiel()));
    } else if (button == buttonVeld) {
      getNavigation().goToPage(new Page5Profielen(getProfiel()));
    } else if (button == buttonGbaElement) {
      getNavigation().goToPage(new Page6Profielen(getProfiel()));
    } else if (button == buttonGbaCatHist) {
      getNavigation().goToPage(new Page8Profielen(getProfiel()));
    } else if (button == buttonIndicaties) {
      getNavigation().goToPage(new IndicatiesTab(getProfiel()));
    } else if (button == buttonZaakConfiguraties) {
      getNavigation().goToPage(new ZaakConfiguratiesTab(getProfiel()));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNew() {

    setProfiel(new Profiel());
    form.reset();
    checkButtons();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();
    Page2ProfielenBean b = form.getBean();

    if (!getProfiel().isStored()) {
      checkProfiel(b.getProfiel());
    }

    getProfiel().setProfiel(b.getProfiel());
    getProfiel().setOmschrijving(b.getOmschrijving());

    getServices().getProfielService().save(getProfiel());
    checkButtons();

    successMessage("Profiel is opgeslagen.");

    super.onSave();
  }

  private void checkButtons() {

    for (Button b : new Button[]{ buttonGebruikers, buttonParameters, buttonActies, buttonVeld, buttonGbaElement,
        buttonGbaCatHist }) {
      b.setEnabled(getProfiel().isStored());
    }
  }

  private void checkProfiel(String profiel) {
    List<Profiel> profList = getServices().getProfielService().getProfielen();

    for (Profiel prof : profList) {
      if (prof.getProfiel().equals(profiel)) {
        throw new ProException(ENTRY, WARNING,
            "Het opgegeven profiel is reeds opgeslagen. Voer een " + "unieke profielnaam in a.u.b.");
      }
    }
  }
}
