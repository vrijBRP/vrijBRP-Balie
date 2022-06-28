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

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.vrsclient.api.SignaleringControllerApi;
import nl.procura.burgerzaken.vrsclient.api.SignaleringRequest;
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

  public Optional<SignaleringResult> checkSignalering(Aanvraagnummer aanvraagnummer, String bsn) {
    Bsn bsnValidation = new Bsn(bsn);
    SignaleringControllerApi api = getApi();
    SignaleringRequest request = new SignaleringRequest();
    request.bsn(new SignaleringRequestBSN()
        .aanvraagnummer(aanvraagnummer.getNummer())
        .bsn(bsnValidation.getDefaultBsn()))
        .accessToken(getAccessToken())
        .pseudoniem(parameterService.getServices().getGebruiker().getGebruikersnaam())
        .instantieCode(parameterService.getSysteemParm(VRS_INSTANTIE_CODE, true));

    SignaleringResponse response = api.signaleringsRequest(request);
    if (response.getResultaatCode() == SignaleringResponse.ResultaatCodeEnum.NO_HIT) {
      return Optional.empty();
    }
    SignaleringResult.SignaleringResultBuilder builder = SignaleringResult.builder()
        .description(response.getResultaatOmschrijving());
    ofNullable(response.getSignaleringInformatie()).ifPresent(si -> builder.signaleringen(si.getSignaleringen()));
    ofNullable(response.getMededelingRvIG()).ifPresent(builder::note);
    return Optional.of(builder.build());
  }

  public boolean isEnabled() {
    return new ProcuraDate(parameterService.getSysteemParameter(VRS_START_DATE).getValue()).isExpired();
  }

  public SignaleringControllerApi getApi() {
    String url = parameterService.getProxyUrl(VRS_SERVICE_URL, true);
    long timeout = Long.parseLong(parameterService.getSysteemParm(VRS_SERVICE_TIMEOUT, true));
    VrsClient vrsClient = new VrsClient(url, Duration.ofSeconds(timeout));
    return new SignaleringControllerApi(vrsClient);
  }

  private String getAccessToken() {
    long timeout = Long.parseLong(parameterService.getSysteemParm(VRS_SERVICE_TIMEOUT, true));
    String customIssuerUri = parameterService.getSysteemParm(VRS_IDP_SERVICE_URL, true);
    String tokenUrl = StringUtils.defaultString(customIssuerUri, getApi().getIDPIssuerResponse().getIssuerUri());
    String proxyTokenUrl = parameterService.getProxyUrl(tokenUrl, true);
    VrsClient vrsClient = new VrsClient(proxyTokenUrl, Duration.ofSeconds(timeout));

    TokenRequest tokenRequest = new TokenRequest()
        .clientId(parameterService.getSysteemParm(VRS_CLIENT_ID, true))
        .clientSecret(parameterService.getSysteemParm(VRS_CLIENT_SECRET, true))
        .scope(parameterService.getSysteemParm(VRS_CLIENT_SCOPE, true))
        .resourceServer(parameterService.getSysteemParm(VRS_CLIENT_RESOURCE_SERVER, true));

    return new TokenApi(vrsClient).getTokenResponse(tokenRequest).getAccess_token();
  }
}
