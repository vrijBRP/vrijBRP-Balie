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

import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;

import java.util.List;
import java.util.Optional;

import nl.procura.commons.elements.core.CommonElementUtils;
import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.dto.raas.bestand.RaasBestandDto;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.raas.rest.client.RaasRestClient;
import nl.procura.raas.rest.domain.Page;
import nl.procura.raas.rest.domain.ResponseMessage;
import nl.procura.raas.rest.domain.ResponseMessages;
import nl.procura.raas.rest.domain.aanvraag.DeleteAanvraagRequest;
import nl.procura.raas.rest.domain.aanvraag.FindAanvraagRequest;
import nl.procura.raas.rest.domain.aanvraag.UpdateAanvraagRequest;
import nl.procura.raas.rest.domain.raas.bestand.DeleteRaasBestandRequest;
import nl.procura.raas.rest.domain.raas.bestand.FindRaasBestandRequest;
import nl.procura.raas.rest.domain.raas.bestand.ProcessRaasBestandRequest;
import nl.procura.standard.exceptions.ProException;

public class RaasService extends AbstractService {

  public RaasService() {
    super("RAAS");
  }

  private RaasRestClient getRaasClient() {
    ParameterService service = getServices().getParameterService();
    String baseUrl = service.getSysteemParameter(ParameterConstant.RAAS_ENDPOINT).getValue();
    return new RaasRestClient(baseUrl, "", "");
  }

  public boolean isRaasServiceActive() {
    return isTru(getServices().getParameterService().getSysteemParameter(ParameterConstant.RAAS_ENABLED).getValue());
  }

  public int getRaasCode() {
    return Math.max(getServices().getGebruiker().getLocatie().getCodeRaas().intValue(), 0); // default 0
  }

  @ThrowException("Fout bij wijzigen RAAS aanvraag")
  public List<ResponseMessage> updateAanvraag(UpdateAanvraagRequest request) {
    return getRaasClient().getAanvraag().update(request).getMessages();
  }

  @ThrowException("Fout bij wijzigen RAAS aanvraag")
  public void reload(DocAanvraagDto aanvraag) {
    FindAanvraagRequest request = new FindAanvraagRequest(aanvraag.getAanvraagNr().getValue());
    DocAanvraagDto reloaded = getServices().getRaasService().get(request);
    CommonElementUtils.copy(reloaded, aanvraag);
  }

  @ThrowException("Fout bij wijzigen RAAS aanvraag")
  public boolean isAanvraag(Long aanvrNr) {
    FindAanvraagRequest request = new FindAanvraagRequest();
    request.setAanvraagNr(aanvrNr);
    return findFirst(request).isPresent();
  }

  @ThrowException("Fout bij ophalen RAAS aanvragen")
  public Optional<DocAanvraagDto> findFirst(FindAanvraagRequest request) {
    return getRaasClient().getAanvraag().find(request).getPage().getContent().stream().findFirst();
  }

  @ThrowException("Fout bij ophalen RAAS aanvragen")
  public DocAanvraagDto get(FindAanvraagRequest request) {
    Optional<DocAanvraagDto> dto = findFirst(request);
    return dto.orElseThrow(() -> new ProException(INFO, "Aanvraag nog niet bekend in de RAAS service."));
  }

  @ThrowException("Fout bij ophalen RAAS aanvragen")
  public Page<DocAanvraagDto> getAanvragen(FindAanvraagRequest request) {
    return getRaasClient().getAanvraag().find(request).getPage();
  }

  @ThrowException("Fout bij ophalen RAAS bestanden")
  public Page<RaasBestandDto> getAanvragen(FindRaasBestandRequest request) {
    return getRaasClient().getRaas().find(request).getPage();
  }

  @ThrowException("Fout bij verwijderen RAAS aanvragen")
  public ResponseMessages delete(DeleteAanvraagRequest request) {
    return getRaasClient().getAanvraag().delete(request).getMessages();
  }

  @ThrowException("Fout bij verwijderen RAAS bestanden")
  public ResponseMessages delete(DeleteRaasBestandRequest request) {
    return getRaasClient().getRaas().delete(request).getMessages();
  }

  @ThrowException("Fout bij verwerken RAAS bestanden")
  public ResponseMessages process(ProcessRaasBestandRequest request) {
    return getRaasClient().getRaas().process(request).getMessages();
  }

  @ThrowException("Fout bij testen RAAS verbinding")
  public ResponseMessages test() {
    return getRaasClient().getRaas().test().getMessages();
  }
}
