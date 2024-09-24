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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.filesystem.FilesystemDMSStorage;
import nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreDMSStorage;

public class DmsCombinedStorage extends AbstractDmsStorage {

  private final FilesystemDMSStorage  localStore;
  private final ObjectStoreDMSStorage objectStore;

  public DmsCombinedStorage(Services services, FilesystemDMSStorage localStore, ObjectStoreDMSStorage objectStore) {
    super("Combined DMS store");
    setServices(services);
    this.localStore = localStore;
    this.objectStore = objectStore;
  }

  @Override
  public DMSResult getDocumentsByPL(BasePLExt pl) {
    List<DMSDocument> documentList = new ArrayList<>();
    documentList.addAll(localStore.getDocumentsByPL(pl).getDocuments());
    documentList.addAll(objectStore.getDocumentsByPL(pl).getDocuments());
    return toDmsResult(documentList);
  }

  @Override
  public DMSResult getDocumentsById(String id) {
    List<DMSDocument> documentList = new ArrayList<>();
    documentList.addAll(localStore.getDocumentsById(id).getDocuments());
    documentList.addAll(objectStore.getDocumentsById(id).getDocuments());
    return toDmsResult(documentList);
  }

  @Override
  public DMSResult getDocumentsByZaak(Zaak zaak) {
    List<DMSDocument> documentList = new ArrayList<>();
    documentList.addAll(localStore.getDocumentsByZaak(zaak).getDocuments());
    documentList.addAll(objectStore.getDocumentsByZaak(zaak).getDocuments());
    return toDmsResult(documentList);
  }

  @Override
  public DMSResult getDocumentsByQuery(String query) {
    return toDmsResult(objectStore.getDocumentsByQuery(query).getDocuments());
  }

  @Override
  public int countDocumentsById(String id) {
    return localStore.countDocumentsById(id) + objectStore.countDocumentsById(id);
  }

  @Override
  public int countDocumentsByPL(BasePLExt pl) {
    return localStore.countDocumentsByPL(pl) + objectStore.countDocumentsByPL(pl);
  }

  @Override
  public int countDocumentsByZaak(Zaak zaak) {
    return localStore.countDocumentsByZaak(zaak) + objectStore.countDocumentsByZaak(zaak);
  }

  @Override
  public DMSDocument save(DMSDocument dmsDocument) {
    return getStorage().save(dmsDocument);
  }

  @Override
  public void updateMetadata(String collection, String id, Map<String, String> metadata) {
    getStorage().updateMetadata(collection, id, metadata);
  }

  @Override
  public void delete(DMSDocument dmsDocument) {
    if (dmsDocument.getStorage() == DMSStorageType.DEFAULT) {
      localStore.delete(dmsDocument);
    } else {
      getStorage().delete(dmsDocument);
    }
  }

  private DMSStorage getStorage() {
    DMSStorageType dmsType = DMSStorageType.valueOfCode(getSysteemParm(ParameterConstant.DOC_DMS_TYPE, false));
    if (dmsType == DMSStorageType.OBJECT_STORAGE) {
      return objectStore;
    } else {
      return localStore;
    }
  }
}
