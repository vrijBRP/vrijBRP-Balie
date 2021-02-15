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

import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2.Page2ProfielenMap;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.TellingTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTellingType;

import ch.lambdaj.group.Group;

public class TellingProfiel extends TellingTemplate {

  public TypeProfiel get(List<Zaak> zaken) {

    TypeProfiel profiel = new TypeProfiel(zaken);

    Page2ProfielenMap map = new Page2ProfielenMap();

    Map<String, List<Zaak>> groups = new TreeMap<>();

    for (Zaak zaak : zaken) {

      String profielen = map.getProfielen(zaak.getIngevoerdDoor()).replaceAll(",", " en ");

      if (!groups.containsKey(profielen)) {
        groups.put(profielen, new ArrayList<>());
      }

      groups.get(profielen).add(zaak);
    }

    for (Entry<String, List<Zaak>> profGroup : groups.entrySet()) {

      String prof = profGroup.getKey();

      String oms = fil(prof) ? prof : "Onbekend";

      ProfielType profielType = new ProfielType(oms, profGroup.getValue());

      for (Group<Zaak> type : getGroupsByType(profGroup.getValue()).subgroups()) {

        ZaakType zaakType = (ZaakType) type.key();

        profielType.getTypes().add(new DashboardTellingType(zaakType.getOms(), type.findAll()));
      }

      profielType.getTypes().sort(new TypeSorter());

      profiel.getProfielen().add(profielType);
    }

    return profiel;
  }

  public static class ProfielType extends DashboardTellingType {

    private final List<DashboardTellingType> types = new ArrayList<>();

    public ProfielType() {
    }

    private ProfielType(String oms, List<Zaak> zaken) {
      super(oms, zaken);
    }

    public List<DashboardTellingType> getTypes() {
      return types;
    }
  }

  public static class TypeProfiel extends DashboardTellingType<String, Zaak> {

    private final List<ProfielType> profielen = new ArrayList<>();

    public TypeProfiel() {
    }

    public TypeProfiel(List<Zaak> zaken) {
      super("Profiel", zaken);
    }

    public List<ProfielType> getProfielen() {
      return profielen;
    }
  }
}
