/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.components.layouts;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.HANDLEIDING_BEHEERDER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.HANDLEIDING_GEBRUIKER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.HANDLEIDING_HUP;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.HANDLEIDING_INSCHRIJVING;

import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;

public enum Manual {

  USER(HANDLEIDING_GEBRUIKER, "Voor gebruikers", "handleiding-gebruikers-2.9.pdf"),
  ADMIN(HANDLEIDING_BEHEERDER, "Voor beheerders", "handleiding-beheerders-2.7.pdf"),
  FIRST_REGISTRATION(HANDLEIDING_INSCHRIJVING, "Voor 1e inschrijving", "handleiding-1e-inschrijving.pdf"),
  HUP(HANDLEIDING_HUP, "Uitvoeringsprocedures", "HUP-BRP-3.9.pdf");

  private final ParameterConstant parameter;
  private final String            title;
  private final String            fileName;

  Manual(ParameterConstant parameter, String title, String fileName) {
    this.parameter = parameter;
    this.title = title;
    this.fileName = fileName;
  }

  public ParameterConstant parameter() {
    return parameter;
  }

  public String title() {
    return title;
  }

  public String fileName() {
    return fileName;
  }
}
