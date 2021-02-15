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

package nl.procura.gba.web.services.beheer.vrs;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;

import java.time.Duration;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.procura.burgerzaken.vrsclient.api.SignaleringControllerApi;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestBSN;
import nl.procura.burgerzaken.vrsclient.model.SignaleringResponse;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Bsn;

@Singleton
public class VrsService {

  private final ParameterService parameterService;

  @Inject
  public VrsService(ParameterService parameterService) {
    this.parameterService = parameterService;
  }

  public SignaleringControllerApi getApi() {
    String url = parameterService.getSysteemParameter(VRS_SERVICE_URL).getValue();
    long timeout = Long.parseLong(parameterService.getSysteemParameter(VRS_SERVICE_TIMEOUT).getValue());
    VrsClient vrsClient = new VrsClient(url, Duration.ofSeconds(timeout));
    return new SignaleringControllerApi(vrsClient);
  }

  public boolean isEnabled() {
    ProcuraDate date = new ProcuraDate(parameterService.getSysteemParameter(VRS_START_DATE).getValue());
    return date.isExpired();
  }

  @ThrowException("Fout bij uitvoeren van van de signaleringscontrole van het RVIG")
  public Optional<SignaleringResult> checkSignalering(Aanvraagnummer aanvraagnummer, String bsn) {
    Bsn bsnValidation = new Bsn(bsn);
    SignaleringControllerApi api = getApi();
    SignaleringRequestBSN srBSN = new SignaleringRequestBSN();
    srBSN.setAanvraagnummer(aanvraagnummer.getNummer());
    srBSN.setBsn(bsnValidation.getDefaultBsn());
    SignaleringResponse response = api.signaleringsRequestBSN(srBSN);
    if (response.getResultaatCode() == SignaleringResponse.ResultaatCodeEnum.NO_HIT) {
      return Optional.empty();
    }
    SignaleringResult.SignaleringResultBuilder builder = SignaleringResult.builder()
        .description(response.getResultaatOmschrijving());
    ofNullable(response.getSignaleringInformatie()).ifPresent(si -> builder.signaleringen(si.getSignaleringen()));
    ofNullable(response.getMededelingRvIG()).ifPresent(builder::note);
    return Optional.of(builder.build());
  }
}
