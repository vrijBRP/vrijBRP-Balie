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

package nl.procura.gba.web.modules.beheer.overig;

import com.vaadin.data.validator.RegexpValidator;

public class MapValidator extends RegexpValidator {

  private static final String ERROR_STRING = "Geen geldige mapnaam.";
  private static final String REGEX        = "^([\\s\\w/\\\\-]*)$";
  // We staan alleen spaties, letters,cijfers,underscores,slashes en koppeltekens toe. Ook de lege string als mapnaam wordt toegestaan.

  public MapValidator() {
    super(REGEX, ERROR_STRING);
  }
}
