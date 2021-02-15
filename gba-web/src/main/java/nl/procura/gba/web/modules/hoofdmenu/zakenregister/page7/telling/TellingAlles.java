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

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTellingType;

import ch.lambdaj.group.Group;

public class TellingAlles extends TellingTemplate {

  public TypeAlles get(Group<Zaak> typeGroups) {

    TypeAlles type = new TypeAlles();

    for (Group<Zaak> typeGroup : typeGroups.subgroups()) {

      ZaakType zaakType = (ZaakType) typeGroup.key();

      type.getTypes().add(new DashboardTellingType(zaakType.getOms(), typeGroup.findAll()));
    }

    type.getTypes().sort(new TypeSorter());

    type.setZaken(typeGroups.findAll());

    return type;
  }

  public static class TypeAlles extends DashboardTellingType {

    private final List<DashboardTellingType> types = new ArrayList<>();

    public List<DashboardTellingType> getTypes() {
      return types;
    }
  }
}
