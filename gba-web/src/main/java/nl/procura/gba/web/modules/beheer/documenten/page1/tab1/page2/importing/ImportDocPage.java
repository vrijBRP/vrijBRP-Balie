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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.importing;

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.importing.DocumentExportBean.*;

import java.util.List;

import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gba.web.common.misc.ImportTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.documenten.components.DocumentImportExportHandler;
import nl.procura.gba.web.modules.beheer.documenten.components.ImportDocumentArguments;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

public class ImportDocPage extends NormalPageTemplate {

  private final Uploader     docUploader = new Uploader();
  private ImportTable        table       = null;
  private DocumentImportForm form1       = null;
  private DocumentImportForm form2       = null;

  public ImportDocPage() {
    super("Importeren van documenten");
    addButton(buttonPrev);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new DocumentImportForm(ALIAS, DMS_DOCUMENT_TYPE, VERTROUWELIJKHEID, OMSCHRIJVING, FORMATS);
      form2 = new DocumentImportForm(KOPIE_OPSLAAN, PROTOCOLLERING, STANDAARD_GESELECTEERD,
          IEDEREEN_TOEGANG, STILLBORN_ALLOWED);
      table = new ImportTable();

      addComponent(docUploader);
      addComponent(new Fieldset("Document", form1));
      addComponent(new Fieldset("Opslag en toegang", form2));
      addComponent(new Fieldset("Resultaten", table));
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private class Uploader extends DocUploader {

    @Override
    public void uploadSucceeded(final SucceededEvent event) {

      table.clear();
      form1.commit();
      form2.commit();
      clearMessage();

      final DocumentImportExportHandler docImExHandl = new DocumentImportExportHandler();

      getWindow().addWindow(
          new ConfirmDialog("Overschrijven?", "Wilt u eventuele al bestaande<br> documenten overschrijven?") {

            @Override
            public void buttonNo() {
              close();
              uploadDocs(false);
            }

            @Override
            public void buttonYes() {
              close();
              uploadDocs(true);
            }

            private void uploadDocs(boolean overwrite) {

              //form1
              docImExHandl.setOverwriteDocs(overwrite);
              docImExHandl.setAlias(form1.getBean().getAlias());
              docImExHandl.setOmschrijving(form1.getBean().getOmschrijving());
              docImExHandl.setDocumentTypeOms(form1.getBean().getDocumentTypeOms());
              docImExHandl.setVertrouwelijkheid(form1.getBean().getVertrouwelijkheid());
              docImExHandl.setFormats(form1.getBean().getFormats());

              //form2
              docImExHandl.setKopieOpslaan(form2.getBean().getKopieOpslaan());
              docImExHandl.setProtocollering(form2.getBean().getProtocollering());
              docImExHandl.setStandaardGeselecteerd(form2.getBean().getStandaardGeselecteerd());
              docImExHandl.setIedereenToegang(form2.getBean().getIedereenToegang());
              docImExHandl.setStillbornAllowed(form2.getBean().getStillbornAllowed());

              table.getRecords().clear();

              ImportDocumentArguments args = new ImportDocumentArguments();
              args.setGbaWindow((GbaWindow) table.getWindow());
              args.setImportFile(getFile());
              args.setImportFileName(getFileName());

              try {

                List<List<String>> docTemplReports = docImExHandl.importDocsAndTemplates(args);

                if (docTemplReports.size() != 0) {
                  for (List<String> reports : docTemplReports) {
                    for (String report : reports) {
                      table.addMessage(report);
                    }
                  }
                  addMessage("Bestand " + event.getFilename() + " is succesvol geüpload.",
                      Icons.ICON_OK);
                } else {
                  addMessage("Alle documenten zijn reeds geïmporteerd.", Icons.ICON_WARN);
                }
              } catch (ProException e) {
                addMessage(e.getMessage(), Icons.ICON_WARN);
              }

              table.reloadRecords();
            }
          });
    }
  }
}
