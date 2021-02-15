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

package nl.procura.gba.web.services.beheer.raas;

import static nl.procura.standard.Globalfunctions.fil;

import org.apache.commons.lang3.Validate;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.raas.rest.domain.aanvraag.AfsluitingStatusType;
import nl.procura.raas.rest.domain.aanvraag.LeveringStatusType;
import nl.procura.raas.rest.domain.aanvraag.UpdateAanvraagRequest;
import nl.procura.raas.rest.domain.aanvraag.VerwerkingStatusType;
import nl.procura.standard.ProcuraDate;

import lombok.Data;

@Data
public class AfsluitRequest {

  private Long                 aanvraagNummer;
  private LeveringStatusType   statusLevering;
  private AfsluitingStatusType statusAfsluiting;
  private String               nrNLDocIn;
  private boolean              aanpassingLevering;
  private boolean              updateProweb = false;

  public AfsluitRequest() {
  }

  public UpdateAanvraagRequest createRequest() {
    Validate.notNull(aanvraagNummer);
    Validate.notNull(statusLevering);
    Validate.notNull(statusAfsluiting);
    Validate.notNull(nrNLDocIn);

    UpdateAanvraagRequest request = new UpdateAanvraagRequest(aanvraagNummer);
    DocAanvraagDto docAanvraag = new DocAanvraagDto();
    docAanvraag.withUpdateProweb(updateProweb);
    docAanvraag.getLevering().withStatus(statusLevering);
    docAanvraag.withNrNederlandsDocIn(fil(nrNLDocIn) ? nrNLDocIn : "");

    // Afsluiting alleen voor Procura administratie
    ProcuraDate today = new ProcuraDate();
    docAanvraag.getAfsluiting()
        .withDatum(Integer.valueOf(today.getSystemDate()))
        .withTijd(Integer.valueOf(today.getSystemTime()))
        .withStatus(statusAfsluiting);

    if (!LeveringStatusType.DOCUMENT_GOED.equals(statusLevering)) {
      docAanvraag.withStatusVerwerking(VerwerkingStatusType.STATUS_LEV_NIET_VERSTUURD_RAAS);
    } else {
      docAanvraag.withStatusVerwerking(VerwerkingStatusType.STATUS_AFSL_NIET_VERSTUURD_RAAS);
    }

    request.setAanvraag(docAanvraag);
    return request;
  }

  public boolean isAanpassingLevering() {
    return LeveringStatusType.DOCUMENT_GOED.equals(statusLevering);
  }
}
