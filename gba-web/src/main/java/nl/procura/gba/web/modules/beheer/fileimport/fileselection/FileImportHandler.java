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

import static java.util.Optional.ofNullable;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportProcess;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTableFilter;

import lombok.Setter;

public abstract class FileImportHandler {

  @Setter
  private Runnable finishImportListener;

  public abstract FileImportProcess getFileImportProcess();

  public abstract GbaApplication getApplication();

  public abstract FileImportTable getTable(FileImport fileImport);

  public abstract FileImportTableFilter getTableFilter();

  public void finishImport() {
    ofNullable(finishImportListener).ifPresent(Runnable::run);
  }
}
