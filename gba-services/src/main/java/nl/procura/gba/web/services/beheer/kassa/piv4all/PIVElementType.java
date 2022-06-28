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

package nl.procura.gba.web.services.beheer.kassa.piv4all;

import static nl.procura.gba.web.services.beheer.kassa.piv4all.PIVElementType.PIVElementAlignment.LEFT;
import static nl.procura.gba.web.services.beheer.kassa.piv4all.PIVElementType.PIVElementAlignment.RIGHT;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PIVElementType {

  KLANTEN_IDENTIFICATIE(LEFT, false, "Klanten-identificatie", "0", 0, 9),
  NAMESPACE(LEFT, true, "Naamspatie", " ", 9, 1),
  KLANTENNAAM(LEFT, true, "Klantennaam", " ", 10, 25),
  ZENDEND_SYSTEEM(LEFT, false, "Zendend systeem", " ", 35, 10),
  GEBRUIKERS_ID(RIGHT, false, "Gebruikers ID", " ", 45, 8),
  KASSA_ID(RIGHT, true, "Kassa-ID", "0", 53, 2),
  DATUM(RIGHT, true, "Datum", "0", 55, 6),
  TIJD(RIGHT, false, "Tijd", "0", 61, 4),
  PRODUCTCODE(RIGHT, true, "Productcode", "0", 65, 13),
  TEKEN_GELEVERD_AANTAL(LEFT, false, "Teken geleverd aantal", "+", 78, 1),
  GELEVERD_AANTAL(RIGHT, false, "Geleverd aantal", "0", 79, 6),
  INDICATIE_PRIJSBETALING(LEFT, false, "Indicatie prijsbetaling", "+", 85, 1),
  TEKEN_PRIJS(LEFT, false, "Teken prijs", "+", 86, 1),
  PRIJS(RIGHT, false, "Prijs", "0", 87, 8);

  private PIVElementAlignment alignment;
  private boolean             required;
  private String              label;
  private String              defaultValue;
  private int                 start;
  private int                 length;

  public enum PIVElementAlignment {

    LEFT,
    RIGHT
  }
}
