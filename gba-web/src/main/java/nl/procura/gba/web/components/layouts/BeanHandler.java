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

package nl.procura.gba.web.components.layouts;

import static nl.procura.standard.Globalfunctions.astr;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.common.MiscUtils;
import nl.procura.standard.Globalfunctions;

public class BeanHandler {

  private final static Logger LOGGER = LoggerFactory.getLogger(BeanHandler.class);
  private static final String GET    = "get";
  private static final String IS     = "is";
  private static final String SET    = "set";

  private BeanHandler() {
  }

  @SuppressWarnings("unchecked")
  public static <T> T trim(Object o) {
    Method[] methods = o.getClass().getMethods();
    for (Method method : methods) {
      if (isGetter(method)) {
        try {
          Object orgGetterValue = method.invoke(o);
          String getterValue = MiscUtils.trimAllowed(Globalfunctions.trim(astr(orgGetterValue)));

          if ("-1".equals(getterValue)) {
            getterValue = "";
          }

          if ((orgGetterValue != getterValue)) {
            Method setter = getSetter(method, methods);
            if (setter != null) {
              setter.invoke(o, getterValue);
            }
          }
        } catch (Exception e) {
          LOGGER.debug(e.toString());
        }
      }
    }

    return (T) o;
  }

  private static Method getSetter(Method method, Method[] copyMethods) {
    String setterName = getSettername(method.getName());
    for (Method m : copyMethods) {
      if (m.getName().equals(setterName)) {
        return m;
      }
    }

    return null;
  }

  private static String getSettername(String gettername) {

    if (gettername.startsWith(GET) && gettername.length() > 3) {
      return SET + gettername.substring(3);
    } else if (gettername.startsWith(IS) && gettername.length() > 2) {
      return SET + gettername.substring(2);
    } else {
      throw new IllegalArgumentException("De gegeven getternaam begint niet met 'get' of 'is'.");
    }
  }

  private static boolean isGetter(Method m) {
    return (m.getName().startsWith(GET) || m.getName().startsWith(
        IS)) && (m.getParameterTypes().length <= 0) && !Void.class.equals(m.getReturnType());
  }
}
