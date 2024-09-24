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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.GsonBuilder;

import nl.procura.burgerzaken.requestinbox.api.ListItemsRequest;
import nl.procura.burgerzaken.requestinbox.api.ListItemsResponse;
import nl.procura.burgerzaken.requestinbox.api.RequestConfig;
import nl.procura.burgerzaken.requestinbox.api.UpdateItemRequest;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItem;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus;

public class ItemsApiTest extends AbstractTestWireMockClient {

  @Test
  public void mustReturnItems() {
    ListItemsRequest request = new ListItemsRequest();
    request.status(InboxItemStatus.RECEIVED);
    RequestConfig config = new RequestConfig();
    config.accessToken("myToken");
    ListItemsResponse response = getItemsApi().listRequests(request, config);
    Assertions.assertEquals(2, response.getTotalItems());
    InboxItem item = response.getItems().get(0);
    System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(item));
    Assertions.assertEquals("018c8194-77bb-7921-81c7-4247c0ed62a9", item.getId());
    Assertions.assertEquals("Test 1", item.getDescription());
    Assertions.assertEquals("received", item.getStatus());
    Assertions.assertEquals("pip", item.getSource());
    Assertions.assertEquals("vrijbrp", item.getPreferredChannel());
    Assertions.assertEquals("2023-12-19T11:17:19+01:00", item.getRegisteredOn().toString());
    Assertions.assertEquals("[]", item.getSupplements().toString());
    Assertions.assertEquals("https://vrijbrp.nl/bsn/123456789", item.getCustomers().get(0).getCustomer());
    Assertions.assertEquals("initiator", item.getCustomers().get(0).getRole());
    Assertions.assertEquals("authorized_representative", item.getCustomers().get(0).getAuthorizationIndication());
    Assertions.assertEquals("VotingCard", item.getType().getName());
    Assertions.assertEquals("[]", item.getDocuments().toString());
    Assertions.assertEquals("[]", item.getWithdraws().toString());
    //Assertions.assertNull(item.getSupplementedRequest());
    Assertions.assertNull(item.getWithdrawRequest());
  }

  @Test
  public void mustPatchItem() {
    RequestConfig config = new RequestConfig();
    config.accessToken("myToken");
    String id = "018cd3c7-4adb-7b45-93db-8fdb62a501f9";
    UpdateItemRequest request = new UpdateItemRequest().status(InboxItemStatus.HANDLED);
    InboxItem item = getItemsApi().patchRequest(id, request, config);
    Assertions.assertEquals("018cd3c7-4adb-7b45-93db-8fdb62a501f9", item.getId());
  }
}
