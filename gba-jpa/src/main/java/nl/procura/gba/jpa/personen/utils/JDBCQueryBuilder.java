/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.jpa.personen.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

@Builder
public class JDBCQueryBuilder {

  private boolean keepalive;

  @Override
  public String toString() {
    Map<String, Object> str = new HashMap<>();
    if (keepalive) {
      str.put("tcpKeepAlive", true);
    }
    String out = str.entrySet().stream()
        .map(entry -> entry.getKey() + "=" + entry.getValue())
        .collect(Collectors.joining("&"));
    return StringUtils.isNotBlank(out) ? "?" + out : "";
  }
}
