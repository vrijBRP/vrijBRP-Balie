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

package nl.procura.gba.common;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.PROGRAMMING;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import nl.procura.standard.exceptions.ProException;

/**
 * Een HashMap waarin alle niet-null elementen in voorkomen
 */
public class ConditionalMap extends HashMap<String, Object> {

  public ConditionalMap() {
  }

  public ConditionalMap(String key, String value) {
    putString(key, value);
  }

  public void putLong(String key, long value) {
    if (value > 0) {
      put(key, value);
    }
  }

  public void putList(String key, List value) {
    if (!value.isEmpty()) {
      put(key, value);
    }
  }

  public void putSet(String key, Set value) {
    if (!value.isEmpty()) {
      put(key, value);
    }
  }

  public void putString(String key, String value) {
    if (fil(value)) {
      put(key, value);
    }
  }

  public void putPosString(String key, String value) {
    if (pos(value)) {
      put(key, value);
    }
  }

  public boolean containsKeys(String... keys) {
    for (String key : keys) {
      if (containsKey(key)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public Object put(String key, Object value) {
    return (value != null) ? super.put(key, value) : value;
  }

  @Override
  public Object get(Object key) {

    if (size() == 0) {
      throw new ProException(PROGRAMMING, ERROR, "Geen argumenten meegegeven voor deze actie");
    }

    return super.get(key);
  }
}
