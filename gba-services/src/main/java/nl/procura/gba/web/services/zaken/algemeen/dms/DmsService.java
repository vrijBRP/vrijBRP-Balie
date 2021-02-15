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

import java.io.File;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;

public class DmsService extends AbstractService {

  private final LokaleDmsService  lokaleDms  = new LokaleDmsService();
  private final ExterneDmsService externeDms = new ExterneDmsService();

  public DmsService() {
    super("DMS");
  }

  public int getAantalDocumenten(BasePLExt pl) {
    return getOpties().getAantalDocumenten(pl);
  }

  public int getAantalDocumenten(Zaak zaak) {
    return getOpties().getAantalDocumenten(zaak);
  }

  public DmsStream getBestand(DmsDocument record) {
    return getOpties().getBestand(record);
  }

  public DmsResultaat getDocumenten(BasePLExt pl) {
    return getOpties().getDocumenten(pl);
  }

  public DmsResultaat getDocumenten(Zaak zaak) {
    return getOpties().getDocumenten(zaak);
  }

  public ExterneDmsService getExterneDms() {
    return externeDms;
  }

  public LokaleDmsService getLokaleDms() {
    return lokaleDms;
  }

  public void save(byte[] bytes, String titel, String extensie, String gebruiker, String datatype, String id,
      String zaakId, String dmsNaam, String vertrouwelijkheid) {
    getOpties().save(bytes, titel, extensie, gebruiker, datatype, id, zaakId, dmsNaam, vertrouwelijkheid);
  }

  /**
   * Voor uploads
   */
  public void save(File bestand, String titel, String bestandsnaam, String gebruiker, BasePLExt pl,
      String vertrouwelijkheid) {
    getOpties().save(bestand, titel, bestandsnaam, gebruiker, pl, vertrouwelijkheid);
  }

  public DmsDocument save(File bestand, String titel, String bestandsnaam, String gebruiker, Zaak zaak,
      String vertrouwelijkheid) {
    return getOpties().save(bestand, titel, bestandsnaam, gebruiker, zaak, vertrouwelijkheid);
  }

  public DmsDocument save(Zaak zaak, File bestand, DmsDocument dmsDocument) {
    return getOpties().save(zaak, bestand, dmsDocument);
  }

  public void save(PrintActie printActie, byte[] documentBytes) {
    getOpties().save(printActie, documentBytes);
  }

  @Override
  public void setServices(Services services) {
    super.setServices(services);
    getLokaleDms().setServices(services);
    externeDms.setServices(services);
  }

  public void delete(DmsDocument record) {
    getOpties().delete(record);
  }

  private DmsOptiesService getOpties() {
    String dmsType = getParm(ParameterConstant.DOC_DMS_TYPE);
    boolean isProwebArchief = DmsType.STORAGE.getCode().equals(dmsType);
    return isProwebArchief ? externeDms : getLokaleDms();
  }
}
