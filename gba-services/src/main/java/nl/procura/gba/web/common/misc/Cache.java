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

package nl.procura.gba.web.common.misc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Caching van objecten gedurende een x aantal milliseconden
 */
public class Cache<T> {

  private final Map<Object, CacheItem> cache = Collections.synchronizedMap(new HashMap<>());
  private long                         time;

  public Cache(long time) {
    this.time = time;
  }

  public synchronized T get(Object key) {
    clean();
    CacheItem cacheItem = cache.get(key);
    return cacheItem != null ? cacheItem.getObject() : null;
  }

  public boolean is(boolean useCache, Object key) {
    return useCache && get(key) != null;
  }

  public synchronized void put(Object key, T value) {
    cache.remove(key);
    cache.put(key, new CacheItem(value));
  }

  public synchronized void cleanAll() {
    cache.clear();
  }

  private void clean() {
    for (Entry<Object, CacheItem> entry : new HashMap<>(cache).entrySet()) {
      if (entry.getValue().isExpired()) {
        cache.remove(entry.getKey());
      }
    }
  }

  private class CacheItem {

    private final long addedTime = System.currentTimeMillis();
    private final T    object;

    CacheItem(T object) {
      this.object = object;
    }

    public T getObject() {
      return object;
    }

    public boolean isExpired() {
      return (System.currentTimeMillis() - addedTime) > time;
    }
  }
}
