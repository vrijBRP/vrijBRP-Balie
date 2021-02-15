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

package examples.nl.procura.gba.overige;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementViewer;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.GbaRestGebruikerSyncVraag;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.GbaRestGebruikerToevoegenSyncVraag;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.GbaRestGebruikerVerwijderenSyncVraag;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.GbaRestGebruikerWachtwoordSyncVraag;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientGebruikerWijzigenVoorbeelden extends GbaRestClientVoorbeelden {

  public GbaRestClientGebruikerWijzigenVoorbeelden() throws GbaRestClientException {
    gebruikerToevoegen();
    gebruikerWachtwoordWijzigen();
    gebruikerVerwijderen();
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientGebruikerWijzigenVoorbeelden.class);
  }

  @Timer
  protected void gebruikerToevoegen() throws GbaRestClientException {

    GbaRestGebruikerToevoegenSyncVraag gebrVraag = new GbaRestGebruikerToevoegenSyncVraag();
    gebrVraag.setGebruikersnaam("testje");
    gebrVraag.setNaam("Dit is een test");
    gebrVraag.setAdmin(true);
    gebrVraag.setGeblokkeerd(false);
    gebrVraag.setDatumIngang(20170101);
    gebrVraag.setDatumEinde(20180101);

    GbaRestGebruikerWachtwoordSyncVraag wwVraag = new GbaRestGebruikerWachtwoordSyncVraag();
    wwVraag.setGebruikersnaam("testje");
    wwVraag.setDatum(20170914);
    wwVraag.setTijd(91011);
    wwVraag.setWachtwoord("abc");
    wwVraag.setResetPassword(false);

    GbaRestGebruikerSyncVraag syncVraag = new GbaRestGebruikerSyncVraag();
    syncVraag.setGebruikerToevoegen(gebrVraag);
    syncVraag.setWachtwoord(wwVraag);
    GbaRestElementViewer.info(getObject(client.getGebruiker().synchronize(syncVraag)));
  }

  @Timer
  protected void gebruikerWachtwoordWijzigen() throws GbaRestClientException {
    GbaRestGebruikerWachtwoordSyncVraag wwVraag = new GbaRestGebruikerWachtwoordSyncVraag();
    wwVraag.setGebruikersnaam("2");
    wwVraag.setDatum(20170914);
    wwVraag.setTijd(91011);
    wwVraag.setWachtwoord("abc");
    wwVraag.setResetPassword(false);
    GbaRestGebruikerSyncVraag syncVraag = new GbaRestGebruikerSyncVraag();
    syncVraag.setWachtwoord(wwVraag);
    GbaRestElementViewer.info(getObject(client.getGebruiker().synchronize(syncVraag)));
  }

  @Timer
  protected void gebruikerVerwijderen() throws GbaRestClientException {
    GbaRestGebruikerVerwijderenSyncVraag verwVraag = new GbaRestGebruikerVerwijderenSyncVraag();
    verwVraag.setGebruikersnaam("a");
    GbaRestGebruikerSyncVraag syncVraag = new GbaRestGebruikerSyncVraag();
    syncVraag.setGebruikerVerwijderen(verwVraag);
    GbaRestElementViewer.info(getObject(client.getGebruiker().synchronize(syncVraag)));
  }
}
