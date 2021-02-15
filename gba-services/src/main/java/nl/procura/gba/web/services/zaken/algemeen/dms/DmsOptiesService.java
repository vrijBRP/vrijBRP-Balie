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
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;

public interface DmsOptiesService {

  DmsStream getBestand(DmsDocument record);

  DmsResultaat getDocumenten(BasePLExt pl);

  DmsResultaat getDocumenten(Zaak zaak);

  int getAantalDocumenten(BasePLExt pl);

  int getAantalDocumenten(Zaak zaak);

  void save(byte[] bytes, String titel, String extensie, String gebruiker, String datatype, String id,
      String zaakId, String dmsNaam, String vertrouwelijkheid);

  void save(File bestand, String titel, String bestandsnaam, String gebruiker, BasePLExt pl,
      String vertrouwelijkheid);

  DmsDocument save(File bestand, String titel, String bestandsnaam, String gebruiker, Zaak zaak,
      String vertrouwelijkheid);

  DmsDocument save(Zaak zaak, File bestand, DmsDocument dmsDocument);

  void save(PrintActie printActie, byte[] documentBytes);

  void delete(DmsDocument record);
}
