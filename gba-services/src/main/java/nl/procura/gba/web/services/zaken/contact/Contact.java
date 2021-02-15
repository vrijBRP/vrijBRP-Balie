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

package nl.procura.gba.web.services.zaken.contact;

import java.util.Optional;

import nl.procura.gba.common.DateTime;
import nl.procura.java.Pair;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Contact {

  private final String                 name;
  private final long                   anr;
  private final long                   bsn;
  private final Pair<String, DateTime> emailWithDate;

  public Contact(String name, long anr, long bsn, Pair<String, DateTime> emailWithDate) {
    this.name = name;
    this.anr = anr;
    this.bsn = bsn;
    this.emailWithDate = emailWithDate;
  }

  public Contact(String name, long anr, long bsn) {
    this(name, anr, bsn, null);
  }

  public String getName() {
    return name;
  }

  public long getAnr() {
    return anr;
  }

  public long getBsn() {
    return bsn;
  }

  public Optional<Pair<String, DateTime>> getEmailWithDate() {
    return Optional.ofNullable(emailWithDate);
  }
}
