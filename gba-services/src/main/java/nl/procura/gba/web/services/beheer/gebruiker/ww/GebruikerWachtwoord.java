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

package nl.procura.gba.web.services.beheer.gebruiker.ww;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.jpa.personen.db.UsrPwHist;
import nl.procura.gba.jpa.personen.db.UsrPwHistPK;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.standard.ProcuraDate;

public class GebruikerWachtwoord extends UsrPwHist {

  public GebruikerWachtwoord() {
    // Altijd een primary key aanmaken
    setId(new UsrPwHistPK());
    ProcuraDate in = new ProcuraDate();
    getId().setDIn(along(in.getSystemDate()));
    getId().setTIn(along(in.getSystemTime()));
  }

  public GebruikerWachtwoord(Gebruiker gebruiker, byte[] encryptedPw, boolean reset) {
    this();
    setGebruiker(gebruiker);
    setPw(encryptedPw);
    setResetPw(toBigDecimal(reset ? 1 : 0));
  }

  public long getCUsr() {
    return getId().getCUsr();
  }

  public long getDIn() {
    return getId().getDIn();
  }

  public void setDIn(long dIn) {
    getId().setDIn(dIn);
  }

  public Gebruiker getGebruiker() {
    Usr u = getUsr();
    return (u != null) ? ReflectionUtil.deepCopyBean(Gebruiker.class, u) : null;
  }

  public void setGebruiker(Gebruiker gebruiker) {
    getId().setCUsr(gebruiker.getCUsr());
  }

  public long getTIn() {
    return getId().getTIn();
  }

  public void setTIn(long tIn) {
    getId().setTIn(tIn);
  }

}
