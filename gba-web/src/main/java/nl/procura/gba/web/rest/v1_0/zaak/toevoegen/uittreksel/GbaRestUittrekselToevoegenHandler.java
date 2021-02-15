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

package nl.procura.gba.web.rest.v1_0.zaak.toevoegen.uittreksel;

import static nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakType.UITTREKSEL;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.util.List;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakAlgemeenToevoegenHandler;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakToevoegenVraag;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaakPersoon;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZakenService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class GbaRestUittrekselToevoegenHandler extends GbaRestZaakAlgemeenToevoegenHandler {

  public GbaRestUittrekselToevoegenHandler(Services services) {
    super(services);
  }

  /**
   * Zoek het document op
   */
  private static DocumentRecord getDocument(DocumentZakenService service, String documentCode) {

    Services services = service.getServices();
    DocumentRecord document = services.getDocumentService().getDocument(along(documentCode));

    if (document == null) {
      throw new ProException(ERROR, "Geen document gevonden met code: " + documentCode);
    }

    return document;
  }

  public List<GbaRestElement> toevoegen(GbaRestZaakToevoegenVraag vraagParm) {

    GbaRestZaakUittrekselToevoegenVraag vraag = new GbaRestZaakUittrekselToevoegenVraag(vraagParm);
    DocumentZakenService uittreksels = getServices().getDocumentZakenService();
    List<GbaRestElement> deelZaken = vraag.getDeelzaken().getElementen();
    String zaakId = "";

    if (deelZaken.isEmpty()) {
      throw new ProException(ERROR, "Geen deelzaken");
    }

    DocumentZaak zaak = (DocumentZaak) algemeen(vraag, uittreksels.getNewZaak());

    checkZaakId(zaak);

    for (GbaRestElement deelZaak : deelZaken) {

      if (fil(zaakId)) {
        zaak.setZaakId(zaakId);
      }

      BsnFieldValue relBsn = new BsnFieldValue(deelZaak.get(GbaRestElementType.BSN).getWaarde(true));
      AnrFieldValue relAnummer = new AnrFieldValue();
      RelatieType relatieType = RelatieType.ONBEKEND;

      zaak.setRelBsn(relBsn.getBigDecimalValue());
      zaak.setDoc(getDocument(uittreksels, deelZaak.get(GbaRestElementType.DOCUMENT_CODE).getWaarde(true)));
      zaak.setIdVragen(toBigDecimal(0));
      zaak.getPersonen().add(new DocumentZaakPersoon(relAnummer, relBsn, relatieType));
    }

    uittreksels.save(zaak);
    zaakId = zaak.getZaakId();
    return getAntwoord(zaakId, UITTREKSEL);
  }
}
