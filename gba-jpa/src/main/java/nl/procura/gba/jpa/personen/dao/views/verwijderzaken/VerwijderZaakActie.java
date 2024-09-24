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

package nl.procura.gba.jpa.personen.dao.views.verwijderzaken;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;

public abstract class VerwijderZaakActie<T> {

  public abstract int verwijder();

  public abstract List<T> getResultaten(int maxAantal);

  public abstract long getAantal();

  public abstract List<VerwijderZakenQuery> getQueries();

  protected static int toNumericDate(int year, int months) {
    return Integer.parseInt(LocalDate.now()
        .minusYears(year)
        .minusMonths(months)
        .format(DateTimeFormatter.ofPattern("yyyyMMdd")));
  }

  protected static List<ZaakKey> toZaakKeys(List<String> zaakIds, ZaakType zaakType) {
    return zaakIds.stream().map(zaakId -> new ZaakKey(zaakId, zaakType)).collect(Collectors.toList());
  }
}
