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

package nl.procura.gba.web.services.beheer.vrs;

import static java.util.Optional.ofNullable;
import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.IDENTITEITSONDERZOEK;
import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.REISDOCUMENTAANVRAAG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_BASISREGISTER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_ID;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_RESOURCE_SERVER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_SCOPE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_SECRET;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_IDP_SERVICE_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_INSTANTIE_CODE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_REG_MELDING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_SERVICE_TIMEOUT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_SERVICE_URL;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.Globalfunctions.pos;
import static org.apache.commons.lang3.StringUtils.getIfBlank;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.procura.burgerzaken.vrsclient.api.AanvragenApi;
import nl.procura.burgerzaken.vrsclient.api.DocumentenApi;
import nl.procura.burgerzaken.vrsclient.api.RegistratieMeldingApi;
import nl.procura.burgerzaken.vrsclient.api.SignaleringApi;
import nl.procura.burgerzaken.vrsclient.api.TokenApi;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRequest;
import nl.procura.burgerzaken.vrsclient.api.VrsMetadata;
import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.api.model.TokenRequest;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponse;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvragenResponse;
import nl.procura.burgerzaken.vrsclient.model.RegistratieMeldingReisdocumentResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentPersoonsgegevensResponse;
import nl.procura.burgerzaken.vrsclient.model.SignaleringResponse;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.zaken.inhoudingen.BasisregisterDocument;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingBasisregister;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.gba.web.services.zaken.reisdocumenten.SignaleringResult;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Bsn;

public class VrsService {

  private final ParameterService parameterService;

  public VrsService(ParameterService parameterService) {
    this.parameterService = parameterService;
  }

  public Optional<SignaleringResult> checkSignalering(VrsRequest request) {
    request.metadata(getVrsMetadata());
    SignaleringApi api = getSignaleringApi();
    SignaleringResponse response = api.signaleringsRequest(request);
    SignaleringResult.SignaleringResultBuilder builder = SignaleringResult.builder()
        .resultaatCode(response.getResultaatCode())
        .resultaatOmschrijving(response.getResultaatOmschrijving())
        .mededelingRvIG(response.getMededelingRvIG());

    ofNullable(response.getSignaleringInformatie())
        .ifPresent(si -> builder.signaleringen(si.getSignaleringen())
            .persoon(si.getPersoon()));

    return Optional.of(builder.build());
  }

  public Optional<SignaleringResult> checkIdentiteit(BasePLExt pl) {
    return checkSignalering(new VrsRequest()
        .bsn(new Bsn(pl.getPersoon().getBsn().getDescr())));
  }

  public List<ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentPersoonsgegevensResponse> getReisdocumentenLijst(BasePLExt pl,
      Aanvraagnummer aanvraagnummer) {
    return getReisdocumenten(pl, aanvraagnummer)
        .map(response -> response.getReisdocumentenLijst()
            .stream()
            .map(ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentInformatiePersoonsgegevens::getReisdocument)
            .collect(Collectors.toList()))
        .orElse(new ArrayList<>());
  }

  public Optional<ReisdocumentInformatiePersoonsGegevensInstantieResponse> getReisdocumenten(BasePLExt pl, Aanvraagnummer aanvraagnummer) {
    return getReisdocumenten(new VrsRequest()
        .aanleiding(aanvraagnummer != null ? REISDOCUMENTAANVRAAG : IDENTITEITSONDERZOEK)
        .bsn(new Bsn(pl.getPersoon().getBsn().toLong()))
        .aanvraagnummer(aanvraagnummer != null ? aanvraagnummer.getNummer() : null));
  }

  public DocumentInhoudingBasisregister getBasisregister(BasePLExt pl) {
    if (isRegistratieMeldingEnabled()) {
      List<BasisregisterDocument> documenten = getReisdocumentenLijst(pl, null)
          .stream()
          .map(BasisregisterDocument::new)
          .collect(Collectors.toList());
      return new DocumentInhoudingBasisregister(documenten);
    }
    return null;
  }

  private Optional<ReisdocumentInformatiePersoonsGegevensInstantieResponse> getReisdocumenten(VrsRequest request) {
    if (isBasisregisterEnabled()) {
      request.metadata(getVrsMetadata());
      return ofNullable(getDocumentenApi().documenten(request).response());
    }
    return Optional.empty();
  }

  public Optional<ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse> getReisdocument(VrsRequest request) {
    if (isBasisregisterEnabled()) {
      request.metadata(getVrsMetadata());
      return ofNullable(getDocumentenApi().document(request).response());
    }
    return Optional.empty();
  }

  public Optional<ControleAanvragenResponse> getAanvragen(BasePLExt pl, Aanvraagnummer aanvraagnummer) {
    return getAanvragen(new VrsRequest()
        .aanleiding(aanvraagnummer != null ? REISDOCUMENTAANVRAAG : IDENTITEITSONDERZOEK)
        .bsn(new Bsn(pl.getPersoon().getBsn().toLong()))
        .aanvraagnummer(aanvraagnummer != null ? aanvraagnummer.getNummer() : null));
  }

  private Optional<ControleAanvragenResponse> getAanvragen(VrsRequest request) {
    if (isBasisregisterEnabled()) {
      request.metadata(getVrsMetadata());
      return ofNullable(getAanvragenApi().aanvragen(request).response());
    }
    return Optional.empty();
  }

  public Optional<ControleAanvraagVolledigResponse> getAanvraag(VrsRequest request) {
    if (isBasisregisterEnabled()) {
      request.metadata(getVrsMetadata());
      return ofNullable(getAanvragenApi().aanvraagDetails(request).response());
    }
    return Optional.empty();
  }

  public RegistratieMeldingReisdocumentResponse registreerMelding(DocumentInhouding zaak) {
    ProcuraDate datumTijd = new ProcuraDate(zaak.getVrsDatumTijd().getDate());
    ProcuraDate datumTijdMelding = new ProcuraDate(zaak.getVrsDatumRedenMelding().getDate());
    VrsMeldingRequest request = new VrsMeldingRequest()
        .metadata(getVrsMetadata())
        .documentnummer(zaak.getNummerDocument())
        .datumTijd(datumTijd)
        .melding(zaak.getVrsMelding())
        .reden(zaak.getVrsReden())
        .datumReden(datumTijdMelding);
    return getMeldingApi().meldingRequest(request).response();
  }

  public boolean isEnabled() {
    return pos(parameterService.getSysteemParameter(VRS_ENABLED).getValue());
  }

  public boolean isBasisregisterEnabled() {
    return isEnabled() && isTru(parameterService.getSysteemParm(VRS_BASISREGISTER, false));
  }

  public boolean isRegistratieMeldingEnabled() {
    return pos(parameterService.getSysteemParameter(VRS_REG_MELDING).getValue());
  }

  private SignaleringApi getSignaleringApi() {
    return new SignaleringApi(getVrsClient());
  }

  private AanvragenApi getAanvragenApi() {
    return new AanvragenApi(getVrsClient());
  }

  private RegistratieMeldingApi getMeldingApi() {
    return new RegistratieMeldingApi(getVrsClient());
  }

  private DocumentenApi getDocumentenApi() {
    return new DocumentenApi(getVrsClient());
  }

  private TokenApi getTokenApi() {
    return new TokenApi(getVrsClient());
  }

  private VrsClient getVrsClient() {
    String url = parameterService.getProxyUrl(VRS_SERVICE_URL, true);
    long timeout = Long.parseLong(parameterService.getSysteemParm(VRS_SERVICE_TIMEOUT, true));
    return new VrsClient(url, Duration.ofSeconds(timeout));
  }

  private VrsMetadata getVrsMetadata() {
    return new VrsMetadata()
        .accessToken(getAccessToken())
        .pseudoniem("gebruiker-" + parameterService.getServices().getGebruiker().getCUsr())
        .instantieCode(parameterService.getSysteemParm(VRS_INSTANTIE_CODE, true));
  }

  private String getAccessToken() {
    long timeout = Long.parseLong(parameterService.getSysteemParm(VRS_SERVICE_TIMEOUT, true));
    String customIssuerUri = parameterService.getSysteemParm(VRS_IDP_SERVICE_URL, false);
    String tokenUrl = getIfBlank(customIssuerUri, () -> getTokenApi().getIDPIssuerResponse().getIssuerUri());
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
