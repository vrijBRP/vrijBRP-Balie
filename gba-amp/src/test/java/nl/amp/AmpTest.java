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

package nl.amp;

import org.junit.jupiter.api.Test;

import nl.amp.api.domain.BevestigingKoppelingOrderRequest;
import nl.amp.api.domain.BevestigingVerwerkingAnnuleringRequest;
import nl.amp.api.domain.BevestigingVerwerkingUitreikingRequest;
import nl.amp.api.domain.BlokkeringRequest;
import nl.amp.api.domain.Geslacht;
import nl.amp.api.domain.ObjectFactory;
import nl.amp.api.domain.OphalenAnnuleringenRequest;
import nl.amp.api.domain.OphalenOrdersRequest;
import nl.amp.api.domain.OphalenUitreikingenRequest;
import nl.amp.api.domain.OrderUpdateRequest;
import nl.amp.api.domain.VoormeldingRequest;
import nl.procura.amp.AMPSoapHandler;

public class AmpTest extends AbstractTestWireMockClient {

  private static final ObjectFactory of = new ObjectFactory();

  @Test
  public void mustTestBevestigingKoppelingOrder() {
    AMPSoapHandler handler = getSoapHandler();
    System.out.println("bevestigingKoppelingOrder: " + handler.bevestigingKoppelingOrder(bevestigingKoppelingOrder()));
  }

  @Test
  public void mustTestBevestigVerwerkingAnnulering() {
    AMPSoapHandler handler = getSoapHandler();
    System.out.println("bevestigingVerwerkingAnnulering: " + handler.bevestigingAnnulering(
        bevestigingVerwerkingAnnulering()));
  }

  @Test
  public void mustTestBevestigVerwerkingUitreiking() {
    AMPSoapHandler handler = getSoapHandler();
    System.out.println("bevestigingVerwerkingUitreiking: "
        + handler.bevestigUitreiking(bevestigingVerwerkingUitreiking()));
  }

  @Test
  public void mustTestBlokkering() {
    AMPSoapHandler handler = getSoapHandler();
    System.out.println("blokkering: " + handler.blokkeer(blokkering()));
  }

  @Test
  public void mustTestOphalenAnnuleringen() {
    AMPSoapHandler handler = getSoapHandler();
    System.out.println("ophalenAnnuleringen: " + handler.ophalenAnnuleringen(ophalenAnnuleringen()));
    System.out.println("Ophalen order: " + handler.ophalenOrders(ophalenOrders()).getOrders());
    System.out.println("ophalenUitreikingen: " + handler.ophalenUitreikingen(ophalenUitreikingen()));
    System.out.println("orderUpdate: " + handler.orderUpdate(orderUpdate()));
    System.out.println("Bezorgmelding: " + handler.voormelding(voorvermelding()));
  }

  @Test
  public void mustTestOphalenOrders() {
    AMPSoapHandler handler = getSoapHandler();
    System.out.println("Ophalen orders: " + handler.ophalenOrders(ophalenOrders()).getOrders());
  }

  @Test
  public void mustTestOphalenUitreikingen() {
    AMPSoapHandler handler = getSoapHandler();
    System.out.println("ophalenUitreikingen: " + handler.ophalenUitreikingen(ophalenUitreikingen()));
  }

  @Test
  public void mustTestOrderUpdate() {
    AMPSoapHandler handler = getSoapHandler();
    System.out.println("orderUpdate: " + handler.orderUpdate(orderUpdate()));
  }

  @Test
  public void mustTestVoormelding() {
    AMPSoapHandler handler = getSoapHandler();
    System.out.println("Voormelding: " + handler.voormelding(voorvermelding()));
  }

  private static VoormeldingRequest voorvermelding() {
    return of.createVoormeldingRequest()
        .withAanvraagnummer(1234567890L)
        .withAdresgegevens(of.createAdresgegevens()
            .withHuisnummer(1)
            .withHuisnummerToevoeging("A")
            .withPostcode("1234AB")
            .withStraatnaam("Straatnaam")
            .withWoonplaats("Woonplaats"))
        .withDiensttype("Diensttype")
        .withGemeentecode((short) 1963)
        .withGemeentenaam("Gemeentenaam")
        .withHoofdorder(true)
        .withInTeHoudenReisdocumenten(
            of.createArrayOfInTeHoudenReisdocument(of.createArrayOfInTeHoudenReisdocument()
                .withInTeHoudenReisdocument(of.createInTeHoudenReisdocument()
                    .withReisdocumentnummer("123")
                    .withReisdocumentomschrijving("bla"))))
        .withLocatiecode(1L)
        .withOpmerkingen(of.createVoormeldingRequestOpmerkingen("Opmerking"))
        .withPersoonsgegevens(of.createPersoonsgegevens()
            .withVoorletters("F.")
            .withVoornamen("Frits")
            .withVoorvoegsels("van")
            .withAchternaam("Janssen")
            .withEmailadres("info@procura.nl")
            .withGeboortedatum(20000101)
            .withGeslacht(Geslacht.M)
            .withTelefoonnummer1("1234")
            .withTelefoonnummer2("4567"))
        .withReferentienummer(of.createVoormeldingRequestReferentienummer("123"))
        .withReisdocumentOmschrijving("ReisdocumentOmschrijving");
  }

  private static OphalenOrdersRequest ophalenOrders() {
    return of.createOphalenOrdersRequest()
        .withGemeentecode((short) 1963);
  }

  private static BevestigingKoppelingOrderRequest bevestigingKoppelingOrder() {
    return of
        .createBevestigingKoppelingOrderRequest()
        .withGemeentecode((short) 1963)
        .withGemeentestatus("")
        .withOrderreferentieid("");
  }

  private static OrderUpdateRequest orderUpdate() {
    return of.createOrderUpdateRequest()
        .withGemeentecode((short) 1963)
        .withGemeentestatus("")
        .withOrderreferentieid("")
        .withReisdocumentnummer("123");
  }

  private static BlokkeringRequest blokkering() {
    return of.createBlokkeringRequest()
        .withGemeentecode((short) 1963)
        .withGemeentestatus("")
        .withOmschrijving("")
        .withOrderreferentieid("");
  }

  private static OphalenUitreikingenRequest ophalenUitreikingen() {
    return of.createOphalenUitreikingenRequest()
        .withGemeentecode((short) 1963);
  }

  private static BevestigingVerwerkingUitreikingRequest bevestigingVerwerkingUitreiking() {
    return of.createBevestigingVerwerkingUitreikingRequest()
        .withGemeentecode((short) 1963)
        .withGemeentestatus("")
        .withOrderreferentieid("");
  }

  private static OphalenAnnuleringenRequest ophalenAnnuleringen() {
    return of.createOphalenAnnuleringenRequest()
        .withGemeentecode((short) 1963);
  }

  private static BevestigingVerwerkingAnnuleringRequest bevestigingVerwerkingAnnulering() {
    return of.createBevestigingVerwerkingAnnuleringRequest()
        .withGemeentecode((short) 1963)
        .withGemeentestatus("")
        .withOrderreferentieid("");
  }
}
