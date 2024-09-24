/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.burgerzaken.keesy.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import nl.procura.burgerzaken.keesy.api.ApiResponse;
import nl.procura.burgerzaken.keesy.api.model.GeefStartUrlRequest;
import nl.procura.burgerzaken.keesy.api.model.GeefStartUrlResponse;
import nl.procura.burgerzaken.keesy.api.model.GeefZaakRequest;
import nl.procura.burgerzaken.keesy.api.model.GeefZaakResponse;
import nl.procura.burgerzaken.keesy.api.model.MaakZaakRequest;
import nl.procura.burgerzaken.keesy.api.model.MaakZaakRequestPersoon;
import nl.procura.burgerzaken.keesy.api.model.MaakZaakResponse;
import nl.procura.burgerzaken.keesy.api.model.SignalenRequest;
import nl.procura.burgerzaken.keesy.api.model.SignalenResponse;

public class KeesyApiTest extends AbstractTestWireMockClient {

  @Test
  public void mustReturnSignalen() {
    SignalenRequest request = new SignalenRequest()
        .externUserId("aa")
        .groupId("bb");

    SignalenResponse response = getSignalenApi().geefAantalSignalen(request).getEntity();
    assertEquals(6, response.getAantal());
    assertEquals(1, response.getSignalen().get(0).getAantal());
    assertEquals("K226524", response.getSignalen().get(0).getExternZaakId());

    assertEquals(2, response.getSignalen().get(1).getAantal());
    assertEquals("PR123", response.getSignalen().get(1).getExternZaakId());

    assertEquals(3, response.getSignalen().get(2).getAantal());
    assertEquals("ABC-123/A1", response.getSignalen().get(2).getExternZaakId());
  }

  @Test
  public void mustReturnSignaal() {
    SignalenRequest request = new SignalenRequest()
        .externUserId("aa")
        .groupId("bb")
        .externZaakId("cc");

    SignalenResponse response = getSignalenApi().geefAantalSignalen(request).getEntity();
    assertEquals(3, response.getAantal());
    assertEquals(3, response.getSignalen().get(0).getAantal());
    assertEquals("K226524", response.getSignalen().get(0).getExternZaakId());
  }

  @Test
  public void mustReturnStartUrl() {
    GeefStartUrlRequest request = new GeefStartUrlRequest()
        .externUserId("aa")
        .groupId("bb");

    GeefStartUrlResponse response = getAutorisatieApi().geefStartUrl(request).getEntity();
    assertEquals("https://www.fakeurl.blabla", response.getUrl());
  }

  @Test
  public void mustGeefZaak() {
    GeefZaakRequest request = new GeefZaakRequest()
        .externZaakId("cc");

    GeefZaakResponse response = getZakenApi().geefZaak(request).getEntity();
    assertEquals("abcd", response.getZaakId());
  }

  @Test
  public void mustGeefFout() {
    GeefZaakRequest request = new GeefZaakRequest()
        .externZaakId("dd");

    ApiResponse<GeefZaakResponse> response = getZakenApi().geefZaak(request);
    assertEquals("ZaakReferentie niet gevonden (LOGID:34315380)",
        response.getError().getException().message());
  }

  @Test
  public void mustMaakZaak() {
    MaakZaakRequest request = new MaakZaakRequest()
        .groupId("MyGroup")
        .zaakSoort("Huwelijk")
        .behandelaarId("burgerzaken@procura.nl")
        .onderwerp("Huwelijk sluiten")
        .externZaakId("K128")
        .persoon(new MaakZaakRequestPersoon()
            .naam("Frits Janssen")
            .email("burgerzaken@procura.nl")
            .telnr("1234567890"));

    ApiResponse<MaakZaakResponse> response = getZakenApi().maakZaak(request);
    assertTrue(response.isSuccessful());
    assertEquals("123456789", response.getEntity().getZaakId());
  }
}
