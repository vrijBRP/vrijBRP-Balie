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

package nl.procura.gba.web.modules.beheer.fileimport;

import static nl.procura.gba.web.modules.beheer.fileimport.FileImportProcess.FIRST_REGISTRATION;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.procura.gba.web.modules.beheer.fileimport.types.AbstractFileImport;
import nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport;

public enum FileImportType {

  FIRST_REGISTRANTS("first_registrants_2022",
      "Eerste inschrijvingen 2022",
      FIRST_REGISTRATION,
      new RegistrantImport());

  private final String             id;
  private final String             descr;
  private final FileImportProcess  process;
  private final AbstractFileImport importer;

  FileImportType(String id,
      String descr,
      FileImportProcess process,
      AbstractFileImport importer) {
    this.id = id;
    this.descr = descr;
    this.process = process;
    this.importer = importer;
  }

  public static Optional<FileImportType> getById(String id) {
    return Arrays.stream(FileImportType.values())
        .filter(var -> var.getId().equalsIgnoreCase(id))
        .findFirst();
  }

  public static List<FileImportType> getByProcess(FileImportProcess process) {
    return Arrays.stream(FileImportType.values())
        .filter(var -> var.getProcess() == process)
        .collect(Collectors.toList());
  }

  public String getId() {
    return id;
  }

  public String getDescr() {
    return descr;
  }

  public FileImportProcess getProcess() {
    return process;
  }

  public AbstractFileImport getImporter() {
    return importer;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
