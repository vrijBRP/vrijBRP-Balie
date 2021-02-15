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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.inbox;

import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakAlgemeenToevoegenHandler;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxVerwerkingType;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.inbox.geboorte.InboxGeboorteProcessor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.inbox.huwelijk.InboxGetuigenProcessor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.inbox.huwelijk.InboxHuwelijkProcessor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.inbox.huwelijk.InboxVoornemenProcessor;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.standard.exceptions.ProException;

public class GbaRestInboxVerwerkenHandler extends GbaRestZaakAlgemeenToevoegenHandler {

  public GbaRestInboxVerwerkenHandler(Services services) {
    super(services);
  }

  public CaseProcessingResult verwerken(Zaak zaak) {
    InboxRecord inboxRecord = (InboxRecord) zaak;
    CaseProcessingResult result;

    switch (GbaRestInboxVerwerkingType.get(inboxRecord.getVerwerkingId())) {
      case HUWELIJK_RESERVERING:
        result = new InboxHuwelijkProcessor(getServices()).addReservering(inboxRecord);
        break;

      case HUWELIJK_VOORNEMEN:
        result = new InboxVoornemenProcessor(getServices()).addVoornemen(inboxRecord);
        break;

      case HUWELIJK_GETUIGEN:
        result = new InboxGetuigenProcessor(getServices()).addGetuigen(inboxRecord);
        break;

      case GEBOORTE_AANGIFTE:
        result = new InboxGeboorteProcessor(getServices()).addAangifte(inboxRecord);
        break;

      case ZAAK_BIJLAGEN:
        result = new InboxBijlageProcessor(getServices()).addBijlage(inboxRecord);
        break;

      case ONBEKEND:
      default:
        throw new ProException(
            "De zaak kan niet worden verwerkt!. Onbekend id: " + inboxRecord.getVerwerkingId());
    }

    return result;
  }
}
