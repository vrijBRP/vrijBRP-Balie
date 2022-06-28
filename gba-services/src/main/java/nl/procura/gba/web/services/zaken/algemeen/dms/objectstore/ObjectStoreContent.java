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

package nl.procura.gba.web.services.zaken.algemeen.dms.objectstore;

import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;

import nl.procura.gba.web.services.zaken.algemeen.dms.DMSContent;
import nl.procura.objectstore.rest.domain.object.search.FieldName;
import nl.procura.objectstore.rest.domain.object.search.StorageObject;

import lombok.Data;

@Data
public class ObjectStoreContent implements DMSContent {

  private StorageObject  storageObject;
  private ContentFetcher contentFetcher;

  private ObjectStoreContent() {
  }

  public static ObjectStoreContent from(StorageObject storageObject, ContentFetcher fileContentFetcher) {
    ObjectStoreContent content = new ObjectStoreContent();
    content.setStorageObject(storageObject);
    content.setContentFetcher(fileContentFetcher);
    return content;
  }

  @Override
  public InputStream getInputStream() {
    return contentFetcher.get(storageObject);
  }

  @Override
  public Long getSize() {
    return NumberUtils.toLong(storageObject.getFields().getAsString(FieldName.FILE_SIZE.getName()), 0);
  }

  @Override
  public String getFilename() {
    return storageObject.getFields().getAsString(FieldName.FILE_NAME.getName());
  }

  @Override
  public String getExtension() {
    return FilenameUtils.getExtension(getFilename());
  }

  @Override
  public String getLocation() {
    return "";
  }

  public interface ContentFetcher {

    InputStream get(StorageObject object);
  }
}
