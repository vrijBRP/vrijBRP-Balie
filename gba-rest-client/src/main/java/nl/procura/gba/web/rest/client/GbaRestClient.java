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

package nl.procura.gba.web.rest.client;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import nl.procura.gba.web.rest.v1_0.document.contactgegevens.GbaRestDocumentAntwoord;
import nl.procura.gba.web.rest.v1_0.document.genereren.GbaRestDocumentGenererenVraag;
import nl.procura.gba.web.rest.v1_0.gebruiker.GbaRestGebruikerAntwoord;
import nl.procura.gba.web.rest.v1_0.gebruiker.GbaRestGebruikerVraag;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.GbaRestGebruikerSyncAntwoord;
import nl.procura.gba.web.rest.v1_0.gebruiker.sync.GbaRestGebruikerSyncVraag;
import nl.procura.gba.web.rest.v1_0.klapper.GbaRestKlapperAntwoord;
import nl.procura.gba.web.rest.v1_0.klapper.GbaRestKlapperVraag;
import nl.procura.gba.web.rest.v1_0.persoon.GbaRestPersoonAntwoord;
import nl.procura.gba.web.rest.v1_0.persoon.GbaRestPersoonNummerAntwoord;
import nl.procura.gba.web.rest.v1_0.persoon.GbaRestPersoonPersoonslijstAntwoord;
import nl.procura.gba.web.rest.v1_0.persoon.GbaRestPersoonVraag;
import nl.procura.gba.web.rest.v1_0.persoon.contact.GbaRestPersoonContactgegevenAntwoord;
import nl.procura.gba.web.rest.v1_0.persoon.contact.GbaRestPersoonContactgegevenToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakSleutelAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakStatusUpdateVraag;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakVraag;
import nl.procura.gba.web.rest.v1_0.zaak.aantekening.GbaRestZaakAantekeningToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.attribuut.GbaRestZaakAttribuutToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.attribuut.GbaRestZaakAttribuutVerwijderenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandToevoegenVraag2;
import nl.procura.gba.web.rest.v1_0.zaak.dashboard.GbaRestDashboardAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.dashboard.GbaRestDashboardVraag;
import nl.procura.gba.web.rest.v1_0.zaak.identificatie.GbaRestZaakIdentificatieToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.relatie.GbaRestZaakRelatieToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.relatie.GbaRestZaakRelatieVerwijderenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.GbaRestZaakVerwerkenAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.GbaRestZaakVerwerkenVraag;

public class GbaRestClient extends GenericGbaRestClient {

  private final Document  document  = new Document();
  private final Zaak      zaak      = new Zaak();
  private final Klapper   klapper   = new Klapper();
  private final Persoon   persoon   = new Persoon();
  private final Gebruiker gebruiker = new Gebruiker();

  public GbaRestClient(String url, String applicatie, String gebruikersnaam, String wachtwoord) {
    super(url, applicatie, gebruikersnaam, wachtwoord);
  }

  public Document getDocument() {
    return document;
  }

  public Zaak getZaak() {
    return zaak;
  }

  public Persoon getPersoon() {
    return persoon;
  }

  public Gebruiker getGebruiker() {
    return gebruiker;
  }

  public Klapper getKlapper() {
    return klapper;
  }

  public class Document {

    public GbaRestClientResponse<InputStream> genereren(long code, String type, String bsn)
        throws GbaRestClientException {
      String url = "/rest/v1.0/document/genereren/" + code + "/" + type + "/" + bsn;
      return GET(InputStream.class, url, MediaType.APPLICATION_OCTET_STREAM);
    }

    public GbaRestClientResponse<InputStream> genereren(GbaRestDocumentGenererenVraag vraag)
        throws GbaRestClientException {
      String url = "/rest/v1.0/document/genereren";
      return POST(InputStream.class, url, vraag, APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM, null);
    }

    public GbaRestClientResponse<GbaRestDocumentAntwoord> get(String documentnummer) throws GbaRestClientException {
      return GET(GbaRestDocumentAntwoord.class, "/rest/v1.0/document/documentnummer/" + documentnummer);
    }
  }

  public class Klapper {

    public GbaRestClientResponse<GbaRestKlapperAntwoord> get(GbaRestKlapperVraag vraag) throws GbaRestClientException {
      return POST(GbaRestKlapperAntwoord.class, "/rest/v1.0/klapper/zoeken", vraag);
    }
  }

  public class Zaak {

    private final Bestand       bestand       = new Bestand();
    private final Aantekening   aantekening   = new Aantekening();
    private final Attribuut     attribuut     = new Attribuut();
    private final Identificatie identificatie = new Identificatie();
    private final Relatie       relatie       = new Relatie();

    public GbaRestClientResponse<GbaRestZaakAntwoord> get(GbaRestZaakVraag vraag) throws GbaRestClientException {
      return POST(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/zoeken", vraag);
    }

    public GbaRestClientResponse<GbaRestZaakSleutelAntwoord> getSleutels(GbaRestZaakVraag vraag)
        throws GbaRestClientException {
      return POST(GbaRestZaakSleutelAntwoord.class, "/rest/v1.0/zaak/zoeken/sleutels", vraag);
    }

    public GbaRestClientResponse<GbaRestZaakAntwoord> toevoegen(GbaRestZaakToevoegenVraag vraag)
        throws GbaRestClientException {
      return POST(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/toevoegen", vraag);
    }

    public GbaRestClientResponse<GbaRestZaakVerwerkenAntwoord> verwerken(GbaRestZaakVerwerkenVraag vraag)
        throws GbaRestClientException {
      return POST(GbaRestZaakVerwerkenAntwoord.class, "/rest/v1.0/zaak/verwerken", vraag);
    }

    public GbaRestClientResponse<GbaRestZaakAntwoord> setStatus(GbaRestZaakStatusUpdateVraag vraag)
        throws GbaRestClientException {
      return POST(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/status/toevoegen", vraag);
    }

    public GbaRestClientResponse<GbaRestZaakAntwoord> controles() throws GbaRestClientException {
      return GET(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/controles");
    }

    public GbaRestClientResponse<GbaRestDashboardAntwoord> dashboard(GbaRestDashboardVraag vraag)
        throws GbaRestClientException {
      return POST(GbaRestDashboardAntwoord.class, "/rest/v1.0/zaak/dashboard", vraag);
    }

    public Bestand getBestand() {
      return bestand;
    }

    public Aantekening getAantekening() {
      return aantekening;
    }

    public Attribuut getAttribuut() {
      return attribuut;
    }

    public Identificatie getIdentificatie() {
      return identificatie;
    }

    public Relatie getRelatie() {
      return relatie;
    }

    public class Aantekening {

      public GbaRestClientResponse<GbaRestZaakAntwoord> toevoegen(
          GbaRestZaakAantekeningToevoegenVraag vraag) throws GbaRestClientException {
        return POST(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/aantekening/toevoegen", vraag);
      }
    }

    public class Attribuut {

      public GbaRestClientResponse<GbaRestZaakAntwoord> toevoegen(GbaRestZaakAttribuutToevoegenVraag vraag)
          throws GbaRestClientException {
        return POST(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/attribuut/toevoegen", vraag);
      }

      public GbaRestClientResponse<GbaRestZaakAntwoord> verwijderen(
          GbaRestZaakAttribuutVerwijderenVraag vraag) throws GbaRestClientException {
        return POST(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/attribuut/verwijderen", vraag);
      }
    }

    public class Relatie {

      public GbaRestClientResponse<GbaRestZaakAntwoord> toevoegen(GbaRestZaakRelatieToevoegenVraag vraag)
          throws GbaRestClientException {
        return POST(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/relatie/toevoegen", vraag);
      }

      public GbaRestClientResponse<GbaRestZaakAntwoord> verwijderen(
          GbaRestZaakRelatieVerwijderenVraag vraag) throws GbaRestClientException {
        return POST(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/relatie/verwijderen", vraag);
      }
    }

    public class Identificatie {

      public GbaRestClientResponse<GbaRestZaakAntwoord> toevoegen(
          GbaRestZaakIdentificatieToevoegenVraag vraag) throws GbaRestClientException {
        return POST(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/identificatie/toevoegen", vraag);
      }
    }

    public class Bestand {

      public GbaRestClientResponse<GbaRestZaakAntwoord> toevoegen(GbaRestZaakBestandToevoegenVraag2 vraag)
          throws GbaRestClientException {
        return POST(GbaRestZaakAntwoord.class, "/rest/v1.0/zaak/bestand/toevoegen2", vraag);
      }

      public GbaRestClientResponse<GbaRestZaakAntwoord> toevoegen(GbaRestZaakBestandToevoegenVraag vraag)
          throws GbaRestClientException {

        String sContentDisposition = "attachment; filename=\"" + vraag.getBestandsNaam() + "\"";
        String url = "/rest/v1.0/zaak/bestand/toevoegen/" + vraag.getZaakId() + "/" + vraag.getBestandsNaam();

        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Disposition", sContentDisposition);

        return POST(GbaRestZaakAntwoord.class, url, vraag.getInputStream(), APPLICATION_OCTET_STREAM,
            APPLICATION_JSON, headers);
      }

      public GbaRestClientResponse<GbaRestZaakBestandAntwoord> get(String zaakId) throws GbaRestClientException {
        return GET(GbaRestZaakBestandAntwoord.class, "/rest/v1.0/zaak/bestand/zoeken/" + zaakId);
      }

      public GbaRestClientResponse<InputStream> get(String zaakId, String bestandsnaam) throws GbaRestClientException {
        return GET(InputStream.class, "/rest/v1.0/zaak/bestand/zoeken/" + zaakId + "/" + bestandsnaam,
            APPLICATION_OCTET_STREAM);
      }

      public GbaRestClientResponse<GbaRestZaakBestandAntwoord> verwijderen(String zaakId,
          String bestandsnaam) throws GbaRestClientException {
        return GET(GbaRestZaakBestandAntwoord.class,
            "/rest/v1.0/zaak/bestand/verwijderen/" + zaakId + "/" + bestandsnaam);
      }
    }
  }

  public class Persoon {

    private final Contactgegevens contactgegevens = new Contactgegevens();

    public GbaRestClientResponse<GbaRestPersoonAntwoord> get(String nummer) throws GbaRestClientException {
      return GET(GbaRestPersoonAntwoord.class, "/rest/v1.0/persoon/zoeken/" + nummer);
    }

    public GbaRestClientResponse<GbaRestPersoonAntwoord> get(GbaRestPersoonVraag vraag) throws GbaRestClientException {
      return POST(GbaRestPersoonAntwoord.class, "/rest/v1.0/persoon/zoeken", vraag);
    }

    public GbaRestClientResponse<GbaRestPersoonPersoonslijstAntwoord> getPersoonslijst(String nummer)
        throws GbaRestClientException {
      return GET(GbaRestPersoonPersoonslijstAntwoord.class, "/rest/v1.0/persoon/zoeken/persoonslijst/" + nummer);
    }

    public GbaRestClientResponse<GbaRestPersoonPersoonslijstAntwoord> getPersoonslijsten(
        GbaRestPersoonVraag vraag) throws GbaRestClientException {
      return POST(GbaRestPersoonPersoonslijstAntwoord.class, "/rest/v1.0/persoon/zoeken/persoonslijsten", vraag);
    }

    public GbaRestClientResponse<GbaRestPersoonNummerAntwoord> getNummers(String nummer) throws GbaRestClientException {
      return GET(GbaRestPersoonNummerAntwoord.class, "/rest/v1.0/persoon/zoeken/nummer/" + nummer);
    }

    public Contactgegevens getContactgegevens() {
      return contactgegevens;
    }

    public class Contactgegevens {

      public GbaRestClientResponse<GbaRestZaakAntwoord> toevoegen(
          GbaRestPersoonContactgegevenToevoegenVraag vraag) throws GbaRestClientException {

        return POST(GbaRestPersoonContactgegevenAntwoord.class, "/rest/v1.0/persoon/contactgegevens/toevoegen",
            vraag);
      }
    }
  }

  public class Gebruiker {

    public GbaRestClientResponse<GbaRestGebruikerAntwoord> get(GbaRestGebruikerVraag vraag)
        throws GbaRestClientException {
      return POST(GbaRestGebruikerAntwoord.class, "/rest/v1.0/gebruiker/zoeken", vraag);
    }

    public GbaRestClientResponse<GbaRestGebruikerSyncAntwoord> synchronize(GbaRestGebruikerSyncVraag vraag)
        throws GbaRestClientException {
      return POST(GbaRestGebruikerSyncAntwoord.class, "/rest/v1.0/gebruiker/synchronize", vraag);
    }
  }
}
