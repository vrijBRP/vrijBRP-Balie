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

package nl.procura.diensten.gbav.utils;

import nl.procura.diensten.gbav.utils.acties.*;

public class GbavService {

  private final Acties acties = new Acties();
  private final String gebruikersnaam, wachtwoord, endpoint;

  public GbavService(String gebruikersnaam, String wachtwoord, String endpoint) {

    this.gebruikersnaam = gebruikersnaam;
    this.wachtwoord = wachtwoord;
    this.endpoint = endpoint;
  }

  public String getGebruikersnaam() {
    return gebruikersnaam;
  }

  public String getWachtwoord() {
    return wachtwoord;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public Acties getActies() {
    return acties;
  }

  private void updateCredentials(GbavActie actie) {
    actie.setGebruikersnaam(gebruikersnaam);
    actie.setWachtwoord(wachtwoord);
    actie.setEndpoint(endpoint);
  }

  public class Acties {

    private final GbavBeperkteZoekActie      beperkteZoekActie      = new GbavBeperkteZoekActie();
    private final GbavVolledigeZoekActie     volledigeZoekActie     = new GbavVolledigeZoekActie();
    private final GbavWachtwoordActie        wachtwoordActie        = new GbavWachtwoordActie();
    private final GbavAfnemerIndicatiesActie afnemerIndicatiesActie = new GbavAfnemerIndicatiesActie();

    public GbavBeperkteZoekActie getBeperkteZoekActie() {
      updateCredentials(beperkteZoekActie);
      return beperkteZoekActie;
    }

    public GbavVolledigeZoekActie getVolledigeZoekActie() {
      updateCredentials(volledigeZoekActie);
      return volledigeZoekActie;
    }

    public GbavWachtwoordActie getWachtwoordActie() {
      updateCredentials(wachtwoordActie);
      return wachtwoordActie;
    }

    public GbavAfnemerIndicatiesActie getAfnemerIndicatiesActie() {
      updateCredentials(afnemerIndicatiesActie);
      return afnemerIndicatiesActie;
    }
  }
}
