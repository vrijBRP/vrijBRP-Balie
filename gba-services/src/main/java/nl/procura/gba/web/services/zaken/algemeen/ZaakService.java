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

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;

public interface ZaakService<T extends Zaak> extends ZaakNumbers<T> {

  /**
   * Gegevens die zijn afgeleid van andere gegevens, zoals persoonslijsten van a-nrs, bsn's
   */
  T setVolledigeZaakExtra(T zaak);

  /**
   * Naam van de service
   */
  String getName();

  /**
   * returns a new zaak
   */
  Zaak getNewZaak();

  /**
   * Load only the database
   */
  T getStandardZaak(T zaak);

  /**
   * Completely load zaak
   */
  T getCompleteZaak(T zaak);

  /**
   * returns the zaaktype
   */
  ZaakType getZaakType();

  /**
   * Saving the zaak
   */
  void save(T zaak);

  /**
   * Update de status
   */
  void updateStatus(T zaak, ZaakStatusType huidigeStatus, ZaakStatusType nieuweStatus, String opmerking);

  /**
   * Update historisch zaakgegevens.
   */
  void setZaakHistory(T zaak);

  /**
   * Verwijder de zaak
   */
  void delete(T zaak);
}
