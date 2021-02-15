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

package nl.procura.gbaws.web.rest.client;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.client.GbaRestClientResponse;
import nl.procura.gba.web.rest.client.GenericGbaRestClient;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestProcuraSelecterenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestProcuraSelecterenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccountsAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.deblokkeren.GbaWsRestGbavDeblokkerenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.deblokkeren.GbaWsRestGbavDeblokkerenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.update.GbaWsRestGbavAccountUpdatenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.update.GbaWsRestGbavAccountUpdatenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.genereren.GbaWsRestGbavWachtwoordGenererenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.versturen.GbaWsRestGbavWachtwoordVersturenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.versturen.GbaWsRestGbavWachtwoordVersturenVraag;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabellenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabellenVraag;

public class GbaWsRestClient extends GenericGbaRestClient {

  public GbaWsRestClient(String url, String applicatie, String gebruikersnaam, String wachtwoord) {
    super(url, applicatie, gebruikersnaam, wachtwoord);
  }

  public Procura getProcura() {
    return new Procura();
  }

  public Gbav getGbav() {
    return new Gbav();
  }

  public Tabellen getTabel() {
    return new Tabellen();
  }

  public class Procura {

    public Database getDatabase() {
      return new Database();
    }

    public class Database {

      public GbaRestClientResponse<GbaWsRestProcuraSelecterenAntwoord> selecteren(
          GbaWsRestProcuraSelecterenVraag vraag) throws GbaRestClientException {
        return POST(GbaWsRestProcuraSelecterenAntwoord.class, "/rest/v1.0/procura/database/selecteren", vraag);
      }
    }
  }

  public class Gbav {

    public GbaRestClientResponse<GbaWsRestGbavAccountsAntwoord> getAccounts() throws GbaRestClientException {
      return GET(GbaWsRestGbavAccountsAntwoord.class, "/rest/v1.0/gbav/accounts");
    }

    public GbaRestClientResponse<GbaWsRestGbavDeblokkerenAntwoord> accountDeblokkeren(
        GbaWsRestGbavDeblokkerenVraag vraag) throws GbaRestClientException {

      return POST(GbaWsRestGbavDeblokkerenAntwoord.class, "/rest/v1.0/gbav/account/deblokkeren", vraag);
    }

    public GbaRestClientResponse<GbaWsRestGbavAccountUpdatenAntwoord> accountUpdaten(
        GbaWsRestGbavAccountUpdatenVraag vraag) throws GbaRestClientException {

      return POST(GbaWsRestGbavAccountUpdatenAntwoord.class, "/rest/v1.0/gbav/account/updaten", vraag);
    }

    public GbaRestClientResponse<GbaWsRestGbavWachtwoordGenererenAntwoord> wachtwoordGenereren()
        throws GbaRestClientException {
      return GET(GbaWsRestGbavWachtwoordGenererenAntwoord.class, "/rest/v1.0/gbav/wachtwoord/genereren");
    }

    public GbaRestClientResponse<GbaWsRestGbavWachtwoordVersturenAntwoord> wachtwoordVersturen(
        GbaWsRestGbavWachtwoordVersturenVraag vraag) throws GbaRestClientException {
      return POST(GbaWsRestGbavWachtwoordVersturenAntwoord.class, "/rest/v1.0/gbav/wachtwoord/versturen", vraag);
    }
  }

  public class Tabellen {

    public GbaRestClientResponse<GbaWsRestTabellenAntwoord> getTabellen(GbaWsRestTabellenVraag vraag)
        throws GbaRestClientException {
      return POST(GbaWsRestTabellenAntwoord.class, "/rest/v1.0/tabel/tabellen", vraag);
    }
  }
}
