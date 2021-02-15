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

package nl.procura.diensten.gba.wk.procura.argumenten;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class WKArgumentsLogger {

  private Map<String, Object> map = new LinkedHashMap<>();

  public WKArgumentsLogger(ZoekArgumenten a) {

    put("code_gemeentedeel", a.getCode_gemeentedeel());
    put("code_lokatie", a.getCode_lokatie());
    put("code_object", a.getCode_object());
    put("code_straat", a.getCode_straat());
    put("datum_einde", a.getDatum_einde());
    put("gemeentedeel", a.getGemeentedeel());
    put("huisletter", a.getHuisletter());
    put("huisnummer", a.getHuisnummer());
    put("huisnummeraanduiding", a.getHuisnummeraanduiding());
    put("huisnummertoevoeging", a.getHuisnummertoevoeging());
    put("lokatie", a.getLokatie());
    put("postcode", a.getPostcode());
    put("straatnaam", a.getStraatnaam());
    put("volgcode_einde", a.getVolgcode_einde());
    put("geen_bewoners", a.isGeen_bewoners());
    put("extra_pl_gegevens", a.isExtra_pl_gegevens());
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
