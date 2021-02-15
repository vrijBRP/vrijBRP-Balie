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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.gv;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;

public class GbaRestGvHandler extends GbaRestElementHandler {

  public GbaRestGvHandler(Services services) {
    super(services);
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    GvAanvraag zaak = (GvAanvraag) zaakParm;

    GbaRestElement gv = gbaZaak.add(GEGEVENSVERSTREKKING);
    GbaRestElement verzoek = gv.add(VERZOEK);
    add(verzoek, TOEGEKEND, zaak.getToekenningType().getCode(), zaak.getToekenningType().getOms());

    GbaRestElement addressering = verzoek.add(ADDRESSERING);
    add(addressering, NAAM, zaak.getAanvrager());

    GbaRestElement deelZaken = gbaZaak.add(GbaRestElementType.DEELZAKEN);
    GbaRestElement deelZaak = deelZaken.add(GbaRestElementType.DEELZAAK);

    add(deelZaak, GbaRestElementType.BSN, zaak.getBurgerServiceNummer());
    add(deelZaak, GbaRestElementType.ANR, zaak.getAnummer());
  }
}
