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

package nl.procura.burgerzaken.requestinbox.api;

import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
public class ListItemsRequest {

  private List<InboxItemStatus> statusses         = new ArrayList<>();
  private List<String>          types             = new ArrayList<>();
  private List<String>          requestHandlers   = new ArrayList<>();
  private List<String>          handlingChannels  = new ArrayList<>();
  private List<String>          customers         = new ArrayList<>();
  private List<String>          preferredChannels = new ArrayList<>();

  private int page         = 1;
  private int itemsPerPage = 3;

  public ListItemsRequest status(InboxItemStatus status) {
    ofNullable(status).ifPresent(this.statusses::add);
    return this;
  }

  public ListItemsRequest preferredChannel(String preferredChannel) {
    ofNullable(preferredChannel).ifPresent(this.preferredChannels::add);
    return this;
  }

  public ListItemsRequest requestHandler(String value) {
    ofNullable(value).ifPresent(this.requestHandlers::add);
    return this;
  }

  public ListItemsRequest handlingChannel(String value) {
    ofNullable(value).ifPresent(this.handlingChannels::add);
    return this;
  }

  public ListItemsRequest type(String type) {
    ofNullable(type).ifPresent(this.types::add);
    return this;
  }

  public ListItemsRequest customer(String customer) {
    ofNullable(customer).ifPresent(this.customers::add);
    return this;
  }
}
