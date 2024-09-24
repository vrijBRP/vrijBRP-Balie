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

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import nl.procura.commons.core.exceptions.ProException;

import lombok.Data;

@Data
public class DMSFileContent implements DMSContent {

  private File        file;
  private InputStream inputStream;

  private DMSFileContent() {
  }

  public static DMSFileContent from(File file) {
    DMSFileContent content = new DMSFileContent();
    content.setFile(file);
    return content;
  }

  @Override
  public InputStream getInputStream() {
    try {
      return new FileInputStream(file);
    } catch (FileNotFoundException exception) {
      throw new ProException(WARNING, "Fout bij laden bestand", exception);
    }
  }

  @Override
  public byte[] getBytes() {
    try {
      return FileUtils.readFileToByteArray(file);
    } catch (IOException exception) {
      throw new ProException(WARNING, "Fout bij laden bestand", exception);
    }
  }

  @Override
  public Long getSize() {
    return file.length();
  }

  @Override
  public String getFilename() {
    return file.getName();
  }

  @Override
  public String getExtension() {
    return FilenameUtils.getExtension(getFilename());
  }

  @Override
  public String getLocation() {
    return parsePath(file.getAbsolutePath());
  }

  private String parsePath(String pad) {
    String[] splits = pad.split("documenten");
    if (splits.length > 1) {
      return FilenameUtils.separatorsToUnix(splits[1]);
    }
    return pad;
  }
}
