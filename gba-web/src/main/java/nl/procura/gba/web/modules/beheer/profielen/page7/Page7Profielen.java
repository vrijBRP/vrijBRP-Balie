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

package nl.procura.gba.web.modules.beheer.profielen.page7;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gba.web.common.misc.ImportTable;
import nl.procura.gba.web.modules.beheer.profielen.ModuleProfielPageTemplate;
import nl.procura.gba.web.modules.beheer.profielen.components.ImportProfielArgumenten;
import nl.procura.gba.web.modules.beheer.profielen.components.ProfielImportExportHandler;
import nl.procura.gba.web.modules.beheer.profielen.page1.Page1Profielen;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

public class Page7Profielen extends ModuleProfielPageTemplate {

  private final Uploader docUploader = new Uploader();
  private final CheckBox cb1         = new CheckBox("Gebruikers koppelen", true);
  private final CheckBox cb2         = new CheckBox("Acties importeren", true);
  private final CheckBox cb3         = new CheckBox("Velden importeren", true);
  private final CheckBox cb4         = new CheckBox("BRP-elementen importeren", true);
  private final CheckBox cb5         = new CheckBox("BRP-categorieën importeren", true);
  private final CheckBox cb6         = new CheckBox("Parameters importeren", true);
  private ImportTable    table       = null;

  public Page7Profielen() {

    super("Importeren van profielen");

    addButton(buttonPrev);

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(cb6);
      addComponent(cb1);
      addComponent(cb2);
      addComponent(cb3);
      addComponent(cb4);
      addComponent(cb5);

      table = new ImportTable();

      addComponent(docUploader);

      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPage(Page1Profielen.class);
  }

  private class Uploader extends DocUploader {

    @Override
    public void uploadSucceeded(SucceededEvent event) {

      try {
        table.getRecords().clear();

        ImportProfielArgumenten args = new ImportProfielArgumenten();

        args.setImportGebruikers((Boolean) cb1.getValue());
        args.setImportActies((Boolean) cb2.getValue());
        args.setImportVelden((Boolean) cb3.getValue());
        args.setImportElementen((Boolean) cb4.getValue());
        args.setImportCategorieen((Boolean) cb5.getValue());
        args.setImportParameters((Boolean) cb6.getValue());
        args.setWindow((GbaWindow) getWindow());
        args.setBestand(getFile());

        for (String melding : ProfielImportExportHandler.importProfielen(args)) {
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
