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

package nl.procura.burgerzaken.requestinbox.api.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class InboxItem {

  private String                       id;
  private String                       source;
  private OffsetDateTime               registeredOn;
  private String                       preferredChannel;
  private String                       description;
  private String                       status;
  private String                       requestHandler;
  private String                       handlingChannel;
  private InboxItemType                type;
  private List<InboxItemCustomer>      customers;
  private Object                       withdrawRequest;
  private List<Object>                 withdraws;
  private List<InboxSupplementItem>    supplements;
  private InboxSupplementedRequestItem supplementedRequest;
  private Map<String, Object>          body;
  private List<InboxDocument>          documents;
}
