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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.eigenschap;

import static ch.lambdaj.Lambda.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.TellingTemplate;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTellingType;

import ch.lambdaj.group.Group;

public class TellingLocatie extends TellingTemplate {

  public TypeLocatie get(List<Zaak> zaken) {

    TypeLocatie locatie = new TypeLocatie(zaken);

    Group<Zaak> locGroups = getGroupsByLocatie(zaken);

    for (Group<Zaak> locGroup : locGroups.subgroups()) {

      Locatie loc = (Locatie) locGroup.key();

      String oms = loc != null ? loc.getLocatie() : "Onbekend";

      LocatieType locType = new LocatieType(oms, locGroup.findAll());

      for (Group<Zaak> type : getGroupsByType(locGroup.findAll()).subgroups()) {

        ZaakType zaakType = (ZaakType) type.key();

        locType.getTypes().add(new DashboardTellingType(zaakType.getOms(), type.findAll()));
      }

      locType.getTypes().sort(new TypeSorter());

      locatie.getLocaties().add(locType);
    }

    return locatie;
  }

  private Group<Zaak> getGroupsByLocatie(List<Zaak> zaken) {
    return group(zaken, by(on(Zaak.class).getLocatieInvoer()));
  }

  public static class LocatieType extends DashboardTellingType {

    private final List<DashboardTellingType> types = new ArrayList<>();

    public LocatieType() {
    }

    private LocatieType(String oms, List<Zaak> zaken) {
      super(oms, zaken);
    }

    public List<DashboardTellingType> getTypes() {
      return types;
    }
  }

  public static class TypeLocatie extends DashboardTellingType<String, Zaak> {

    private final List<LocatieType> locaties = new ArrayList<>();

    public TypeLocatie() {
    }

    public TypeLocatie(List<Zaak> zaken) {
      super("Locatie", zaken);
    }

    public List<LocatieType> getLocaties() {
      return locaties;
    }
  }
}
