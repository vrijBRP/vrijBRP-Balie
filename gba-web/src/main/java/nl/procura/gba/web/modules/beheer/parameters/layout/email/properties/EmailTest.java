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

package nl.procura.gba.web.modules.beheer.parameters.layout.email.properties;

import nl.procura.gba.web.common.misc.email.Email;

public class EmailTest extends Email {

  public EmailTest() {

    setSubject("Proweb personen test e-mail");
    addLine("============================================");
    addLine("Proweb personen test e-mail");
    addLine("============================================");
    addBreak();
    addLine("De e-mail is bedoeld voor testdoeleinden");
    addBreak();
    addLine("De Proweb personen applicatie");
  }
}
