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

package nl.procura.rdw.functions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RdwReflection {

  public static Object instantiate(Class<?> c) {

    try {
      Constructor<?>[] constructors = c.getConstructors();
      for (Constructor<?> co : constructors) {
        for (Class<?> pc : co.getParameterTypes()) {
          return co.newInstance(instantiate(pc));
        }

        return c.newInstance();
      }
    } catch (Exception e) {
      throw new RuntimeException("Fout bij laden klasse", e);
    }

    return null;
  }

  public static void setValue(Object parent, String name, Class c, Object child) {
    try {
      String setname = "set" + name.replace("get", "");
      Method setMethod = parent.getClass().getMethod(setname, c);
      setMethod.invoke(parent, child);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void setValue(Object parent, String name, Object child) {
    try {
      String setname = "set" + name.replace("get", "");
      if (hasMethod(parent, setname)) {
        Method setMethod = parent.getClass().getMethod(setname, child.getClass());
        setMethod.invoke(parent, child);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean hasMethod(Object parent, String setname) {
    for (Method method : parent.getClass().getMethods()) {
      if (setname.equalsIgnoreCase(method.getName())) {
        return true;
      }
    }
    return false;
  }
}
