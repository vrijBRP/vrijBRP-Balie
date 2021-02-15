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

import nl.bprbzk.gba.lrdplus.version1.Resultaat;
import nl.procura.diensten.gbav.exceptions.GbavConfigException;
import nl.procura.diensten.gbav.soap.GbavSoapHandler;
import nl.procura.diensten.gbav.utils.GbavAntwoord;
import nl.procura.diensten.gbav.utils.vraag.beperkt.BeperkteGbavAntwoordConverter;

public class GbavWachtwoordActie extends GbavActie {

  public GbavAntwoord wijzig(String nieuwWachtwoord) {

    GbavAntwoord vraagAntwoord = new GbavAntwoord();

    check();

    if (!fil(nieuwWachtwoord)) {
      throw new GbavConfigException("GBA-V configuratie: Geen nieuw wachtwoord meegegeven");
    }

    GbavSoapHandler soaphandler = null;

    try {

      // Maak soaphandler
      soaphandler = new GbavSoapHandler(getEndpoint(), getGebruikersnaam(), getWachtwoord());

      // Zoek
      Resultaat resultaat = soaphandler.wijzigWachtwoord(nieuwWachtwoord);

      // Converteer antwoord
      BeperkteGbavAntwoordConverter.convert(resultaat, vraagAntwoord.getResultaat());

      // Zet XML
      return vraagAntwoord;
    } catch (Exception e) {
      throw new RuntimeException("Fout bij versturen bericht", e);

    } finally {
      if (soaphandler != null) {
        vraagAntwoord.getXml().setVraag(soaphandler.getRequest().getXmlMessage());
        vraagAntwoord.getXml().setAntwoord(soaphandler.getResponse().getXmlMessage());
      }
    }
  }
}
