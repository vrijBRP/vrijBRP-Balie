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

package nl.procura.gba.web.common.misc.email;

import static nl.procura.gba.web.common.misc.email.EmailAddressType.FROM;
import static nl.procura.gba.web.common.misc.email.EmailAddressType.REPLY_TO;

import java.util.ArrayList;
import java.util.List;

public class EmailAddressList extends ArrayList<EmailAddress> {

  public void add(EmailAddressType type) {
    add(new EmailAddress(type));
  }

  public void add(EmailAddressType type, String functie, String internetAdres) {
    add(new EmailAddress(type, functie, internetAdres));
  }

  public List<EmailAddress> get(EmailAddressType... types) {
    List<EmailAddress> adressen = new ArrayList<>();
    for (EmailAddress adres : this) {
      if (adres.getType().is(types) && adres.isFilled() && adres.isOk()) {
        adressen.add(adres);
      }
    }
    return adressen;
  }

  public String getToString(EmailAddressType... types) {
    StringBuilder emails = new StringBuilder();
    for (EmailAddress adres : get(types)) {
      emails.append(adres.getNameAndEmail());
    }
    return emails.toString();
  }

  public void reset() {
    for (EmailAddress emailAdres : new ArrayList<>(this)) {
      if (emailAdres.getType().is(FROM, REPLY_TO)) {
        remove(emailAdres);
      }
    }
  }
}
