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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2;

import static nl.procura.gba.common.MiscUtils.cleanPath;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.beheer.gebruikers.page2.Page2GebruikersBean.MAP;
import static nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieSoortType.*;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;
import static nl.procura.commons.core.exceptions.ProExceptionType.PROGRAMMING;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import com.vaadin.ui.Button;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.beheer.documenten.*;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page5.Page5Documenten;
import nl.procura.gba.web.modules.beheer.overig.CheckPadEnOpslaanDocument;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab1DocumentenPage2 extends DocumentenTabPage {

  private static final String     SAVE_FORMATS_ERROR  = "Er gaat iets mis bij het opslaan van de uitvoerformaten.";
  private final Button            buttonGebr          = new Button("Gebruikers");
  private final Button            buttonPrintERS      = new Button("Printers");
  private final Button            buttonStempels      = new Button("Stempels");
  private final Button            buttonKenmerken     = new Button("Kenmerken");
  private final Button            buttonGv            = new Button("Gegevensverstrekking");
  private final OptieLayout       optieLayout         = new OptieLayout();
  private Tab1DocumentenPage2Form form                = null;
  private UitvoerformaatForm      formUitvoerformaat  = null;
  private OpslagEnToegangForm     formOpslagEnToegang = null;
  private DocumentRecord          document;
  private InfoLayout              geenKoppelingLayout = null;

  public Tab1DocumentenPage2(DocumentRecord document) {

    super("Toevoegen / muteren document");
    setMargin(true);
    this.document = document;

    addButton(buttonPrev, buttonNew, buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new Tab1DocumentenPage2Form(document);
      formUitvoerformaat = new UitvoerformaatForm(document);
      formOpslagEnToegang = new OpslagEnToegangForm(document);

      optieLayout.getLeft().addComponent(form);
      optieLayout.getLeft().addComponent(formUitvoerformaat);
      optieLayout.getLeft().addComponent(formOpslagEnToegang);

      optieLayout.getRight().setWidth("200px");
      optieLayout.getRight().setCaption("Gegevens koppelen");

      optieLayout.getRight().addButton(buttonGebr, this);
      optieLayout.getRight().addButton(buttonPrintERS, this);
      optieLayout.getRight().addButton(buttonStempels, this);
      optieLayout.getRight().addButton(buttonKenmerken, this);
      optieLayout.getRight().addButton(buttonGv, this);

      addComponent(optieLayout);

      geenKoppelingLayout = new InfoLayout(setClass("red", "Dit document is nog niet gekoppeld aan gebruikers"),
          "");

      checkGebruikerKoppeling();
      checkButtonGebrKoppelen();
      checkButtonKmKoppelen();
      checkButtonGvKoppelen();
    } else if (event.isEvent(AfterReturn.class)) {
      checkGebruikerKoppeling();
    }

    super.event(event);
  }

  public DocumentRecord getDocument() {
    return document;
  }

  public void setDocument(DocumentRecord document) {
    this.document = document;
  }

  public Tab1DocumentenPage2Form getForm() {
    return form;
  }

  public void setForm(Tab1DocumentenPage2Form form) {
    this.form = form;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (buttonGebr.equals(button)) {
      getNavigation().goToPage(new KoppelGebruikersAanDocumentenPage(document));
    } else if (buttonPrintERS.equals(button)) {
      getNavigation().goToPage(new KoppelPrintoptiesAanDocumentenPage(document));
    } else if (buttonStempels.equals(button)) {
      getNavigation().goToPage(new KoppelStempelsAanDocumentenPage(document));
    } else if (buttonKenmerken.equals(button)) {
      getNavigation().goToPage(new KoppelKenmerkenAanDocumentenPage(document));
    } else if (buttonGv.equals(button)) {
      getNavigation().goToPage(
          new Page5Documenten(document, BINNENGEM, VERSTREK_BEP, GRONDSLAG, TOEKENNING, PROCESACTIE,
              REACTIE));
    }

    super.handleEvent(button, keyCode);
  }

  /**
   * Geeft terug of document gekoppeld is aan gebruikers.
   */
  public boolean isGekoppeldAanGebruikers() {
    if (getDocument().isIedereenToegang()) {
      return true;
    }
    return getDocument().isGekoppeldAanGebruikers();
  }

  @Override
  public void onNew() {
    form.reset();
    formUitvoerformaat.reset();
    formOpslagEnToegang.reset();

    setDocument(new DocumentRecord());
    checkGebruikerKoppeling();
    checkButtonGebrKoppelen();
    checkButtonKmKoppelen();
    checkButtonGvKoppelen();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();
    formUitvoerformaat.commit();
    formOpslagEnToegang.commit();

    Tab1DocumentenPage2Bean b = form.getBean();
    String docName = trim(b.getNaam());
    String cleanedPath = cleanPath(b.getMap());

    if (docNotSaved()) {
      checkDocumentName(docName);
    } else if (changeDocName(docName)) {
      checkDocumentName(docName);
    }

    new CheckPadEnOpslaanDocument(cleanedPath, b, form) {

      @Override
      protected void nietOpslaanDocumentActions() {
        form.setDocumentmapContainer(); // ingevoerde mapnaam wordt niet in lijstje opgenomen
        form.getField(MAP).setValue(getDocument().getPad());
      }

      @Override
      protected void opslaanDocument(String legalPath, Tab1DocumentenPage2Bean b) {
        Tab1DocumentenPage2.this.saveDocument(legalPath, b);
        form.setDocumentmapContainer(); // maakt veldje leeg bij eventueel lege map na clean()
      }

      @Override
      protected void welOpslaanDocumentActies(String legalPath, Tab1DocumentenPage2Bean b) {
        Tab1DocumentenPage2.this.saveDocument(legalPath, b);
        form.setDocumentmapContainer();
      }
    };
  }

  private boolean changeDocName(String docName) {
    return !getDocument().getDocument().equals(docName);
  }

  private void checkButtonGebrKoppelen() {
    buttonGebr.setEnabled(document.isStored());
  }

  private void checkButtonGvKoppelen() {
    buttonGv.setEnabled(document.isStored() && document.getDocumentType() == DocumentType.GV_AANVRAAG);
  }

  private void checkButtonKmKoppelen() {
    buttonKenmerken.setEnabled(document.isStored());
  }

  private void checkButtonPrintKoppelen() {
    buttonPrintERS.setEnabled(document.isStored());
  }

  private void checkButtonStempelsKoppelen() {
    buttonStempels.setEnabled(document.isStored());
  }

  /**
   * Controleert of er al een document opgeslagen is met de meegegeven string als naam.
   */

  private void checkDocumentName(String name) {
    List<DocumentRecord> docList = getAllDocuments();

    for (DocumentRecord doc : docList) {
      if (trim(doc.getDocument()).equals(name)) {
        throw new ProException(ENTRY, WARNING,
            "De ingevoerde naam is reeds opgeslagen. Kies" + " een unieke documentnaam a.u.b.");
      }
    }
  }

  /**
   * Toon een melding als het document niet gekoppeld is.
   */
  private void checkGebruikerKoppeling() {

    // aan iedereen gekoppeld
    if (!getDocument().isStored() || isGekoppeldAanGebruikers()) {
      removeComponent(geenKoppelingLayout);
    } else if (!pos(getComponentIndex(geenKoppelingLayout))) { // Niet al toegevoegd
      addComponent(geenKoppelingLayout, getComponentIndex(optieLayout));
    }
  }

  private boolean docNotSaved() {
    return !getDocument().isStored();
  }

  private List<DocumentRecord> getAllDocuments() {
    return getServices().getDocumentService().getDocumenten(false);
  }

  private void saveDocument(String docPath, Tab1DocumentenPage2Bean b) {

    UitvoerformaatBean bU = formUitvoerformaat.getBean();
    OpslagEnToegangBean bOT = formOpslagEnToegang.getBean();

    // documentform
    setDocumentSettings(docPath, b);

    // uitvoerformatenform
    setUitvoerformaten(bU);

    // opslag en toegang form
    setOpslagEnToegang(bOT);
    getDocument().setIedereenToegang(getDocument().isStored() && getDocument().isIedereenToegang());
    // voorkom dat allAllowed = -1.

    getServices().getDocumentService().save(getDocument());

    checkGebruikerKoppeling();
    checkButtonGebrKoppelen();
    checkButtonKmKoppelen();
    checkButtonPrintKoppelen();
    checkButtonStempelsKoppelen();
    checkButtonGvKoppelen();

    form.updateCode(getDocument().getCDocument());

    successMessage("Document is opgeslagen.");
  }

  private void setDocumentSettings(String docPath, Tab1DocumentenPage2Bean b) {

    getDocument().setVDocument(toBigDecimal(defaultNul(b.getVolgnr())));
    getDocument().setDocument(b.getNaam());
    getDocument().setAlias(b.getAlias());
    getDocument().setBestand(b.getSjabloon());
    getDocument().setType(b.getType().getType());
    getDocument().setPad(docPath);
    getDocument().setDatumVerval(new DateTime(along(b.getVervaldatum().getValue())));
    getDocument().setOmschrijving(b.getOmschrijving());
    getDocument().setAantal(aval(b.getAantal()));
    getDocument().setDocumentDmsType(b.getDocumentDmsType());
    getDocument().setVertrouwelijkheid(b.getVertrouwelijkheid());
  }

  private void setOpslagEnToegang(OpslagEnToegangBean bOT) {
    getDocument().setKopieOpslaan(bOT.isKopie());
    getDocument().setProtocolleren(bOT.isProtocollering());
    getDocument().setStandaardDocument(bOT.isStandaard());
    getDocument().setStillbornAllowed(bOT.isStillborn());
  }

  private void setUitvoerformaten(UitvoerformaatBean bU) {

    StringWriter sw = new StringWriter();
    Properties p = new Properties();

    try {
      p.put(UitvoerformaatType.PDF.getId(), bU.isPdf(UitvoerformaatType.PDF) ? "1" : "0");
      p.put(UitvoerformaatType.PDF_A1.getId(), bU.isPdf(UitvoerformaatType.PDF_A1) ? "1" : "0");
      p.put(UitvoerformaatType.ODT.getId(), bU.isOdt() ? "1" : "0");
      p.put(UitvoerformaatType.DOC.getId(), bU.isDoc() ? "1" : "0");

      p.store(sw, null);
      getDocument().setFormats(sw.toString());

    } catch (IOException | NullPointerException | ClassCastException e) {
      throw new ProException(PROGRAMMING, ERROR, SAVE_FORMATS_ERROR, e);
    }
  }
}
