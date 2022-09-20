/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.fileimport.fileselection;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.component.layout.VLayout;

public class FileImportWindow extends GbaModalWindow {

  private final FileImportHandler fileImportHandler;

  public FileImportWindow(FileImportHandler fileImportHandler) {
    super("Importeer uit bestand", "450px");
    this.fileImportHandler = fileImportHandler;
  }

  @Override
  public void attach() {
    VLayout vLayout = new VLayout();
    vLayout.setMargin(true);
    vLayout.add(new FileImportForm(fileImportHandler));
    setContent(vLayout);
    super.attach();
  }
}
