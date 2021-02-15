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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken;

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.inbox.GbaRestInboxVerwerkenHandler;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.GbaRestRiskAnalysisProcessingHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMelding;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMeldingType;
import nl.procura.standard.exceptions.ProException;

public class GbaRestZaakVerwerkenHandler extends GbaRestHandler {

  public GbaRestZaakVerwerkenHandler(Services services) {
    super(services);
  }

  /**
   * find cases
   */
  public List<Zaak> getZaken(GbaRestZaakVerwerkenVraag vraag) {
    ZakenService zdb = getServices().getZakenService();
    return zdb.getStandaardZaken(zdb.getMinimaleZaken(new ZaakArgumenten(vraag.getZaakId())));
  }

  public GbaRestZaakAntwoord verwerken(GbaRestZaakVerwerkenVraag vraag) {
    List<CaseProcessingResult> resultaten = new ArrayList<>();
    for (Zaak zaak : getZaken(vraag)) {
      switch (zaak.getType()) {
        case INBOX:
          resultaten.add(new GbaRestInboxVerwerkenHandler(getServices()).verwerken(zaak));
          break;
        case RISK_ANALYSIS:
          resultaten.add(new GbaRestRiskAnalysisProcessingHandler(getServices()).process(zaak));
          break;

        case ONBEKEND:
        default:
          throw new ProException(ERROR, "Onbekend zaaktype: " + zaak.getZaakId());
      }
    }

    // check messages
    GbaRestZaakAntwoord antwoord = new GbaRestZaakAntwoord();
    for (CaseProcessingResult resultaat : resultaten) {
      for (ProException melding : resultaat.getInfo()) {
        switch (melding.getSeverity()) {
          case ERROR:
            antwoord.getMeldingen().add(
                new ProRestMelding(ProRestMeldingType.FOUT, 0, melding.getMessage()));
            break;

          case WARNING:
            antwoord.getMeldingen().add(
                new ProRestMelding(ProRestMeldingType.WAARSCHUWING, 0, melding.getMessage()));
            break;

          case INFO:
          case UNKNOWN:
          default:
            antwoord.getMeldingen().add(
                new ProRestMelding(ProRestMeldingType.INFO, 0, melding.getMessage()));
            break;
        }
      }
    }

    return antwoord;
  }
}
