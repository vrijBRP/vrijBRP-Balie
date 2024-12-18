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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages;

import nl.procura.raas.rest.domain.aanvraag.EnumType;
import nl.procura.standard.Globalfunctions;

public class RaasBeanValue {

  private final EnumType value;
  private final boolean  withCode;

  RaasBeanValue(EnumType value, boolean withCode) {
    this.value = value;
    this.withCode = withCode;
  }

  @Override
  public String toString() {
    return (withCode && Globalfunctions.fil(value.getCode().toString()) ? (value.getCode() + ": ") : "") +
        value.getName();
  }
}
