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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.gpk;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.GPK;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.gpk.GpkAanvraag;

public class GbaRestGpkHandler extends GbaRestElementHandler {

  public GbaRestGpkHandler(Services services) {
    super(services);
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    GpkAanvraag zaak = (GpkAanvraag) zaakParm;

    GbaRestElement gpk = gbaZaak.add(GPK);

    add(gpk, GbaRestElementType.NUMMER, zaak.getNr());
    add(gpk, GbaRestElementType.KAART_TYPE, zaak.getKaart().getCode(), zaak.getKaart().getOms());
    add(gpk, GbaRestElementType.DATUM_PRINT, zaak.getDatumPrint().getLongDate(),
        zaak.getDatumPrint().getFormatDate());
    add(gpk, GbaRestElementType.DATUM_VERVAL, zaak.getDatumVerval().getLongDate(),
        zaak.getDatumVerval().getFormatDate());
    add(gpk, GbaRestElementType.AFGEVER, zaak.getAfgever());

    GbaRestElement deelZaken = gbaZaak.add(GbaRestElementType.DEELZAKEN);
    GbaRestElement deelZaak = deelZaken.add(GbaRestElementType.DEELZAAK);

    add(deelZaak, GbaRestElementType.BSN, zaak.getBurgerServiceNummer());
    add(deelZaak, GbaRestElementType.ANR, zaak.getAnummer());
  }
}
