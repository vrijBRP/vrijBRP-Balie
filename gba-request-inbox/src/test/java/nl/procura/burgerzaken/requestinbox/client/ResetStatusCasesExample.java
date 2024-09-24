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

package nl.procura.burgerzaken.requestinbox.client;

import static nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus.RECEIVED;

import java.time.Duration;

import nl.procura.burgerzaken.requestinbox.api.ApiClient;
import nl.procura.burgerzaken.requestinbox.api.DocumentsApi;
import nl.procura.burgerzaken.requestinbox.api.ItemsApi;
import nl.procura.burgerzaken.requestinbox.api.ListItemsRequest;
import nl.procura.burgerzaken.requestinbox.api.ListItemsResponse;
import nl.procura.burgerzaken.requestinbox.api.RequestConfig;
import nl.procura.burgerzaken.requestinbox.api.TokenApi;
import nl.procura.burgerzaken.requestinbox.api.TokenApi.TokenRequest;
import nl.procura.burgerzaken.requestinbox.api.UpdateItemRequest;
import nl.procura.burgerzaken.requestinbox.api.model.InboxDocument;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItem;

import lombok.SneakyThrows;

public class ResetStatusCasesExample {

  public static void main(String[] args) {
    new ResetStatusCasesExample();
  }

  @SneakyThrows
  public ResetStatusCasesExample() {
    // SET ENV VARS: USER, PW, URL
    String url = System.getenv("URL");
    String user = System.getenv("USER");
    String pw = System.getenv("PW");

    System.out.println("url = " + url);
    System.out.println("user = " + user);
    System.out.println("pw = " + pw);

    ApiClient apiClient = new OkHttpRequestInboxClient(url, Duration.ofSeconds(10));

    TokenRequest tokenRequest = new TokenRequest(user, pw);
    String token = new TokenApi(apiClient).getToken(tokenRequest);
    RequestConfig config = new RequestConfig().accessToken(token);
    ItemsApi itemsApi = new ItemsApi(apiClient);
    DocumentsApi documentsApi = new DocumentsApi(apiClient);

    ListItemsRequest request = new ListItemsRequest();
    request.page(1).itemsPerPage(30);

    ListItemsResponse response = itemsApi.listRequests(request, config);
    System.out.println("Total items: " + response.getTotalItems());
    System.out.println("Requests: " + response.getItems().size());

    for (InboxItem inboxRequest : response.getItems()) {
      System.out.println("inboxRequest.getId() = " + inboxRequest.getId());
      System.out.println("inboxRequest.getSource() = " + inboxRequest.getSource());
      System.out.println("inboxRequest.getRegisteredOn() = " + inboxRequest.getRegisteredOn());
      System.out.println("inboxRequest.getPreferredChannel() = " + inboxRequest.getPreferredChannel());
      System.out.println("inboxRequest.getDescription() = " + inboxRequest.getDescription());
      System.out.println("inboxRequest.getStatus() = " + inboxRequest.getStatus());
      System.out.println("inboxRequest.getType() = " + inboxRequest.getType());
      System.out.println("inboxRequest.getCustomers() = " + inboxRequest.getCustomers());
      System.out.println("inboxRequest.getWithdraws() = " + inboxRequest.getWithdraws());
      System.out.println("inboxRequest.getSupplements() = " + inboxRequest.getSupplements());
      //System.out.println("inboxRequest.getSupplementedRequest() = " + inboxRequest.getSupplementedRequest());
      System.out.println("inboxRequest.getBody() = " + inboxRequest.getBody());
      System.out.println("inboxRequest.getDocuments() = " + inboxRequest.getDocuments());

      for (InboxDocument inboxDocument : inboxRequest.getDocuments()) {
        byte[] content = documentsApi.getDocumentContent(inboxDocument.getId(), config);
        System.out.println("inboxDocument.getId() = " + inboxDocument.getId());
        System.out.println("inboxDocument.content() = " + content.length);
      }

      resetStatus(inboxRequest, itemsApi, config);
    }
  }

  private static void resetStatus(InboxItem inboxRequest, ItemsApi requestApi, RequestConfig config) {
    UpdateItemRequest updateRequestRequest = new UpdateItemRequest();
    updateRequestRequest.status(RECEIVED);
    requestApi.patchRequest(inboxRequest.getId(), updateRequestRequest, config);
  }
}
