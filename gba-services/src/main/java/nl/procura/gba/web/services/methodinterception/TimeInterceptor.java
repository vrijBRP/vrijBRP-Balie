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

package nl.procura.gba.web.services.methodinterception;

import static nl.procura.standard.Globalfunctions.trim;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.web.services.aop.Timer;

/**
 * Rekent de tijdsduur van methodes uit.
 */
public class TimeInterceptor extends ServiceMethodInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(TimeInterceptor.class);

  @Override
  public Object invoke(MethodInvocation in) throws Throwable {

    long st = System.currentTimeMillis();

    Timer ann = getAnnotation(in, Timer.class);

    if (ann != null && ann.newLine()) {
      LOGGER.debug("==============");
    }

    try {
      return in.proceed(); // Voer de methode uit.
    } finally {

      if (LOGGER.isDebugEnabled()) {

        long et = System.currentTimeMillis();
        long time = (et - st);

        String className = in.getMethod().getDeclaringClass().getSimpleName();
        String methodName = in.getMethod().getName();

        if (!className.equalsIgnoreCase("ServiceZakenregister")) {

          LOGGER.debug(trim("TIME: " + time + " ms. " + className + ": " + methodName + " " + toStr(
              in.getArguments())));

          if (ann != null && ann.newLine()) {
            LOGGER.debug("==============");
          }
        }
      }
    }
  }

  private String toStr(Object[] args) {

    StringBuilder sb = new StringBuilder();

    for (Object o : args) {

      if (o != null) {

        sb.append(o.getClass().getSimpleName() + ", ");
      }
    }

    return sb.toString();
  }
}
