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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox;

import java.util.function.Supplier;

import com.vaadin.ui.Button;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.form.document.preview.FilePreviewWindow;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.document.DocumentenTabel;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.vaadin.theme.ProcuraWindow;

public class RequestInboxDocumentsButton extends Button {

  private boolean                   loaded = false;
  private final Supplier<DMSResult> dmsResultSupplier;

  public RequestInboxDocumentsButton(Supplier<DMSResult> dmsResultSupplier) {
    this.dmsResultSupplier = dmsResultSupplier;
  }

  public GbaApplication getGbaApplication() {
    return (GbaApplication) super.getApplication();
  }

  @Override
  public void attach() {
    if (!loaded) {
      setCaption("Bijlagen (" + dmsResultSupplier.get().getDocuments().size() + ")");
      addListener((ClickListener) event -> {
        DMSResult dmsResult = dmsResultSupplier.get();
        DocumentWindow window = new DocumentWindow(dmsResult);
        // Open first document if there is only one
        if (dmsResult.getDocuments().size() == 1) {
          window.openFirstDocument();
        } else {
          getGbaApplication().getParentWindow().addWindow(window);
        }
      });
      loaded = true;
    }
    super.attach();
  }

  private class DocumentWindow extends GbaModalWindow {

    private final DocumentenTabel table;
    private final DMSResult       dmsResult;

    public DocumentWindow(DMSResult dmsResult) {
      super(true, "Bijlages", "900px");
      table = new DocumentenTabel() {

        @Override
        public boolean isPopup() {
          return true;
        }

        @Override
        public DMSResult getOpgeslagenBestanden() {
          return dmsResult;
        }
      };
      this.dmsResult = dmsResult;
      addComponent(table);
    }

    public void openFirstDocument() {
      ProcuraWindow parentWindow = RequestInboxDocumentsButton.this.getGbaApplication().getParentWindow();
      FilePreviewWindow.preview(parentWindow, table.getPreviewFile(dmsResult.getDocuments().get(0)));
    }
  }
}
