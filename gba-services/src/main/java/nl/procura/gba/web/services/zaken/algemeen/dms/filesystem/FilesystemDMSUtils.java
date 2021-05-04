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

import static java.util.Collections.singletonList;
import static nl.procura.standard.Globalfunctions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import nl.procura.gba.web.services.zaken.algemeen.dms.DMSBytesContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSFileContent;
import nl.procura.standard.exceptions.ProException;

public class FilesystemDMSUtils {

  public FilesystemDMSUtils() {
  }

  public static int countFilesByZaakId(File folder, String zaakId) {
    int count = 0;
    if (folder.exists() && folder.isDirectory()) {
      IndexFile indexFile = new IndexFile(folder);
      List<IndexLine> indexLines = indexFile.getRowsByZaakId(zaakId);
      for (IndexLine line : indexLines) {
        if (new File(folder, line.getFileName()).exists()) {
          count++;
        }
      }
    }

    return count;
  }

  public static int countFilesInFolder(File folder) {
    int count = 0;
    if (folder.exists() && folder.isDirectory()) {
      try {
        Path dir = FileSystems.getDefault().getPath(folder.getPath());
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
          for (Path path : directoryStream) {
            if (path.toFile().exists()) {
              if (!equalsIgnoreCase(IndexFile.INDEX, path.getFileName().toString())) {
                count++;
              }
            } else {
              count += countFilesInFolder(path.toFile());
            }
          }
        }
      } catch (IOException e) {
        throw new ProException("Fout bij inlezen bestanden", e);
      }
    }

    return count;
  }

  public static void getFilesByZaakId(File folder,
      List<DMSDocument> dmsDocuments,
      HashSet<String> filterMap,
      String zaakId) {

    if (folder.exists() && folder.isDirectory()) {

      IndexFile indexFile = new IndexFile(folder);
      List<IndexLine> rowsByZaakId = indexFile.getRowsByZaakId(zaakId);

      for (IndexLine row : rowsByZaakId) {
        File file = new File(folder, row.getFileName());

        if (file.isFile()) {
          IndexLine indexLine = indexFile.toIndexRow(file);
          DMSDocument dmsDocument = indexLine.toDmsDocument(file);
          if (isRequestedFile(indexLine, file, filterMap)) {
            dmsDocuments.add(dmsDocument);
          }
        }
      }

      cleanupIndex(folder, indexFile);
      Collections.sort(dmsDocuments);
    }
  }

  public static void getFilesByFolder(File folder,
      List<DMSDocument> bestanden,
      HashSet<String> filterMap) {

    if (folder.exists() && folder.isDirectory()) {
      try {
        IndexFile indexFile = new IndexFile(folder);
        Path dir = FileSystems.getDefault().getPath(folder.getPath());

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
          for (Path path : directoryStream) {
            File file = path.toFile();
            if (file.exists()) {
              IndexLine indexLine = indexFile.toIndexRow(file);
              DMSDocument dmsDocument = indexLine.toDmsDocument(file);

              if (isRequestedFile(indexLine, file, filterMap)) {
                bestanden.add(dmsDocument);
              }

            } else {
              getFilesByZaakId(path.toFile(), bestanden, filterMap, null);
            }
          }
        }

        cleanupIndex(folder, indexFile);
        Collections.sort(bestanden);

      } catch (IOException e) {
        throw new ProException("Fout bij inlezen bestanden", e);
      }
    }
  }

  public static DMSDocument save(File subFolder, DMSDocument dmsDocument) {
    File file = saveToDisk(subFolder, dmsDocument.getContent());
    dmsDocument.setContent(DMSFileContent.from(file));
    IndexLine indexLine = IndexLine.of(dmsDocument);
    updateIndex(subFolder, true, singletonList(indexLine));
    return dmsDocument;
  }

  public static File normalizeFolder(File folder, String subFolderName) {
    File pad = normalizeFolder(new File(folder, subFolderName));
    if (!pad.exists()) {
      pad.mkdirs();
    }

    return pad;
  }

  public static List<File> normalizeFolders(File folder, List<String> fileNames) {
    List<File> list = new ArrayList<>();
    for (String filename : fileNames) {
      if (fil(filename)) {
        list.add(new File(folder, filename));
        list.add(new File(folder, getDividedFolder(filename)));
      }
    }

    return list;
  }

  private static File saveToDisk(File subFolder, DMSContent dmsContent) {
    if (dmsContent instanceof DMSFileContent) {
      DMSFileContent dmsFileContent = (DMSFileContent) dmsContent;
      File file = new File(subFolder.getAbsolutePath(), dmsFileContent.getFile().getName());
      try {
        FileUtils.copyFile(dmsFileContent.getFile(), file);
        return file;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else if (dmsContent instanceof DMSBytesContent) {
      DMSBytesContent dmsBytesContent = (DMSBytesContent) dmsContent;
      File file = new File(subFolder.getAbsolutePath(), dmsBytesContent.getFilename());
      try (FileOutputStream stream = new FileOutputStream(file)) {
        IOUtils.write(dmsBytesContent.getBytes(), stream);
        return file;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new RuntimeException("Unknown type of DMSContent");
    }
  }

  private static File normalizeFolder(File file) {
    return file.exists() ? file : new File(file.getParentFile(), getDividedFolder(file.getName()));
  }

  /**
   * File exists in indexfile, but not one filesystem
   */
  private static boolean fileExists(String[] filenames, String filename) {
    return Arrays.stream(filenames).anyMatch(fn -> equalsIgnoreCase(fn, filename));
  }

  private static String getDividedFolder(String fileName) {
    StringBuilder sb = new StringBuilder();
    for (String part : fileName.split("(?<=\\G...)")) {
      sb.append(part).append("/");
    }
    return sb.toString();
  }

  private static boolean isRequestedFile(IndexLine indexLine, File bestand, HashSet<String> filterMap) {
    if (eq(IndexFile.INDEX, bestand.getName())) {
      return false;
    }
    return (filterMap == null) || filterMap.contains(indexLine.getDataType());
  }

  private static synchronized void cleanupIndex(File folder, IndexFile indexFile) {
    String[] filenames = folder.list();
    List<IndexLine> lines = indexFile.getLines();
    List<IndexLine> cleanedLines = new ArrayList<>();
    for (IndexLine row : lines) {
      if (fileExists(filenames, row.getFileName())) {
        cleanedLines.add(row);
      }
    }

    if (!cleanedLines.containsAll(lines)) {
      updateIndex(folder, false, cleanedLines);
    }

    removeFolder(folder);
  }

  private static synchronized void updateIndex(File map, boolean append, List<IndexLine> rows) {
    FileWriter fw = null;
    try {
      File indexfile = new File(map, IndexFile.INDEX);
      if (!append && rows.size() == 0) {
        indexfile.delete();
        return;
      }

      fw = new FileWriter(indexfile, append);
      for (IndexLine row : rows) {
        fw.write(row.toString() + "\n");
      }

      fw.flush();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeQuietly(fw);
    }
  }

  /**
   * Remove folder if empty
   */
  private static void removeFolder(File dir) {
    if (dir.isDirectory()) {
      File[] files = dir.listFiles();
      if (files == null || files.length == 0) {
        if (dir.delete()) {
          removeFolder(dir.getParentFile());
        }
      }
    }
  }
}
