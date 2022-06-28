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
import java.util.function.Supplier;

import nl.procura.burgerzaken.vrsclient.api.SignaleringControllerApi;
import nl.procura.burgerzaken.vrsclient.api.TokenApi;
import nl.procura.burgerzaken.vrsclient.api.model.TokenRequest;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestBSN;
import nl.procura.burgerzaken.vrsclient.model.SignaleringResponse;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Bsn;

public class VrsService {

  private final ParameterService parameterService;

  public VrsService(ParameterService parameterService) {
    this.parameterService = parameterService;
  }

  public SignaleringControllerApi getApi() {
    String url = parameterService.getProxyUrl(VRS_SERVICE_URL, true);
    long timeout = Long.parseLong(parameterService.getSysteemParm(VRS_SERVICE_TIMEOUT, true));
    VrsClient vrsClient = new VrsClient(url, Duration.ofSeconds(timeout));
    return new SignaleringControllerApi(vrsClient, getTokenSupplier());
  }

  public Supplier<String> getTokenSupplier() {
    return () -> {
      long timeout = Long.parseLong(parameterService.getSysteemParm(VRS_SERVICE_TIMEOUT, true));
      String issuerUri = getApi().getIDPIssuerResponse().getIssuerUri();
      String url = parameterService.getProxyUrl(issuerUri, true);
      VrsClient vrsClient = new VrsClient(url, Duration.ofSeconds(timeout));

      TokenRequest tokenRequest = new TokenRequest();
      tokenRequest.setClientId(parameterService.getSysteemParm(VRS_CLIENT_ID, true));
      tokenRequest.setClientSecret(parameterService.getSysteemParm(VRS_CLIENT_SECRET, true));
      tokenRequest.setScope(parameterService.getSysteemParm(VRS_CLIENT_SCOPE, true));
      tokenRequest.setResourceServer(parameterService.getSysteemParm(VRS_CLIENT_RESOURCE_SERVER, true));

      return new TokenApi(vrsClient).getTokenResponse(tokenRequest).getAccess_token();
    };
  }

  public boolean isEnabled() {
    ProcuraDate date = new ProcuraDate(parameterService.getSysteemParameter(VRS_START_DATE).getValue());
    return date.isExpired();
  }

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
