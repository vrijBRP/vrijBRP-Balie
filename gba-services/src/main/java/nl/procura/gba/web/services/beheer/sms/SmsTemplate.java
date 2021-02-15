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

package nl.procura.gba.web.services.beheer.sms;

import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.gba.jpa.personen.db.Sms;
import nl.procura.sms.rest.domain.Sender;

public class SmsTemplate extends Sms {

  private Sender sender;

  public SmsTemplate() {
    setSenderId("");
    setContent("");
    setActivated(false);
  }

  public SmsTemplate(SmsType type) {
    this();
    setSmsType(type);
  }

  public SmsType getSmsType() {
    return SmsType.get(getType());
  }

  public void setSmsType(SmsType type) {
    setType(type.getCode());
  }

  public boolean isActivated() {
    return pos(getActive());
  }

  public void setActivated(boolean activated) {
    setActive(toBigDecimal(activated ? 1 : 0));
  }

  public Sender getSender() {
    return sender;
  }

  public void setSender(Sender sender) {
    this.sender = sender;
  }

  public boolean isAutoSend() {
    return pos(getAuto());
  }

  public void setAutoSend(boolean autoSend) {
    setAuto(toBigDecimal(autoSend ? 1 : 0));
  }
}
