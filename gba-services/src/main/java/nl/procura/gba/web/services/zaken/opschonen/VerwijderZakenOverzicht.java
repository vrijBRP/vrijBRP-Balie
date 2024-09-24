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
    add(UITTREKSEL, 20);
    add(FORMULIER, 10);
    add(VERSTREKKINGSBEPERKING, 5);
    add(NAAMGEBRUIK, 5);
    add(BINNENVERHUIZING, 5);
    add(BUITENVERHUIZING, 5);
    add(EMIGRATIE, 10);
    add(HERVESTIGING, 5);
    add(VOG, 10);
    add(GPK, 5);
    add(REISDOCUMENT, 16);
    add(INHOUDING_VERMISSING, 16);
    add(RIJBEWIJS, 11);
    add(TERUGMELDING, 5);
    add(GEBOORTE, 1);
    add(ERKENNING, 1);
    add(HUW_GPS, 1);
    add(NAAMSKEUZE, 1);
    add(OVERLIJDEN_GEMEENTE, 1);
    add(LIJKVINDING, 1);
    add(OVERLIJDEN_BUITENLAND, 1);
    add(LEVENLOOS, 1);
    add(INDICATIE, 1);
    add(CORRESPONDENTIE, 5);
    add(GEGEVENSVERSTREKKING, 20);
    add(OMZETTING_GPS, 1);
    add(ONTBINDING_HUW_GPS, 1);
    add(INBOX, 1);
    add(ONDERZOEK, 10);
    add(EERSTE_INSCHRIJVING, 110);
    add(RISICOANALYSE, 5);
    add(PL_MUTATIE, 110);
    add(NATURALISATIE, 10);
  }

  public void add(VerwijderZaakType verwijderZaakType, int jaar) {
    acties.add(new VerwijderZakenActie(new VerwijderVerlopenZaakActie(verwijderZaakType, jaar)));
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
