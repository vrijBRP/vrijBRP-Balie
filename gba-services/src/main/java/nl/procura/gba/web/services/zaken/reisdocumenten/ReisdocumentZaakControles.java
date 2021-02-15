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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static nl.procura.gba.common.ZaakStatusType.*;
import static nl.procura.gba.web.services.zaken.reisdocumenten.LeveringType.DOCUMENT_GOED;
import static nl.procura.gba.web.services.zaken.reisdocumenten.SluitingType.AANVRAAG_NIET_AFGESLOTEN;

import java.util.Optional;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.gba.web.services.beheer.raas.RaasService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakStatusHistorie;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesTemplate;
import nl.procura.gba.web.services.zaken.algemeen.controle.ZaakControle;
import nl.procura.raas.rest.domain.aanvraag.UpdateAanvraagRequest;

/**
 * Alle controles van reisdocumenten
 */
public class ReisdocumentZaakControles extends ControlesTemplate<ReisdocumentService> {

  public ReisdocumentZaakControles(ReisdocumentService service) {
    super(service);
  }

  /**
   * Controle per zaak
   */
  public ReisdocumentAanvraag controleer(ReisdocumentAanvraag zaak) {

    if (zaak.getAanvraagnummer().isCorrect() && !zaak.getStatus().isEindStatus()) {

      controleerRaasService(zaak);

      // Zet status op document ontvangen indien van toepassing
      if (zaak.getStatus().isMaximaal(INBEHANDELING)) {
        controleerStatusDocumentOntvangen(zaak);
      }
    }

    return zaak;
  }

  /**
   * Algemene controles van de reisdocumenten
   */
  @Override
  public Controles getControles(ControlesListener listener) {

    Controles controles = new Controles();
    ZaakArgumenten zaakArgumenten = new ZaakArgumenten(INBEHANDELING);

    for (ReisdocumentAanvraag zaak : getService().getMinimalZaken(zaakArgumenten)) {
      controles.addControle(new ZaakControle("Reisdocumenten", getService().getStandardZaak(zaak)));
    }

    return controles;
  }

  /**
   * Check for changes in RAAS service
   */
  private void controleerRaasService(ReisdocumentAanvraag zaak) {
    RaasService raasService = getService().getServices().getRaasService();
    Optional<RdmRaasComparison> raasComparison = getService().getRaasComparison(zaak, true);

    if (raasComparison.isPresent()) {
      RdmRaasComparison comparison = raasComparison.get();

      if (comparison.isDifferent()) {
        zaak.setReisdocumentStatus(comparison.getUpdatedStatus());
        getService().save(zaak);
      }

      // Update status to 'verwerkt' because the case is closed.
      if (comparison.shouldCaseBeClosed()) {
        getService().getServices()
            .getZaakStatusService()
            .updateStatus(zaak, VERWERKT_IN_GBA,
                comparison.getUpdateRemarks());
      }

      // Update RAAS case
      UpdateAanvraagRequest ur = new UpdateAanvraagRequest(comparison.getRaasCase().getAanvraagNr().getValue());
      ur.setAanvraag(new DocAanvraagDto().withUpdateProweb(false));
      raasService.updateAanvraag(ur);
    }
  }

  /**
   * Als het document is ontvangen dan de zaak is nog niet verwerkt dan status toevoegen
   */
  private void controleerStatusDocumentOntvangen(ReisdocumentAanvraag zaak) {
    ZaakStatusHistorie statusHistorie = zaak.getZaakHistorie().getStatusHistorie();
    getService().setZaakHistory(zaak);

    ReisdocumentStatus status = zaak.getReisdocumentStatus();
    boolean isGeleverd = status.getStatusLevering() == DOCUMENT_GOED;
    boolean isNietAfgesloten = status.getStatusAfsluiting() == AANVRAAG_NIET_AFGESLOTEN;
    boolean heeftStatusDocumentOntvangen = statusHistorie.isHuidigeStatus(DOCUMENT_ONTVANGEN);

    if (isGeleverd && !heeftStatusDocumentOntvangen && isNietAfgesloten) {
      getService().getZaakStatussen().updateStatus(zaak, zaak.getStatus(), DOCUMENT_ONTVANGEN, "");
    }
  }

}
