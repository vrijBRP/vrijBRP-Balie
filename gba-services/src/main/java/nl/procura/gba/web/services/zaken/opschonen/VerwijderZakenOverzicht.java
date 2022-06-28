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

import static nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType.*;

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
