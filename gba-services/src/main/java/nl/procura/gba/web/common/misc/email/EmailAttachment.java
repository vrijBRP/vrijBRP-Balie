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

package nl.procura.gba.web.common.misc.email;

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import nl.procura.standard.exceptions.ProException;

public class EmailAttachment {

  private String fileName;
  private byte[] content  = new byte[0];
  private String fileType = "application/octet-stream";

  public EmailAttachment(String fileName, byte[] content) {
    this.fileName = fileName;
    this.content = content;
  }

  public EmailAttachment(String fileName, InputStream is) {
    this.fileName = fileName;
    try {
      this.content = IOUtils.toByteArray(is);
    } catch (IOException e) {
      throw new ProException(ERROR, "Kan bijlage niet vinden");
    } finally {
      IOUtils.closeQuietly(is);
    }
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }
}
