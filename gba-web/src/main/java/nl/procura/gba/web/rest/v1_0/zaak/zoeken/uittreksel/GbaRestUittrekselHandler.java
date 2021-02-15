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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.uittreksel;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaakPersoon;

public class GbaRestUittrekselHandler extends GbaRestElementHandler {

  public GbaRestUittrekselHandler(Services services) {
    super(services);
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    DocumentZaak zaak = (DocumentZaak) zaakParm;

    GbaRestElement uitt = gbaZaak.add(UITTREKSEL);

    DocumentRecord doc = zaak.getDoc();
    add(uitt, DOCUMENT_CODE, doc.getCDocument(), doc.getDocument(), doc.getOmschrijving());
    add(uitt, DOCUMENT_AFNEMER, zaak.getDocumentAfn());
    add(uitt, DOCUMENT_DOEL, zaak.getDocumentDoel());

    if (zaak.getPersonen().size() > 0) {

      GbaRestElement deelZaken = gbaZaak.add(DEELZAKEN);

      for (DocumentZaakPersoon persoon : zaak.getPersonen()) {

        GbaRestElement deelZaak = deelZaken.add(DEELZAAK);

        add(deelZaak, BSN, persoon.getBurgerServiceNummer());
        add(deelZaak, ANR, persoon.getAnummer());
      }
    }
  }
}
