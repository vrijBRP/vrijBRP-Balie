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

package nl.procura.gba.web.modules.bs.geboorte.page82;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.erkenning.ModuleErkenning;
import nl.procura.gba.web.modules.bs.erkenning.overzicht.ErkenningOverzichtLayout;
import nl.procura.gba.web.modules.bs.geboorte.page80.Page80Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page84.Page84Geboorte;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.geboorte.RedenVerplicht;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.ProcuraWindow;

/**
 * Geboorte
 */

public class Page82Geboorte<T extends DossierGeboorte> extends BsPage<T> {

  private final Button erkenningsButton = new Button("Ga naar het erkenningsproces");

  private ErkenningOverzichtLayout erkenningOverzichtLayout = null;

  public Page82Geboorte() {
    this("Geboorte - erkenning");
  }

  public Page82Geboorte(String caption) {

    super(caption);
    addButton(buttonPrev);
    addButton(erkenningsButton);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    if (!getZaakDossier().getVragen().heeftErkenningBijGeboorte()) {
      throw new ProException(WARNING, "Rond eerst de erkenning af.");
    }

    if (!getZaakDossier().getErkenningBijGeboorte().getDossier().getStatus().isMinimaal(ZaakStatusType.OPGENOMEN)) {
      throw new ProException(WARNING, "Voltooi eerste het erkenningsproces.");
    }

    return super.checkPage();
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInfo("Overzicht van het erkenningsproces");

      addComponent(new Page82GeboorteForm1(getZaakDossier()));

      getErkenningsProces();

      if (getZaakDossier().getVragen().heeftErkenningBijGeboorte()) {

        erkenningOverzichtLayout = new ErkenningOverzichtLayout(getZaakDossier().getErkenningBijGeboorte());

        addComponent(erkenningOverzichtLayout);
      }

    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == erkenningsButton) {
      getWindow().addWindow(new ErkenningWindow(getWindow(), getErkenningsProces()));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {
    getNavigation().goToPage(Page84Geboorte.class);
    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPage(Page80Geboorte.class);
    super.onPreviousPage();
  }

  /**
   * Zet het erkenningsproces op
   */
  private Dossier getErkenningsProces() {

    Dossier dossier;
    DossierErkenning erkenning;

    if (getZaakDossier().getVragen().heeftErkenningBijGeboorte()) {
      dossier = getZaakDossier().getErkenningBijGeboorte().getDossier();
      erkenning = (DossierErkenning) dossier.getZaakDossier();
    } else {

      // Nieuwe zaken en automatisch de aangever als erkenning aanmerken

      dossier = (Dossier) getServices().getErkenningService().getNewZaak();
      dossier.setDatumIngang(new DateTime());
      erkenning = (DossierErkenning) dossier.getZaakDossier();

      if (getZaakDossier().getAangever().getGeslacht() == Geslacht.MAN) {
        BsPersoonUtils.kopieDossierPersoon(getZaakDossier().getAangever(), erkenning.getErkenner());
      } else {
        if (getZaakDossier().getRedenVerplichtBevoegd() == RedenVerplicht.DUO_MOEDER) {
          BsPersoonUtils.kopieDossierPersoon(getZaakDossier().getAangever(), erkenning.getErkenner());
        }
      }
    }

    // Kopieer moeder naar erkenning
    BsPersoonUtils.kopieDossierPersoon(getZaakDossier().getMoeder(), erkenning.getMoeder());

    erkenning.setDossierGeboorte(getZaakDossier());
    erkenning.setErkenningsType(ErkenningsType.ERKENNING_BIJ_AANGIFTE);
    erkenning.setGelijkMetGeboorte(true);

    // Eerkenning opslaan
    getServices().getErkenningService().save(dossier);

    // Geboorte opslaan
    getZaakDossier().setErkenningBijGeboorte(erkenning);

    getServices().getGeboorteService().save(getDossier());

    for (DossierPersoon geboorteKind : getZaakDossier().getKinderen()) {
      DossierPersoon erkenningKind;
      if (erkenning.getDossier().heeftPersoon(geboorteKind)) {
        erkenningKind = erkenning.getDossier().getPersoon(geboorteKind);
        BsPersoonUtils.kopieDossierPersoon(geboorteKind, erkenningKind);
      } else {
        erkenningKind = BsPersoonUtils.kopieNieuwDossierPersoon(geboorteKind);
        getServices().getDossierService().addKind(erkenning, erkenningKind);
      }
      if (erkenningKind != null) {
        for (DossierNationaliteit natio : getDossier().getNationaliteiten()) {
          erkenningKind.toevoegenNationaliteit(natio);
        }
      }
    }
    return dossier;
  }

  /**
   * Update het overzichtscherm met de erkenningsgegevens
   */
  private void updateOverzichtscherm() {

    if (getZaakDossier().getVragen().heeftErkenningBijGeboorte()) {

      if (erkenningOverzichtLayout != null) {
        removeComponent(erkenningOverzichtLayout);
      }

      erkenningOverzichtLayout = new ErkenningOverzichtLayout(getZaakDossier().getErkenningBijGeboorte());
      addComponent(erkenningOverzichtLayout);
    }
  }

  public class ErkenningWindow extends GbaModalWindow {

    private ErkenningWindow(ProcuraWindow parentWindow, Dossier dossier) {

      setCaption("Erkenning bij geboorte");

      parentWindow.getParent();

      setWidth("90%");
      setHeight("80%");

      if (parentWindow.isModal()) {
        setWidth("80%");
        setHeight("70%");
      }

      VerticalLayout v = new VerticalLayout();
      v.setSizeFull();
      v.setMargin(false);
      v.addComponent(new ModuleErkenning(dossier));

      setContent(v);
    }

    @Override
    public void closeWindow() {

      updateOverzichtscherm();

      super.closeWindow();
    }
  }
}
