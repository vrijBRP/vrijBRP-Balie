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

package nl.procura.gba.web.modules.zaken.common;

import static com.vaadin.ui.Window.Notification.TYPE_ERROR_MESSAGE;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.vaadin.component.window.Message.TYPE_SUCCESS;
import static org.apache.commons.io.IOUtils.toByteArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.vaadin.ui.Button;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.beheer.documenten.components.DocumentImportExportHandler;
import nl.procura.gba.web.modules.zaken.document.DocumentenTabel;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

public class ButtonDownloadAttachments extends Button {

  public ButtonDownloadAttachments(Supplier<DocumentenTabel> tableSupplier) {
    super("Downloaden");
    addListener(getClickListener(tableSupplier));
  }

  private ClickListener getClickListener(Supplier<DocumentenTabel> tableSupplier) {
    return clickEvent -> {
      DocumentenTabel documentenTabel = tableSupplier.get();
      List<DMSDocument> documents = documentenTabel.getSelectedValues(DMSDocument.class);
      if (documents.isEmpty()) {
        documents = documentenTabel.getAllValues(DMSDocument.class);
      }
      try {
        if (documents.isEmpty()) {
          throw new ProException(INFO, "Geen documenten geselecteerd");
        }
        GbaApplication app = (GbaApplication) getWindow().getApplication();
        if (documents.size() == 1) {
          downloadSingleFile(documents, app);
        } else {
          downloadMultipleFiles(documents, app);
        }
      } catch (IOException e) {
        new Message(getWindow(), "Fout bij downloaden bestanden", TYPE_ERROR_MESSAGE);
      }
    };
  }

  /**
   * Don't create ZIP file if one file
   */
  private void downloadSingleFile(List<DMSDocument> documents, GbaApplication app) {
    DMSContent content = documents.get(0).getContent();
    new Message(getWindow(), "1 bijlage gedownload", TYPE_SUCCESS);
    new DownloadHandlerImpl(app.getParentWindow()).download(content.getInputStream(), content.getFilename(), true);
  }

  /**
   * Create a Zip-file if it's multiple files
   */
  private void downloadMultipleFiles(List<DMSDocument> documents, GbaApplication app) throws IOException {
    Map<String, byte[]> map = new HashMap<>();
    for (DMSDocument document : documents) {
      DMSContent content = document.getContent();
      map.put(content.getFilename(), content.getBytes());
    }
    new Message(getWindow(), "Zip-bestand met " + documents.size() + " bijlages aangemaakt", TYPE_SUCCESS);
    DocumentImportExportHandler.exportObject(map, "bijlages.zip", new DownloadHandlerImpl(app.getParentWindow()));
  }
}
