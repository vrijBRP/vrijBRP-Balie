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

package nl.procura.gba.jpa.personen.dao.views.verwijderzaken;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class VerwijderZakenQuery {

  private final StringBuilder       sql        = new StringBuilder();
  private final Map<String, Object> parameters = new HashMap<>();

  public VerwijderZakenQuery sql(String sql) {
    this.sql.append(sql);
    return this;
  }

  public VerwijderZakenQuery param(String key, Object val) {
    parameters.put(key, val);
    return this;
  }

  @Override
  public String toString() {
    return "SQL: " + sql + " Parameters: " + parameters;
  }
}
