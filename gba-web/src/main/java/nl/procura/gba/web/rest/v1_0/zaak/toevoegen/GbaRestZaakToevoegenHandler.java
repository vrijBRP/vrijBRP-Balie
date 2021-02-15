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

package nl.procura.gba.web.rest.v1_0.zaak.toevoegen;

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.correspondentie.GbaRestCorrespondentieToevoegenHandler;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox.GbaRestInboxToevoegenHandler;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.uittreksel.GbaRestUittrekselToevoegenHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.standard.exceptions.ProException;

public class GbaRestZaakToevoegenHandler extends GbaRestHandler {

  public GbaRestZaakToevoegenHandler(Services services) {
    super(services);
  }

  public List<GbaRestElement> toevoegen(GbaRestZaakToevoegenVraag vraag) {

    List<GbaRestElement> zaken = new ArrayList<>();

    switch (vraag.getZaakType()) {
      case CORRESPONDENTIE:
        zaken.addAll(new GbaRestCorrespondentieToevoegenHandler(getServices()).toevoegen(vraag));
        break;

      case INBOX:
        zaken.addAll(new GbaRestInboxToevoegenHandler(getServices()).toevoegen(vraag));
        break;

      case UITTREKSEL:
        zaken.addAll(new GbaRestUittrekselToevoegenHandler(getServices()).toevoegen(vraag));
        break;

      case INDICATIE:
      case INHOUD_VERMIS:
      case LEVENLOOS_GEBOREN_KIND:
      case LIJKVINDING:
      case NAAMGEBRUIK:
      case OVERLIJDEN_IN_BUITENLAND:
      case OVERLIJDEN_IN_GEMEENTE:
      case REISDOCUMENT:
      case RIJBEWIJS:
      case TERUGMELDING:
      case COVOG:
      case ERKENNING:
      case NAAMSKEUZE:
      case GEBOORTE:
      case GPK:
      case HUWELIJK_GPS_GEMEENTE:
      case VERHUIZING:
      case VERSTREKKINGSBEPERKING:
        throw new ProException(ERROR, vraag.getZaakType() + " is nog niet uitgewerkt");

      case ONBEKEND:
      default:
        throw new ProException(ERROR, "Onbekende zaaktype:  " + vraag.getZaakType());
    }

    return zaken;
  }
}
