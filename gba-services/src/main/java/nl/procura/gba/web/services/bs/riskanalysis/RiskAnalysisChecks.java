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

package nl.procura.gba.web.services.bs.riskanalysis;

import static java.lang.String.format;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesTemplate;
import nl.procura.gba.web.services.zaken.algemeen.controle.StandaardControle;

/**
 * All checks of riskanalysis
 */
public class RiskAnalysisChecks extends ControlesTemplate<RiskAnalysisService> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RiskAnalysisChecks.class);

  private final RiskAnalysisService service;

  public RiskAnalysisChecks(RiskAnalysisService service) {
    super(service);
    this.service = service;
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    Controles controles = new Controles();
    service.getApplicableCases().stream()
        .map(this::getRelocationCheck)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .forEach(controles::add);
    return controles;
  }

  private Optional<RelocationCheck> getRelocationCheck(RiskAnalysisRelatedCase relatedCase) {
    Optional<RiskProfile> profile = service.getApplicableRiskProfile(relatedCase.getZaak())
        .filter(p -> !relatedCase.getZaak().getStatus().isEindStatus());

    if (profile.isPresent()) {
      if (RiskAnalysisService.hasRiskAnalysisCase(relatedCase.getZaak())) {
        return Optional.empty();
      }
      LOGGER.info("Risicoprofiel {} gevonden voor zaak {}", profile.get().getName(), relatedCase.getZaakId());
      return Optional.of(new RelocationCheck(profile.get(), relatedCase));
    }

    // remove so personen-zaken-ws cases will be processed
    LOGGER.info("Geen risicoprofiel gevonden voor zaak {}", relatedCase.getZaakId());
    service.removeWaitForRiskAnalysis(relatedCase.getZaak());
    return Optional.empty();
  }

  public class RelocationCheck extends StandaardControle {

    public RelocationCheck(RiskProfile riskProfile, RiskAnalysisRelatedCase relatedCase) {
      super("Risicoprofiel", riskProfile.getName());
      setId(relatedCase.getZaakId());

      service.getServices().getZakenService().getVolledigeZaak(relatedCase.getZaak());
      Dossier dossier = service.getNewZaak(riskProfile, relatedCase);
      service.save(dossier);

      String msg = "Risicoanalyse met zaak-id %s toegevoegd voor %d perso(o)n(en)";
      addOpmerking(format(msg, dossier.getZaakId(), relatedCase.getNumberOfPersons()));
    }

    @Override
    public boolean isGewijzigd() {
      return true;
    }
  }
}
