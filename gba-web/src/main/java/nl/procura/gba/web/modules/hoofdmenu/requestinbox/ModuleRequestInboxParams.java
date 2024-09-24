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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox;

import nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.validation.Bsn;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModuleRequestInboxParams {

  @Builder.Default
  private String title = "";

  @Builder.Default
  private Bsn bsn = new Bsn();

  private InboxItemStatus  status;
  private RequestInboxItem item;

  @Builder.Default
  private Runnable updateListener = () -> {};
}
