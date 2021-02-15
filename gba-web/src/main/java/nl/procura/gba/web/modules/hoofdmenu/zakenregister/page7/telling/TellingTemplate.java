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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling;

import static ch.lambdaj.Lambda.*;

import java.util.Comparator;
import java.util.List;

import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTellingType;

import ch.lambdaj.group.Group;

public class TellingTemplate {

  @SuppressWarnings("unused")
  public static <T> List<T> get(Class<T> T, List<Zaak> zaken) {
    return (List<T>) zaken;
  }

  protected static Group<Zaak> getGroupsByType(List<Zaak> zaken) {
    return group(zaken, by(on(Zaak.class).getType()));
  }

  public static class DateSorter implements Comparator<Zaak> {

    @Override
    public int compare(Zaak o1, Zaak o2) {
      long d1 = o1.getDatumTijdInvoer().getLongDate();
      long d2 = o2.getDatumTijdInvoer().getLongDate();
      return Long.compare(d1, d2);
    }
  }

  public static class TypeSorter implements Comparator<DashboardTellingType> {

    @Override
    public int compare(DashboardTellingType o1, DashboardTellingType o2) {
      return o1.getOms().compareTo(o2.getOms());
    }
  }
}
