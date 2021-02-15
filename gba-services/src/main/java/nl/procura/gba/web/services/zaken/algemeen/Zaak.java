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

package nl.procura.gba.web.services.zaken.algemeen;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;

public interface Zaak extends DatabaseTable, IdentificatieNummers {

  BasePLExt getBasisPersoon();

  void setBasisPersoon(BasePLExt basisPersoon);

  String getBron();

  void setBron(String bron);

  DateTime getDatumIngang();

  void setDatumIngang(DateTime date);

  DateTime getDatumTijdInvoer();

  void setDatumTijdInvoer(DateTime dateTime);

  Gemeente getGemeente();

  void setGemeente(Gemeente gemeente);

  Identificatie getIdentificatie();

  void setIdentificatie(Identificatie identificatie);

  UsrFieldValue getIngevoerdDoor();

  void setIngevoerdDoor(UsrFieldValue usr);

  String getLeverancier();

  void setLeverancier(String leverancier);

  Locatie getLocatieInvoer();

  void setLocatieInvoer(Locatie locatie);

  String getSoort();

  ZaakStatusType getStatus();

  void setStatus(ZaakStatusType status);

  ZaakType getType();

  ZaakHistorie getZaakHistorie();

  String getZaakId();

  void setZaakId(String zaakId);

  default boolean isToevoegenAanZaaksysteem() {
    return true;
  }
}
