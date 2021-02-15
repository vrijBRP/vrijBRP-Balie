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

package nl.procura.gba.web.modules.beheer.documenten.components;

import java.io.File;

import nl.procura.gba.web.windows.GbaWindow;

public class ImportDocumentArguments {

  private GbaWindow gbaWindow      = null;
  private File      importFile     = null;
  private String    importFileName = null;

  public GbaWindow getGbaWindow() {
    return gbaWindow;
  }

  public void setGbaWindow(GbaWindow gbaWindow) {
    this.gbaWindow = gbaWindow;
  }

  public File getImportFile() {
    return importFile;
  }

  public void setImportFile(File importFile) {
    this.importFile = importFile;
  }

  public String getImportFileName() {
    return importFileName;
  }

  public void setImportFileName(String importFileName) {
    this.importFileName = importFileName;
  }
}
