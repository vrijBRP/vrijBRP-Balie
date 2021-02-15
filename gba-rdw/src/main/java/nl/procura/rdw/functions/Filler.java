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

import static nl.procura.rdw.functions.RdwReflection.instantiate;
import static nl.procura.rdw.functions.RdwReflection.setValue;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Filler {

  @SuppressWarnings("unchecked")
  public static void fill(Object obj) {

    try {
      for (Method m : obj.getClass().getMethods()) {
        String name = m.getName();

        if (name.startsWith("get") && !name.matches("getClass")) {
          try {
            Method getMethod = obj.getClass().getMethod(name);
            Object returnValue = getMethod.invoke(obj);

            boolean isNull = (returnValue == null);
            Class<?> returnType = getMethod.getReturnType();
            boolean isJavaClass = returnType.toString().contains("class java");
            boolean isList = returnType.toString().equals("interface java.util.List");

            if (isList) {
              List<?> list = ((List<?>) returnValue);

              if (isNull) {
                Type genericType = m.getGenericReturnType();

                if (genericType instanceof ParameterizedType) {
                  ParameterizedType pt = (ParameterizedType) genericType;
                  setValue(obj, name, returnType,
                      Filler.createListOfType((Class) pt.getActualTypeArguments()[0]));
                }
              } else {

                if (list != null && list.size() > 0) {
                  for (Object currentObj : list) {
                    fill(currentObj);
                  }
                }
              }
            } else {
              if (!isJavaClass) {
                if (isNull) {
                  Object newObj = instantiate(returnType);

                  fill(newObj);
                  setValue(obj, name, newObj);
                } else {
                  fill(returnValue);
                }
              } else {
                if (isNull && (returnType == String.class)) {
                  setValue(obj, name, "");
                } else if (isNull && (returnType == BigInteger.class)) {
                  setValue(obj, name, BigInteger.valueOf(0));
                }
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("unused")
  private static <T> List<T> createListOfType(Class<T> type) {
    return new ArrayList<>();
  }
}
