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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import lombok.Getter;

@Getter
public enum ZaakPersoonType {

  AANGEVER("Aangever"),
  OVERLEDENE("Overledene"),
  HOOFDBEWONER("Hoofdbewoner"),
  MOEDER("Moeder"),
  VADER("Vader"),
  ERKENNER("Erkenner"),
  BETROKKENE("Betrokkene"),
  PARTNER("Partner"),
  PARTNER_1("Partner 1"),
  PARTNER_2("Partner 2"),
  KIND("Kind");

  private final String caption;

  ZaakPersoonType(String caption) {
    this.caption = caption;
  }
}
