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

package nl.procura.gba.web.services.gba.tabellen;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.HashMap;
import java.util.Map;

public class TabelRecordAttributen {

  public static String EU = "eu";
  public static String ID = "id";

  private Map<String, String> map;

  public TabelRecordAttributen() {
  }

  public TabelRecordAttributen(Map<String, String> map) {
    this.map = map;
  }

  public Map<String, String> getMap() {
    return map;
  }

  public boolean isEu() {
    return pos(get(EU));
  }

  public boolean isId() {
    return pos(get(ID));
  }

  public void setEu() {
    set(EU, "1");
  }

  public void setId() {
    set(ID, "1");
  }

  private String get(String key) {
    return map != null ? astr(map.get(key)) : "";
  }

  private void set(String key, String id) {
    if (map == null) {
      map = new HashMap<>();
    }
    map.put(key, id);
  }
}
