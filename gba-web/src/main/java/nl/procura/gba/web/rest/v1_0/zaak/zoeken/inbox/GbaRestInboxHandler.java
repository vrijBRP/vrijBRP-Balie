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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.inbox;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.INBOX;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.VERWERKING_ID;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class GbaRestInboxHandler extends GbaRestElementHandler {

  public GbaRestInboxHandler(Services services) {
    super(services);
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    InboxRecord zaak = (InboxRecord) zaakParm;
    GbaRestElement inbox = gbaZaak.add(INBOX);
    add(inbox, VERWERKING_ID, zaak.getVerwerkingId());
  }
}
