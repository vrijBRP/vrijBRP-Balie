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

package nl.procura.gba.web.modules.bs.onderzoek.page30;

import com.vaadin.ui.Button;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.onderzoek.BsPageOnderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow.BronWindow;
import nl.procura.gba.web.modules.bs.onderzoek.page40.PlWarningLayout;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Onderzoek - uitbreiding
 */
public class Page30Onderzoek extends BsPageOnderzoek {

  private final Button         buttonAddBron = new Button("Toevoegen");
  private final Button         buttonDelBron = new Button("Verwijderen");
  private final VLayout        vervolgLayout = new VLayout();
  private Page30OnderzoekForm1 form1;
  private Page30OnderzoekForm2 form2;
  private Page30OnderzoekTable table;
  private PlWarningLayout      warningLayout;

  public Page30Onderzoek() {
    super("Onderzoek - uitbreiding");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonNext);

      form1 = new Page30OnderzoekForm1(getZaakDossier()) {

        @Override
        protected void onChangeVervolg(Boolean vervolg) {
          setVervolg(vervolg);
        }
      };

      addComponent(new BsStatusForm(getDossier()));
      setInfo("Bepaal aan de hand van de beschikbare informatie of uitbreiding van het onderzoek noodzakelijk is. " +
          "<br/>Vul - indien van toepassing - de vervolgactie(s) in. " +
          "<br/>Pas indien nodig de afhandeltermijn aan in de TMV. " +
          "Druk op Volgende (F2) om verder te gaan");

      warningLayout = new PlWarningLayout(getZaakDossier(), false) {

        @Override
        protected void onGeenWijzigingen() {
          infoMessage("Er zijn geen wijzigingen");
        }
      };

      addComponent(warningLayout);
      addComponent(form1);
      addComponent(vervolgLayout);
      setVervolg(getZaakDossier().getFase1Vervolg());
      warningLayout.reload(false);
    }

    super.event(event);
  }

  private void setVervolg(Boolean vervolg) {

    form2 = null;
    vervolgLayout.removeAllComponents();

    if (vervolg != null && vervolg) {
      form2 = new Page30OnderzoekForm2(form1, getZaakDossier());
      vervolgLayout.addComponent(form2);
      table = new Page30OnderzoekTable(getZaakDossier());

      OptieLayout ol = new OptieLayout();
      ol.getRight().setWidth("200px");
      ol.getRight().setCaption("Opties");
      ol.getRight().addButton(buttonAddBron, this);
      ol.getRight().addButton(buttonDelBron, this);
      ol.getLeft().addComponent(new Fieldset("Externe bron(nen)"));
      ol.getLeft().addComponent(table);
      vervolgLayout.add(ol);
    }
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (buttonAddBron.equals(button)) {
      getWindow().addWindow(new BronWindow(getZaakDossier(), new DossierOnderzoekBron()) {

        @Override
        public void closeWindow() {
          table.init();
          super.closeWindow();
        }
      });
    } else if (buttonDelBron.equals(button)) {
      new DeleteProcedure<DossierOnderzoekBron>(table) {

        @Override
        public void deleteValue(DossierOnderzoekBron bron) {
          getServices().getOnderzoekService().deleteBron(getZaakDossier(), bron);
        }

        @Override
        protected void afterDelete() {
          table.init();
        }
      };
    }
    super.handleEvent(button, keyCode);
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {
      form1.commit();
      getZaakDossier().setFase1DatumIngang(new DateTime(form1.getBean().getStartFase1Op()));
      getZaakDossier().setFase1DatumEinde(new DateTime(form1.getBean().getStartFase1Tm()));
      getZaakDossier().setFase1Reactie(form1.getBean().getReactieOntvangen());
      getZaakDossier().setFase1Toelichting(form1.getBean().getToelichting1());
      getZaakDossier().setFase1Vervolg(form1.getBean().getVervolgacties());

      getZaakDossier().setFase2DatumIngang(null);
      getZaakDossier().setFase2DatumEinde(null);
      getZaakDossier().setFase2OnderzoekGewenst(null);
      getZaakDossier().setFase2DatumOnderzoek(null);
      getZaakDossier().setFase2Toelichting(null);

      if (form2 != null) {
        form2.commit();
        getZaakDossier().setFase2DatumIngang(new DateTime(form2.getBean().getStartFase2Op()));
        getZaakDossier().setFase2DatumEinde(new DateTime(form2.getBean().getStartFase2Tm()));
        getZaakDossier().setFase2OnderzoekGewenst(form2.getBean().getOnderzoekTerPlaatse());
        getZaakDossier().setFase2DatumOnderzoek(new DateTime(form2.getBean().getUitgevoerdOp()));
        getZaakDossier().setFase2Toelichting(form2.getBean().getToelichting2());
      }

      getServices().getOnderzoekService().save(getDossier());
      return true;
    }

    return false;
  }

  @Override
  public void onPreviousPage() {
    checkPersonsInResearch(() -> goToPreviousProces());
  }

  @Override
  public void onNextPage() {
    checkPersonsInResearch(() -> goToNextProces());
  }

  /**
   * Only proceed if persons are 'in research' or user chooses to proceed.
   */
  private void checkPersonsInResearch(ChangeListener p) {
    if (getZaakDossier().getAantalOnderzoek() == 0) {
      getWindow().addWindow(new ConfirmDialog("Geen personen in onderzoek?",
          "Er staan op dit moment geen persoonslijsten in onderzoek.</br>" +
              "Is dit de bedoeling?",
          "400px") {

        @Override
        public void buttonYes() {
          super.buttonYes();
          p.onChange();
        }
      });
    } else {
      p.onChange();
    }
  }
}
