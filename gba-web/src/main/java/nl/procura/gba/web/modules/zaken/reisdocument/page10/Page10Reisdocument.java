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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.VLUCHTELINGEN_PASPOORT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.VREEMDELINGEN_PASPOORT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringStatusType.JA;
import static nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringStatusType.NEE;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import java.util.Optional;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.reisdocument.ReisdocumentAanvraagPage;
import nl.procura.gba.web.modules.zaken.reisdocument.page11.Page11Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page14.Page14Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page18.Page18Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page21.Page21Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page23.Page23Reisdocument;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.gba.web.services.zaken.legezaak.LegeZaak;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentC1;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Nieuw reisdocument
 */
public class Page10Reisdocument extends ReisdocumentAanvraagPage implements ValueChangeListener {

  private final Button            buttonDocumenten = new Button("Reisdocumenten");
  private Page10ReisdocumentForm1 form1            = null;
  private Page10ReisdocumentForm2 form2            = null;

  public Page10Reisdocument(ReisdocumentAanvraag aanvraag) {
    super("Reisdocument: nieuw", aanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      ReisdocumentAanvraag aanvraag = getAanvraag();
      BasePLExt pl = getPl();
      form1 = new Page10ReisdocumentForm1(pl, aanvraag, getServices());
      form1.setAfhaalLocaties(getApplication());
      form1.getReisdocumentField().addListener(this);

      ReisdocumentService reisdocumentService = getServices().getReisdocumentService();
      Optional<SignaleringResult> signalering = reisdocumentService.checkAanvraag(aanvraag.getAanvraagnummer(), pl);

      form2 = new Page10ReisdocumentForm2(pl, getServices().getDocumentInhoudingenService(), signalering.orElse(null));

      OptieLayout ol = new OptieLayout();
      ol.getLeft().addComponent(form1);

      OptieLayout.Right right = ol.getRight();
      right.setWidth("200px");
      right.setCaption("Opties");

      buttonDocumenten.setDescription("Overzicht documenten");

      right.addButton(buttonDocumenten, this);
      signalering.ifPresent(s -> right.addButton(new Button("Signalering"), e -> this.showSignalering(s)));
      addComponent(ol);
      addComponent(form2);

      recheckDocument();

    } else if (event.isEvent(AfterBackwardReturn.class)) {
      form2.recheckDocumenten();
      form2.repaint();
    }

    super.event(event);
  }

  private void showSignalering(SignaleringResult signalering) {
    getWindow().addWindow(new SignaleringWindow(signalering));
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonDocumenten) {
      getNavigation().goToPage(new Page14Reisdocument(getPl()));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    if (getNavigation().getPreviousPage() instanceof Page18Reisdocument) {
      throw new ProException(INFO, "U kunt niet meer terug naar het aanvraagnummer scherm");
    }
    super.onPreviousPage();
  }

  @Override
  public void onNextPage() {

    form1.commit();

    checkAanvraag();

    // Gooi bij signalering een vraag op

    if (form2.isSprakeVanSignalering()) {
      ConfirmDialog confirmDialog = new ConfirmDialog("Wilt u verder ondanks de signalering?") {

        @Override
        public void buttonYes() {
          closeWindow();
          continueNextPage();
        }
      };
      getWindow().addWindow(confirmDialog);

    } else {
      continueNextPage();
    }

    super.onNextPage();
  }

  @Override
  public void valueChange(ValueChangeEvent event) {

    if (event.getProperty() == form1.getReisdocumentField()) {
      // Bij wijziging reisdocument
      recheckDocument();
    }
  }

  private void checkAanvraag() {

    if (form2.moetNogDocumentInleveren()) {
      throw new ProException(ENTRY, WARNING, "Aanvrager dient eerst de verlopen documenten in te leveren.");
    }

    getAanvraag().setGebruikerUitgifte(new UsrFieldValue());
    getAanvraag().setGratis(form1.isJeugdTarief());
    getAanvraag().setLengte(toBigDecimal(along(form1.getBean().getLengte())));
    getAanvraag().setLocatieInvoer(getApplication().getServices().getGebruiker().getLocatie());
    getAanvraag().setRedenAfwezig(form1.getBean().getRedenNietAanwezig());
    getAanvraag().setReisdocumentType(form1.getReisdocumentType());
    getAanvraag().setSpoed(form1.getBean().getSpoed());
    getAanvraag().setSignalering(form2.isSprakeVanSignalering() ? JA : NEE);

    if (form1.getBean().getAfhaalLocatie() != null) {
      getAanvraag().setLocatieAfhaal(form1.getBean().getAfhaalLocatie());
    }
  }

  private void checkDerdeGezag(ReisdocumentType type) {
    form2.checkDerdeGezag(type);
  }

  private void checkJeugdTarief(ReisdocumentType type) {
    form1.checkJeugdTarief(type);
  }

  private void checkSignalering() {
    form2.checkSignalering();
  }

  private void continueNextPage() {

    boolean idkaart = form1.getReisdocumentType() == ReisdocumentType.NEDERLANDSE_IDENTITEITSKAART;
    int pllt = getPl().getPersoon().getGeboorte().getLeeftijd();
    int lt = form1.getReisdocumentType().getLeeftijdToestemming();
    boolean isLeeftijdToestemming = (lt > 0) && (pllt < lt);
    boolean isCuratele = !idkaart && this.getPl().getGezag().staatOnderCuratele();

    ReisdocumentService service = getApplication().getServices().getReisdocumentService();
    service.save(getAanvraag());

    // Een ID opvragen in het Zaak-DMS
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(getAanvraag());

    if (isLeeftijdToestemming || isCuratele) {
      // Naar toestemmingenscherm
      toNextPage(new Page21Reisdocument(isLeeftijdToestemming, getAanvraag()));

    } else {
      toNextPage(new Page11Reisdocument(getAanvraag()));
    }
  }

  private void gotoC1() {

    ReisdocumentC1 c1 = new ReisdocumentC1();

    for (Reisdocument document : getServices().getReisdocumentService().getReisdocumentHistorie(getPl())) {
      ReisdocumentType reisdocumentType = ReisdocumentType.get(document.getNederlandsReisdocument().getVal());
      if (reisdocumentType.isDocument(VREEMDELINGEN_PASPOORT, VLUCHTELINGEN_PASPOORT)) {
        if (!pos(document.getDatumInhoudingVermissing().getCode())) {
          c1.setDocumentSoort(document.getNederlandsReisdocument().getVal());
          c1.setDocumentSoortVreemdelingen(reisdocumentType.isDocument(VREEMDELINGEN_PASPOORT));
          c1.setDocumentSoortVluchtelingen(reisdocumentType.isDocument(VLUCHTELINGEN_PASPOORT));
          c1.setDocumentNummer(document.getDocumentNummerFormat());
          c1.setDatumVerstrekt(document.getDatumUitgifte().getDescr());
          c1.setDatumEindeGeldig(document.getDatumEindeGeldigheid().getDescr());
          c1.setVerstrektDoor(document.getAutoriteitVanAfgifte().getDescr());

          DocumentInhouding inhouding = getServices().getDocumentInhoudingenService().getInhouding(getPl(),
              document);
          if (inhouding != null && inhouding.getInhoudingType() == InhoudingType.VERMISSING) {
            c1.setDatumVerklaring(inhouding.getDatumTijdInvoer().getFormatDate());
          }
        }
      }
    }

    c1.setAanvraag(getAanvraag());
    c1.setPersoon(new DocumentPL(getPl()));

    LegeZaak legeZaak = new LegeZaak(getPl(), getApplication().getServices().getGebruiker());
    getNavigation().goToPage(new Page23Reisdocument(c1, legeZaak));
  }

  private void recheckDocument() {

    ReisdocumentType type = form1.getReisdocumentType();

    checkJeugdTarief(type);
    checkDerdeGezag(type);
    checkSignalering();

    form1.repaint();
    form2.repaint();
  }

  private void toNextPage(final ZakenPage page) {
    if (form1.getReisdocumentType() == ReisdocumentType.VREEMDELINGEN_PASPOORT) {
      ConfirmDialog confirmDialog = new ConfirmDialog("Wilt u nog een <b>C1-document</b> maken?") {

        @Override
        public void buttonNo() {
          super.buttonNo();
          Page10Reisdocument.this.getNavigation().goToPage(page);
        }

        @Override
        public void buttonYes() {
          super.buttonYes();
          gotoC1();
        }
      };

      getWindow().addWindow(confirmDialog);

    } else {
      getNavigation().goToPage(page);
    }
  }
}
