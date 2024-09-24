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

package nl.procura.gba.web.components.layouts.form.document.sign;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.component.dialog.ConfirmDialog;

public class DocSignWindow extends GbaModalWindow {

  private final DocSignPage page;

  public DocSignWindow(DocSignParameters parameters) {
    super("Ondertekenen", "700px");
    this.page = new DocSignPage(parameters);
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(this.page);
  }

  @Override
  public void close() {
    if (page.hasUnretrievedDocuments()) {
      showConfirmDialog();
    } else {
      super.close();
    }
  }

  private void showConfirmDialog() {
    ConfirmDialog confirmDialog = new ConfirmDialog("Afsluiten",
        "Nog niet alle ondertekende documenten zijn opgehaald."
            + "<br/>Toch dit scherm sluiten?",
        "400px") {

      @Override
      public void buttonYes() {
        super.buttonYes();
        closeNow();
      }

      @Override
      public void buttonNo() {
        super.buttonNo();
      }
    };
    if (getGbaApplication() != null) {
      getGbaApplication().getParentWindow().addWindow(confirmDialog);
    }
  }

  private void closeNow() {
    super.close();
  }
}
