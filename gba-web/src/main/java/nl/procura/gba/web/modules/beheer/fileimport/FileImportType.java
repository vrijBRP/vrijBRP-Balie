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
import java.util.function.Function;
import java.util.stream.Collectors;

import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImporter;
import nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporter;
import nl.procura.gba.web.modules.beheer.fileimport.types.RegistrantImporterTable;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTableListener;

public enum FileImportType {

  FIRST_REGISTRANTS("first_registrants_2022", "Eerste inschrijvingen 2022",
      FIRST_REGISTRATION, new RegistrantImporter(),
      RegistrantImporterTable::new);

  private final String                                             id;
  private final String                                             descr;
  private final FileImportProcess                                  process;
  private final FileImporter                                       converter;
  private final Function<FileImportTableListener, FileImportTable> table;

  FileImportType(String id,
      String descr,
      FileImportProcess process,
      FileImporter converter,
      Function<FileImportTableListener, FileImportTable> table) {
    this.id = id;
    this.descr = descr;
    this.process = process;
    this.converter = converter;
    this.table = table;
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

  public FileImporter getConverter() {
    return converter;
  }

  public Function<FileImportTableListener, FileImportTable> getTable() {
    return table;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
