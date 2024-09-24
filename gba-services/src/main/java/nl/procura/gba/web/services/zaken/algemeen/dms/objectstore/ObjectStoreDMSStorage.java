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

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_OBJECT_STORAGE_PW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_OBJECT_STORAGE_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_OBJECT_STORAGE_USERNAME;
import static nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreFieldName.ALIAS;
import static nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreFieldName.CONFIDENTIALITY;
import static nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreFieldName.CUSTOMER_ID;
import static nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreFieldName.DATA_TYPE;
import static nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreFieldName.DOCUMENT_TYPE_DESCRIPTION;
import static nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreFieldName.DOSSIER_ID;
import static nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreFieldName.TITLE;
import static nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreFieldName.USER;
import static nl.procura.standard.Globalfunctions.along;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.codec.binary.Base64;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.AbstractDmsStorage;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSStorageType;
import nl.procura.storage.client.StorageClient;
import nl.procura.storage.client.StorageClientConfig;
import nl.procura.storage.client.model.AddObjectRequest;
import nl.procura.storage.client.model.FindObjectQuery;
import nl.procura.storage.client.model.Metadata;
import nl.procura.storage.client.model.SearchOptions;
import nl.procura.storage.client.model.StorageFile;
import nl.procura.storage.client.model.StorageObject;
import nl.procura.storage.client.model.UpdateObjectRequest;
import nl.procura.storage.queryparser.Query;
import nl.procura.storage.queryparser.QueryParser;

public class ObjectStoreDMSStorage extends AbstractDmsStorage {

  public ObjectStoreDMSStorage() {
    super("ObjectStore service");
  }

  @Override
  public DMSResult getDocumentsByPL(BasePLExt pl) {
    return toDmsResult(getClient().objects()
        .find(getCollection(), getSearchByPL(pl))
        .getElements().stream()
        .map(this::toDmsDocument)
        .collect(Collectors.toList()));
  }

  @Override
  public DMSResult getDocumentsByZaak(Zaak zaak) {
    return toDmsResult(getClient().objects()
        .find(getCollection(), getSearchByZaak(zaak))
        .getElements().stream()
        .map(this::toDmsDocument)
        .collect(Collectors.toList()));
  }

  @Override
  public DMSResult getDocumentsById(String id) {
    return toDmsResult(getClient().objects()
        .find(getCollection(), getSearchById(id))
        .getElements().stream()
        .map(this::toDmsDocument)
        .collect(Collectors.toList()));
  }

  @Override
  public DMSResult getDocumentsByQuery(String query) {
    return toDmsResult(getClient().objects()
        .find(getCollection(), getSearchByQuery(query))
        .getElements().stream()
        .map(this::toDmsDocument)
        .collect(Collectors.toList()));
  }

  @Override
  public int countDocumentsById(String id) {
    return getClient().objects().find(getCollection(), getSearchById(id)).getTotalElements();
  }

  @Override
  public int countDocumentsByPL(BasePLExt pl) {
    return getClient().objects().find(getCollection(), getSearchByPL(pl)).getTotalElements();
  }

  @Override
  public int countDocumentsByZaak(Zaak zaak) {
    return getClient().objects().find(getCollection(), getSearchByZaak(zaak)).getTotalElements();
  }

  @Override
  public DMSDocument save(DMSDocument dmsDocument) {
    Metadata metadata = new Metadata()
        .add(ALIAS.getCode(), dmsDocument.getAlias())
        .add(TITLE.getCode(), defaultIfBlank(dmsDocument.getTitle(), dmsDocument.getContent().getFilename()))
        .add(USER.getCode(), dmsDocument.getUser())
        .add(DATA_TYPE.getCode(), dmsDocument.getDatatype())
        .add(CUSTOMER_ID.getCode(), dmsDocument.getCustomerId())
        .add(DOSSIER_ID.getCode(), dmsDocument.getZaakId())
        .add(CONFIDENTIALITY.getCode(), dmsDocument.getConfidentiality())
        .add(DOCUMENT_TYPE_DESCRIPTION.getCode(), dmsDocument.getDocumentTypeDescription());

    AddObjectRequest request = new AddObjectRequest();
    request.setMetadata(metadata);
    request.setFile(new StorageFile()
        .setFilename(dmsDocument.getContent().getFilename())
        .setBase64(Base64.encodeBase64String(dmsDocument.getContent().getBytes())));

    getClient().objects().add(getCollection(), request);
    return dmsDocument;
  }

  @Override
  public void delete(DMSDocument dmsDocument) {
    getClient().objects().delete(getCollection(), dmsDocument.getUuid()).getDetails();
  }

  @Override
  public void updateMetadata(String collection, String id, Map<String, String> data) {
    UpdateObjectRequest request = new UpdateObjectRequest();
    Metadata metadata = new Metadata();
    metadata.putAll(data);
    request.setMetadata(metadata);
    getClient().objects().update(collection, id, request).getDetails();
  }

  private StorageClient getClient() {
    String baseUrl = getServices().getAantekeningService().getSysteemParm(DOC_OBJECT_STORAGE_URL, true);
    String un = getServices().getAantekeningService().getSysteemParm(DOC_OBJECT_STORAGE_USERNAME, true);
    String pw = getServices().getAantekeningService().getSysteemParm(DOC_OBJECT_STORAGE_PW, true);
    return StorageClient.builder().config(StorageClientConfig.builder()
        .baseUrl(baseUrl)
        .username(un)
        .password(pw)
        .build()).build();
  }

  private String getCollection() {
    return "vrijbrp-file-archive";
  }

  private DMSDocument toDmsDocument(StorageObject object) {
    String created = object.getMetadata().getCreated();
    LocalDateTime dateTime = LocalDateTime.parse(created);
    return DMSDocument.builder()
        .collection(getCollection())
        .uuid(object.getId())
        .content(ObjectStoreContent.from(object, this::getFileByStorageObject))
        .alias(getField(object, ALIAS.getCode()))
        .title(getField(object, TITLE.getCode()))
        .user(getField(object, USER.getCode()))
        .datatype(getField(object, DATA_TYPE.getCode()))
        .customerId(getField(object, CUSTOMER_ID.getCode()))
        .zaakId(getField(object, DOSSIER_ID.getCode()))
        .date(along(dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"))))
        .time(along(dateTime.format(DateTimeFormatter.ofPattern("HHmmss"))))
        .documentTypeDescription(getField(object, DOCUMENT_TYPE_DESCRIPTION.getCode()))
        .confidentiality(getField(object, CONFIDENTIALITY.getCode()))
        .storage(DMSStorageType.OBJECT_STORAGE)
        .otherProperties(getOtherProperties(object))
        .build();
  }

  private Map<String, String> getOtherProperties(StorageObject object) {
    Map<String, String> properties = new HashMap<>();
    METADATA_LOOP: for (Entry<String, Object> entry : object.getMetadata().entrySet()) {
      for (ObjectStoreFieldName fieldName : ObjectStoreFieldName.values()) {
        if (fieldName.getCode().equals(entry.getKey()) || entry.getKey().startsWith("_")) {
          continue METADATA_LOOP;
        }
      }
      properties.put(entry.getKey(), entry.getValue().toString());
    }
    return properties;
  }

  private String getField(StorageObject object, String code) {
    return object.getMetadata().getAsString(code);
  }

  private InputStream getFileByStorageObject(StorageObject object) {
    return new ByteArrayInputStream(getClient().objects().getFile(getCollection(), object.getId()));
  }

  private FindObjectQuery getSearchById(String id) {
    return new FindObjectQuery()
        .setQuery(Query.or(Stream.of(id)
            .map(val -> Query.is(DOSSIER_ID.getCode(), val))
            .toArray(Query[]::new)));
  }

  private FindObjectQuery getSearchByPL(BasePLExt pl) {
    return new FindObjectQuery()
        .setQuery(Query.or(getCustomerIds(pl)
            .stream()
            .map(id -> Query.is(CUSTOMER_ID.getCode(), id))
            .toArray(Query[]::new)));
  }

  private FindObjectQuery getSearchByZaak(Zaak zaak) {
    return getSearchByQuery(Query.is(DOSSIER_ID.getCode(), zaak.getZaakId()));
  }

  private FindObjectQuery getSearchByQuery(Query query) {
    return new FindObjectQuery(query, new SearchOptions(0, 100));
  }

  private FindObjectQuery getSearchByQuery(String query) {
    return getSearchByQuery(QueryParser.parse(query));
  }
}
