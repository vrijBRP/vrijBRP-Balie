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

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_OBJECT_STORAGE_ENABLE;
import static nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid.ONBEKEND;
import static nl.procura.standard.Globalfunctions.isTru;

import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.filesystem.FilesystemDMSStorage;
import nl.procura.gba.web.services.zaken.algemeen.dms.objectstore.ObjectStoreDMSStorage;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class DMSService extends AbstractService {

  private final FilesystemDMSStorage  localStore  = new FilesystemDMSStorage();
  private final ObjectStoreDMSStorage objectStore = new ObjectStoreDMSStorage();

  public DMSService() {
    super("DMS");
  }

  public int countDocumentsByPL(BasePLExt pl) {
    return getStorageService().countDocumentsByPL(pl);
  }

  public int countDocumentsByZaak(Zaak zaak) {
    return zaak != null ? getStorageService().countDocumentByZaak(zaak) : 0;
  }

  public DMSResult getDocumentsByPL(BasePLExt pl) {
    return pl != null ? getStorageService().getDocumentsByPL(pl) : new DMSResult();
  }

  public DMSResult getDocumentsByZaak(Zaak zaak) {
    return zaak != null ? getStorageService().getDocumentsByZaak(zaak) : new DMSResult();
  }

  public DMSDocument save(DMSDocument dmsDocument) {
    return getStorageService().save(dmsDocument);
  }

  public DMSDocument save(BasePLExt pl, DMSDocument dmsDocument) {
    dmsDocument.setCustomerId(getCustomerId(pl));
    return getStorageService().save(dmsDocument);
  }

  public DMSDocument save(Zaak zaak, DMSDocument dmsDocument) {
    dmsDocument.setCustomerId(getCustomerId(zaak));
    dmsDocument.setZaakId(zaak.getZaakId());
    return getStorageService().save(dmsDocument);
  }

  public void save(PrintActie printActie, byte[] documentBytes) {
    if (printActie.getZaak() != null) {
      DocumentRecord document = printActie.getDocument();
      DocumentService documentService = getServices().getDocumentService();
      DocumentVertrouwelijkheid vertrouwelijkheid = documentService
          .getStandaardVertrouwelijkheid(document.getVertrouwelijkheid(), ONBEKEND);

      String ext = printActie.getPrintOptie().getUitvoerformaatType().getType();
      DMSDocument dmsDocument = DMSDocument.builder()
          .content(DMSBytesContent.fromExtension(ext, documentBytes))
          .title(document.getDocument())
          .user(getServices().getGebruiker().getNaam())
          .datatype(document.getType())
          .customerId(printActie.getZaak().getBurgerServiceNummer().getStringValue())
          .zaakId(printActie.getZaak().getZaakId())
          .documentTypeDescription(document.getDocumentDmsType())
          .confidentiality(vertrouwelijkheid.getNaam())
          .alias(document.getAlias())
          .build();

      save(dmsDocument);
    }
  }

  public FilesystemDMSStorage getLocalStore() {
    return localStore;
  }

  public ObjectStoreDMSStorage getObjectStore() {
    return objectStore;
  }

  @Override
  public void setServices(Services services) {
    super.setServices(services);
    getLocalStore().setServices(services);
    getObjectStore().setServices(services);
  }

  public void delete(DMSDocument record) {
    getStorageService().delete(record);
  }

  private DMSStorage getStorageService() {
    if (isTru(getSysteemParm(DOC_OBJECT_STORAGE_ENABLE, false))) {
      return new DmsCombinedStorage(getServices(), getLocalStore(), getObjectStore());
    } else {
      return getLocalStore();
    }
  }

  private String getCustomerId(BasePLExt pl) {
    if (pl != null && pl.getPersoon() != null) {
      BasePLValue bsn = pl.getPersoon().getBsn();
      BasePLValue anr = pl.getPersoon().getAnr();
      if (bsn.isNotBlank()) {
        return bsn.getCode();
      } else if (anr.isNotBlank()) {
        return anr.getCode();
      }
    }

    return "";
  }

  private String getCustomerId(Zaak zaak) {
    if (zaak != null) {
      BsnFieldValue bsn = zaak.getBurgerServiceNummer();
      AnrFieldValue anr = zaak.getAnummer();
      if (bsn.isCorrect()) {
        return bsn.getStringValue();
      } else if (anr.isCorrect()) {
        return anr.getStringValue();
      }
    }
    return "";
  }
}
