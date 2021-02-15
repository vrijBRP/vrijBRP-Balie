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

import java.io.ByteArrayInputStream;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestand;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandToevoegenVraag2;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMelding;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientZaakBestandVoorbeelden extends GbaRestClientVoorbeelden {

  private final static Logger LOGGER = LoggerFactory.getLogger(GbaRestClientZaakBestandVoorbeelden.class.getName());

  public GbaRestClientZaakBestandVoorbeelden() throws GbaRestClientException {
    bestandToevoegen();
    bestandToevoegen2();
    bestandenZoeken();
    bestandZoeken();
    bestandVerwijderen();
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientZaakBestandVoorbeelden.class);
  }

  @Timer
  protected void bestandToevoegen() throws GbaRestClientException {
    GbaRestZaakBestandToevoegenVraag vraag = new GbaRestZaakBestandToevoegenVraag();
    vraag.setZaakId("0030-yq3-l3p");
    vraag.setBestandsNaam("1.txt");
    vraag.setInputStream(new ByteArrayInputStream("Hello World".getBytes()));

    getObject(client.getZaak().getBestand().toevoegen(vraag));
  }

  @Timer
  protected void bestandToevoegen2() throws GbaRestClientException {
    GbaRestZaakBestandToevoegenVraag2 vraag = new GbaRestZaakBestandToevoegenVraag2();
    vraag.setZaakId("1600-kj0-oyn");
    vraag.setTitel("voorbeeld2");
    vraag.setBestandsNaam("2.txt");
    vraag.setInhoud("Hello World".getBytes());

    getObject(client.getZaak().getBestand().toevoegen(vraag));
  }

  @Timer
  protected void bestandenZoeken() throws GbaRestClientException {
    GbaRestZaakBestandAntwoord antwoord = getObject(client.getZaak().getBestand().get("0637120486"));

    for (ProRestMelding melding : antwoord.getMeldingen()) {
      LOGGER.info("Melding: " + melding.getOmschrijving());
    }

    for (GbaRestZaakBestand bestand : antwoord.getBestanden()) {
      info("bestand");
      info(2, "titel", bestand.getTitel());
      info(2, "naam", bestand.getBestandsnaam());
      info(2, "gebruiker", bestand.getGebruiker());
      info(2, "datum", bestand.getDatum());
      info(2, "tijd", bestand.getTijd());
      info(2, "dms-naam", bestand.getDmsNaam());
      info("");
    }
  }

  @Timer
  protected void bestandZoeken() throws GbaRestClientException {
    String zaakId = "PROWEB_70_091811702";
    GbaRestZaakBestandAntwoord antwoord = getObject(client.getZaak().getBestand().get(zaakId));

    for (GbaRestZaakBestand bestand : antwoord.getBestanden()) {
      String bestandsnaam = bestand.getBestandsnaam();
      String extension = FilenameUtils.getExtension(bestandsnaam);
      writeStream(
          getStream(client.getZaak().getBestand().get(bestand.getZaakId(), bestandsnaam).getClientResponse()),
          extension);
      break;
    }
  }

  @Timer
  protected void bestandVerwijderen() throws GbaRestClientException {
    String zaakId = "PROWEB_70_091811702";
    GbaRestZaakBestandAntwoord antwoord = getObject(client.getZaak().getBestand().get(zaakId));

    for (GbaRestZaakBestand bestand : antwoord.getBestanden()) {
      String bestandsnaam = bestand.getBestandsnaam();
      LOGGER.info("Verwijderen: " + bestandsnaam);
      getObject(client.getZaak().getBestand().verwijderen(bestand.getZaakId(), bestandsnaam));
      break;
    }
  }
}
