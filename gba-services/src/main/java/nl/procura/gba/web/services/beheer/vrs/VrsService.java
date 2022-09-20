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
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_ID;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_RESOURCE_SERVER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_SCOPE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_SECRET;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_IDP_SERVICE_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_INSTANTIE_CODE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_SERVICE_TIMEOUT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_SERVICE_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_START_DATE;
import static org.apache.commons.lang3.StringUtils.getIfBlank;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.vrsclient.api.SignaleringControllerApi;
import nl.procura.burgerzaken.vrsclient.api.SignaleringRequest;
import nl.procura.burgerzaken.vrsclient.api.TokenApi;
import nl.procura.burgerzaken.vrsclient.api.model.TokenRequest;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestV2Bsn;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestV2Bsn.AanleidingEnum;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestV2PersoonsGegevens;
import nl.procura.burgerzaken.vrsclient.model.SignaleringResponse;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;

public class VrsService {

  private final ParameterService parameterService;

  public VrsService(ParameterService parameterService) {
    this.parameterService = parameterService;
  }

  public Optional<SignaleringResult> checkSignalering(VrsRequest request) {
    SignaleringControllerApi api = getApi();

    SignaleringRequest signaleringRequest = getSignaleringRequest(request)
        .accessToken(getAccessToken())
        .pseudoniem("gebruiker-" + parameterService.getServices().getGebruiker().getCUsr())
        .instantieCode(parameterService.getSysteemParm(VRS_INSTANTIE_CODE, true));

    SignaleringResponse response = api.signaleringsRequest(signaleringRequest);
    SignaleringResult.SignaleringResultBuilder builder = SignaleringResult.builder()
        .resultaatCode(response.getResultaatCode())
        .resultaatOmschrijving(response.getResultaatOmschrijving())
        .mededelingRvIG(response.getMededelingRvIG());

    ofNullable(response.getSignaleringInformatie()).ifPresent(si -> builder
        .signaleringen(si.getSignaleringen())
        .persoon(si.getPersoon()));

    return Optional.of(builder.build());
  }

  private SignaleringRequest getSignaleringRequest(VrsRequest vrsRequest) {
    SignaleringRequest signaleringRequest = new SignaleringRequest();
    if (vrsRequest.getBsn() != null) {
      signaleringRequest.v2Bsn(getBsnRequest(vrsRequest));
    } else if (StringUtils.isNotBlank(vrsRequest.getGeslachtsnaam())) {
      signaleringRequest.v2PersoonsGegevens(getPersoonsgegevensRequest(vrsRequest));
    } else {
      throw new ProException(ProExceptionSeverity.WARNING, "Raadpleeg VRS op BSN of geslachtsnaam en geboortedatum");
    }
    return signaleringRequest;
  }

  private SignaleringRequestV2PersoonsGegevens getPersoonsgegevensRequest(VrsRequest vrsRequest) {
    SignaleringRequestV2PersoonsGegevens req = new SignaleringRequestV2PersoonsGegevens();
    req.setGeslachtsnaam(vrsRequest.getGeboortedatum());
    req.setGeboortedatum(toGeboorteDatum(vrsRequest.getGeboortedatum()));

    if (vrsRequest.getAanvraagnummer() == null) {
      req.setAanleiding(SignaleringRequestV2PersoonsGegevens.AanleidingEnum.IDENTITEITSONDERZOEK);
    } else {
      req.setAanleiding(SignaleringRequestV2PersoonsGegevens.AanleidingEnum.REISDOCUMENTAANVRAAG);
      req.aanvraagnummer(vrsRequest.getAanvraagnummer().getNummer());
    }
    return req;
  }

  private SignaleringRequestV2Bsn getBsnRequest(VrsRequest vrsRequest) {
    SignaleringRequestV2Bsn req = new SignaleringRequestV2Bsn();
    req.setBsn(vrsRequest.getBsn().getDefaultBsn());
    if (vrsRequest.getAanvraagnummer() == null) {
      req.setAanleiding(AanleidingEnum.IDENTITEITSONDERZOEK);
    } else {
      req.setAanleiding(AanleidingEnum.REISDOCUMENTAANVRAAG);
      req.aanvraagnummer(vrsRequest.getAanvraagnummer().getNummer());
    }
    return req;
  }

  private LocalDate toGeboorteDatum(String geboortedatum) {
    return new ProcuraDate(geboortedatum)
        .getDateFormat()
        .toInstant().atZone(ZoneId.systemDefault())
        .toLocalDate();
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
    String customIssuerUri = parameterService.getSysteemParm(VRS_IDP_SERVICE_URL, false);
    String tokenUrl = getIfBlank(customIssuerUri, () -> getApi().getIDPIssuerResponse().getIssuerUri());
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
