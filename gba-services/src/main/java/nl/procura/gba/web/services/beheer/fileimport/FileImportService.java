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

package nl.procura.gba.web.services.beheer.fileimport;

import com.google.gson.Gson;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nl.procura.gba.jpa.personen.dao.FileImportDao;
import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.jpa.personen.db.FileRecord;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;

public class FileImportService extends AbstractService {

  public FileImportService() {
    super("FileImport");
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(FileImport fileImport) {
    saveEntity(fileImport);
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(FileImport fileImport, List<FileRecord> fileRecords) {
    saveEntity(fileImport);
    for (FileRecord fileRecord : fileRecords) {
      if (!fileRecord.isStored()) {
        fileRecord.setcFileImport(fileImport.getcCFileImport());
        saveEntity(fileRecord);
      }
    }
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(FileImport fileImport) {
    removeEntity(fileImport);
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(List<FileImportRecord> fileRecords) {
    for (FileImportRecord fileImportRecord : fileRecords) {
      removeEntity(findEntity(FileRecord.class, fileImportRecord.getId()));
    }
  }

  public List<FileImport> getFileImports() {
    return FileImportDao.getFileImports();
  }

  public List<FileImport> getFileImports(String type) {
    return getFileImports().stream()
        .filter(fi -> Objects.equals(fi.getTemplate(), type))
        .collect(Collectors.toList());
  }

  public List<FileRecord> getFileRecords(FileImport fileImport) {
    return FileImportDao.getFileRecords(fileImport);
  }

  public List<FileImportRecord> getFileImportRecords(FileImport fileImport) {
    Gson gson = new Gson();
    return getFileRecords(fileImport)
        .stream()
        .map(record -> {
          FileImportRecord fileImportRecord = gson.fromJson(new String(record.getContent()), FileImportRecord.class);
          fileImportRecord.setId(record.getcFileRecord());
          return fileImportRecord;
        })
        .collect(Collectors.toList());
  }
}
