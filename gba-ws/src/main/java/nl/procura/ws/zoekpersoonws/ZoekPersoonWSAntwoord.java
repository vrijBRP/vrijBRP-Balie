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

package nl.procura.ws.zoekpersoonws;

import nl.procura.diensten.zoekpersoon.objecten.Persoonslijst;

public class ZoekPersoonWSAntwoord {

  private String[]        meldingen;
  private Persoonslijst[] persoonslijsten = new Persoonslijst[0];
  private int             databron        = 0;

  public ZoekPersoonWSAntwoord() {
  }

  public String[] getMeldingen() {
    return meldingen;
  }

  public void setMeldingen(String[] meldingen) {
    this.meldingen = meldingen;
  }

  public Persoonslijst[] getPersoonslijsten() {
    return persoonslijsten;
  }

  public void setPersoonslijsten(Persoonslijst[] persoonslijsten) {
    this.persoonslijsten = persoonslijsten;
  }

  public int getDatabron() {
    return databron;
  }

  public void setDatabron(int databron) {
    this.databron = databron;
  }
}
