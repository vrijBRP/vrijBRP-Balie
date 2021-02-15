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

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.def;

import nl.bprbzk.gba.gba_v.vraag_v0.VraagPLRequest;
import nl.bprbzk.gba.gba_v.vraag_v0.VraagResponse;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.BasePLUtils;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgsSplitter;
import nl.procura.diensten.gba.ple.procura.arguments.PLNumber;
import nl.procura.diensten.gbav.exceptions.GbavConfigException;
import nl.procura.diensten.gbav.soap.GbavSoapHandler;
import nl.procura.diensten.gbav.utils.vraag.GbavVraagAntwoord;
import nl.procura.diensten.gbav.utils.vraag.volledig.VolledigeGbavAntwoordConverter;
import nl.procura.diensten.gbav.utils.vraag.volledig.VolledigeGbavVraagConverter;

public class GbavVolledigeZoekActie extends GbavActie {

  public GbavVraagAntwoord zoek(PLEArgs args, BasePLBuilder builder) {

    GbavVraagAntwoord vraagAntwoord = new GbavVraagAntwoord();
    check();

    if (!def(args)) {
      throw new GbavConfigException("GBA-V configuratie: Geen (zoek)args meegegeven");
    }

    for (PLEArgs deelArgumenten : PLEArgsSplitter.verdeel(args)) {
      deelZoeken(deelArgumenten, builder, vraagAntwoord);
    }

    if (args.isSearchRelations()) {
      for (PLNumber nummer : BasePLUtils.getRelations(builder, args.getNumbers())) {
        PLEArgs relatieArgumenten = new PLEArgs();
        relatieArgumenten.addNummer(astr(nummer.getNummer()));

        for (PLEArgs deelArgumenten : PLEArgsSplitter.verdeel(relatieArgumenten)) {
          deelZoeken(deelArgumenten, builder, vraagAntwoord);
        }
      }
    }

    return vraagAntwoord;
  }

  private void deelZoeken(PLEArgs a, BasePLBuilder plBuilder, GbavVraagAntwoord vraagAntwoord) {

    GbavSoapHandler soaphandler = null;

    try {

      // Maak soaphandler
      soaphandler = new GbavSoapHandler(getEndpoint(), getGebruikersnaam(), getWachtwoord());

      // Converteer PleArgumenten
      VraagPLRequest vraag = VolledigeGbavVraagConverter.converteer(a);

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
  }
}
