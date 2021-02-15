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

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import org.aopalliance.intercept.MethodInvocation;

import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.jpa.personen.utils.GbaJpaStorage;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.standard.threadlocal.ThreadLocalStorage;

/**
 * Onderschept methodes in services
 */
public class TransactionInterceptor extends ServiceMethodInterceptor {

  public TransactionInterceptor() {
  }

  @Override
  public Object invoke(MethodInvocation in) throws Throwable {

    beginTransaction(in);
    Object result;

    try {

      result = in.proceed();
      commitOrRollback(in, true);
    } catch (RuntimeException e) {

      commitOrRollback(in, false);
      throw e;
    }

    return result;
  }

  /**
   * Begin een transactie
   */
  private void beginTransaction(MethodInvocation in) {

    Transactional ann = getAnnotation(in, Transactional.class);

    if (ann != null) {

      if (getMethod() == null) {

        setMethod(in.getMethod());

        GbaJpa.getManager().getTransaction().begin();
      }
    }
  }

  private void commitOrRollback(MethodInvocation in, boolean doCommit) {

    Transactional ann = getAnnotation(in, Transactional.class);

    if (ann != null) {

      if (getMethod() == in.getMethod()) {

        try {

          EntityManager manager = GbaJpa.getManager();

          if (doCommit || ann.commitWithException()) {

            manager.getTransaction().commit();
            manager.clear();

            if (ann.closeAfterCommit()) {
              manager.close();
            }
          } else {
            if (manager.getTransaction().isActive()) {
              manager.getTransaction().rollback();
            }
          }
        } finally {
          setMethod(null);
        }
      }
    }
  }

  private Method getMethod() {
    return ThreadLocalStorage.get(GbaJpaStorage.class).getMethod();
  }

  private void setMethod(Method method) {
    ThreadLocalStorage.get(GbaJpaStorage.class).setMethod(method);
  }
}
