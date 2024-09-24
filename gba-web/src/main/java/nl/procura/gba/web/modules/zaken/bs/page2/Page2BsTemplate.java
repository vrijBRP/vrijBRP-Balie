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

package nl.procura.gba.web.modules.zaken.bs.page2;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.gba.web.modules.bs.common.pages.persooncontrole.BsPersoonControleWindow;
import nl.procura.gba.web.modules.bs.erkenning.overzicht.ErkenningOverzichtBuilder;
import nl.procura.gba.web.modules.bs.geboorte.overzicht.GeboorteOverzichtBuilder;
import nl.procura.gba.web.modules.bs.huwelijk.overzicht.form1.HuwelijkOverzichtBuilder;
import nl.procura.gba.web.modules.bs.lv.summary.LvSummaryBuilder;
import nl.procura.gba.web.modules.bs.naamskeuze.overzicht.NaamskeuzeOverzichtBuilder;
import nl.procura.gba.web.modules.bs.omzetting.overzicht.form1.OmzettingOverzichtBuilder;
import nl.procura.gba.web.modules.bs.onderzoek.overzicht.OnderzoekOverzichtBuilder;
import nl.procura.gba.web.modules.bs.ontbinding.overzicht.form1.OntbindingOverzichtBuilder;
import nl.procura.gba.web.modules.bs.overlijden.buitenland.overzicht.OverlijdenBuitenlandOverzichtBuilder;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.overzicht.OverlijdenGemeenteOverzichtBuilder;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.overzicht.LevenloosOverzichtBuilder;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.overzicht.LijkvindingOverzichtBuilder;
import nl.procura.gba.web.modules.bs.registration.summary.RegistrationSummaryBuilder;
import nl.procura.gba.web.modules.bs.riskanalysis.summary.RiskAnalysisSummaryBuilder;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.zaken.bs.page3.Page3BsTemplate;
import nl.procura.gba.web.modules.zaken.common.ZakenOverzichtPage;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.levenloos.DossierLevenloos;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.standard.exceptions.ProException;

/**
 * Inzage zaak scherm
 */
public class Page2BsTemplate extends ZakenOverzichtPage<Dossier> {

  private final String       fragment;
  private final DocumentType documentType;
  private final ProfielActie profielActie;

  public Page2BsTemplate(Dossier dossier, DocumentType documentType, String fragment, String caption,
      ProfielActie profielActie) {

    super(dossier, caption);

    this.documentType = documentType;
    this.fragment = fragment;
    this.profielActie = profielActie;

    setMargin(true);

    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {

    buttonAanpassen.setEnabled(getApplication().isProfielActie(profielActie));

    addOptieButton(buttonAanpassen);
    addOptieButton(buttonDoc);
    addOptieButton(buttonFiat);
    addOptieButton(buttonVerwerken);
  }

  @Override
  protected void addTabs(ZaakTabsheet<Dossier> tabsheet) {

    ZaakDossier zaakDossier = getZaak().getZaakDossier();

    if (zaakDossier instanceof DossierLevenloos) {
      LevenloosOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierGeboorte) {
      GeboorteOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierHuwelijk) {
      HuwelijkOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierOmzetting) {
      OmzettingOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierOntbinding) {
      OntbindingOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierOverlijdenGemeente) {
      OverlijdenGemeenteOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierLijkvinding) {
      LijkvindingOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierOverlijdenBuitenland) {
      OverlijdenBuitenlandOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierErkenning) {
      ErkenningOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierNaamskeuze) {
      NaamskeuzeOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierOnderzoek) {
      OnderzoekOverzichtBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierRiskAnalysis) {
      RiskAnalysisSummaryBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierRegistration) {
      RegistrationSummaryBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else if (zaakDossier instanceof DossierLv) {
      LvSummaryBuilder.addTab(tabsheet, getZaak().getZaakDossier());
    } else {
      throw new ProException(WARNING, "Deze zaak is onvolledig en kan niet worden geladen.");
    }
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page3BsTemplate(getZaak(), documentType, getTitle()));
  }

  @Override
  protected void goToZaak() {

    BsPersoonControleWindow window = new BsPersoonControleWindow(getZaak()) {

      @Override
      public void afterBijwerken() {
        getTabsheet().reloadTabs();
      }

      @Override
      public void onGoToZaak() {
        getGbaApplication().getServices().getMemoryService().setObject(Dossier.class, getZaak());
        getGbaApplication().openWindow(getParentWindow(), new HomeWindow(), fragment);
        close();
      }
    };

    getParentWindow().addWindow(window);
  }
}
