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

package nl.procura.gba.web.services.applicatie;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nl.procura.gba.web.services.AbstractService;

public class MemoryStorageService extends AbstractService {

  private final Map<Object, Object> map = Collections.synchronizedMap(new HashMap<>());

  public MemoryStorageService() {
    super("Geheugenopslag");
  }

  @SuppressWarnings("unchecked")
  public <T> T getAndRemoveByClass(Class<T> cl) {
    return (T) getAndRemoveObject(cl);
  }

  public Object getAndRemoveObject(Object key) {
    return getObject(key, true);
  }

  public Object getObject(Object key) {
    return getObject(key, false);
  }

  public void setObject(Object key, Object value) {
    map.put(key, value);
  }

  private Object getObject(Object key, boolean remove) {
    Object object = null;
    if (map.containsKey(key)) {
      object = map.get(key);
      if (remove) {
        map.remove(key);
      }
    }

    return object;
  }
}
