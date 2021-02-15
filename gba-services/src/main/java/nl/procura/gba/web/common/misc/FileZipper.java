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

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import nl.procura.standard.exceptions.ProException;

public class FileZipper {

  private File            parentDir = null;
  private ZipOutputStream out       = null;

  public FileZipper(InputStream is) {
    ZipInputStream zis = new ZipInputStream(is);

    try {
      ZipEntry ze = zis.getNextEntry();
      while (ze != null) {
        onReadEntry(zis, ze);
        ze = zis.getNextEntry();
      }
    } catch (IOException e) {
      throw new ProException(SELECT, WARNING, "Fout bij lezen zip-bestand", e);
    } finally {
      IOUtils.closeQuietly(zis);
      IOUtils.closeQuietly(is);
    }
  }

  public FileZipper(OutputStream os, File... files) {

    try {
      openZip(os);

      for (File file : files) {
        parentDir = file;
        readFiles(parentDir);
      }
    } catch (IOException e) {
      throw new ProException(SELECT, WARNING, "Fout bij schrijven ZIP bestand", e);
    } finally {
      closeZip();
    }
  }

  public FileZipper(OutputStream os, Map<String, byte[]> map) {

    try {
      openZip(os);

      for (Object o : map.entrySet()) {
        Entry<String, byte[]> e = (Entry<String, byte[]>) o;
        addEntry(e.getKey(), e.getValue());
      }
    } catch (IOException e) {
      throw new ProException(SELECT, WARNING, "Fout bij schrijven ZIP bestand", e);
    } finally {
      closeZip();
    }
  }

  @SuppressWarnings("unused")
  public void onReadEntry(ZipInputStream zis, ZipEntry ze) {
  }

  private void addEntry(File file) throws IOException {
    addEntry(getRelatiefPad(file), FileUtils.readFileToByteArray(file));
  }

  private void addEntry(String key, byte[] bytes) throws IOException {
    out.putNextEntry(new ZipEntry(key));
    out.write(bytes);
    out.closeEntry();
  }

  private void closeZip() {
    IOUtils.closeQuietly(out);
  }

  private String getRelatiefPad(File file) {
    return normalize(file).replaceAll(normalize(parentDir.getParentFile()), "").replaceAll("^/", "");
  }

  private String normalize(File file) {
    return file.getAbsolutePath().replaceAll("\\\\", "/");
  }

  private void openZip(OutputStream os) {
    out = new ZipOutputStream(os);
  }

  private void readFiles(File dir) throws IOException {

    if (dir.exists()) {
      if (dir.isDirectory()) {
        for (File file : dir.listFiles()) {
          readFiles(file);
        }
      } else {
        addEntry(dir);
      }
    }
  }
}
