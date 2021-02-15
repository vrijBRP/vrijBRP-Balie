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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties;

import com.vaadin.ui.Upload;

import nl.procura.gba.web.common.misc.ImportTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties.export.ImportSelectieArgumenten;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties.export.SelectieImportExportHandler;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

public class Page4SelectiesZaken4 extends NormalPageTemplate {

  private final Uploader docUploader = new Uploader();
  private ImportTable    table       = null;

  public Page4SelectiesZaken4() {
    super("Importeren van selecties");
    addButton(buttonPrev);
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new ImportTable();
      addComponent(docUploader);
      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private class Uploader extends DocUploader {

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {

      try {
        table.getRecords().clear();

        ImportSelectieArgumenten args = new ImportSelectieArgumenten();
        args.setWindow((GbaWindow) getWindow());
        args.setBestand(getFile());

        for (String melding : SelectieImportExportHandler.importSelecties(args)) {
          table.addMessage(melding);
        }

        table.reloadRecords();
        addMessage("Bestand " + event.getFilename() + " is succesvol geupload.", Icons.ICON_OK);

      } catch (Exception e) {
        e.printStackTrace();
        addMessage("Bestand " + event.getFilename() + " is niet succesvol geupload.", Icons.ICON_ERROR);
      }
    }
  }
}
