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

package nl.procura.ws.woningkaartws;

import nl.procura.diensten.woningkaart.objecten.Adres;

public class WoningkaartWSAntwoord {

  private String[] meldingen;

  private Adres[] adressen = new Adres[0];

  public WoningkaartWSAntwoord() {
  }

  public String[] getMeldingen() {
    return meldingen;
  }

  public void setMeldingen(String[] meldingen) {
    this.meldingen = meldingen;
  }

  public Adres[] getAdressen() {
    return adressen;
  }

  public void setAdressen(Adres[] adressen) {
    this.adressen = adressen;
  }
}
