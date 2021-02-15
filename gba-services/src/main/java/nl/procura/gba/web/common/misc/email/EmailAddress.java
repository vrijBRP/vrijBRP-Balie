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

import static nl.procura.standard.Globalfunctions.fil;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import nl.procura.commons.core.exceptions.ProException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "name", "email" })
public class EmailAddress implements Comparable<EmailAddress> {

  private EmailAddressType type;
  private String           function = "";
  private String           name     = "";
  private String           email    = "";

  public EmailAddress() {
    this(null);
  }

  public EmailAddress(EmailAddressType type) {
    this.type = type;
  }

  public EmailAddress(EmailAddressType type, String function, String internetAdres) {
    this.type = type;
    this.function = function;

    if (fil(internetAdres)) {
      InternetAddress ia = get(internetAdres);
      this.name = ia.getPersonal();
      this.email = ia.getAddress();
    }
  }

  public EmailAddress(EmailAddressType type, String function, String name, String email) {
    this.type = type;
    this.function = function;
    this.name = name;

    if (fil(email)) {
      InternetAddress ia = get(email);
      this.email = ia.getAddress();
    }
  }

  @Override
  public int compareTo(EmailAddress o) {
    return o.getType().getSort() >= getType().getSort() ? -1 : 1;
  }

  public String getNameAndEmail() {
    return fil(name) ? (name + " <" + email + ">") : email;
  }

  public boolean isFilled() {
    return fil(getNameAndEmail());
  }

  public boolean isOk() {
    get(getNameAndEmail());
    return true;
  }

  @Override
  public String toString() {

    StringBuilder s = new StringBuilder(function);
    s.append(": ");

    if (fil(name)) {
      s.append(name);
    }

    if (fil(email)) {
      if (fil(name)) {
        s.append(" <");
        s.append(email);
        s.append(">");
      } else {
        s.append(email);
      }
    }

    return s.toString();
  }

  private InternetAddress get(String value) {
    try {
      return new InternetAddress(value);
    } catch (AddressException e) {
      throw new ProException("Fout e-mailadres: " + value + " (" + e.getLocalizedMessage() + ")");
    }
  }
}
