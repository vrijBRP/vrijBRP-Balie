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

package nl.procura.gba.web.modules.bs.onderzoek.aanschrijving;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.gba.web.modules.bs.onderzoek.aanschrijving.AanschrijvingBean.F_AANSCHRIJFPERSOON;
import static nl.procura.gba.web.modules.bs.onderzoek.aanschrijving.AanschrijvingBean.F_EXTERNE_BRON;
import static nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType.FASE_1;
import static nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType.FASE_2;
import static nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType.FASE_BESLUIT;
import static nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType.FASE_EXTRA;
import static nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType.FASE_OVERIG;
import static nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType.FASE_VOORNEMEN;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.ONDERZOEK;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.ONDERZOEK_BESLUIT;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.ONDERZOEK_FASE1;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.ONDERZOEK_FASE2_EXTERNE_BRON;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.ONDERZOEK_FASE_EXTRA;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.ONDERZOEK_VOORNEMEN;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.components.layouts.form.document.PrintSelectRecordFilter;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.bs.onderzoek.document.DossierOnderzoekTemplateData;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanschrijvingFaseType;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Onderzoek - aanschrijving
 */
public class AanschrijvingPage extends NormalPageTemplate {

  private AanschrijvingForm      form1;
  private final VLayout          soortLayout = new VLayout();
  private PrintLayout            printLayout;
  private MotiveringLayout       motiveringLayout;
  private final DossierOnderzoek zaakDossier;

  public final Button buttonPreview = new Button("Voorbeeld / e-mailen");
  public final Button buttonPrint   = new Button("Afdrukken (F3)");

  public AanschrijvingPage(DossierOnderzoek zaakDossier) {
    this.zaakDossier = zaakDossier;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPreview);
      addButton(buttonPrint, 1f);
      addButton(buttonClose);

      motiveringLayout = new MotiveringLayout(getServices());

      form1 = new AanschrijvingForm(zaakDossier) {

        @Override
        protected void onChangeSoort(AanschrijvingFaseType soort) {
          motiveringLayout.load(soort);
          setSoort(soort);
        }
      };

      setInfo("Selecteer de persoon en het document. Druk het document af. "
          + "<br/>Druk op Volgende (F2) om verder te gaan.");

      addComponent(new HLayout(form1, motiveringLayout).widthFull().heightFull());
      addComponent(soortLayout);
      setSoort(zaakDossier.getAanschrijvingFase());
    }

    super.event(event);
  }

  private void setSoort(AanschrijvingFaseType fase) {

    soortLayout.removeAllComponents();
    printLayout = null;

    if (FASE_1.equals(fase)) {
      soortLayout.addComponent(getFase1Layout());

    } else if (FASE_2.equals(fase)) {
      soortLayout.addComponent(getFase2Layout());

    } else if (FASE_EXTRA.equals(fase)) {
      soortLayout.addComponent(getFaseExtraLayout());

    } else if (FASE_VOORNEMEN.equals(fase)) {
      soortLayout.addComponent(getFaseVoornemenLayout());

    } else if (FASE_BESLUIT.equals(fase)) {
      soortLayout.addComponent(getFaseBesluitLayout());

    } else if (FASE_OVERIG.equals(fase)) {
      soortLayout.addComponent(getFaseOverigLayout());
    }

    buttonPreview.setEnabled(printLayout != null);
    buttonPrint.setEnabled(printLayout != null);
  }

  private Fieldset getFase1Layout() {
    printLayout = new PrintLayout(zaakDossier, zaakDossier.getDossier(), null, ONDERZOEK_FASE1);
    VLayout layout = new VLayout();
    layout.addComponent(printLayout);
    return new Fieldset("Documenten - onderzoek (1e fase), 1e aanschrijving", layout);
  }

  private Fieldset getFase2Layout() {
    VLayout layout = new VLayout();
    printLayout = new PrintLayout(zaakDossier, zaakDossier.getDossier(), null, ONDERZOEK_FASE2_EXTERNE_BRON);
    layout.addComponent(printLayout);
    return new Fieldset("Documenten - onderzoek (2e fase), 2e (of latere) aanschrijving", layout);
  }

  private Fieldset getFaseExtraLayout() {
    VLayout layout = new VLayout();
    printLayout = new PrintLayout(zaakDossier, zaakDossier.getDossier(), null, ONDERZOEK_FASE_EXTRA);
    layout.addComponent(printLayout);
    return new Fieldset("Documenten - onderzoek, extra aanschrijving", layout);
  }

  private Fieldset getFaseVoornemenLayout() {
    VLayout layout = new VLayout();
    printLayout = new PrintLayout(zaakDossier, zaakDossier.getDossier(), null, ONDERZOEK_VOORNEMEN);
    layout.addComponent(printLayout);
    return new Fieldset("Documenten - onderzoek, voornemen ambtshalve wijziging", layout);
  }

  private Fieldset getFaseBesluitLayout() {
    VLayout layout = new VLayout();
    printLayout = new PrintLayout(zaakDossier, zaakDossier.getDossier(), null, ONDERZOEK_BESLUIT);
    layout.addComponent(printLayout);
    return new Fieldset("Documenten - onderzoek, besluit", layout);
  }

  private Fieldset getFaseOverigLayout() {
    VLayout layout = new VLayout();
    printLayout = new PrintLayout(zaakDossier, zaakDossier.getDossier(), null, ONDERZOEK);
    layout.addComponent(printLayout);
    return new Fieldset("Documenten - onderzoek, overige", layout);
  }

  public boolean checkPage() {
    form1.commit();
    zaakDossier.setAanschrijvingFase(form1.getBean().getSoort());

    switch (form1.getBean().getSoort()) {
      case FASE_1:
        zaakDossier.setAanschrDatumInFase1(new DateTime(form1.getBean().getDatumFase1()));
        zaakDossier.setAanschrDatumEindFase1(new DateTime(form1.getBean().getDatumFase1End()));
        break;
      case FASE_2:
        zaakDossier.setAanschrDatumInFase2(new DateTime(form1.getBean().getDatumFase2()));
        zaakDossier.setAanschrDatumEindFase2(new DateTime(form1.getBean().getDatumFase2End()));
        break;
      case FASE_EXTRA:
        zaakDossier.setAanschrDatumInExtra(new DateTime(form1.getBean().getDatumExtra()));
        zaakDossier.setAanschrDatumEindExtra(new DateTime(form1.getBean().getDatumExtraEnd()));
        break;
      case FASE_VOORNEMEN:
        zaakDossier.setAanschrDatumInVoornemen(new DateTime(form1.getBean().getDatumVoornemen()));
        zaakDossier.setAanschrDatumEindVoornemen(new DateTime(form1.getBean().getDatumVoornemenEnd()));
        break;
      case FASE_BESLUIT:
        zaakDossier.setAanschrDatumBesluit(new DateTime(form1.getBean().getDatumBesluit()));
        break;
    }

    motiveringLayout.save();
    getServices().getOnderzoekService().save(zaakDossier.getDossier());
    return false;
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonPrint) || (button == buttonPreview)) {
      if (printLayout == null) {
        throw new ProException(WARNING, "Selecteer een soort aanschrijving");
      }
    }

    if (button == buttonPrint || keyCode == ShortcutAction.KeyCode.F3) {
      setModel();
      printLayout.doPrint(false);
      getWindow().closeWindow();

    } else if (button == buttonPreview) {
      setModel();
      printLayout.doPrint(true);
    }

    super.handleEvent(button, keyCode);
  }

  private void setModel() {
    checkPage();

    DossierPersoon aanschrijfpersoon = (DossierPersoon) form1.getField(F_AANSCHRIJFPERSOON).getValue();
    DossierOnderzoekBron externeBron = (DossierOnderzoekBron) form1.getField(F_EXTERNE_BRON).getValue();
    String msg = motiveringLayout.getMessage();

    if (form1.getField(F_EXTERNE_BRON).isVisible()) {
      form1.getField(F_EXTERNE_BRON).commit();
      printLayout.setModel(new DossierOnderzoekTemplateData(zaakDossier, msg, externeBron));

    } else {
      form1.getField(F_AANSCHRIJFPERSOON).commit();
      printLayout.setModel(new DossierOnderzoekTemplateData(zaakDossier, msg, aanschrijfpersoon));
    }
  }

  private static class PrintLayout extends PrintMultiLayout {

    public PrintLayout(Object model, Zaak zaak, PrintSelectRecordFilter selectListener, DocumentType... types) {
      super(model, zaak, selectListener, types);
    }

    @Override
    public void doPrint(boolean isPreview) {
      super.doPrint(isPreview);
    }
  }
}
