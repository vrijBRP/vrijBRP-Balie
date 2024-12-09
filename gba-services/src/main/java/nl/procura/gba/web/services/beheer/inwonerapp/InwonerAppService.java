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

package nl.procura.gba.web.services.beheer.inwonerapp;

import static java.util.Optional.ofNullable;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.CITIZEN;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.SYSTEM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INW_APP_DEBUG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INW_APP_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INW_APP_KEESY_API_KEY;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INW_APP_KEESY_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INW_APP_KEESY_USER_GROUP;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INW_APP_KEESY_USER_ID;
import static nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdType.INWONER_APP;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.EMAIL;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.TEL_MOBIEL;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.TEL_THUIS;
import static nl.procura.standard.Globalfunctions.pos;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

import nl.procura.burgerzaken.keesy.api.ApiClient;
import nl.procura.burgerzaken.keesy.api.ApiClientConfig;
import nl.procura.burgerzaken.keesy.api.ApiResponse;
import nl.procura.burgerzaken.keesy.api.AutorisatieApi;
import nl.procura.burgerzaken.keesy.api.DocumentenApi;
import nl.procura.burgerzaken.keesy.api.SignalenApi;
import nl.procura.burgerzaken.keesy.api.ZakenApi;
import nl.procura.burgerzaken.keesy.api.model.DownloadVerzamelDocumentPDFRequest;
import nl.procura.burgerzaken.keesy.api.model.GeefStartUrlRequest;
import nl.procura.burgerzaken.keesy.api.model.GeefZaakRequest;
import nl.procura.burgerzaken.keesy.api.model.MaakZaakRequest;
import nl.procura.burgerzaken.keesy.api.model.MaakZaakRequestPersoon;
import nl.procura.burgerzaken.keesy.api.model.MaakZaakResponse;
import nl.procura.burgerzaken.keesy.api.model.Signaal;
import nl.procura.burgerzaken.keesy.api.model.SignalenRequest;
import nl.procura.burgerzaken.keesy.api.model.SignalenResponse;
import nl.procura.burgerzaken.keesy.api.model.SluitZaakRequest;
import nl.procura.burgerzaken.keesy.api.model.SluitZaakResponse;
import nl.procura.burgerzaken.keesy.client.OkHttpKeesyClient;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.BrpPasswordGenerator;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;
import nl.procura.gba.web.services.applicatie.meldingen.types.InwonerMelding;
import nl.procura.gba.web.services.zaken.algemeen.ControleerbareService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatieService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InwonerAppService extends AbstractService implements ControleerbareService {

  public InwonerAppService() {
    super("InwonerApp");
  }

  @Override
  public void check() {
    try {
      check(getSignalen());
    } catch (Exception e) {
      addMessage(false, SYSTEM, ERROR, "Fout bij het aanroepen Inwoner.app", e.getMessage(), e);
    }
  }

  public void check(List<InwonerSignaal> signalen) {
    int signalenAantal = signalen.stream().mapToInt(InwonerSignaal::getAantal).sum();
    if (signalenAantal > 0) {
      getServices().getMeldingService().add(new InwonerMelding(signalenAantal));
    } else {
      List<ServiceMelding> meldingen = getServices().getMeldingService().getMeldingen(CITIZEN);
      meldingen.forEach(melding -> getServices().getMeldingService().delete(melding));
    }
  }

  public Optional<Integer> getAantalSignalen(String zaakId) {
    SignalenApi signalenApi = new SignalenApi(getKeesyClient());
    return Optional.ofNullable(signalenApi.geefAantalSignalen(new SignalenRequest()
        .externUserId(getExternUserId())
        .groupId(getUserGroupId())
        .externZaakId(zaakId))
        .onError(msg -> new ProException(WARNING, "Fout bij het ophalen van inwoner.app signalen", msg))
        .getEntity()
        .getAantal());

  }

  public List<InwonerSignaal> getSignalen() {
    List<InwonerSignaal> signalen = new ArrayList<>();
    if (isEnabled()) {
      SignalenApi signalenApi = new SignalenApi(getKeesyClient());
      SignalenRequest request = new SignalenRequest()
          .externUserId(getExternUserId())
          .groupId(getUserGroupId());

      ApiResponse<SignalenResponse> response = signalenApi.geefAantalSignalen(request);
      response.onError(msg -> new ProException(WARNING, "Fout bij het ophalen van inwoner.app signalen", msg));
      if (response.isSuccessful()) {
        if (response.getEntity().getSignalen() != null) {
          for (Signaal signaal : response.getEntity().getSignalen()) {
            Zaak zaak = getZaak(signaal);
            signalen.add(new InwonerSignaal()
                .setAantal(signaal.getAantal())
                .setZaak(zaak)
                .setExternId(signaal.getExternZaakId())
                .setOmschrijving(ofNullable(zaak)
                    .map(ZaakUtils::getTypeEnOmschrijving)
                    .orElse("Onbekende zaak (" + signaal.getExternZaakId() + ")")));
          }
        }
      }
    }
    return signalen;
  }

  @Transactional
  public boolean sluitZaak(Zaak zaak) {
    if (isEnabled()) {
      SluitZaakRequest sluitZaakRequest = createSluitZaakRequest(zaak);
      ZakenApi zakenApi = new ZakenApi(getKeesyClient());
      ApiResponse<SluitZaakResponse> response = zakenApi.sluitZaak(sluitZaakRequest);
      if (response.isSuccessful()) {
        return true;
      } else {
        throw new ProException(WARNING, "Fout bij het afsluiten van zaak: " +
            response.getError().getException().message());
      }
    }
    return false;
  }

  @Transactional
  public boolean maakZaak(Zaak zaak) {
    if (isEnabled()) {
      MaakZaakRequest request = createMaakZaakRequest(zaak);
      if (isBlank(request.persoon().telnr())) {
        throw new ProException(WARNING, "Geen telefoonnummer opgeslagen voor de burger");
      }
      request.externZaakId(getStoredInwonerAppExternalId(zaak).orElse(generateSignalenId()));
      saveZaakIdentificatie(zaak, request.externZaakId());
      ZakenApi zakenApi = new ZakenApi(getKeesyClient());
      ApiResponse<MaakZaakResponse> response = zakenApi.maakZaak(request);
      if (response.isSuccessful()) {
        return true;
      } else {
        throw new ProException(WARNING, "Fout bij het aanmaken van zaak: " +
            response.getError().getException().message());
      }
    }
    return false;
  }

  public String generateSignalenId() {
    String externId = BrpPasswordGenerator.newPassword();
    if (getServices().getZaakIdentificatieService().isExternId(externId)) {
      return generateSignalenId();
    }
    return externId;
  }

  private void saveZaakIdentificatie(Zaak zaak, String externId) {
    ZaakIdentificatie id = new ZaakIdentificatie();
    id.setInternId(zaak.getZaakId());
    id.setExternId(externId);
    id.setZaakType(zaak.getType());
    id.setType(INWONER_APP.getCode());
    zaak.getZaakHistorie().getIdentificaties().getNummers().add(id);
    getServices().getZaakIdentificatieService().save(id);
  }

  public SluitZaakRequest createSluitZaakRequest(Zaak zaak) {
    return new SluitZaakRequest()
        .userId(getExternUserId())
        .zaakId(getStoredInwonerAppExternalId(zaak)
            .map(this::getInwonerAppInternalId)
            .orElseThrow(() -> new IllegalArgumentException("Geen externe zaak id gevonden voor zaak: "
                + zaak.getZaakId())));
  }

  public MaakZaakRequest createMaakZaakRequest(Zaak zaak) {
    Services services = getServices();

    BasePLExt pl = services.getPersonenWsService()
        .getPersoonslijst(zaak.getBurgerServiceNummer().getStringValue());

    if (pl.getCats().isEmpty()) {
      throw new IllegalArgumentException("Geen persoonsgegevens gevonden voor BSN: " + zaak.getBurgerServiceNummer());
    }

    List<PlContactgegeven> contactgegevens = services.getContactgegevensService()
        .getContactgegevens(zaak);

    return new MaakZaakRequest()
        .groupId(getUserGroupId())
        .zaakSoort(InwonerSignaalType.getByZaakType(zaak.getType())
            .orElseThrow(() -> new IllegalArgumentException("Onbekende zaaksoort: " + zaak.getType()))
            .getNaam())
        .behandelaarId(getExternUserId())
        .onderwerp(zaak.getType().getOms())
        .persoon(new MaakZaakRequestPersoon()
            .naam(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam())
            .email(getContact(contactgegevens, EMAIL))
            .telnr(getContact(contactgegevens, TEL_MOBIEL, TEL_THUIS)));
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    return new InwonerAppControles(this).getControles(listener);
  }

  private String getContact(List<PlContactgegeven> contactgegevens, String... contact) {
    return contactgegevens.stream()
        .filter(g -> g.getContactgegeven().isGegeven(contact))
        .filter(g -> isNotBlank(g.getAant()))
        .findFirst()
        .map(PlContactgegeven::getAant)
        .orElse("");
  }

  private Zaak getZaak(Signaal signaal) {
    ZaakArgumenten args = new ZaakArgumenten(signaal.getExternZaakId());
    List<Zaak> zaken = getServices().getZakenService().getMinimaleZaken(args);
    return zaken.isEmpty() ? null : zaken.get(0);
  }

  public boolean isEnabled() {
    return pos(getSysteemParm(INW_APP_ENABLED, false));
  }

  public boolean isDebugEnabled() {
    return pos(getSysteemParm(INW_APP_DEBUG, false));
  }

  private ApiClient getKeesyClient() {
    String url = getSysteemParm(INW_APP_KEESY_URL, true);
    String apiKey = getSysteemParm(INW_APP_KEESY_API_KEY, true);
    ApiClientConfig config = new ApiClientConfig()
        .baseUrl(url)
        .apiKey(apiKey)
        .debug(isDebugEnabled());
    return new OkHttpKeesyClient(config, Duration.ofSeconds(5));
  }

  public Optional<String> getExternalURL(InwonerAppRequest request) {
    return getExternalURL().map(url -> {
      String json = new Gson().toJson(request);
      String params = String.format(json);
      try {
        return url + "&API_PARAMS=" + URLEncoder.encode(params, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private Optional<String> getStoredInwonerAppExternalId(Zaak zaak) {
    ZaakIdentificatieService zaakIdService = getServices().getZaakIdentificatieService();
    return Optional.ofNullable(defaultIfBlank(zaakIdService.getIdentificaties(zaak)
        .getNummer(INWONER_APP.getCode()), null));
  }

  public boolean supportZaak(Zaak zaak) {
    return isEnabled() && InwonerSignaalType.getByZaakType(zaak.getType()).isPresent();
  }

  public Optional<String> getExternalURL() {
    AutorisatieApi autorisatieApi = new AutorisatieApi(getKeesyClient());
    return Optional.of(autorisatieApi.geefStartUrl(new GeefStartUrlRequest()
        .externUserId(getExternUserId())
        .groupId(getUserGroupId()))
        .onError(msg -> new ProException(WARNING, "Fout bij het bevragen van inwoner.app: ", msg))
        .getEntity()
        .getUrl());
  }

  public Optional<byte[]> getSummary(Zaak zaak) {
    if (isEnabled()) {
      return getStoredInwonerAppExternalId(zaak)
          .map(this::getInwonerAppInternalId)
          .map(this::downloadSummary);
    }
    return Optional.empty();
  }

  private String getExternUserId() {
    String userId = getParm(INW_APP_KEESY_USER_ID);
    return defaultIfBlank(userId, getServices().getGebruiker().getEmail()).toUpperCase();
  }

  private String getUserGroupId() {
    return getParm(INW_APP_KEESY_USER_GROUP, true).toUpperCase();
  }

  private String getInwonerAppInternalId(String zaakId) {
    ZakenApi zakenApi = new ZakenApi(getKeesyClient());
    return zakenApi.geefZaak(new GeefZaakRequest().externZaakId(zaakId))
        .onError(msg -> new ProException(WARNING, "Fout bij het bevragen van inwoner.app: ", msg))
        .getEntity()
        .getZaakId();
  }

  private byte[] downloadSummary(String zaakId) {
    DocumentenApi documentenApi = new DocumentenApi(getKeesyClient());
    return documentenApi.downloadVerzamelPDF(new DownloadVerzamelDocumentPDFRequest()
        .zaakId(zaakId)
        .inclusiefInhoudsopgave(true)
        .inclusiefBerichtenoverzicht(true))
        .onError(msg -> new ProException(WARNING, "Fout bij het ophalen van inwoner.app signalen", msg))
        .getEntity();
  }
}
