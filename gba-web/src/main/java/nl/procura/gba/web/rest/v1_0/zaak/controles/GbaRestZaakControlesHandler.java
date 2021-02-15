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

package nl.procura.gba.web.rest.v1_0.zaak.controles;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.ControleerbareService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controle;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ZaakControle;

/**
 * Vertaald de zaakcontroles naar GBA rest elementen
 */
public class GbaRestZaakControlesHandler extends GbaRestHandler {

  public GbaRestZaakControlesHandler(Services services) {
    super(services);
  }

  public List<GbaRestElement> getControles() {

    List<GbaRestElement> antwoord = new ArrayList<>();

    GbaRestElement elementen = new GbaRestElement(GbaRestElementType.CONTROLES);

    antwoord.add(elementen);

    Controles controles = getZaakControles();

    for (Controle controle : controles) {

      GbaRestElement element = elementen.add(GbaRestElementType.CONTROLE);

      StringBuilder oms = new StringBuilder();
      oms.append(controle.getOnderwerp());
      oms.append(" - ");
      oms.append(controle.getId());
      oms.append(" - ");
      oms.append(controle.getOmschrijving());

      GbaRestElementHandler.add(element, OMSCHRIJVING, oms.toString());
      GbaRestElementHandler.add(element, OPMERKINGEN, controle.getOpmerkingenString());
      GbaRestElementHandler.add(element, ZAAK_AANGEPAST, controle.isGewijzigd());

      if (controle instanceof ZaakControle) {

        ZaakControle zc = (ZaakControle) controle;

        Zaak zaak = zc.getZaak();
        DateTime dt = zaak.getDatumTijdInvoer();
        ZaakStatusType status = zaak.getStatus();
        ZaakType zaakType = zaak.getType();

        GbaRestElementHandler.add(element, DATUM_INVOER, dt);
        GbaRestElementHandler.add(element, TIJD_INVOER, dt.getLongTime(), dt.getFormatTime());
        GbaRestElementHandler.add(element, TYPE, zaakType.getCode(), zaakType.getOms());
        GbaRestElementHandler.add(element, STATUS, status.getCode(), status.getOms());
        GbaRestElementHandler.add(element, ZAAKID, zaak.getZaakId());
      } else {

        GbaRestElementHandler.add(element, DATUM_INVOER, "");
        GbaRestElementHandler.add(element, TIJD_INVOER, "");
        GbaRestElementHandler.add(element, TYPE, "");
        GbaRestElementHandler.add(element, STATUS, "");
        GbaRestElementHandler.add(element, ZAAKID, "");
      }
    }

    return antwoord;
  }

  private Controles getZaakControles() {

    Controles controles = new Controles();
    for (ControleerbareService zdb : getServices().getServices(ControleerbareService.class)) {
      controles.addGewijzigdeControles(zdb.getControles(null));
    }

    return controles;
  }
}
