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

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreFieldName.*;
import static nl.procura.objectstore.rest.domain.object.search.QueryOperator.EQUALS;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.vaadin.service.FileTypeResolver;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.AbstractDmsStorage;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSStorageType;
import nl.procura.objectstore.rest.client.StorageClient;
import nl.procura.objectstore.rest.domain.file.FileRequest;
import nl.procura.objectstore.rest.domain.object.add.AddObject;
import nl.procura.objectstore.rest.domain.object.add.AddObjectRequest;
import nl.procura.objectstore.rest.domain.object.add.StorageFile;
import nl.procura.objectstore.rest.domain.object.delete.DeleteObjectRequest;
import nl.procura.objectstore.rest.domain.object.search.*;
import nl.procura.standard.exceptions.ProException;

public class ObjectStoreDMSStorage extends AbstractDmsStorage {

  public ObjectStoreDMSStorage() {
    super("ObjectStore service");
  }

  @Override
  public DMSResult getDocumentsByPL(BasePLExt pl) {
    return toDmsResult(getClient().getObjects()
        .search(getSearchByPL(pl))
        .getResults().stream()
        .map(this::toDmsDocument)
        .collect(Collectors.toList()));
  }

  @Override
  public DMSResult getDocumentsByZaak(Zaak zaak) {
    return toDmsResult(getClient().getObjects()
        .search(getSearchByZaak(zaak))
        .getResults().stream()
        .map(this::toDmsDocument)
        .collect(Collectors.toList()));
  }

  @Override
  public int countDocumentsByPL(BasePLExt pl) {
    return getClient().getObjects().search(getSearchByPL(pl).setPageSize(0)).getTotalCount();
  }

  @Override
  public int countDocumentByZaak(Zaak zaak) {
    return getClient().getObjects().search(getSearchByZaak(zaak).setPageSize(0)).getTotalCount();
  }

  @Override
  public DMSDocument save(DMSDocument dmsDocument) {

    try {
      AddObjectRequest request = new AddObjectRequest(getCollection());
      String dataAsBase64 = Base64.encodeBase64String(IOUtils.toByteArray(dmsDocument.getContent().getInputStream()));

      Fields fields = new Fields();
      fields.set(ALIAS.getCode(), dmsDocument.getAlias());
      fields.set(TITLE.getCode(), defaultIfBlank(dmsDocument.getTitle(), dmsDocument.getContent().getFilename()));
      fields.set(USER.getCode(), dmsDocument.getUser());
      fields.set(DATA_TYPE.getCode(), dmsDocument.getDatatype());
      fields.set(CUSTOMER_ID.getCode(), dmsDocument.getCustomerId());
      fields.set(DOSSIER_ID.getCode(), dmsDocument.getZaakId());
      fields.set(CONFIDENTIALITY.getCode(), dmsDocument.getConfidentiality());
      fields.set(DOCUMENT_TYPE_DESCRIPTION.getCode(), dmsDocument.getDocumentTypeDescription());

      request.addObject(new AddObject()
          .setFields(fields)
          .setFile(new StorageFile()
              .setFilename(dmsDocument.getContent().getFilename())
              .setMediaType(FileTypeResolver.getMIMEType(dmsDocument.getContent().getFilename()))
              .setDataAsBase64(dataAsBase64)));
      getClient().getObjects().add(request);
      return dmsDocument;

    } catch (IOException e) {
      throw new ProException("Fout bij opslaan document.", e);
    }
  }

  @Override
  public void delete(DMSDocument dmsDocument) {
    int results = getClient().getObjects().delete(new DeleteObjectRequest(new SearchObjectRequest()
        .setCollection(getCollection())
        .setUuid(dmsDocument.getUuid())))
        .getResult();
    if (results < 0) {
      throw new ProException(INFO, "Er zijn geen documenten verwijderd.");
    }
  }

  private StorageClient getClient() {
    String baseUrl = getServices().getAantekeningService().getSysteemParm(DOC_OBJECT_STORAGE_URL, true);
    String un = getServices().getAantekeningService().getSysteemParm(DOC_OBJECT_STORAGE_USERNAME, true);
    String pw = getServices().getAantekeningService().getSysteemParm(DOC_OBJECT_STORAGE_PW, true);
    return new StorageClient(baseUrl, un, pw);
  }

  private String getCollection() {
    return "balie-bestanden";
  }

  private DMSDocument toDmsDocument(StorageObject object) {
    String created = getField(object, FieldName.CREATED.getName());
    LocalDateTime dateTime = LocalDateTime.parse(created);
    return DMSDocument.builder()
        .uuid(object.getUuid())
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
        .storage(DMSStorageType.OBJECTSTORE)
        .build();
  }

  private String getField(StorageObject object, String code) {
    return object.getFields().getAsString(code);
  }

  private InputStream getFileByStorageObject(StorageObject object) {
    String fileUUID = getField(object, FieldName.FILE_UUID.getName());
    return getClient().getFiles().getFile(new FileRequest()
        .setSearch(new SearchObjectRequest()
            .setCollection(getCollection())
            .setQuery(Query.is(FieldName.FILE_UUID.getName(), EQUALS, fileUUID))));
  }

  private SearchObjectRequest getSearchByPL(BasePLExt pl) {
    return new SearchObjectRequest()
        .setCollection(getCollection())
        .setQuery(Query.or(getCustomerIds(pl)
            .stream()
            .map(id -> Query.is(CUSTOMER_ID.getCode(), EQUALS, id))
            .toArray(Query[]::new)));
  }

  private SearchObjectRequest getSearchByZaak(Zaak zaak) {
    return new SearchObjectRequest()
        .setCollection(getCollection())
        .setQuery(Query.is(DOSSIER_ID.getCode(), EQUALS, zaak.getZaakId()));
  }
}
