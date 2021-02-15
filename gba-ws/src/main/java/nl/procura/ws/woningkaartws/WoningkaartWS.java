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

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.wk.baseWK.BaseWKBuilder;
import nl.procura.diensten.gba.wk.baseWK.BaseWKMessage;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.diensten.woningkaart.objecten.Adres;
import nl.procura.gbaws.requests.wk.WkRequestHandlerWS;

public class WoningkaartWS {

  public WoningkaartWSAntwoord zoeken(ZoekArgumenten zoekArgumenten, LoginArgumenten loginArgumenten) {

    final List<String> meldingen = new ArrayList<>();
    final WoningkaartWSAntwoord antwoord = new WoningkaartWSAntwoord();

    if (zoekArgumenten == null) {
      meldingen.add("0|Geen zoekargumenten meegegeven.");

    } else if (loginArgumenten == null) {
      meldingen.add("0|Geen loginArgumenten meegegeven.");

    } else {
      final String uname = loginArgumenten.getGebruikersnaam();
      final String pw = loginArgumenten.getWachtwoord();

      final BaseWKBuilder builder = search(uname, pw, zoekArgumenten);
      final List<Adres> addresses = builder.toAdressen();
      antwoord.setAdressen(addresses.toArray(new Adres[addresses.size()]));

      for (final BaseWKMessage message : builder.getZoekResultaat().getMessages()) {
        meldingen.add(message.getCode() + "|" + message.getWaarde());
      }
    }

    antwoord.setMeldingen(meldingen.toArray(new String[0]));
    return antwoord;
  }

  private BaseWKBuilder search(String u, String p, ZoekArgumenten a) {
    final WkRequestHandlerWS wk = new WkRequestHandlerWS(u, p, a);
    wk.execute();
    return wk.getBasisWKHandler();
  }
}
