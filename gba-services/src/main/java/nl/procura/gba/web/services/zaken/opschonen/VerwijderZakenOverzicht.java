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

package nl.procura.gba.web.services.zaken.opschonen;

import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.BINNENVERHUIZING;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.BUITENVERHUIZING;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.CORRESPONDENTIE;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.EERSTE_INSCHRIJVING;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.EMIGRATIE;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.ERKENNING;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.FORMULIER;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.GEBOORTE;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.GEGEVENSVERSTREKKING;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.GPK;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.HERVESTIGING;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.HUW_GPS;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.INBOX;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.INDICATIE;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.INHOUDING_VERMISSING;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.LEVENLOOS;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.LIJKVINDING;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.LV;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.NAAMGEBRUIK;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.NAAMSKEUZE;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.NATURALISATIE;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.OMZETTING_GPS;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.ONDERZOEK;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.ONTBINDING_HUW_GPS;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.OVERLIJDEN_BUITENLAND;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.OVERLIJDEN_GEMEENTE;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.PL_MUTATIE;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.REISDOCUMENT;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.RIJBEWIJS;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.RISICOANALYSE;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.TERUGMELDING;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.UITTREKSEL;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.VERSTREKKINGSBEPERKING;
import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.VOG;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderVerlopenZaakActie;
import nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType;

public class VerwijderZakenOverzicht {

  private final List<VerwijderZakenActie> acties = new ArrayList<>();

  public VerwijderZakenOverzicht() {
    add(BINNENVERHUIZING, 5, 0);
    add(CORRESPONDENTIE, 10, 0);
    add(EERSTE_INSCHRIJVING, 110, 0);
    add(EMIGRATIE, 10, 0);
    add(ERKENNING, 2, 6);
    add(FORMULIER, 10, 0);
    add(GEBOORTE, 2, 6);
    add(GEGEVENSVERSTREKKING, 20, 0);
    add(GPK, 10, 0);
    add(HERVESTIGING, 5, 0);
    add(HUW_GPS, 12, 0);
    add(INBOX, 1, 0);
    add(INDICATIE, 1, 0);
    add(INHOUDING_VERMISSING, 16, 0);
    add(BUITENVERHUIZING, 5, 0);
    add(LV, 2, 6);
    add(LEVENLOOS, 2, 6);
    add(LIJKVINDING, 2, 6);
    add(NAAMGEBRUIK, 5, 0);
    add(NAAMSKEUZE, 2, 6);
    add(NATURALISATIE, 12, 0);
    add(OMZETTING_GPS, 2, 6);
    add(ONDERZOEK, 10, 0);
    add(ONTBINDING_HUW_GPS, 2, 6);
    add(OVERLIJDEN_BUITENLAND, 110, 0);
    add(OVERLIJDEN_GEMEENTE, 2, 6);
    add(PL_MUTATIE, 110, 0);
    add(REISDOCUMENT, 16, 0);
    add(RIJBEWIJS, 1, 0);
    add(RISICOANALYSE, 5, 0);
    add(TERUGMELDING, 10, 0);
    add(UITTREKSEL, 20, 0);
    add(VERSTREKKINGSBEPERKING, 5, 0);
    add(VOG, 1, 0);
  }

  public void add(VerwijderZaakType verwijderZaakType, int jaar, int maanden) {
    acties.add(new VerwijderZakenActie(new VerwijderVerlopenZaakActie(verwijderZaakType, jaar, maanden)));
  }

  public List<VerwijderZakenActie> getActies() {
    return acties;
  }

  public VerwijderZakenActie getActie(VerwijderZaakType verwijderZaakType) {
    return acties.stream()
        .filter(actie -> actie.getVerwijderActie().getType() == verwijderZaakType)
        .findFirst()
        .orElse(null);
  }
}
