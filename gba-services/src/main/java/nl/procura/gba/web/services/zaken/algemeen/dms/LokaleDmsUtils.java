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

package nl.procura.gba.web.services.zaken.algemeen.dms;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.web.services.zaken.algemeen.dms.IndexBestand.IndexRegel;
import nl.procura.standard.Globalfunctions;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;

@SuppressWarnings("deprecation")
public class LokaleDmsUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(LokaleDmsUtils.class.getName());

  public LokaleDmsUtils() {
  }

  public static File getGenormaliseerdeFolder(File folder, String bestandsnaam) {

    File pad = getGenormaliseerdeMap(new File(folder, bestandsnaam));

    if (!pad.exists()) {
      pad.mkdirs();
    }

    return pad;
  }

  public static List<File> getGenormaliseerdeMappen(File folder, List<String> fileNames) {

    List<File> list = new ArrayList<>();

    for (String filename : fileNames) {

      if (fil(filename)) {

        list.add(new File(folder, filename));
        list.add(new File(folder, getOpgedeeldeMap(filename)));
      }
    }

    return list;
  }

  public static int laadAantalBestandenByZaakId(File folder, String zaakId) {

    int count = 0;
    if (folder.exists() && folder.isDirectory()) {
      IndexBestand indexFile = new IndexBestand(folder);
      List<IndexRegel> rowsByZaakId = indexFile.getRowsByZaakId(zaakId);
      for (IndexRegel row : rowsByZaakId) {
        if (new File(folder, row.getVolledigeBestandsnaam()).exists()) {
          count++;
        }
      }
    }

    return count;
  }

  public static void laadBestandenByZaakId(File folder, List<LokaleDmsDocument> bestanden, HashSet<String> filterMap,
      String zaakId) {

    if (folder.exists() && folder.isDirectory()) {

      IndexBestand indexFile = new IndexBestand(folder);
      List<IndexRegel> rowsByZaakId = indexFile.getRowsByZaakId(zaakId);

      for (IndexRegel row : rowsByZaakId) {

        File file = new File(folder, row.getVolledigeBestandsnaam());

        if (file.isFile()) {

          LokaleDmsDocument cof = new LokaleDmsDocument();
          cof.setBestandsnaam(file.getName());
          cof.setPad(file.getAbsolutePath());
          cof.setExtensie(getFileExtension(file.getName()));

          IndexRegel ir = indexFile.getIndexRow(file);
          cof.setTitel(ir.getTitel());
          cof.setAangemaaktDoor(ir.getAangemaaktDoor());
          cof.setDatum(ir.getDatum());
          cof.setTijd(ir.getTijd());
          cof.setDatatype(ir.getDataType());
          cof.setZaakId(ir.getZaakId());
          cof.setDmsNaam(ir.getDmsNaam());
          cof.setVertrouwelijkheid(ir.getVertrouwelijkheid());

          if (!isGevraagdeBestand(ir, file, filterMap)) {
            continue;
          }

          bestanden.add(cof);
        }
      }

      opschonenIndex(folder, indexFile);

      Collections.sort(bestanden);
    }
  }

  private static boolean isFile(Path path) {
    return FilenameUtils.getExtension(path.getFileName().toString()).length() > 0;
  }

  public static int laadAantalBestandenByFolder(File folder) {

    int count = 0;
    if (folder.exists() && folder.isDirectory()) {
      try {
        Path dir = FileSystems.getDefault().getPath(folder.getPath());
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
          for (Path path : directoryStream) {
            if (isFile(path)) {
              if (!Globalfunctions.equalsIgnoreCase(IndexBestand.INDEX, path.getFileName().toString())) {
                count++;
              }
            } else {
              count += laadAantalBestandenByFolder(path.toFile());
            }
          }
        }
      } catch (IOException e) {
        throw new ProException("Fout bij inlezen bestanden", e);
      }
    }

    return count;
  }

  public static void laadBestandenByFolder(File folder, List<LokaleDmsDocument> bestanden,
      HashSet<String> filterMap) {

    if (folder.exists() && folder.isDirectory()) {
      try {
        IndexBestand indexFile = new IndexBestand(folder);
        Path dir = FileSystems.getDefault().getPath(folder.getPath());

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir)) {
          for (Path path : directoryStream) {

            if (isFile(path)) {

              File file = path.toFile();

              LokaleDmsDocument cof = new LokaleDmsDocument();
              cof.setBestandsnaam(file.getName());
              cof.setPad(file.getAbsolutePath());
              cof.setExtensie(getFileExtension(file.getName()));

              IndexRegel ir = indexFile.getIndexRow(file);
              cof.setTitel(ir.getTitel());
              cof.setAangemaaktDoor(ir.getAangemaaktDoor());
              cof.setDatum(ir.getDatum());
              cof.setTijd(ir.getTijd());
              cof.setDatatype(ir.getDataType());
              cof.setZaakId(ir.getZaakId());
              cof.setDmsNaam(ir.getDmsNaam());
              cof.setVertrouwelijkheid(ir.getVertrouwelijkheid());

              if (!isGevraagdeBestand(ir, file, filterMap)) {
                continue;
              }

              bestanden.add(cof);
            } else {
              laadBestandenByZaakId(path.toFile(), bestanden, filterMap, null);
            }
          }
        }

        opschonenIndex(folder, indexFile);

        Collections.sort(bestanden);
      } catch (IOException e) {
        throw new ProException("Fout bij inlezen bestanden", e);
      }
    }
  }

  public static void opslaan(byte[] documentBytes, File folder, String extensie, String titel, String aangemaaktDoor,
      String datatype, String zaakId, String dmsNaam, String vertrouwelijkheid) {

    String tijdstempel = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    File opslagBestand = new File(folder.getAbsolutePath(), (tijdstempel + "." + extensie));
    FileOutputStream stream = null;

    try {
      stream = new FileOutputStream(opslagBestand);
      IOUtils.write(documentBytes, stream);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(stream);
    }

    LOGGER.debug("Opslaan bestand: " + opslagBestand.getAbsolutePath());

    updateIndex(folder, true,
        asList(addIndexRegel(tijdstempel, extensie, titel, aangemaaktDoor, datatype, zaakId, dmsNaam,
            vertrouwelijkheid)));
  }

  public static DmsDocument opslaan(File bestand, File map, String titel, String gebruiker, String datatype,
      String zaakId,
      String dmsNaam, String vertrouwelijkheid) {

    File opslagBestand = new File(map.getAbsolutePath(), bestand.getName());

    try {
      FileUtils.copyFile(bestand, opslagBestand);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    LOGGER.debug("Opslaan bestand: " + opslagBestand.getAbsolutePath());

    String bestandsnaam = FilenameUtils.getBaseName(bestand.getName());
    String extensie = FilenameUtils.getExtension(bestand.getName());

    IndexRegel indexRegel = addIndexRegel(bestandsnaam, extensie, titel, gebruiker, datatype, zaakId, dmsNaam,
        vertrouwelijkheid);
    updateIndex(map, true, asList(indexRegel));
    DmsDocument dmsDocument = toDmsDocument(indexRegel);
    dmsDocument.setPad(bestand.getAbsolutePath());
    return dmsDocument;
  }

  private static DmsDocument toDmsDocument(IndexRegel indexRegel) {
    DmsDocument document = new DmsDocument();
    document.setAangemaaktDoor(indexRegel.getAangemaaktDoor());
    document.setBestandsnaam(indexRegel.getVolledigeBestandsnaam());
    document.setDatatype(indexRegel.getDataType());
    document.setDmsNaam(indexRegel.getDmsNaam());
    document.setExtensie(indexRegel.getBestandsextensie());
    document.setTitel(indexRegel.getTitel());
    document.setZaakId(indexRegel.getZaakId());
    document.setVertrouwelijkheid(indexRegel.getVertrouwelijkheid());
    document.setDatum(along(indexRegel.getDatum()));
    document.setTijd(along(indexRegel.getTijd()));

    return document;
  }

  private static IndexRegel addIndexRegel(String bestandsnaam, String extensie, String titel, String aangemaaktDoor,
      String datatype, String zaakId, String dmsNaam, String vertrouwelijkheid) {

    IndexRegel indexRow = new IndexRegel(new ProcuraDate());
    indexRow.setBestandsnaam(bestandsnaam);
    indexRow.setExtensie(extensie);
    indexRow.setTitel(titel);
    indexRow.setAangemaaktDoor(aangemaaktDoor);
    indexRow.setDataType(datatype);
    indexRow.setZaakId(zaakId);
    indexRow.setDmsNaam(dmsNaam);
    indexRow.setVertrouwelijkheid(vertrouwelijkheid);

    return indexRow;
  }

  private static boolean bestaatBestand(String[] bestandsnamen, String bestandsnaam) {

    for (String fn : bestandsnamen) {
      if (equalsIgnoreCase(fn, bestandsnaam)) {
        return true;
      }
    }

    return false;
  }

  private static String getFileExtension(String bestandsnaam) {
    return FilenameUtils.getExtension(bestandsnaam);
  }

  private static File getGenormaliseerdeMap(File bestand) {
    return bestand.exists() ? bestand : new File(bestand.getParentFile(), getOpgedeeldeMap(bestand.getName()));
  }

  private static String getOpgedeeldeMap(String fileName) {

    StringBuilder sb = new StringBuilder();

    for (String part : fileName.split("(?<=\\G...)")) {
      sb.append(part + "/");
    }

    return sb.toString();
  }

  private static boolean isGevraagdeBestand(IndexRegel regel, File bestand, HashSet<String> filterMap) {
    if (eq(IndexBestand.INDEX, bestand.getName())) {
      return false;
    }
    return (filterMap == null) || filterMap.contains(regel.getDataType());
  }

  /**
   * Verwijder overbodige regels
   */
  private static synchronized void opschonenIndex(File folder, IndexBestand indexFile) {

    List<IndexRegel> regels = new ArrayList<>();

    String[] bestandsnamen = folder.list();

    List<IndexRegel> rows = indexFile.getRows();

    for (IndexRegel row : rows) {

      if (bestaatBestand(bestandsnamen, row.getBestandsnaam() + "." + row.getBestandsextensie())) {

        // Bestand komt voor in indexfile, maar niet op filesystem

        regels.add(row);
      }
    }

    if (!regels.containsAll(rows)) {

      updateIndex(folder, false, regels);
    }

    verwijderFolder(folder);
  }

  private static synchronized void updateIndex(File map, boolean toevoegen, List<IndexRegel> rows) {

    FileWriter fw = null;

    try {
      File indexfile = new File(map, IndexBestand.INDEX);

      LOGGER.debug("Opslaan index: " + rows.size() + " regels.");

      if (!toevoegen && rows.size() == 0) {
        indexfile.delete(); // Indexfile verwijderen
        return;
      }

      fw = new FileWriter(indexfile, toevoegen);

      for (IndexRegel row : rows) {
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
   * Verwijder mappen. Net zolang er geen andere bestanden in staan.
   */
  private static void verwijderFolder(File dir) {

    if (dir.isDirectory() && (dir.listFiles() == null || dir.listFiles().length == 0)) {

      if (dir.delete()) {

        verwijderFolder(dir.getParentFile());
      }
    }
  }
}
