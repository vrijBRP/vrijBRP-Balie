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

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.filesystem.FilesystemDMSStorage;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;

public class DMSService extends AbstractService {

  private final FilesystemDMSStorage lokaleDms = new FilesystemDMSStorage();

  public DMSService() {
    super("DMS");
  }

  public int countDocumentsByPL(BasePLExt pl) {
    return getDmsService().countDocumentsByPL(pl);
  }

  public int countDocumentsByZaak(Zaak zaak) {
    return getDmsService().countDocumentByZaakId(zaak);
  }

  public DMSResult getDocumentsByPL(BasePLExt pl) {
    return getDmsService().getDocumentsByPL(pl);
  }

  public DMSResult getDocumentsByZaak(Zaak zaak) {
    return getDmsService().getDocumentsByZaak(zaak);
  }

  public DMSDocument save(String subFolderName, DMSDocument dmsDocument) {
    return getDmsService().save(subFolderName, dmsDocument);
  }

  public DMSDocument save(BasePLExt pl, DMSDocument dmsDocument) {
    return getDmsService().saveByPerson(pl, dmsDocument);
  }

  public DMSDocument save(Zaak zaak, DMSDocument dmsDocument) {
    return getDmsService().saveByZaak(zaak, dmsDocument);
  }

  public void save(PrintActie printActie, byte[] documentBytes) {
    getDmsService().save(printActie, documentBytes);
  }

  public FilesystemDMSStorage getLocalDMS() {
    return lokaleDms;
  }

  @Override
  public void setServices(Services services) {
    super.setServices(services);
    getLocalDMS().setServices(services);
  }

  public void delete(DMSDocument record) {
    getDmsService().delete(record);
  }

  private DMSStorage getDmsService() {
    //String dmsType = getParm(ParameterConstant.DOC_DMS_TYPE);
    //boolean isProwebArchief = DmsType.STORAGE.getCode().equals(dmsType);
    return getLocalDMS();
  }
}
