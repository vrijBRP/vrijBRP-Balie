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

import nl.procura.burgerzaken.requestinbox.api.model.InboxItemTypeName;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.adresonderzoek.InboxAdresOnderzoekProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.afschriftbs.InboxAfschriftbsProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.betaling.InboxBetalingProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.onbekend.InboxOnbekendProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.reisdocument.InboxReisdocumentProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.stempas.InboxStempasProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.verloren_reisdoc.InboxVerlorenReisdocumentProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.vog.InboxVogProcessor;

public class RequestInboxProcessors {

  public static RequestInboxBodyProcessor get(RequestInboxItem record, Services services) {
    InboxItemTypeName inboxVerwerkingType = InboxItemTypeName.getByName(record.getTypeName());
    switch (inboxVerwerkingType) {
      case VRIJBRP_VOTING_CARD:
        return new InboxStempasProcessor(record, services);
      case VRIJBRP_TRAVEL_DOCUMENT:
        return new InboxReisdocumentProcessor(record, services);
      case VRIJBRP_PAYMENT:
        return new InboxBetalingProcessor(record, services);
      case VRIJBRP_ADDRESS_RESEARCH:
        return new InboxAdresOnderzoekProcessor(record, services);
      case VRIJBRP_CERTIFICATE_OF_CONDUCT:
        return new InboxVogProcessor(record, services);
      case VRIJBRP_CIVIL_RECORD:
        return new InboxAfschriftbsProcessor(record, services);
      case VRIJBRP_LOST_TRAVEL_DOCUMENT:
        return new InboxVerlorenReisdocumentProcessor(record, services);
      case UNKNOWN:
        break;
    }
    return new InboxOnbekendProcessor(record, services);
  }
}
