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

package nl.procura.gba.web.services.bs.algemeen.persoon;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MOEDER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.VADER_DUO_MOEDER;
import static nl.procura.standard.Globalfunctions.*;

import java.util.Comparator;
import java.util.Objects;

import nl.procura.gba.web.services.gba.functies.Geslacht;

public class DossierPersoonComparators {

  private static final int HIGHER = -1;
  private static final int LOWER  = 1;

  public static Comparator<DossierPersoon> getDefault() {
    return (o1, o2) -> {
      // Fallback sorting that is the default
      if (o1.getDossierPersoonType().is(VADER_DUO_MOEDER, MOEDER) &&
          o2.getDossierPersoonType().is(VADER_DUO_MOEDER, MOEDER)) {

        // Always female parent first
        boolean manOfDuoMoeder1 = (Geslacht.MAN == o1.getGeslacht() ||
            o1.getDossierPersoonType().is(VADER_DUO_MOEDER));

        boolean manOfDuoMoeder2 = (Geslacht.MAN == o2.getGeslacht() ||
            o2.getDossierPersoonType().is(VADER_DUO_MOEDER));

        if (o1.getGeslacht() == Geslacht.VROUW && manOfDuoMoeder2) {
          return HIGHER;
        }

        if (o1.getGeslacht() == Geslacht.MAN && manOfDuoMoeder1) {
          return LOWER;
        }
      }

      if (fil(o1.getGeslachtsnaam()) && emp(o2.getGeslachtsnaam())) {
        return HIGHER;

      } else if (emp(o1.getGeslachtsnaam()) && fil(o2.getGeslachtsnaam())) {
        return LOWER;

      } else if (o1.getDossierPersoonType() != o2.getDossierPersoonType()) {
        return getCompare(o1.getDossierPersoonType().getCode(), o2.getDossierPersoonType().getCode());

      } else if (!Objects.equals(o1.getVolgorde(), o2.getVolgorde())) {
        return getCompare(o1.getVolgorde(), o2.getVolgorde());
      }

      return getByIndex().compare(o1, o2);
    };
  }

  public static Comparator<DossierPersoon> getByIndex() {
    return (o1, o2) -> {
      if (!Objects.equals(o1.getVolgorde(), o2.getVolgorde())) {
        return getCompare(o1.getVolgorde(), o2.getVolgorde());
      }
      return getCompare(o1.getCode(), o2.getCode());
    };
  }

  private static int getCompare(Long code, Long code2) {
    return Long.compare(along(code), along(code2));
  }
}
