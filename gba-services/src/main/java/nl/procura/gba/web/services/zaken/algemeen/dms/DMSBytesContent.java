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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;

import lombok.Data;

@Data
public class DMSBytesContent implements DMSContent {

  private String filename;
  private byte[] bytes;

  private DMSBytesContent() {
  }

  public static DMSBytesContent fromFilename(String filename, byte[] bytes) {
    DMSBytesContent content = new DMSBytesContent();
    content.setFilename(filename);
    content.setBytes(bytes);
    return content;
  }

  public static DMSBytesContent fromExtension(String extension, byte[] bytes) {
    String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    DMSBytesContent content = new DMSBytesContent();
    content.setFilename(timestamp + "." + extension);
    content.setBytes(bytes);
    return content;
  }

  @Override
  public InputStream getInputStream() {
    return new ByteArrayInputStream(bytes);
  }

  @Override
  public String getFilename() {
    return filename;
  }

  @Override
  public String getExtension() {
    return FilenameUtils.getExtension(getExtension());
  }

  @Override
  public String getLocation() {
    return "";
  }
}
