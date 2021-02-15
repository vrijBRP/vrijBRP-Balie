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

package nl.procura.gba.web.services.beheer.email;

import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.gba.jpa.personen.db.Email;

public class EmailTemplate extends Email {

  public EmailTemplate() {
    setGeldigheid(1);
  }

  public EmailTemplate(EmailType type) {
    this();
    setType(type);
    setInhoud("");
  }

  public EmailType getType() {
    return EmailType.get(getEmail());
  }

  public void setType(EmailType type) {
    setEmail(type.getCode());
  }

  public EmailTypeContent getTypeContent() {
    return EmailTypeContent.get(getContentType());
  }

  public void setTypeContent(EmailTypeContent type) {
    setContentType(type.getCode());
  }

  public boolean isGeactiveerd() {
    return pos(getActief());
  }

  public void setGeactiveerd(boolean geactiveerd) {
    setActief(toBigDecimal(geactiveerd ? 1 : 0));
  }
}
