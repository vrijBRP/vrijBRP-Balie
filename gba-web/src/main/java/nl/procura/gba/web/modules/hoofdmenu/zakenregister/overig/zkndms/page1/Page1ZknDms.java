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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page1;

import static nl.procura.bsm.rest.v1_0.objecten.stuf.zkn0310.zsdms.BsmZknDmsRestElementTypes.*;
import static nl.procura.standard.Globalfunctions.date2str;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElement;
import nl.procura.bsm.rest.v1_0.objecten.stuf.zkn0310.zsdms.GeefZaakDocumentLezenAntwoordRestElement;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow.PreviewFile;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.parameters.container.ZaakDmsVariantContainer;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page3.Page3ZknDms;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page4.Page4ZknDms;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.zkndms.ZaakDmsService;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1ZknDms extends NormalPageTemplate {

  private final InfoLayout geenZaakInfo       = new InfoLayout("Geen zaak gevonden in het zaaksysteem", "");
  private final InfoLayout geenZaakStatusInfo = new InfoLayout("Geen status gevonden in het zaaksysteem", "");
  private final InfoLayout geenDocumentenInfo = new InfoLayout("Geen documenten gevonden in het zaaksysteem", "");

  private final Zaak                 zaak;
  private final Page1ZknDmsZaakForm  zaakForm    = new Page1ZknDmsZaakForm();
  private final OptieLayout          optieLayout = new OptieLayout();
  private boolean                    geladen     = false;
  private Page1ZknDmsBetrokkeneTable betrokkenenTable;
  private Page1ZknDmsStatusTable     statussenTable;
  private Page1ZknDmsDocumentTable   documentenTable;

  public Page1ZknDms(Zaak zaak) {
    this.zaak = zaak;

    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonSearch.setCaption("Herladen (F3)");

      optieLayout.getLeft().addComponent(new Fieldset("Zaakdetails"));
      optieLayout.getLeft().addComponent(zaakForm);

      optieLayout.getRight().setCaption("Opties");
      optieLayout.getRight().setWidth("130px");
      optieLayout.getRight().addButton(buttonSearch, this);

      addComponent(optieLayout);

      addComponent(new Fieldset("Heeft betrekking op"));
      betrokkenenTable = new Page1ZknDmsBetrokkeneTable() {

        @Override
        public void onClick(Record record) {
          getNavigation().addPage(new Page3ZknDms(record.getObject(BsmRestElement.class)));
        }
      };

      addComponent(betrokkenenTable);

      addComponent(new Fieldset("Zaakstatussen"));

      statussenTable = new Page1ZknDmsStatusTable() {

        @Override
        public void onClick(Record record) {
          getNavigation().addPage(new Page4ZknDms(record.getObject(BsmRestElement.class)));
        }
      };

      addComponent(statussenTable);

      documentenTable = new Page1ZknDmsDocumentTable() {

        @Override
        public void onClick(Record record) {
          openBestand(record.getObject(BsmRestElement.class));
        }
      };

      addComponent(new Fieldset("Documenten"));
      addComponent(documentenTable);
    }
    if (event.isEvent(LoadPage.class)) {
      laadZaakgegevens();
    }

    super.event(event);
  }

  @Override
  public void onSearch() {
    geladen = false;
    laadZaakgegevens();
    super.onSearch();
  }

  /**
   * Zoekt de statussen in het zaaksysteem
   */
  private List<BsmRestElement> getBetrokkenen(BsmRestElement zaakElement) {
    List<BsmRestElement> betrokkenen = new ArrayList<>();

    if (zaakElement.isAdded(INITIATOR)) {
      BsmRestElement initiator = zaakElement.get(INITIATOR);
      initiator.add(OMSCHRIJVING_BETROKKENHEID).set("Initiator");
      betrokkenen.add(initiator);
    }

    if (zaakElement.isFilled(BETROKKENEN)) {
      BsmRestElement betrokkenenElement = zaakElement.get(BETROKKENEN);
      if (betrokkenenElement.heeftElementen()) {
        betrokkenen.addAll(betrokkenenElement.getAll(BETROKKENE));
      }
    }
    return betrokkenen;
  }

  /**
   * Zoekt de documenten in het zaaksysteem
   */
  private BsmRestElement getDocument(String documentIdentificatie) {

    ZaakDmsService zakenDms = getApplication().getServices().getZaakDmsService();
    GeefZaakDocumentLezenAntwoordRestElement antwoord = zakenDms.getZaakDocument(documentIdentificatie);
    BsmRestElement zaakElement = antwoord.getZaak();
    BsmRestElement document = new BsmRestElement();

    if (zaakElement.heeftElementen()) {
      BsmRestElement documentenElement = zaakElement.get(DOCUMENTEN);
      if (documentenElement.heeftElementen()) {
        document = documentenElement.get(DOCUMENT);
      }
    }

    return document;
  }

  /**
   * Zoekt de documenten in het zaaksysteem
   */
  private List<BsmRestElement> getDocumenten(BsmRestElement zaakElement) {
    List<BsmRestElement> documenten = new ArrayList<>();
    if (zaakElement.isFilled(DOCUMENTEN)) {
      BsmRestElement documentenElement = zaakElement.get(DOCUMENTEN);
      if (documentenElement.heeftElementen()) {
        documenten.addAll(documentenElement.getAll(DOCUMENT));
      }
    }
    return documenten;
  }

  /**
   * Zoekt de statussen in het zaaksysteem
   */
  private List<BsmRestElement> getStatussen(BsmRestElement zaakElement) {
    List<BsmRestElement> statussen = new ArrayList<>();
    if (zaakElement.isFilled(STATUSSEN)) {
      BsmRestElement statussenElement = zaakElement.get(STATUSSEN);
      if (statussenElement.heeftElementen()) {
        statussen.addAll(statussenElement.getAll(STATUS));
      }
    }
    return statussen;
  }

  /**
   * @return
   */
  private BsmRestElement getZaakDetails() {
    ZaakDmsService zakenDms = getApplication().getServices().getZaakDmsService();
    return zakenDms.getZaakDetails(zaak).getZaak();
  }

  /**
   * @return
   */
  private BsmRestElement getZaakStatussen() {
    ZaakDmsService zakenDms = getApplication().getServices().getZaakDmsService();
    return zakenDms.getZaakStatussen(zaak).getZaak();
  }

  /**
   * @return
   */
  private BsmRestElement getZaakDocumenten() {
    ZaakDmsService zakenDms = getApplication().getServices().getZaakDmsService();
    return zakenDms.getZaakDocumenten(zaak).getZaak();
  }

  private void laadZaakgegevens() {

    if (geladen) {
      return;
    }

    updateZaakDetails();
    geladen = true;
  }

  private void openBestand(BsmRestElement document) {

    try {
      String documentId = document.getElementWaarde(DOCUMENT_IDENTIFICATIE);
      BsmRestElement contentDocument = getDocument(documentId);

      if (contentDocument.heeftElementen()) {

        BestandType type = BestandType.getType(contentDocument.getElementWaarde(DOCUMENT_BESTANDSNAAM));
        if (BestandType.ONBEKEND.equals(type)) {
          type = BestandType.getType(contentDocument.getElementWaarde(DOCUMENT_LINK));
        }

        String titel = contentDocument.getElementWaarde(DOCUMENT_TITEL);
        byte[] inhoud = Base64.decodeBase64(contentDocument.getElementWaarde(DOCUMENT_INHOUD));
        String downloadnaam = documentId + "." + type.getType();

        PreviewFile previewFile = new PreviewFile(inhoud, titel, downloadnaam, type);
        previewFile.setProperty("Identificatie", document.getElementWaarde(DOCUMENT_IDENTIFICATIE));
        previewFile.setProperty("Titel", document.getElementWaarde(DOCUMENT_TITEL));
        previewFile.setProperty("Omschrijving", document.getElementWaarde(DOCUMENT_OMSCHRIJVING));
        previewFile.setProperty("Beschrijving", document.getElementWaarde(DOCUMENT_BESCHRIJVING));
        previewFile.setProperty("Vertrouwelijk", document.getElementWaarde(DOCUMENT_AANDUIDINGVERTROUWELIJK));
        previewFile.setProperty("Status", document.getElementWaarde(DOCUMENT_STATUS));
        previewFile.setProperty("Creatiedatum", date2str(document.getElementWaarde(DOCUMENT_DATUM_CREATIE)));
        previewFile.setProperty("Bestandsnaam", contentDocument.getElementWaarde(DOCUMENT_BESTANDSNAAM));
        previewFile.setProperty("Link", document.getElementWaarde(DOCUMENT_LINK));

        FilePreviewWindow.preview(getWindow(), previewFile);
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }

  /**
   * Vul opnieuw de tabel
   */
  private void updateBetrokkenenTable(BsmRestElement zaakElement) {

    List<BsmRestElement> betrokkenen = getBetrokkenen(zaakElement);
    betrokkenenTable.setBetrokkenen(betrokkenen);
    betrokkenenTable.init();
  }

  /**
   * Update het statusformulier
   */
  private void updateDetailsForm(BsmRestElement zaakElement) {

    optieLayout.getLeft().removeComponent(geenZaakInfo);
    int formIndex = optieLayout.getLeft().getComponentIndex(zaakForm);

    if (!zaakElement.heeftElementen()) {
      optieLayout.getLeft().addComponent(geenZaakInfo, formIndex);
    } else {
      // Zaak
      Page1ZknDmsZaakBean bean2 = new Page1ZknDmsZaakBean();

      if (zaakElement.isAdded(ZAAKTYPE)) {
        BsmRestElement zaakType = zaakElement.get(ZAAKTYPE);
        bean2.setOmschrijving(
            zaakType.getElementWaarde(OMSCHRIJVING) + " (" + zaakType.getElementWaarde(CODE) + ")");
        bean2.setStartdatum(date2str(zaakElement.getElementWaarde(STARTDATUM)));
        bean2.setRegistratiedatum(date2str(zaakElement.getElementWaarde(REGISTRATIEDATUM)));
        bean2.setGeplandeEinddatum(date2str(zaakElement.getElementWaarde(GEPLANDE_EINDDATUM)));
        bean2.setUiterlijkeEinddatum(date2str(zaakElement.getElementWaarde(UITERLIJKE_EINDDATUM)));
      }

      zaakForm.setBean(bean2);
    }
  }

  /**
   * Vul opnieuw de tabel
   */
  private void updateDocumentenTable(BsmRestElement zaakElement) {

    List<BsmRestElement> documenten = getDocumenten(zaakElement);
    removeComponent(geenDocumentenInfo);

    if (documenten.isEmpty()) {
      int tableIdex = getComponentIndex(documentenTable);
      addComponent(geenDocumentenInfo, tableIdex);
    }

    documentenTable.setDocumenten(documenten);
    documentenTable.init();
  }

  /**
   * Vul opnieuw de tabel
   */
  private void updateStatussenTable(BsmRestElement zaakElement) {

    List<BsmRestElement> statussen = getStatussen(zaakElement);
    removeComponent(geenZaakStatusInfo);

    if (statussen.isEmpty()) {
      int tableIdex = getComponentIndex(statussenTable);
      addComponent(geenZaakStatusInfo, tableIdex);
    }

    statussenTable.setStatussen(statussen);
    statussenTable.init();
  }

  private void updateZaakDetails() {

    BsmRestElement zaakDetails = getZaakDetails();
    BsmRestElement zaakStatussen = new BsmRestElement();
    BsmRestElement zaakDocumenten = new BsmRestElement();

    String variant = getApplication().getParmValue(ParameterConstant.BSM_ZAKEN_DMS_VARIANT);
    if (ZaakDmsVariantContainer.ALLEEN_ZAAKDETAILS.equals(variant)) {
      zaakStatussen = getZaakDetails();
      zaakDocumenten = getZaakDetails();

    } else if (ZaakDmsVariantContainer.ALLE_BERICHTEN.equals(variant)) {
      zaakStatussen = getZaakStatussen();
      zaakDocumenten = getZaakDocumenten();
    }

    updateDetailsForm(zaakDetails);
    updateBetrokkenenTable(zaakDetails);
    updateStatussenTable(zaakStatussen);
    updateDocumentenTable(zaakDocumenten);
  }
}
