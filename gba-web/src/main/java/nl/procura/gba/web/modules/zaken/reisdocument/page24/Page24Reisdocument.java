/*
 * Copyright 2023 - 2024 Procura B.V.
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
import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.REISDOCUMENTAANVRAAG;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.FS_REISDOC;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.vaadin.theme.twee.ProcuraTheme.ICOON_24.WARNING;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.ui.Button;

import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvragenResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponse;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.application.ProcessChangeInterceptor;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.modules.zaken.common.ZaakHeaderForm;
import nl.procura.gba.web.modules.zaken.reisdocument.ReisdocumentAanvraagPage;
import nl.procura.gba.web.modules.zaken.reisdocument.overzicht.ReisdocumentOverzichtLayoutForm3;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.AanvraagArchiefButton;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.BasisregisterButton;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.SignaleringWindow;
import nl.procura.gba.web.modules.zaken.reisdocument.page14.Page14Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page14.Page14ReisdocumentTable1;
import nl.procura.gba.web.services.beheer.raas.AfsluitRequest;
import nl.procura.gba.web.services.beheer.vrs.VrsService;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentStatus;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;
import nl.procura.gba.web.services.zaken.reisdocumenten.SluitingType;
import nl.procura.raas.rest.domain.aanvraag.AfsluitingStatusType;
import nl.procura.raas.rest.domain.aanvraag.LeveringStatusType;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.validation.Bsn;

/**
 * Uitreiken reisdocument
 */
public class Page24Reisdocument extends ReisdocumentAanvraagPage {

  private final Button buttonDocumenten = new Button("Reisdocumenten");

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
    addButton(buttonDocumenten);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      ReisdocumentService reisdocService = getServices().getReisdocumentService();
      VrsService vrsService = reisdocService.getVrsService();

      Optional<SignaleringResult> signalering = reisdocService
          .checkAanvraag(getAanvraag().getAanvraagnummer(), getPl())
          .filter(SignaleringResult::isHit);

      Optional<ReisdocumentInformatiePersoonsGegevensInstantieResponse> vrsDocumenten = vrsService
          .getReisdocumenten(new VrsRequest()
              .aanleiding(REISDOCUMENTAANVRAAG)
              .bsn(new Bsn(getPl().getPersoon().getBsn().toLong()))
              .aanvraagnummer(getAanvraag().getAanvraagnummer().getNummer()));

      Optional<ControleAanvragenResponse> vrsAanvragen = vrsService.getAanvragen(new VrsRequest()
          .aanleiding(REISDOCUMENTAANVRAAG)
          .bsn(new Bsn(getPl().getPersoon().getBsn().toLong()))
          .aanvraagnummer(getAanvraag().getAanvraagnummer().getNummer()));

      infoLayout = new InfoLayout("Ter informatie", "");
      headerForm = new ZaakHeaderForm(getAanvraag());
      form1 = new Page24ReisdocumentForm1(getAanvraag());
      form4 = new ReisdocumentOverzichtLayoutForm3(getAanvraag(), signalering.orElse(null));
      table1 = new Table1(getPl());
      uitreikLayout = new UitreikLayout();

      addComponent(infoLayout);
      addComponent(headerForm);
      addComponent(new Fieldset("Huidige documenten van deze persoon", table1));
      addComponent(form4);
      addComponent(uitreikLayout);

      checkInfo();

      signalering.ifPresent(response -> {
        Button signaleringButton = new Button("Signalering");
        signaleringButton.addListener((Button.ClickListener) e -> this.showSignalering(response));
        addButton(signaleringButton);
      });

      vrsDocumenten.ifPresent(response -> {
        addButton(new BasisregisterButton(getAanvraag().getAanvraagnummer(), response));
      });

      vrsAanvragen.ifPresent(result -> {
        addButton(new AanvraagArchiefButton(getAanvraag().getAanvraagnummer(), result, true));
      });

    } else if (event.isEvent(AfterReturn.class)) {
      checkInfo();
      table1.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonDocumenten) {

      getNavigation().goToPage(new Page14Reisdocument(getPl()));
    } else if (isKeyCode(button, keyCode, F1, buttonPrev)) {

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
    String msg = form4.isSprakeVanSignalering()
        ? "Wilt u de gegevens opslaan ondanks de signalering?"
        : "Wilt u de gegevens opslaan?";

    getWindow().addWindow(new ConfirmDialog(msg, 400) {

      @Override
      public void buttonYes() {
        LeveringType statLev = form1.getBean().getAflevering();
        SluitingType statAfsl = form1.getBean().getAfsluiting();
        Long aanvrNr = getAanvraag().getAanvraagnummer().toLong();

        // Update RAAS service
        if (getServices().getRaasService().isRaasServiceActive()) {
          if (getServices().getRaasService().isAanvraag(aanvrNr)) {
            AfsluitRequest afsluitRequest = new AfsluitRequest();
            afsluitRequest.setAanvraagNummer(aanvrNr);
            afsluitRequest.setNrNLDocIn("");
            afsluitRequest.setStatusLevering(LeveringStatusType.getByCode(statLev.getCode()));
            afsluitRequest.setStatusAfsluiting(AfsluitingStatusType.getByCode(statAfsl.getCode()));
            getServices().getRaasService().updateAanvraag(afsluitRequest.createRequest());
          }
        }

        ReisdocumentStatus status = getAanvraag().getReisdocumentStatus();
        status.setStatusLevering(statLev);
        status.setStatusAfsluiting(statAfsl);
        status.setDatumTijdAfsluiting(new DateTime());
        getAanvraag().setGebruikerUitgifte(new UsrFieldValue(getServices().getGebruiker()));

        // Opslaan aanvraag
        getServices().getReisdocumentService().save(getAanvraag());

        headerForm.updateBean();
        form4.updateBean();
        successMessage("De gegevens zijn opgeslagen");
        Page24Reisdocument.this.getApplication().getProcess().endProcess();
        super.buttonYes();
      }
    });

    super.onSave();
  }

  private void checkInfo() {

    buttonSave.setEnabled(true);
    uitreikLayout.setVisible(true);

    StringBuilder info = new StringBuilder(
        "Controleer of het uit te reiken document klopt aan de hand van het documentnummer.<br/>Geef vervolgens de nieuwe status in een druk op opslaan.");

    List<String> fouten = new ArrayList<>();

    DocumentInhoudingenService db = getServices().getDocumentInhoudingenService();

    if (db.moetNogInleveren(getPl())) {

      int count = db.getInTeLeverenDocumenten(getPl()).size();
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

    if (fouten.size() > 0) {

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

      getApplication().getProcess().startProcess();
    }

    InfoLayout newinfoLayout = new InfoLayout("Ter informatie", info.toString());

    replaceComponent(infoLayout, newinfoLayout);

    infoLayout = newinfoLayout;
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
