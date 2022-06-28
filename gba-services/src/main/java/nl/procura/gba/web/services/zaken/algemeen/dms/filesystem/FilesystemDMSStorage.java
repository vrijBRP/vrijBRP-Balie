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

import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.DOCUMENTS;
import static org.apache.commons.lang3.StringUtils.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.AbstractDmsStorage;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSFileContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

/**
 * The local storage of documents
 */
public class FilesystemDMSStorage extends AbstractDmsStorage {

  private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemDMSStorage.class.getName());

  public FilesystemDMSStorage() {
    super("Local storage service");
  }

  @Override
  public int countDocumentsByPL(BasePLExt pl) {
    return countDocuments(getCustomerIds(pl), null);
  }

  @Override
  public int countDocumentByZaak(Zaak zaak) {
    return countDocuments(getFoldersFromZaak(zaak), zaak.getZaakId());
  }

  @Override
  @ThrowException(type = DOCUMENTS, value = "Fout bij ophalen opgeslagen bestanden")
  public DMSResult getDocumentsByPL(BasePLExt pl) {
    return toDmsResult(getDocuments(getCustomerIds(pl), null));
  }

  @Override
  @ThrowException(type = DOCUMENTS, value = "Fout bij ophalen opgeslagen bestanden")
  public DMSResult getDocumentsByZaak(Zaak zaak) {
    List<String> folders = getFoldersFromZaak(zaak);
    DMSResult resultaat = toDmsResult(getDocuments(folders, zaak.getZaakId()));
    DMSResult filteredResult = new DMSResult();
    filteredResult.setDocuments(resultaat.getDocuments().stream()
        .filter(document -> equalsIgnoreCase(document.getZaakId(), zaak.getZaakId()))
        .collect(Collectors.toList()));
    return filteredResult;
  }

  @Override
  public DMSDocument save(DMSDocument dmsDocument) {
    if (isBlank(dmsDocument.getCustomerId()) && isBlank(dmsDocument.getZaakId())) {
      throw new ProException("Geen folder bepaald voor het op te slaan document.");
    }
    if (isBlank(dmsDocument.getZaakId())) { // Uploads to the customer, not the case
      dmsDocument.setZaakId(dmsDocument.getCustomerId());
    }
    String folder = defaultIfBlank(dmsDocument.getCustomerId(), dmsDocument.getZaakId());
    File dir = FilesystemDMSUtils.normalizeFolder(getDocumentsFolder(), folder);
    FilesystemDMSUtils.save(dir, dmsDocument);
    return dmsDocument;
  }

  @Override
  public void delete(DMSDocument dmsDocument) {
    if (dmsDocument.getContent() instanceof DMSFileContent) {
      File deleteFile = ((DMSFileContent) dmsDocument.getContent()).getFile();
      if (deleteFile.exists()) {
        if (!deleteFile.delete()) {
          throw new ProException(WARNING, "Fout bij verwijderen van het bestand");
        }
      }
    }
  }

  private File getDocumentsFolder() {
    String basePath = GbaConfig.getPath().getApplicationDir() + "/documenten/output/";
    String customPath = getServices().getParameterService().getParm(ParameterConstant.DOC_OUTPUT_PAD);
    return createFolder(new File(fil(customPath) ? customPath : basePath));
  }

  private List<String> getFoldersFromZaak(Zaak zaak) {
    BsnFieldValue bsn = zaak.getBurgerServiceNummer();
    AnrFieldValue anr = zaak.getAnummer();

    Set<String> folders = new LinkedHashSet<>();
    folders.add(zaak.getZaakId());

    if (bsn.isCorrect()) {
      folders.add(bsn.getStringValue());
      folders.add(astr(bsn.getLongValue()));
    }

    if (anr.isCorrect()) {
      folders.add(anr.getStringValue());
      folders.add(astr(anr.getLongValue()));
    }
    return new ArrayList<>(folders);
  }

  private List<DMSDocument> getDocuments(List<String> subMappen, String subFolder) {
    List<DMSDocument> files = new ArrayList<>();
    for (File dir : FilesystemDMSUtils.normalizeFolders(getDocumentsFolder(), subMappen)) {
      if (fil(subFolder)) {
        files.addAll(FilesystemDMSUtils.getFilesByZaakId(dir, subFolder));
      } else {
        files.addAll(FilesystemDMSUtils.getFilesByFolder(dir));
      }
    }

    Collections.sort(files);
    return files;
  }

  private int countDocuments(List<String> folders, String zaakId) {
    int count = 0;
    for (File dir : FilesystemDMSUtils.normalizeFolders(getDocumentsFolder(), folders)) {
      if (isNotBlank(zaakId)) {
        count += FilesystemDMSUtils.countFilesByZaakId(dir, zaakId);
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