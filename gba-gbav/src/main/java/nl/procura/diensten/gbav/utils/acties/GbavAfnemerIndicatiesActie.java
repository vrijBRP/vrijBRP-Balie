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

import org.apache.commons.lang3.Validate;

import nl.bprbzk.gba.gba_v.vraag_v0.VraagAIRequest;
import nl.bprbzk.gba.gba_v.vraag_v0.VraagResponse;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gbav.soap.GbavSoapHandler;
import nl.procura.diensten.gbav.utils.vraag.GbavVraagAntwoord;
import nl.procura.diensten.gbav.utils.vraag.afnemerindicaties.AfnemerIndicatiesGbavVraagConverter;
import nl.procura.diensten.gbav.utils.vraag.volledig.VolledigeGbavAntwoordConverter;

public class GbavAfnemerIndicatiesActie extends GbavActie {

  public GbavVraagAntwoord zoek(PLEArgs args, BasePLBuilder plBuilder) {

    Validate.notNull(args, "Zoeken afnemer indicaties: Geen argumenten ingegeven");

    GbavVraagAntwoord vraagAntwoord = new GbavVraagAntwoord();

    check();

    GbavSoapHandler soaphandler = null;

    try {

      // Maak soaphandler
      soaphandler = new GbavSoapHandler(getEndpoint(), getGebruikersnaam(), getWachtwoord());

      // Converteer PleArgumenten
      VraagAIRequest vraag = AfnemerIndicatiesGbavVraagConverter.converteer(args);

      // Zoek
      VraagResponse antwoord = soaphandler.vraag(vraag);

      // Converteer antwoord
      VolledigeGbavAntwoordConverter.convert(antwoord, vraagAntwoord, plBuilder);

      // Zet XML
      vraagAntwoord.getXml().setVraag(soaphandler.getRequest().getXmlMessage());
      vraagAntwoord.getXml().setAntwoord(soaphandler.getResponse().getXmlMessage());
    } catch (Exception e) {
      throw new RuntimeException("Fout bij versturen bericht", e);
    } finally {

      if (soaphandler != null) {
        vraagAntwoord.getXml().setVraag(soaphandler.getRequest().getXmlMessage());
        vraagAntwoord.getXml().setAntwoord(soaphandler.getResponse().getXmlMessage());
      }
    }

    return vraagAntwoord;
  }
}
