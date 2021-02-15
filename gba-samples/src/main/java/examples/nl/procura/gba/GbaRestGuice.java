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

package examples.nl.procura.gba;

import static nl.procura.standard.Globalfunctions.pad_right;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

public class GbaRestGuice {

  private final static Logger   LOGGER         = LoggerFactory.getLogger(GbaRestGuice.class.getName());
  private final static Injector createInjector = Guice.createInjector(new GuiceModule());

  public static <T> T getInstance(Class<T> type) {
    return createInjector.getInstance(type);
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.METHOD })
  public @interface Timer {
  }

  public static class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
      Matcher<Class> matchers = Matchers.subclassesOf(GbaRestClientVoorbeelden.class);
      bindInterceptor(matchers, Matchers.annotatedWith(Timer.class), new TimeInterceptor());
    }
  }

  public static class TimeInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation in) throws Throwable {

      String className = in.getMethod().getDeclaringClass().getSimpleName();
      String methodName = in.getMethod().getName();

      String title = className + " - " + methodName;
      LOGGER.info(title);
      LOGGER.info(pad_right("", "=", title.length()));

      long start = System.currentTimeMillis();

      try {
        return in.proceed(); // Voer de methode uit.
      } finally {

        long time = (System.currentTimeMillis() - start);
        LOGGER.info("Time: " + time + " ms.\n");
      }
    }
  }
}
