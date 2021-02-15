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

package nl.procura.gba.web.services.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

import nl.procura.gba.jpa.personen.dao.EventLogDao;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.applicatie.EventLogService;
import nl.procura.gba.web.services.methodinterception.ExceptionInterceptor;
import nl.procura.gba.web.services.methodinterception.TimeInterceptor;
import nl.procura.gba.web.services.methodinterception.TransactionInterceptor;

/**
 * Een module die onderschept alle methodes van Klasse ServiceImpl.
 */
public class ServiceGuiceModule extends AbstractModule {

  @Override
  protected void configure() {

    Matcher<Class> matchers = Matchers.subclassesOf(AbstractService.class)
        .or(Matchers.annotatedWith(Singleton.class));

    bindInterceptor(matchers, getMatcher(Transactional.class), new TransactionInterceptor());
    bindInterceptor(matchers, getMatcher(ThrowException.class), new ExceptionInterceptor());
    bindInterceptor(matchers, getMatcher(Timer.class), new TimeInterceptor());

    bind(EventLogDao.class);
    bind(EventLogService.class);
  }

  /**
   * Returns a non-synthetic matcher for an annotation method
   */
  private Matcher<Method> getMatcher(Class<? extends Annotation> annotation) {
    AbstractMatcher<Method> nonSyntheticMatcher = new AbstractMatcher<Method>() {

      @Override
      public boolean matches(Method t) {
        return !t.isSynthetic() && t.isAnnotationPresent(annotation);
      }
    };
    return nonSyntheticMatcher;
  }
}
