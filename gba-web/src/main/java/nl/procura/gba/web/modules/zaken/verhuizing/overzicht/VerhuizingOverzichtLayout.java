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

package nl.procura.gba.web.modules.zaken.verhuizing.overzicht;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.applicatie.onderhoud.LicenseType.RISK_ANALYSIS;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.modules.zaken.bs.page2.Page2BsTemplate;
import nl.procura.gba.web.modules.zaken.riskanalysis.window.NewRiskAnalysisWindow;
import nl.procura.gba.web.modules.zaken.verhuizing.overzicht.bewoners.BewonersOverzichtWindow;
import nl.procura.gba.web.modules.zaken.verhuizing.page2.*;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagService;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisPersoon;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public abstract class VerhuizingOverzichtLayout extends GbaVerticalLayout implements ClickListener {

  private final Button buttonRiskAnalysis  = new Button("Risicoanalyse");
  private final Button buttonToestemming   = new Button("Toestemming");
  private final Button buttonBewoners      = new Button("Woningkaart");
  private final Button ButtonZoekPersoon   = new Button("Zoek persoon");
  private final Button ButtonWelVerwerken  = new Button("Wel verwerken");
  private final Button ButtonNietVerwerken = new Button("Niet verwerken");

  private final Page2VerhuizingTable1 table1;
  private final VerhuisAanvraag       verhuizing;
  private final OptieLayout           optieLayout1 = new OptieLayout();
  private final OptieLayout           optieLayout2 = new OptieLayout();
  private Page2VerhuizingForm3        form3;

  public VerhuizingOverzichtLayout(final VerhuisAanvraag verhuizing) {
    this.verhuizing = verhuizing;
    setSpacing(true);

    if (verhuizing.getTypeVerhuizing() == VerhuisType.EMIGRATIE) {
      optieLayout1.getLeft().addComponent(new Page2VerhuizingForm2(verhuizing));
    } else {
      optieLayout1.getLeft().addComponent(new Page2VerhuizingForm1(verhuizing));
    }

    form3 = new Page2VerhuizingForm3(verhuizing);
    optieLayout1.getLeft().addComponent(form3);

    if (!verhuizing.getStatus().isEindStatus()) {
      optieLayout1.getLeft().addComponent(new InfoLayout("",
          "Selecteer hieronder, indien nodig, personen waarbij deze zaak niet verwerkt moet worden."));
    }

    addComponent(optieLayout1);

    table1 = new Page2VerhuizingTable1(verhuizing) {

      @Override
      public void onDoubleClick(Record record) {
        if (verhuizing.getStatus().isEindStatus()) {
          throw new ProException(WARNING,
              "Dit is niet mogelijk omdat de zaak al een eindstatus heeft.");
        }
        VerhuisPersoon verhuisPersoon = record.getObject(VerhuisPersoon.class);
        verhuisPersoon.setGeenVerwerking(!verhuisPersoon.isGeenVerwerking());
        onOpslaanVerwerking();
        table1.init();
      }
    };

    optieLayout2.getLeft().addComponent(new Fieldset(table1));

    PresentievraagService presentieVraagService = GbaApplication.getInstance().getServices().getPresentievraagService();
    List<Presentievraag> presentievragen = presentieVraagService.getPresenceQuestionsByZaakId(verhuizing.getZaakId());

    optieLayout2.getLeft().addComponent(new Fieldset("Presentievragen"));

    if (presentievragen.isEmpty()) {
      optieLayout2.getLeft().addComponent(new InfoLayout(null, "Er zijn geen presentievragen uitgevoerd."));
    } else {

      Page2VerhuizingTable2 table2 = new Page2VerhuizingTable2(presentievragen) {

        @Override
        public void onClick(Record record) {
          goToPresentievraag(record.getObject(Presentievraag.class));
        }
      };
      optieLayout2.getLeft().addComponent(table2);
    }

    optieLayout1.getRight().setWidth("180px");
    optieLayout1.getRight().setCaption("Opties");

    // Check is riskanalysis is allowed for this user
    if (Services.getInstance().getOnderhoudService().hasLicenseFor(RISK_ANALYSIS) &&
        Services.getInstance().getGebruiker().getProfielen().isProfielActie(ProfielActie.SELECT_CASE_RISK_ANALYSIS)) {
      optieLayout1.getRight().addButton(buttonRiskAnalysis, this);
    }

    optieLayout1.getRight().addButton(buttonToestemming, this);
    optieLayout1.getRight().addButton(buttonBewoners, this);

    buttonToestemming.setEnabled(verhuizing.isSprakeVanInwoning() && pos(
        verhuizing.getHoofdbewoner().getBurgerServiceNummer().getValue()));

    optieLayout2.getRight().setWidth("180px");
    optieLayout2.getRight().setCaption("Opties");
    optieLayout2.getRight().addButton(ButtonZoekPersoon, this);
    optieLayout2.getRight().addButton(ButtonWelVerwerken, this);
    optieLayout2.getRight().addButton(ButtonNietVerwerken, this);

    addComponent(optieLayout2);
  }

  @Override
  public void buttonClick(ClickEvent event) {

    if (event.getButton() == buttonToestemming) {
      onToestemming();
    } else if (event.getButton() == buttonBewoners) {
      onBewoners();
    } else if (event.getButton() == buttonRiskAnalysis) {
      onRiskAnalysis();
    } else if (asList(ButtonZoekPersoon, ButtonWelVerwerken, ButtonNietVerwerken).contains(event.getButton())) {
      if (!table1.isSelectedRecords()) {
        throw new ProException(WARNING, "Geen persoon geselecteerd");
      }

      Record selectedRecord = table1.getSelectedRecord();
      VerhuisPersoon verhuisPersoon = selectedRecord.getObject(VerhuisPersoon.class);

      if (event.getButton() == ButtonZoekPersoon) {
        goToPersoon(verhuisPersoon);
      } else if (asList(ButtonWelVerwerken, ButtonNietVerwerken).contains(event.getButton())) {
        if (verhuizing.getStatus().isEindStatus()) {
          throw new ProException(WARNING,
              "Dit is niet mogelijk omdat de zaak al een eindstatus heeft.");
        }
        verhuisPersoon.setGeenVerwerking(event.getButton() == ButtonNietVerwerken);
        onOpslaanVerwerking();
        table1.init();
      }
    }
  }

  protected abstract void goToPersoon(VerhuisPersoon persoon);

  protected abstract void goToPresentievraag(Presentievraag presentievraag);

  protected void onBewoners() {
    getParent().getWindow().addWindow(new BewonersOverzichtWindow(verhuizing));
  }

  protected void onRiskAnalysis() throws ProException {

    RiskAnalysisService service = getApplication().getServices().getRiskAnalysisService();
    RiskAnalysisRelatedCase relatedCase = new RiskAnalysisRelatedCase(verhuizing);
    Dossier dossier = service.getRiskAnalysisCase(relatedCase);

    if (dossier != null) {
      GbaPageTemplate page = VaadinUtils.getParent(this, GbaPageTemplate.class);
      // Navigate to page in Home window
      if (getParentWindow() instanceof HomeWindow) {
        ZaakregisterNavigator.navigatoTo(dossier, page, false);
      } else {
        // Navigate to page inside PL window
        page.getNavigation()
            .addPage(new Page2BsTemplate(dossier, DocumentType.RISK_ANALYSE, "bs.risicoanalyse",
                "Risicoanalyses - overzicht",
                ProfielActie.UPDATE_CASE_RISK_ANALYSIS));
      }

    } else if (service.isApplicable(verhuizing)) {
      if (Services.getInstance().getGebruiker().getProfielen().isProfielActie(ProfielActie.UPDATE_CASE_RISK_ANALYSIS)) {
        getParentWindow().addWindow(new NewRiskAnalysisWindow(relatedCase, () -> {
          // Reload tabs
          ZaakTabsheet tabsheet = VaadinUtils.getParent(this, ZaakTabsheet.class);
          if (tabsheet != null) {
            tabsheet.reloadTabs();
          }
        }));
      } else {
        throw new ProException(WARNING, "Geen autorisatie om een risicoanalyse toe te voegen");
      }
    } else {
      throw new ProException(WARNING, "Deze zaak komt niet in aanmerking voor een risicoanalyse");
    }
  }

  protected abstract void onOpslaanVerwerking();

  protected abstract void onToestemming();

  protected void resetForm3() {
    Page2VerhuizingForm3 form3New = new Page2VerhuizingForm3(verhuizing);
    optieLayout1.getLeft().replaceComponent(form3, form3New);
    form3 = form3New;
  }
}
