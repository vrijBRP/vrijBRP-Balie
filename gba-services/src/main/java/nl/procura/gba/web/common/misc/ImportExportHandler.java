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

package nl.procura.gba.web.common.misc;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionType.PROGRAMMING;

import java.io.*;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import nl.procura.gba.common.MiscUtils;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.functies.downloading.DownloadHandler;

public class ImportExportHandler {

  public static void exportObject(Map<String, byte[]> map, String filename, DownloadHandler downloadHandler) {
    ByteArrayOutputStream zipStream = new ByteArrayOutputStream();

    try {
      new FileZipper(zipStream, map);
      downloadHandler.download(new ByteArrayInputStream(zipStream.toByteArray()), filename, true);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ProException(PROGRAMMING, ERROR, "Fout bij het exporteren.", e);
    } finally {
      IOUtils.closeQuietly(zipStream);
    }
  }

  public static void readZip(File file, ImportInterface importInterface) {
    try {
      readZip(new FileInputStream(file), importInterface);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ProException(PROGRAMMING, ERROR, "Fout bij het importeren.", e);
    }
  }

  private static String getDir(String relPath) {
    int lastSlash = MiscUtils.cleanPath(relPath).lastIndexOf("/");
    return (lastSlash >= 0) ? relPath.substring(0, lastSlash) : "";
  }

  private static String getName(String relPath) {
    int lastSlash = MiscUtils.cleanPath(relPath).lastIndexOf("/");
    return (lastSlash >= 0) ? relPath.substring(lastSlash + 1) : relPath;
  }

  private static void readZip(InputStream is, final ImportInterface importInterface) {

    new FileZipper(is) {

      @Override
      public void onReadEntry(ZipInputStream zis, ZipEntry ze) {
        importInterface.importFile(zis, ze, getDir(ze.getName()), getName(ze.getName()));
      }
    };
  }

  public interface ImportInterface {

    void importFile(ZipInputStream zis, ZipEntry entry, String dir, String name);
  }
}
