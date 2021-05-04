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

package nl.procura.gba.web.services.zaken.algemeen.dms.filesystem;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid.ONBEKEND;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.DOCUMENTS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.UniqueList;
import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.*;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

/**
 * The local storage of documents
 */
public class FilesystemDMSStorage extends AbstractService implements DMSStorage {

  private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemDMSStorage.class.getName());

  public FilesystemDMSStorage() {
    super("Lokale DMS");
  }

  @Override
  @ThrowException(type = DOCUMENTS,
      value = "Fout bij ophalen opgeslagen bestanden")
  public DMSResult getDocumentsByPL(BasePLExt pl) {
    return Optional.of(pl)
        .map(p -> {
          List<String> folders = getFoldersFromPL(pl);
          return toDmsResult(getDocuments(folders, null));
        }).orElse(new DMSResult());
  }

  @Override
  public int countDocumentsByPL(BasePLExt pl) {
    return ofNullable(pl)
        .map(p -> {
          List<String> folders = getFoldersFromPL(p);
          return countDocuments(folders, null);
        }).orElse(0);
  }

  @Override
  public int countDocumentByZaakId(Zaak zaak) {

    if (zaak == null) {
      return 0;
    }

    BsnFieldValue bsn = zaak.getBurgerServiceNummer();
    AnrFieldValue anr = zaak.getAnummer();

    List<String> folders = new ArrayList<>();
    folders.add(zaak.getZaakId());

    if (bsn.isCorrect()) {
      folders.add(bsn.getStringValue());
    }

    if (anr.isCorrect()) {
      folders.add(anr.getStringValue());
    }

    return countDocuments(folders, zaak.getZaakId());
  }

  @Override
  public DMSResult getDocumentsByZaak(Zaak zaak) {
    if (zaak == null) {
      return new DMSResult();
    }

    BsnFieldValue bsn = zaak.getBurgerServiceNummer();
    AnrFieldValue anr = zaak.getAnummer();

    List<String> subMappen = new ArrayList<>();
    subMappen.add(zaak.getZaakId()); // Zoek op zaak-id

    if (bsn.isCorrect()) {
      subMappen.add(bsn.getStringValue()); // Zoek op BSN
    }

    if (anr.isCorrect()) {
      subMappen.add(anr.getStringValue()); // Zoek op A-nummer
    }

    DMSResult resultaat = toDmsResult(getDocuments(subMappen, zaak.getZaakId()));

    DMSResult filteredResult = new DMSResult();
    filteredResult.setDocuments(resultaat.getDocuments().stream()
        .filter(d -> equalsIgnoreCase(d.getZaakId(), zaak.getZaakId()))
        .collect(Collectors.toList()));
    return filteredResult;
  }

  @Override
  public DMSDocument save(String folderName, DMSDocument dmsDocument) {
    File dir = FilesystemDMSUtils.normalizeFolder(getDocumentsFolder(), folderName);
    FilesystemDMSUtils.save(dir, dmsDocument);
    return dmsDocument;
  }

  @Override
  public DMSDocument saveByPerson(BasePLExt pl, DMSDocument dmsDocument) {
    String subMap = getFoldersFromPL(pl).get(0);
    dmsDocument.setZaakId(subMap);
    save(subMap, dmsDocument);
    return dmsDocument;
  }

  @Override
  public DMSDocument saveByZaak(Zaak zaak, DMSDocument dmsDocument) {
    List<String> ids = getFoldersFromPL(zaak.getBasisPersoon());
    String zaakId = zaak.getZaakId();
    String subMap = ids.size() > 0 ? ids.get(0) : zaakId;
    dmsDocument.setZaakId(zaakId);
    return save(subMap, dmsDocument);
  }

  @Override
  @ThrowException("Fout bij opslaan document")
  public void save(PrintActie printActie, byte[] documentBytes) {
    if (printActie.getZaak() != null) {
      String id = astr(printActie.getZaak().getBurgerServiceNummer().getLongValue());
      if (!pos(id)) {
        id = printActie.getZaak().getZaakId();
      }

      File dir = FilesystemDMSUtils.normalizeFolder(getDocumentsFolder(), id);
      DocumentRecord document = printActie.getDocument();
      DocumentService documentService = getServices().getDocumentService();
      DocumentVertrouwelijkheid vertrouwelijkheid = documentService
          .getStandaardVertrouwelijkheid(document.getVertrouwelijkheid(), ONBEKEND);

      String ext = printActie.getPrintOptie().getUitvoerformaatType().getType();
      DMSDocument dmsDocument = DMSDocument.builder(DMSBytesContent.fromExtension(ext, documentBytes))
          .title(document.getDocument())
          .user(getServices().getGebruiker().getNaam())
          .datatype(document.getType())
          .zaakId(printActie.getZaak().getZaakId())
          .documentTypeDescription(document.getDocumentDmsType())
          .confidentiality(vertrouwelijkheid.getNaam())
          .alias(document.getAlias())
          .build();

      FilesystemDMSUtils.save(dir, dmsDocument);
    }
  }

  @Override
  public void delete(DMSDocument dmsDocument) {
    if (dmsDocument.getContent() instanceof DMSFileContent) {
      File deleteFile = ((DMSFileContent) dmsDocument.getContent()).getFile();
      if (deleteFile.exists()) {
        deleteFile.delete();
      }
    }
  }

  private File getDocumentsFolder() {
    String basePath = GbaConfig.getPath().getApplicationDir() + "/documenten/output/";
    String customPath = getServices().getParameterService().getParm(ParameterConstant.DOC_OUTPUT_PAD);
    return createFolder(new File(fil(customPath) ? customPath : basePath));
  }

  private DMSResult toDmsResult(List<DMSDocument> dmsDocuments) {
    DMSResult dmsResult = new DMSResult();
    dmsResult.getDocuments().addAll(dmsDocuments);
    Collections.sort(dmsResult.getDocuments());
    return dmsResult;
  }

  private List<String> getFoldersFromPL(BasePLExt pl) {
    List<String> list = new UniqueList<>();
    if (pl != null) {
      if (pl.getPersoon().getBsn().isNotBlank()) {
        list.add(astr(along(pl.getPersoon().getBsn().getCode())));
        list.add(pl.getPersoon().getBsn().getCode());
      }

      if (pl.getPersoon().getAnr().isNotBlank()) {
        list.add(astr(along(pl.getPersoon().getAnr().getCode())));
        list.add(pl.getPersoon().getAnr().getCode());
      }
    }

    return list;
  }

  private List<DMSDocument> getDocuments(List<String> subMappen, String subFolder) {
    List<DMSDocument> files = new ArrayList<>();
    for (File dir : FilesystemDMSUtils.normalizeFolders(getDocumentsFolder(), subMappen)) {
      if (fil(subFolder)) {
        FilesystemDMSUtils.getFilesByZaakId(dir, files, null, subFolder);
      } else {
        FilesystemDMSUtils.getFilesByFolder(dir, files, null);
      }
    }

    return files;
  }

  private int countDocuments(List<String> subMappen, String subFolder) {
    int count = 0;
    for (File dir : FilesystemDMSUtils.normalizeFolders(getDocumentsFolder(), subMappen)) {
      if (fil(subFolder)) {
        count += FilesystemDMSUtils.countFilesByZaakId(dir, subFolder);
      } else {
        count += FilesystemDMSUtils.countFilesInFolder(dir);
      }
    }

    return count;
  }

  private File createFolder(File dir) {
    try {
      if (!dir.exists()) {
        FileUtils.forceMkdir(dir);
        LOGGER.info("Directory: " + dir + " created.");
      }
      return dir;
    } catch (IOException e) {
      throw new ProException(ERROR, "Map kon niet worden aangemaakt");
    }
  }
}
