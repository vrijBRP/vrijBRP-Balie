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

package nl.procura.gba.web.rest.v2.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.jpa.personen.db.FileRecord;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;
import nl.procura.gba.web.modules.beheer.fileimport.types.AbstractFileImport;
import nl.procura.gba.web.rest.v2.model.dataimport.GbaRestDataImport;
import nl.procura.gba.web.rest.v2.model.dataimport.GbaRestDataImportDocument;
import nl.procura.gba.web.rest.v2.model.dataimport.GbaRestDataImportRecord;
import nl.procura.gba.web.rest.v2.model.dataimport.GbaRestDataImportValue;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.gba.web.services.beheer.fileimport.FileImportResult;
import nl.procura.gba.web.services.beheer.fileimport.FileImportService;
import nl.procura.standard.exceptions.ProException;

import au.com.bytecode.opencsv.CSVWriter;

public class GbaRestDataImportService extends GbaRestAbstractService {

  @Transactional
  public GbaRestDataImport add(GbaRestDataImport dataImport) {
    FileImportResult result = FileImportType.getById(dataImport.getType()).map(type -> {
      AbstractFileImport converter = type.getImporter();
      return converter.convert(toCsv(dataImport), getServices());
    }).orElseThrow(() -> new IllegalArgumentException("Unknown type: " + dataImport.getType()));

    List<GbaRestDataImportRecord> records = new ArrayList<>();
    for (FileImportRecord fileImportRecord : result.getRecords()) {
      Map<String, GbaRestDataImportValue> values = fileImportRecord.getValues()
          .entrySet().stream()
          .collect(Collectors.toMap(Map.Entry::getKey,
              val -> new GbaRestDataImportValue(val.getValue().getValue(), val.getValue().getRemark())));

      records.add(new GbaRestDataImportRecord(values, fileImportRecord.getRemarks(), new ArrayList<>()));
    }

    saveDataImport(dataImport, result);
    return new GbaRestDataImport(dataImport.getName(), dataImport.getType(), records, result.getRemarks());
  }

  private void saveDataImport(GbaRestDataImport dataImport, FileImportResult result) {
    FileImportService fileImportService = getServices().getFileImportService();
    FileImport fileImport = fileImportService.getFileImport(dataImport.getName())
        .orElse(new FileImport(dataImport.getName(), dataImport.getType()));

    fileImportService.save(fileImport);
    for (int index = 0; index < result.getRecords().size(); index++) {
      FileImportRecord record = result.getRecords().get(index);
      byte[] bytes = new Gson().toJson(record).getBytes();
      FileRecord fileRecord = new FileRecord(fileImport.getCFileImport(), bytes);
      fileImportService.save(fileImport, fileRecord);
      saveDocuments(dataImport, index, fileRecord);
    }
  }

  private void saveDocuments(GbaRestDataImport dataImport, int index, FileRecord fileRecord) {
    try {
      List<GbaRestDataImportDocument> documents = dataImport.getRecords().get(index).getDocuments();
      if (documents != null && !documents.isEmpty()) {
        String folderId = fileRecord.getUuid();
        for (GbaRestDataImportDocument document : documents) {
          getRestServices().getDmsService().addDocument(folderId,
              document.getDocument(),
              document.getInhoud());
        }
      }
    } catch (Exception e) {
      throw new ProException("Fout bij het opslaan van documenten", e);
    }
  }

  private byte[] toCsv(GbaRestDataImport dataImport) {
    ByteArrayOutputStream csvOs = new ByteArrayOutputStream();
    CSVWriter csv;

    try {
      OutputStreamWriter writer = new OutputStreamWriter(csvOs, StandardCharsets.UTF_8);
      csv = new CSVWriter(writer, ';', '"');
      List<GbaRestDataImportRecord> records = dataImport.getRecords();

      GbaRestDataImportRecord headerRecord = records.get(0);
      csv.writeNext(new ArrayList<>(headerRecord.getValues().keySet()).toArray(new String[0]));
      for (GbaRestDataImportRecord record : records) {
        csv.writeNext(new ArrayList<>(record.getValues().values().stream()
            .map(GbaRestDataImportValue::getValue)
            .collect(Collectors.toList()))
                .toArray(new String[0]));
      }

      try {
        csv.flush();
      } finally {
        IOUtils.closeQuietly(csv);
      }

      return csvOs.toByteArray();
    } catch (IOException e) {
      throw new ProException("Fout bij maken spreadsheet", e);
    } finally {
      IOUtils.closeQuietly(csvOs);
    }
  }
}
