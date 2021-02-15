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

public class EmailTest extends Email {

  public EmailTest() {

    setType(TYPE_TEST);
    setSubject("BRP webservice test e-mail");

    addLine("Beste gebruiker,");
    addNewLine();
    addLine("Dit is een e-mail die verzonden is door de BRP webservice applicatie.");
    addNewLine();
    addLine("============================================");
    addLine("Deze e-mail is bedoeld voor *testdoeleinden*");
    addLine("============================================");
    addNewLine();
    addLine("Er kunnen verschillende momenten zijn waarop u een e-mail van de webservice kunt ontvangen.");
    addNewLine();
    addLine("- Bij gebruik van een fout GBA-V wachtwoord door de webservice.");
    addLine("- Nadat een nieuw wachtwoord is aangemaakt.");
    addLine("- Als het wachtwoord of certificaat bijna verloopt.");
    addNewLine();
    addLine("De BRP webservice.");
  }
}
