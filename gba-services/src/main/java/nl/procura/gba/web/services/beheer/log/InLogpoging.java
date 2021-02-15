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

package nl.procura.gba.web.services.beheer.log;

import static nl.procura.standard.Globalfunctions.pos;

import java.math.BigDecimal;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.Log;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;

public class InLogpoging extends Log {

  private UsrFieldValue gebruiker = new UsrFieldValue();

  public String getBrowser() {
    return getUserAgent();
  }

  public Long getCode() {
    return getCLog();
  }

  public DateTime getDatumTijd() {
    return new DateTime(getDIn(), getTIn());
  }

  public void setDatumTijd(DateTime dateTime) {
    setDIn(BigDecimal.valueOf(dateTime.getLongDate()));
    setTIn(BigDecimal.valueOf(dateTime.getLongTime()));
  }

  public UsrFieldValue getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(UsrFieldValue gebruiker) {
    this.gebruiker = gebruiker;
  }

  public boolean isGeblokkeerd() {
    return pos(getBlok());
  }

  public void setGeblokkeerd(boolean b) {
    setBlok(BigDecimal.valueOf(b ? 1 : 0));
  }

  public boolean isOnbekend() {
    return (getUsrBean() == null) || (getUsrBean().getCUsr() <= 0);
  }
}
