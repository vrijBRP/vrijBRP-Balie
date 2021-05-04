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

package examples.nl.procura.gba.inbox;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.ALGEMEEN;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.ZAAKID;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementViewer;
import nl.procura.gba.web.rest.v1_0.zaak.*;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxVerwerkingType;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.GbaRestZaakVerwerkenVraag;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMelding;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMeldingType;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.Resource;
import nl.procura.standard.exceptions.ProException;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;
import examples.nl.procura.gba.GbaRestGuice;
import examples.nl.procura.gba.GbaRestGuice.Timer;

public class GbaRestClientInboxVoorbeelden extends GbaRestClientVoorbeelden {

  public GbaRestClientInboxVoorbeelden() throws GbaRestClientException {
    toevoegen();
  }

  public static void main(String[] args) {
    GbaRestGuice.getInstance(GbaRestClientInboxVoorbeelden.class);
  }

  @Timer
  protected void toevoegen() throws GbaRestClientException {
    addReservering();
    addVoornemen();
    addGetuigen();
    addBijlage();
    addLogo();
    addTekst();
    addProperties();
    addPdf();
    verwerk(find());
  }

  private void addReservering() throws GbaRestClientException {

    final GbaRestInboxToevoegenVraag vraag = new GbaRestInboxToevoegenVraag();
    vraag.setVerwerkingId(GbaRestInboxVerwerkingType.HUWELIJK_RESERVERING.getCode());
    vraag.setNieuweZaak(true);
    vraag.setDatumIngang(new ProcuraDate().addDays(2).getSystemDate());
    vraag.setZaakId("Gemeente-1234");
    vraag.setOmschrijving("Huwelijksreservering");
    vraag.setBestandsnaam("reservering.xml");
    vraag.setBestand(getBestand("inbox/reservering-request.xml"));

    check(getObject(client.getZaak().toevoegen(vraag)));
  }

  private List<String> find() throws GbaRestClientException {

    GbaRestZaakVraag vraag = new GbaRestZaakVraag();
    vraag.setDatumIngangVanaf(20161108);
    vraag.setTypen(GbaRestZaakType.INBOX);
    vraag.setStatussen(GbaRestZaakStatus.OPGENOMEN);
    vraag.setVraagType(GbaRestZaakVraagType.STANDAARD);

    List<String> zakenIds = new ArrayList<>();
    GbaRestZaakAntwoord antwoord = getObject(client.getZaak().get(vraag));
    for (GbaRestElement zaak : antwoord.getAntwoordElement().getElementen()) {
      zakenIds.add(zaak.get(ALGEMEEN).get(ZAAKID).getWaarde());
    }
    return zakenIds;
  }

  private void verwerk(List<String> zaakIds) throws GbaRestClientException {

    for (String zaakId : zaakIds) {
      GbaRestZaakVerwerkenVraag vraag = new GbaRestZaakVerwerkenVraag();
      vraag.setZaakId(zaakId);
      getObject(client.getZaak().verwerken(vraag));
    }
  }

  private void addVoornemen() throws GbaRestClientException {

    final GbaRestInboxToevoegenVraag vraag = new GbaRestInboxToevoegenVraag();
    vraag.setVerwerkingId(GbaRestInboxVerwerkingType.HUWELIJK_VOORNEMEN.getCode());
    vraag.setNieuweZaak(false);
    vraag.setZaakId("Gemeente-1234");
    vraag.setOmschrijving("Voornemen huwelijk");
    vraag.setBestandsnaam("voornemen.xml");
    vraag.setBestand(getBestand("inbox/voornemen-request.xml"));

    check(getObject(client.getZaak().toevoegen(vraag)));
  }

  private void addGetuigen() throws GbaRestClientException {

    final GbaRestInboxToevoegenVraag vraag = new GbaRestInboxToevoegenVraag();
    vraag.setVerwerkingId(GbaRestInboxVerwerkingType.HUWELIJK_GETUIGEN.getCode());
    vraag.setNieuweZaak(false);
    vraag.setZaakId("Gemeente-1234");
    vraag.setOmschrijving("Getuigen huwelijk");
    vraag.setBestandsnaam("getuigen.xml");
    vraag.setBestand(getBestand("inbox/getuigen-request.xml"));

    check(getObject(client.getZaak().toevoegen(vraag)));
  }

  private void addBijlage() throws GbaRestClientException {

    final GbaRestInboxToevoegenVraag vraag = new GbaRestInboxToevoegenVraag();
    vraag.setVerwerkingId(GbaRestInboxVerwerkingType.ZAAK_BIJLAGEN.getCode());
    vraag.setNieuweZaak(false);
    vraag.setZaakId("Gemeente-1234");
    vraag.setOmschrijving("Bijlagen huwelijk.");
    vraag.setBestandsnaam("bijlagen.xml");
    vraag.setBestand(getBestand("inbox/bijlage-request.xml"));

    check(getObject(client.getZaak().toevoegen(vraag)));
  }

  private void addPdf() throws GbaRestClientException {

    final GbaRestInboxToevoegenVraag vraag = new GbaRestInboxToevoegenVraag();
    vraag.setVerwerkingId(GbaRestInboxVerwerkingType.ZAAK_BIJLAGEN.getCode());
    vraag.setNieuweZaak(false);
    vraag.setZaakId("Gemeente-1234");
    vraag.setOmschrijving("Zomaar een PDF bestand");
    vraag.setBestandsnaam("uittreksel.pdf");
    vraag.setBestand(getBestand("inbox/voorbeeld.pdf"));

    check(getObject(client.getZaak().toevoegen(vraag)));
  }

  private void addLogo() throws GbaRestClientException {

    final GbaRestInboxToevoegenVraag vraag = new GbaRestInboxToevoegenVraag();
    vraag.setVerwerkingId(GbaRestInboxVerwerkingType.ZAAK_BIJLAGEN.getCode());
    vraag.setNieuweZaak(false);
    vraag.setZaakId("Gemeente-1234");
    vraag.setOmschrijving("Logo Procura B.V.");
    vraag.setBestandsnaam("logo.png");
    vraag.setBestand(getBestand("inbox/voorbeeld.png"));

    check(getObject(client.getZaak().toevoegen(vraag)));
  }

  private void addTekst() throws GbaRestClientException {

    final GbaRestInboxToevoegenVraag vraag = new GbaRestInboxToevoegenVraag();
    vraag.setVerwerkingId(GbaRestInboxVerwerkingType.ZAAK_BIJLAGEN.getCode());
    vraag.setNieuweZaak(false);
    vraag.setZaakId("Gemeente-1234");
    vraag.setOmschrijving("Tekstbestand");
    vraag.setBestandsnaam("blabla.txt");
    vraag.setBestand(getBestand("inbox/voorbeeld.txt"));

    check(getObject(client.getZaak().toevoegen(vraag)));
  }

  private void addProperties() throws GbaRestClientException {

    final GbaRestInboxToevoegenVraag vraag = new GbaRestInboxToevoegenVraag();
    vraag.setVerwerkingId(GbaRestInboxVerwerkingType.ZAAK_BIJLAGEN.getCode());
    vraag.setNieuweZaak(false);
    vraag.setZaakId("Gemeente-1234");
    vraag.setOmschrijving("Eigenschappen");
    vraag.setBestandsnaam("example.properties");
    vraag.setBestand(getBestand("inbox/voorbeeld.properties"));

    check(getObject(client.getZaak().toevoegen(vraag)));
  }

  private String getBestand(String filename) {
    InputStream inputStream = null;
    try {
      inputStream = Resource.getAsInputStream(filename);
      return encodeBase64String(IOUtils.toByteArray(inputStream));
    } catch (Exception e) {
      throw new ProException("Fout bij uitlezen bestand", e);
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
  }

  private void check(final GbaRestZaakAntwoord antwoord) {
    for (ProRestMelding melding : antwoord.getMeldingen()) {
      if (melding.getType() == ProRestMeldingType.FOUT) {
        throw new ProException(melding.getOmschrijving());
      }
    }
    System.out.println("Antwoord: " + antwoord.getAntwoordElement().getElementen().size());
    GbaRestElementViewer.info(antwoord);
  }
}
