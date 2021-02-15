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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.correspondentie;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieRoute;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieType;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieZaak;

public class GbaRestCorrespondentieHandler extends GbaRestElementHandler {

  public GbaRestCorrespondentieHandler(Services services) {
    super(services);
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    CorrespondentieZaak zaak = (CorrespondentieZaak) zaakParm;

    GbaRestElement gpk = gbaZaak.add(CORRESPONDENTIE);

    CorrespondentieRoute route = zaak.getCorrespondentieRoute();
    CorrespondentieType type = zaak.getCorrespondentieType();

    add(gpk, ROUTE, route.getCode(), route.getOms());
    add(gpk, TYPE, astr(type.getCode()), type.getOms());
    add(gpk, ANDERS, zaak.getAnders());
    add(gpk, GbaRestElementType.TOELICHTING, zaak.getToelichting());

    GbaRestElement deelZaken = gbaZaak.add(DEELZAKEN);
    GbaRestElement deelZaak = deelZaken.add(DEELZAAK);

    add(deelZaak, BSN, zaak.getBurgerServiceNummer());
    add(deelZaak, ANR, zaak.getAnummer());
  }
}
