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

package nl.procura.diensten.gba.ple.procura.arguments;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;

import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

import nl.procura.burgerzaken.gba.core.enums.GBACat;

public class PLEArgsLogger {

  private Map<String, Object> map = new LinkedHashMap<>();

  public PLEArgsLogger(PLEArgs a) {
    put("toonArchief", a.isShowArchives());
    put("toonHistorie", a.isShowHistory());
    put("toonOpgeschort", a.isShowSuspended());
    put("toonMutaties", a.isShowMutations());
    put("zoekIndicaties", a.isSearchIndications());
    put("opvraagredenIndicaties", a.getReasonForIndications());
    put("zoekOpAdres", a.isSearchOnAddress());
    put("databron", a.getDatasource());
    put("maxFindCount", a.getMaxFindCount());
    put("categorieen", a.getCategories().size() > 0 ? a.getCategories() : EnumSet.allOf(GBACat.class));
    put("nummers", a.getNumbers());
    put("customTemplate", a.getCustomTemplate());
    put("geslachtsnaam", a.getGeslachtsnaam());
    put("voornaam", a.getVoornaam());
    put("voorvoegsel", a.getVoorvoegsel());
    put("geslacht", a.getGeslacht());
    put("geboortedatum", a.getGeboortedatum());
    put("titel", a.getTitel());
    put("straat", a.getStraat());
    put("huisnummer", a.getHuisnummer());
    put("postcode", a.getPostcode());
    put("gemeentedeel", a.getGemeentedeel());
    put("gemeente", a.getGemeente());
  }

  private void put(String key, Object value) {

    if (value == null || (value instanceof String && emp(astr(value)))) {
      value = "-";
    }

    if (value instanceof Collection) {
      StringBuilder sb = new StringBuilder();

      for (Object x : ((Collection<?>) value)) {
        sb.append(x);
        sb.append(", ");
      }

      value = sb.toString();
    }

    map.put(key, astr(value));
  }

  public Map<String, Object> getMap() {
    return map;
  }

  public void setMap(Map<String, Object> map) {
    this.map = map;
  }
}
