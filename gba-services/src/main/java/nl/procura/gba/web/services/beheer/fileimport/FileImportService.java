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

import static java.util.stream.Collectors.toList;
import static nl.procura.gba.jpa.personen.dao.FileImportDao.countAllFileRecords;
import static nl.procura.gba.jpa.personen.dao.FileImportDao.countNewFileRecords;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

import nl.procura.gba.jpa.personen.dao.FileImportDao;
import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.jpa.personen.db.FileRecord;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

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
        fileRecord.setCFileImport(fileImport.getCFileImport());
        saveEntity(fileRecord);
      }
    }
  }

  @ThrowException("Fout bij het opslaan")
  @Transactional
  public void save(FileImport fileImport, FileRecord fileRecord) {
    saveEntity(fileImport);
    if (!fileRecord.isStored()) {
      fileRecord.setCFileImport(fileImport.getCFileImport());
      saveEntity(fileRecord);
    }
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(FileImport fileImport) {
    getFileRecords(fileImport).forEach(this::delete);
    removeEntity(fileImport);
  }

  @ThrowException("Fout bij het verwijderen")
  @Transactional
  public void delete(List<FileImportRecord> fileRecords) {
    fileRecords.forEach(record -> delete(findEntity(FileRecord.class, record.getId())));
  }

  private void delete(FileRecord fileRecord) {
    if (isNotBlank(fileRecord.getUuid())) {
      DMSService dmsService = getServices().getDmsService();
      List<DMSDocument> documents = dmsService.getDocumentsById(fileRecord.getUuid()).getDocuments();
      documents.forEach(dmsService::delete);
    }
    removeEntity(fileRecord);
  }

  public List<FileImport> getFileImports() {
    return FileImportDao.getFileImports();
  }

  public Optional<FileImport> getFileImport(String name) {
    return getFileImports().stream()
        .filter(fi -> fi.getName().equals(name))
        .findFirst();
  }

  public List<FileImport> getFileImports(String type) {
    return getFileImports().stream()
        .filter(fi -> fi.getTemplate().equals(type))
        .collect(toList());
  }

  public List<FileRecord> getFileRecords(FileImport fileImport) {
    return FileImportDao.getFileRecords(fileImport);
  }

  public Optional<FileRecord> getFileRecordById(Long cFileRecord) {
    return FileImportDao.getFileRecordById(cFileRecord);
  }

  public FileImportRecord getFileImportRecord(Long cFileRecord) {
    Gson gson = new Gson();
    return getFileRecordById(cFileRecord).map(fileRecord -> {
      FileImportRecord record = gson.fromJson(new String(fileRecord.getContent()), FileImportRecord.class);
      record.setId(fileRecord.getCFileRecord());
      record.setFileImportId(fileRecord.getCFileImport());
      record.setUuid(fileRecord.getUuid());
      record.setTemplate(fileRecord.getFileImport().getTemplate());
      return record;
    }).orElse(null);
  }

  public List<FileImportRecord> getFileImportRecords(FileImport fileImport) {
    Gson gson = new Gson();
    List<Long> references = FileImportDao.getIdsWithReferenceToRegistration(fileImport);
    return getFileRecords(fileImport).parallelStream()
        .map(fileRecord -> {
          FileImportRecord record = gson.fromJson(new String(fileRecord.getContent()), FileImportRecord.class);
          record.setId(fileRecord.getCFileRecord());
          record.setFileImportId(fileRecord.getCFileImport());
          record.setUuid(fileRecord.getUuid());
          record.setTemplate(fileRecord.getFileImport().getTemplate());
          record.setReference(references.contains(fileRecord.getCFileRecord()));
          return record;
        }).collect(toList());
  }

  public List<String> getZaken(Long cFileRecord) {
    return FileImportDao.getZaakIds(cFileRecord);
  }

  public Count countRecords(FileImport fileImport) {
    return new Count(countNewFileRecords(fileImport), countAllFileRecords(fileImport));
  }

  @Data
  @RequiredArgsConstructor
  public static class Count {

    private final long newRecords;

    private final long totalRecords;

    @Override
    public String toString() {
      return String.format(" (%d / %d)", getNewRecords(), getTotalRecords());
    }

  }
}
