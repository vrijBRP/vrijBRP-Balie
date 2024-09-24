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

package nl.procura.gba.web.modules.bs.common.pages.documentpage.page2;

import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.modules.bs.common.pages.documentpage.page1.Page1BsDocument;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage.ZaakBijlageUploadForm;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage.ZaakBijlageVertrouwelijkheidForm;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSFileContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSService;
import nl.procura.gba.web.services.zaken.documenten.dmstypes.DmsDocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Document toevoegen aan een dossier
 */

public class Page2BsDocument extends ButtonPageTemplate {

  private final Uploader                   docUploader = new Uploader();
  private final Dossier                    dossier;
  private final ChangeListener             listener;
  private ZaakBijlageVertrouwelijkheidForm form;

  public Page2BsDocument(Dossier dossier, ChangeListener listener) {
    this.dossier = dossier;
    this.listener = listener;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      setInfo("Het bestand zal worden gekoppeld aan de zaak");
      addComponent(new ZaakBijlageUploadForm(dossier));
      form = new ZaakBijlageVertrouwelijkheidForm(dossier.getType());
      addComponent(form);
      addComponent(docUploader);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPage(Page1BsDocument.class);
  }

  public class Uploader extends DocUploader {

    @Override
    public void uploadSucceeded(SucceededEvent event) {
      try {
        if (!isValidFileSize()) {
          return;
        }
        DMSService dms = getApplication().getServices().getDmsService();
        String documentDmsType = form.getDmsDocumentType().map(DmsDocumentType::toString).orElse("");
        String vertrouwelijkheid = form.getVertrouwelijkheid().getNaam();
        String naam = getApplication().getServices().getGebruiker().getNaam();

        DMSDocument dmsDocument = DMSDocument.builder()
            .content(DMSFileContent.from(getFile()))
            .user(naam)
            .documentTypeDescription(documentDmsType)
            .confidentiality(vertrouwelijkheid)
            .build();

        dms.save(dossier, dmsDocument);
        super.uploadSucceeded(event);
        listener.onChange();

      } catch (Exception e) {
        getApplication().handleException(getWindow(), e);
      }
    }
  }
}
