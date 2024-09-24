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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.gemeenteinbox;

import java.util.Optional;

import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakAlgemeenToevoegenHandler;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxVerwerkingType;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.gemeenteinbox.geboorte.GemeenteInboxGeboorteProcessor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.gemeenteinbox.huwelijk.GemeenteInboxGetuigenProcessor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.gemeenteinbox.huwelijk.GemeenteInboxHuwelijkProcessor;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.gemeenteinbox.huwelijk.GemeenteInboxVoornemenProcessor;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessor;
import nl.procura.gba.web.services.zaken.gemeenteinbox.GemeenteInboxRecord;
import nl.procura.commons.core.exceptions.ProException;

public class GbaRestGemeenteInboxVerwerkenHandler extends GbaRestZaakAlgemeenToevoegenHandler {

  public GbaRestGemeenteInboxVerwerkenHandler(Services services) {
    super(services);
  }

  public Optional<CaseProcessingResult> process(GemeenteInboxRecord inboxRecord) {
    return Optional.of(getCaseProcessor(getServices(), inboxRecord))
        .map(process -> process.process(inboxRecord));
  }

  private CaseProcessor<GemeenteInboxRecord> getCaseProcessor(Services services, GemeenteInboxRecord inboxRecord) {
    // Legacy case processors
    switch (GbaRestInboxVerwerkingType.get(inboxRecord.getVerwerkingId())) {
      case HUWELIJK_RESERVERING:
        return new GemeenteInboxHuwelijkProcessor(services);
      case HUWELIJK_VOORNEMEN:
        return new GemeenteInboxVoornemenProcessor(services);
      case HUWELIJK_GETUIGEN:
        return new GemeenteInboxGetuigenProcessor(services);
      case GEBOORTE_AANGIFTE:
        return new GemeenteInboxGeboorteProcessor(services);
      case ZAAK_BIJLAGEN:
        return new GemeenteInboxBijlageProcessor(services);
      case ONBEKEND:
      default:
        throw new ProException("De zaak kan niet worden verwerkt!. Onbekend id: "
            + inboxRecord.getVerwerkingId());
    }
  }
}
