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

package nl.procura.gba.web.services.beheer.requestinbox;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static nl.procura.burgerzaken.requestinbox.api.RequestInboxUtils.getUserIdFromUrl;
import static nl.procura.burgerzaken.requestinbox.api.RequestInboxUtils.getVrijBRPChannel;
import static nl.procura.burgerzaken.requestinbox.api.RequestInboxUtils.getVrijBRPUser;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.FAULT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INBOX_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INBOX_ENDPOINT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INBOX_PW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INBOX_USERNAME;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.REQUEST_INBOX;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.pos;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import nl.procura.burgerzaken.requestinbox.api.ApiClient;
import nl.procura.burgerzaken.requestinbox.api.DocumentsApi;
import nl.procura.burgerzaken.requestinbox.api.ItemsApi;
import nl.procura.burgerzaken.requestinbox.api.ListItemsRequest;
import nl.procura.burgerzaken.requestinbox.api.ListItemsResponse;
import nl.procura.burgerzaken.requestinbox.api.RequestConfig;
import nl.procura.burgerzaken.requestinbox.api.TokenApi;
import nl.procura.burgerzaken.requestinbox.api.TokenApi.TokenRequest;
import nl.procura.burgerzaken.requestinbox.api.UpdateItemRequest;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItem;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus;
import nl.procura.burgerzaken.requestinbox.api.model.InboxSupplementedRequestItem;
import nl.procura.burgerzaken.requestinbox.client.OkHttpRequestInboxClient;
import nl.procura.gba.jpa.personen.db.ZaakAttr;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.zaken.algemeen.ControleerbareService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.AttribuutHistorie;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSBytesContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.proweb.rest.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestInboxService extends AbstractService implements ControleerbareService {

  private RequestInboxItem currentItem;

  private final Cache<String, String> cachedAccessToken = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES)
      .maximumSize(1)
      .build();

  public RequestInboxService() {
    super("Verzoeken");
  }

  public Optional<RequestInboxItem> getMyRecord() {
    if (currentItem != null) {
      return of(currentItem);
    } else {
      try {
        ListItemsRequest request = new ListItemsRequest();
        request.requestHandler(getVrijBRPUser(getServices().getGebruiker().getId()))
            .status(InboxItemStatus.RECEIVED)
            .status(InboxItemStatus.PENDING);
        return getRequestInboxItems(request).getItems().stream().findFirst();
      } catch (Exception exc) {
        log.error("Kan verzoeken service niet bereiken", exc);
        PersonenWsService service = getServices().getPersonenWsService();
        service.addMessage(false, FAULT, ERROR,
            "Kan verzoeken service niet bereiken", exc.getMessage(), exc);
        return empty();
      }
    }
  }

  public void openCurrentInboxItem(String id) {
    this.currentItem = getRequestInboxItem(id);
    callListeners(null);
  }

  public RequestInboxItem getCurrentInboxItem() {
    return currentItem;
  }

  public RequestInboxItemResult getRequestInboxItems(ListItemsRequest request) {
    RequestInboxItemResult result = new RequestInboxItemResult();
    return getItems(request)
        .map(response -> {
          result.setTotalItems(response.getTotalItems());
          result.setItems(response.getItems().stream()
              .map(this::toRequestInboxItem)
              .collect(toList()));
          return result;
        }).orElse(result);
  }

  public RequestInboxItem getRequestInboxItem(String id) {
    return getItem(id)
        .map(this::toRequestInboxItem)
        .orElse(null);
  }

  public DMSResult getDocuments(RequestInboxItem item) {
    DMSResult dmsResult = new DMSResult();
    item.getInboxItem().getDocuments().forEach(document -> {
      String documentDmsType = "";
      dmsResult.getDocuments().add(DMSDocument.builder()
          .contentSupplier(
              () -> DMSBytesContent.fromFilename(document.getOriginalName(), getDocumentContent(document.getId())))
          .title(document.getOriginalName())
          .user("")
          .zaakId(item.getId())
          .documentTypeDescription(documentDmsType)
          .confidentiality("")
          .build());
    });
    return dmsResult;
  }

  public List<RequestInboxItem> getRelatedItems(RequestInboxItem item) {
    List<RequestInboxItem> items = new ArrayList<>();
    item.getInboxItem().getSupplements().forEach(supplement -> {
      items.add(getRequestInboxItem(supplement.getId()));
    });
    InboxSupplementedRequestItem supplementedRequest = item.getInboxItem().getSupplementedRequest();
    if (supplementedRequest != null) {
      items.add(getRequestInboxItem(supplementedRequest.getId()));
    }
    return items;
  }

  public void updateItem(RequestInboxItem item, UpdateItemRequest request) {
    RequestConfig config = new RequestConfig().accessToken(getAccessToken());
    getItemsApi().patchRequest(item.getId(), request, config);
    InboxItem inboxItem = item.getInboxItem();

    // Update values
    ofNullable(request.status()).ifPresent(val -> inboxItem.setStatus(val.getId()));
    ofNullable(request.handlingChannel()).ifPresent(inboxItem::setHandlingChannel);
    ofNullable(request.requestHandler()).ifPresent(inboxItem::setRequestHandler);
    item.setHandlerDescription(getHandlerDescription(inboxItem));

    callListeners(ServiceEvent.CHANGE);
  }

  public Optional<String> getRelatedRequestInboxItemId(Zaak zaak) {
    AttribuutHistorie attribuutHistorie = zaak.getZaakHistorie().getAttribuutHistorie();
    return attribuutHistorie.getAttribuut(REQUEST_INBOX)
        .map(ZaakAttr::getWaarde);
  }

  public void openRelatedRequestInboxItem(Zaak zaak) {
    getRelatedRequestInboxItemId(zaak).ifPresent(this::openCurrentInboxItem);
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    return new RequestInboxControles(this).getControles(listener);
  }

  private RequestInboxItem toRequestInboxItem(InboxItem inboxItem) {
    RequestInboxItem record = new RequestInboxItem(inboxItem);
    record.setId(inboxItem.getId());
    record.setContent(JsonUtils.getPrettyObject(inboxItem.getBody()).getBytes());
    record.setHandlerDescription(getHandlerDescription(inboxItem));

    InboxSupplementedRequestItem supplementedRequest = inboxItem.getSupplementedRequest();
    if (supplementedRequest != null) {
      record.setParentId(supplementedRequest.getId());
    }
    return record;
  }

  private String getHandlerDescription(InboxItem inboxItem) {
    String requestHandler = inboxItem.getRequestHandler();
    if (StringUtils.isBlank(requestHandler)) {
      return "Geen";
    }
    if (requestHandler.contains(getVrijBRPChannel())) {
      long userId = along(getUserIdFromUrl(requestHandler));
      Gebruiker gebruiker = getServices().getGebruikerService().getGebruikerByCode(userId, false);
      return "VrijBRP / " + defaultIfBlank(gebruiker.getNaam(), "Onbekende gebruiker");
    }
    return "Onbekend (" + requestHandler + ")";
  }

  private byte[] getDocumentContent(String id) {
    RequestConfig config = new RequestConfig().accessToken(getAccessToken());
    return getDocumentsApi().getDocumentContent(id, config);
  }

  private Optional<InboxItem> getItem(String id) {
    if (isNotBlank(id) && isEnabled()) {
      RequestConfig config = new RequestConfig().accessToken(getAccessToken());
      return ofNullable(getItemsApi().getItem(id, config));
    }
    return Optional.empty();
  }

  private Optional<ListItemsResponse> getItems(ListItemsRequest request) {
    if (isEnabled()) {
      RequestConfig config = new RequestConfig().accessToken(getAccessToken());
      return ofNullable(getItemsApi().listRequests(request, config));
    }
    return Optional.empty();
  }

  public boolean isEnabled() {
    return pos(getSysteemParm(INBOX_ENABLED, false));
  }

  private ItemsApi getItemsApi() {
    return new ItemsApi(getRequestInboxClient());
  }

  private DocumentsApi getDocumentsApi() {
    return new DocumentsApi(getRequestInboxClient());
  }

  private TokenApi getTokenApi() {
    return new TokenApi(getRequestInboxClient());
  }

  private ApiClient getRequestInboxClient() {
    String url = getSysteemParm(INBOX_ENDPOINT, true);
    return new OkHttpRequestInboxClient(url, Duration.ofSeconds(5));
  }

  private String getAccessToken() {
    return cachedAccessToken.get("token", key -> {
      String un = getSysteemParm(INBOX_USERNAME, true);
      String pw = getSysteemParm(INBOX_PW, true);
      TokenRequest tokenRequest = new TokenRequest(un, pw);
      return getTokenApi().getToken(tokenRequest);
    });
  }
}
