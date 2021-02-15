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
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.TellingTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTellingType;

import ch.lambdaj.group.Group;

public class TellingGebruiker extends TellingTemplate {

  public TypeGebruiker get(List<Zaak> zaken) {

    TypeGebruiker gebruiker = new TypeGebruiker(zaken);

    Group<Zaak> gebrGroups = getGroupsByGebruiker(zaken);

    for (Group<Zaak> gebrGroup : gebrGroups.subgroups()) {

      UsrFieldValue usr = (UsrFieldValue) gebrGroup.key();

      String oms = fil(usr.getDescription()) ? usr.getDescription() : "Onbekend";

      GebruikerType gebruikerType = new GebruikerType(oms, gebrGroup.findAll());

      for (Group<Zaak> type : getGroupsByType(gebrGroup.findAll()).subgroups()) {

        ZaakType zaakType = (ZaakType) type.key();

        gebruikerType.getTypes().add(new DashboardTellingType(zaakType.getOms(), type.findAll()));
      }

      gebruikerType.getTypes().sort(new TypeSorter());

      gebruiker.getGebruikers().add(gebruikerType);
    }

    return gebruiker;
  }

  private Group<Zaak> getGroupsByGebruiker(List<Zaak> zaken) {
    return group(zaken, by(on(Zaak.class).getIngevoerdDoor()));
  }

  public static class GebruikerType extends DashboardTellingType {

    private final List<DashboardTellingType> types = new ArrayList<>();

    public GebruikerType() {
    }

    private GebruikerType(String oms, List<Zaak> zaken) {
      super(oms, zaken);
    }

    public List<DashboardTellingType> getTypes() {
      return types;
    }
  }

  public static class TypeGebruiker extends DashboardTellingType<String, Zaak> {

    private final List<GebruikerType> gebruikers = new ArrayList<>();

    public TypeGebruiker() {
    }

    public TypeGebruiker(List<Zaak> zaken) {
      super("Gebruiker", zaken);
    }

    public List<GebruikerType> getGebruikers() {
      return gebruikers;
    }
  }
}
