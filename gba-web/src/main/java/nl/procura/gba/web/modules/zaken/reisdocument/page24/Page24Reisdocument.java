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

package nl.procura.gba.web.modules.zaken.reisdocument.page24;

import static com.vaadin.event.ShortcutAction.KeyCode.F1;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils.checkIdentificatieAkkoord;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.FS_REISDOC;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.vaadin.theme.twee.ProcuraTheme.ICOON_24.WARNING;

import com.vaadin.ui.Button;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.application.ProcessChangeInterceptor;
import nl.procura.gba.web.modules.zaken.common.ZaakHeaderForm;
import nl.procura.gba.web.modules.zaken.identificatie.IdentificatieWindow;
import nl.procura.gba.web.modules.zaken.reisdocument.ReisdocumentAanvraagPage;
import nl.procura.gba.web.modules.zaken.reisdocument.overzicht.ReisdocumentOverzichtLayoutForm3;
import nl.procura.gba.web.modules.zaken.reisdocument.page1.Page1Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.AanvraagArchiefButton;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.BasisregisterButton;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.BrpReisdocumentenButton;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringWindow;
import nl.procura.gba.web.modules.zaken.reisdocument.page14.Page14Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page14.Page14ReisdocumentTable1;
import nl.procura.gba.web.services.beheer.vrs.VrsService;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieStatusListener;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;
import nl.procura.gba.web.services.zaken.reisdocumenten.SluitingType;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.dialog.OkDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Uitreiken reisdocument
 */
public class Page24Reisdocument extends ReisdocumentAanvraagPage {

  private final Button buttonDocumenten = new BrpReisdocumentenButton(this::getPl, () -> Page24Reisdocument.this);

  private ZaakHeaderForm                   headerForm    = null;
  private Page24ReisdocumentForm1          form1         = null;
  private ReisdocumentOverzichtLayoutForm3 form4         = null;
  private InfoLayout                       infoLayout    = null;
  private Table1                           table1;
  private UitreikLayout                    uitreikLayout = null;

  public Page24Reisdocument(ReisdocumentAanvraag aanvraag) {
    super("Reisdocument: uitreiken", aanvraag);
    addButton(buttonPrev);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      ReisdocumentService reisdocService = getServices().getReisdocumentService();
      VrsService vrsService = reisdocService.getVrsService();

      Optional<SignaleringResult> signalering = reisdocService
          .checkAanvraag(getAanvraag().getAanvraagnummer(), getPl())
          .filter(SignaleringResult::isHit);

      infoLayout = new InfoLayout("Ter informatie", "");
      headerForm = new ZaakHeaderForm(getAanvraag());
      form1 = new Page24ReisdocumentForm1(getAanvraag());
      form4 = new ReisdocumentOverzichtLayoutForm3(getAanvraag(), signalering.orElse(null));
      table1 = new Table1(getPl());
      uitreikLayout = new UitreikLayout();

      addComponent(infoLayout);
      addComponent(headerForm);

      if (!vrsService.isRegistratieMeldingEnabled()) {
        addComponent(new Fieldset("Huidige documenten van deze persoon", table1));
      }

      addComponent(form4);
      addComponent(uitreikLayout);

      checkInfo();

      signalering.ifPresent(response -> {
        Button signaleringButton = new Button("Signalering");
        signaleringButton.addListener((Button.ClickListener) e -> this.showSignalering(response));
        addButton(signaleringButton);
      });

      if (vrsService.isBasisregisterEnabled()) {
        addButton(new BasisregisterButton(this::getPl, () -> getAanvraag().getAanvraagnummer(), this::checkInfo));
        addButton(new AanvraagArchiefButton(getPl(), getAanvraag().getAanvraagnummer(), true));
      }

      if (!vrsService.isRegistratieMeldingEnabled()) {
        addButton(buttonDocumenten);
      }

    } else if (event.isEvent(AfterReturn.class)) {
      checkInfo();
      table1.init();
    }
    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (isKeyCode(button, keyCode, F1, buttonPrev)) {
      ProcessChangeInterceptor processChangeInterceptor = new ProcessChangeInterceptor(getWindow(), null) {

        @Override
        protected void proceed(AccordionLink link) {
          onPreviousPage();
        }
      };
      getApplication().getProcess().intercept(processChangeInterceptor);
    } else {
      super.handleEvent(button, keyCode);
    }
  }

  @Override
  public void onSave() {
    form1.commit();
    checkIdentificatie(this::continueSave);
  }

  private void continueSave() {
    String msg = form4.isSprakeVanSignalering()
        ? "Wilt u de gegevens opslaan ondanks de signalering?"
        : "Wilt u de gegevens opslaan?";

    getWindow().addWindow(new ConfirmDialog(msg, 400) {

      @Override
      public void buttonYes() {
        LeveringType statLev = form1.getBean().getAflevering();
        SluitingType statAfsl = form1.getBean().getAfsluiting();
        getServices().getReisdocumentService().afsluiten(getAanvraag(), statLev, statAfsl, getServices().getGebruiker());
        headerForm.updateBean();
        form4.updateBean();
        super.buttonYes();
        showOkMessage(statAfsl);
      }
    });
  }

  private void showOkMessage(SluitingType statAfsl) {
    getApplication().getParentWindow().addWindow(new OkDialog("Voltooid.",
        String.format("De sluiting status is nu: <hr/><b>%s</b>.<hr/>Dit proces is voltooid.",
            statAfsl.getOms())) {

      @Override
      public void closeWindow() {
        if (getApplication() != null) {
          ((GbaApplication) getApplication()).getProcess().endProcess();
          getNavigation().removeOtherPages();
          getNavigation().goToPage(Page1Reisdocument.class);
          super.closeWindow();
        }
      }
    });
  }

  private void checkInfo() {
    buttonSave.setEnabled(true);
    uitreikLayout.setVisible(true);

    StringBuilder info = new StringBuilder(
        "Controleer of het uit te reiken document klopt aan de hand van het documentnummer.<br/>Geef vervolgens de nieuwe status in een druk op opslaan.");

    List<String> fouten = new ArrayList<>();

    DocumentInhoudingenService db = getServices().getDocumentInhoudingenService();

    if (db.moetNogInleveren(getPl())) {
      long count = db.getAantalInTeLeverenDocumenten(getPl());
      String aantal = count == 1 ? "één document" : count + " documenten";
      fouten.add(setClass(false, "Aanvrager dient nog " + aantal + " in te leveren"));
    }

    if (getPl().getPersoon().getStatus().isOpgeschort()) {
      fouten.add(setClass(false, "Uitreiking niet mogelijk wegens opschorting van de persoonlijst"));
    }

    if (getPl().getPersoon().getStatus().isBlokkering()) {
      fouten.add(setClass(false, "Uitreiking niet mogelijk wegens blokkering van de persoonlijst"));
    }

    ReisdocumentType inTeleverenDocument = db.getInTeleverenDocument(getPl(), getAanvraag().getReisdocumentType());
    if (inTeleverenDocument != null) {
      fouten.add(setClass(false, "Aanvrager dient nog een document (" + inTeleverenDocument +
          ") in te leveren"));
    }

    if (!isUitreikbareStatus()) {
      fouten.add(setClass(false, "De aanvraag heeft niet de juiste status voor uitreiking"));
    }

    if (!isFunctiescheidingCorrect()) {
      fouten.add(setClass(false, "Uitreiking niet mogelijk wegens functiescheiding"));
    }

    if (!fouten.isEmpty()) {
      buttonSave.setEnabled(false);
      uitreikLayout.setVisible(false);
      info.append("<ul>");

      for (String fout : fouten) {
        info.append("<li>");
        info.append(fout);
        info.append("</li>");
      }
      info.append("</ul>");

    } else {
      checkIdentificatie(() -> getApplication().getProcess().startProcess());
    }

    InfoLayout newinfoLayout = new InfoLayout("Ter informatie", info.toString());
    replaceComponent(infoLayout, newinfoLayout);
    infoLayout = newinfoLayout;
  }

  private void checkIdentificatie(Runnable runnable) {
    IdentificatieStatusListener idSuccesListener = (saved, newAdded) -> runnable.run();
    checkIdentificatieAkkoord(getParentWindow(), new IdentificatieWindow(idSuccesListener), idSuccesListener);
  }

  /**
   * is er sprake van functiescheiding en wordt uitreiking door dezelfde persoon gedaan?
   */
  private boolean isFunctiescheidingCorrect() {

    boolean fs = isTru(getApplication().getParmValue(FS_REISDOC));
    boolean eqGebruiker = along(getAanvraag().getIngevoerdDoor().getValue()) == getApplication().getServices()
        .getGebruiker().getCUsr();
    return (!fs || !eqGebruiker);
  }

  private void showSignalering(SignaleringResult signalering) {
    getWindow().addWindow(new SignaleringWindow(signalering));
  }

  private boolean isUitreikbareStatus() {
    return getAanvraag().getReisdocumentStatus().isUitTeReiken();
  }

  class Table1 extends Page14ReisdocumentTable1 {

    private Table1(BasePLExt pl) {
      super(pl);
    }

    @Override
    public void onClick(Record record) {
      getNavigation().goToPage(new Page14Reisdocument(getPl()));
      super.onClick(record);
    }
  }

  class UitreikLayout extends VLayout {

    UitreikLayout() {
      setWidth("100%");
      addComponent(new Fieldset("Nieuwe status reisdocument en aanvraag"));
      addComponent(new InfoLayout("", WARNING, "Vergeet niet om op <b>opslaan</b> te drukken na het uitreiken."));
      addComponent(form1);
    }
  }
}
