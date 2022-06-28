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

import org.aopalliance.intercept.MethodInvocation;

import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.standard.exceptions.ProException;

/**
 * Onderschept methodes in services
 */
public class ExceptionInterceptor extends ServiceMethodInterceptor {

  @Override
  public Object invoke(MethodInvocation in) throws Throwable {

    try {
      return in.proceed(); // Voer de methode uit.
    } catch (ProException e) {
      throw e;
    } catch (RuntimeException e) {
      ThrowException th = getAnnotation(in, ThrowException.class);
      if (th != null) {
        throw new ProException(th.type(), th.severity(), th.value(), e);
      }
      throw e;
    }
  }
}
