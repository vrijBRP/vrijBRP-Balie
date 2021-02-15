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

package examples.nl.procura.gba.document;

import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.v1_0.document.contactgegevens.GbaRestDocumentAntwoord;
import nl.procura.gba.web.rest.v1_0.document.genereren.GbaRestDocumentGenererenVraag;
import nl.procura.gba.web.rest.v1_0.document.genereren.GbaRestDocumentPersoon;
import nl.procura.gba.web.rest.v1_0.document.genereren.GbaRestDocumentZaak;
import nl.procura.gbaws.testdata.Testdata;

import examples.nl.procura.gba.GbaRestClientVoorbeelden;

public class GbaRestClientDocumentVoorbeelden extends GbaRestClientVoorbeelden {

  public GbaRestClientDocumentVoorbeelden() throws GbaRestClientException {
    documentContactgegevens();
    documentGenereren1();
    documentGenereren2();
    documentGenereren3();
    documentGenereren4();
  }

  public static void main(String[] args) throws GbaRestClientException {
    new GbaRestClientDocumentVoorbeelden();
  }

  protected void documentGenereren1() throws GbaRestClientException {
    writeStream(getStream(client.getDocument().genereren(910, "pdf",
        Testdata.TEST_BSN_1.toString()).getClientResponse()), "pdf");
  }

  protected void documentGenereren2() throws GbaRestClientException {
    GbaRestDocumentGenererenVraag vraag = new GbaRestDocumentGenererenVraag();
    vraag.setPersoon(new GbaRestDocumentPersoon(Testdata.TEST_BSN_1.toString())
        .addGegeven("reisdocument", "123456"));
    vraag.setDocumentCode(533);
    vraag.setDocumentType("pdf");

    writeStream(getStream(client.getDocument().genereren(vraag).getClientResponse()), "pdf");
  }

  protected void documentGenereren3() throws GbaRestClientException {
    GbaRestDocumentGenererenVraag vraag = new GbaRestDocumentGenererenVraag();
    vraag.setZaak(new GbaRestDocumentZaak("0030-yq3-l3p").addGegeven("bla", "foo"));
    vraag.setDocumentCode(859);
    vraag.setDocumentType("pdf");

    writeStream(getStream(client.getDocument().genereren(vraag).getClientResponse()), "pdf");
  }

  protected void documentGenereren4() throws GbaRestClientException {
    GbaRestDocumentGenererenVraag vraag = new GbaRestDocumentGenererenVraag();
    vraag.setZaak(new GbaRestDocumentZaak("0030-yq3-l3p")
        .addGegeven("bla", "foo"));
    vraag.setDocumentDmsNaam("zs-zaak-samenvatting");
    vraag.setDocumentType("pdf");

    writeStream(getStream(client.getDocument().genereren(vraag).getClientResponse()), "pdf");
  }

  protected void documentContactgegevens() throws GbaRestClientException {
    GbaRestDocumentAntwoord antwoord = getObject(client.getDocument().get("123451"));
    if (antwoord.getDocument() != null) {
      System.out.println(antwoord.getDocument().getPersoon().getGeslachtsnaam());
    }
  }
}
