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
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.TellingTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTellingType;

import ch.lambdaj.group.Group;

public class TellingBron extends TellingTemplate {

  public TypeBron get(List<Zaak> zaken) {

    TypeBron bron = new TypeBron(zaken);

    Group<Zaak> locGroups = getGroupsByBron(zaken);

    for (Group<Zaak> locGroup : locGroups.subgroups()) {

      String oms = (String) locGroup.key();
      String bronOms = fil(oms) ? oms : "Onbekend";

      BronType bronType = new BronType(bronOms, locGroup.findAll());

      for (Group<Zaak> type : getGroupsByType(locGroup.findAll()).subgroups()) {
        ZaakType zaakType = (ZaakType) type.key();
        bronType.getTypes().add(new DashboardTellingType(zaakType.getOms(), type.findAll()));
      }

      bronType.getTypes().sort(new TypeSorter());
      bron.getBronnen().add(bronType);
    }

    return bron;
  }

  private Group<Zaak> getGroupsByBron(List<Zaak> zaken) {
    return group(zaken, by(on(Zaak.class).getBron()));
  }

  public static class BronType extends DashboardTellingType {

    private final List<DashboardTellingType> types = new ArrayList<>();

    public BronType() {
    }

    private BronType(String oms, List<Zaak> zaken) {
      super(oms, zaken);
    }

    public List<DashboardTellingType> getTypes() {
      return types;
    }
  }

  public static class TypeBron extends DashboardTellingType<String, Zaak> {

    private final List<BronType> bronnen = new ArrayList<>();

    public TypeBron() {
    }

    public TypeBron(List<Zaak> zaken) {
      super("Bron", zaken);
    }

    public List<BronType> getBronnen() {
      return bronnen;
    }
  }
}
