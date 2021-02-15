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

package nl.procura.gbaws.mail;

public class EmailNewPass extends Email {

  public EmailNewPass(String gebruiker, String ww, String d_wijz, String d_end) {

    setType(TYPE_NIEUW_WW);
    setSubject("GBA-V nieuw wachtwoord");

    addLine("Beste gebruiker,");
    addNewLine();
    addLine("Dit is een e-mail die verzonden is door de BRP applicatie.");
    addNewLine();
    addLine("Er is een nieuw GBA-V wachtwoord aangemaakt.");
    addNewLine();
    addLine("Gebruiker-id : " + gebruiker);
    addLine("Wachtwoord   : " + ww);
    addLine("Gewijzigd op : " + d_wijz);
    addLine("Geldig t/m   : " + d_end);
    addNewLine();
    addLine("De BRP applicatie.");
  }
}
