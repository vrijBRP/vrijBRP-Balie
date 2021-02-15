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

package nl.procura.gba.web.components.layouts.form.document.email.page1;

import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.gba.web.common.misc.email.Email;
import nl.procura.gba.web.common.misc.email.EmailAddressType;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.standard.ProcuraDate;

public class EmailSummary {

  final StringBuilder content = new StringBuilder();

  public EmailSummary(Email email, String zaakId, String gebruiker, List<PrintRecord> printRecords) {

    ProcuraDate date = new ProcuraDate();

    if (fil(zaakId)) {
      append("Zaak          : ", zaakId, "\n");
    }

    append("Gebruiker     : ", gebruiker, "\n");
    append("Datum         : ", date.getFormatDate(), " ", date.getFormatTime(), "\n");
    append("Onderwerp     : ", email.getSubject(), "\n\n");

    append("E-mailadressen\n");
    append("==============\n");
    append("Van           : ", email.getAdresses().getToString(EmailAddressType.FROM), "\n");
    append("Aan           : ", email.getAdresses().getToString(EmailAddressType.TO), "\n");
    append("Antwoord naar : ", email.getAdresses().getToString(EmailAddressType.REPLY_TO), "\n");
    append("CC            : ", email.getAdresses().getToString(EmailAddressType.CC), "\n");
    append("BCC           : ", email.getAdresses().getToString(EmailAddressType.BCC), "\n\n");

    append("Bijlages\n");
    append("==============\n");
    for (PrintRecord printRecord : printRecords) {
      append(printRecord.getDocument().getEmailDocument(BestandType.PDF), "\n");
    }
    append("\n");

    append("Inhoud\n");
    append("==============\n");
    append(email.getBody().toString());
  }

  public String get() {
    return content.toString();
  }

  private void append(String... vars) {

    for (String var : vars) {
      content.append(var);
    }
  }
}
