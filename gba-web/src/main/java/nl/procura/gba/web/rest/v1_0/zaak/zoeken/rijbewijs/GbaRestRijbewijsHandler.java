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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.rijbewijs;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.rijbewijs.*;

public class GbaRestRijbewijsHandler extends GbaRestElementHandler {

  public GbaRestRijbewijsHandler(Services services) {
    super(services);
  }

  private static void addDeelzaken(GbaRestElement gbaZaak, RijbewijsAanvraag zaak) {
    GbaRestElement deelZaken = gbaZaak.add(DEELZAKEN);
    GbaRestElement deelZaak = deelZaken.add(DEELZAAK);

    add(deelZaak, BSN, zaak.getBurgerServiceNummer());
    add(deelZaak, ANR, zaak.getAnummer());
  }

  private static void addLocaties(RijbewijsAanvraag zaak, GbaRestElement rijbewijs) {
    Locatie la = zaak.getLocatieAfhaal();
    Locatie li = zaak.getLocatieInvoer();

    add(rijbewijs, GbaRestElementType.LOCATIE_AFHAAL, la.getCLocation(), la.getLocatie(), la.getOmschrijving());
    add(rijbewijs, GbaRestElementType.LOCATIE_INVOER, li.getCLocation(), li.getLocatie(), li.getOmschrijving());
  }

  private static void addRdwStatussen(RijbewijsAanvraag zaak, GbaRestElement rijbewijs) {

    GbaRestElement elementen = rijbewijs.add(GbaRestElementType.RDW_STATUSSEN);

    for (RijbewijsAanvraagStatus rdwStatus : zaak.getStatussen().getStatussen()) {

      DateTime dt = rdwStatus.getDatumTijd();
      RijbewijsStatusType status = rdwStatus.getStatus();
      UsrFieldValue gebruiker = rdwStatus.getGebruiker();

      GbaRestElement element = elementen.add(GbaRestElementType.RDW_STATUS);
      add(element, DATUM_INVOER, dt.getLongDate(), dt.getFormatDate());
      add(element, TIJD_INVOER, dt.getLongTime(), dt.getFormatTime());
      add(element, OPMERKINGEN, rdwStatus.getOpmerkingen());
      add(element, GEBRUIKER, gebruiker.getValue(), gebruiker.getDescription());
      add(element, STATUS, status.getCode(), status.getOms());
    }
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    RijbewijsAanvraag zaak = (RijbewijsAanvraag) zaakParm;
    GbaRestElement rijbewijs = gbaZaak.add(GbaRestElementType.RIJBEWIJS);

    RijbewijsAanvraagReden redenAanvraag = zaak.getRedenAanvraag();
    RijbewijsAanvraagSoort soortAanvraag = zaak.getSoortAanvraag();

    add(rijbewijs, AANVRAAGNUMMER, zaak.getAanvraagNummer());
    add(rijbewijs, CODE_VERBLIJFSTITEL, zaak.getCodeVerblijfstitel());
    add(rijbewijs, DATUM_VERTREK, zaak.getDatumVertrek());
    add(rijbewijs, DATUM_VESTIGING, zaak.getDatumVestiging());
    add(rijbewijs, LAND_VERTREK, zaak.getLandVertrek());
    add(rijbewijs, LAND_VESTIGING, zaak.getLandVestiging());
    add(rijbewijs, NATIONALITEITEN, zaak.getNationaliteiten());
    add(rijbewijs, PROCES_VERBAAL, zaak.getProcesVerbaalVerlies());
    add(rijbewijs, RIJBEWIJS_NR, zaak.getRijbewijsnummer());
    add(rijbewijs, SOORT_ID, zaak.getSoortId());
    add(rijbewijs, VERVANGING_RBW_NR, zaak.getVervangingsRbwNr());
    add(rijbewijs, CODE_RAAS, zaak.getCodeRas());
    add(rijbewijs, NAAMGEBRUIK, zaak.getNaamgebruik().getRdwCode(), zaak.getNaamgebruik().getOms());
    add(rijbewijs, REDEN_AANVRAAG, redenAanvraag.getCode(), redenAanvraag.getOms());
    add(rijbewijs, SOORT_AANVRAAG, soortAanvraag.getCode(), soortAanvraag.getOms());

    addLocaties(zaak, rijbewijs);
    addRdwStatussen(zaak, rijbewijs);
    addDeelzaken(gbaZaak, zaak);
  }
}
