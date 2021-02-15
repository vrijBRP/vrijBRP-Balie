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

package nl.procura.diensten.gbav.utils.acties;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.diensten.gbav.exceptions.GbavConfigException;

public class GbavActie {

  private String gebruikersnaam;
  private String wachtwoord;
  private String endpoint;

  protected void check() {

    if (!fil(gebruikersnaam)) {
      throw new GbavConfigException("GBA-V configuratie: Geen gebruikersnaam");
    }

    if (!fil(wachtwoord)) {
      throw new GbavConfigException("GBA-V configuratie: Geen wachtwoord");
    }

    if (!fil(endpoint)) {
      throw new GbavConfigException("GBA-V configuratie: Geen endpoint");
    }
  }

  public String getGebruikersnaam() {
    return gebruikersnaam;
  }

  public void setGebruikersnaam(String gebruikersnaam) {
    this.gebruikersnaam = gebruikersnaam;
  }

  public String getWachtwoord() {
    return wachtwoord;
  }

  public void setWachtwoord(String wachtwoord) {
    this.wachtwoord = wachtwoord;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }
}
