/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.burgerzaken.vrsclient.api.jackson;

import javax.ws.rs.ext.ContextResolver;
import org.codehaus.jackson.map.ObjectMapper;

public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

  private final ObjectMapper objectMapper;

  public ObjectMapperContextResolver() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new ProcuraModule());
  }

  @Override
  public ObjectMapper getContext(Class<?> type) {
    return objectMapper;
  }
}
