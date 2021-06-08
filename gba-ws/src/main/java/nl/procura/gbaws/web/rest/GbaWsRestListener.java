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

package nl.procura.gbaws.web.rest;

import javax.ws.rs.Path;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import nl.procura.gbaws.web.rest.v1_0.gbav.GbaWsRestGbavResources;
import nl.procura.gbaws.web.rest.v1_0.gebruiker.GbaWsRestAuthenticatieValidator;
import nl.procura.gbaws.web.rest.v1_0.procura.database.GbaWsRestProcuraDatabaseResources;
import nl.procura.gbaws.web.rest.v1_0.tabellen.GbaWsRestTabelResources;
import nl.procura.gbaws.web.rest.v2.GbaWsRestInfoResource;
import nl.procura.gbaws.web.rest.v2.personlists.GbaWsRestPersonListsResources;
import nl.procura.proweb.rest.guice.misc.ProRestAuthenticatieValidator;
import nl.procura.proweb.rest.guice.misc.ProRestDefaultMethodInterceptor;
import nl.procura.proweb.rest.guice.modules.ProRestServletModule;

public class GbaWsRestListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new GbaRestModule());
  }

  public class GbaRestModule extends ProRestServletModule {

    @Override
    protected void configureServlets() {

      super.configureServlets();

      bind(ProvisionExceptionMapper.class);
      bind(GbaWsRestJsonJaxbContextResolver.class);
      bind(ProRestAuthenticatieValidator.class).to(GbaWsRestAuthenticatieValidator.class);

      // v1.0
      bind(GbaWsRestGbavResources.class);
      bind(GbaWsRestTabelResources.class);
      bind(GbaWsRestProcuraDatabaseResources.class);

      // v2.0
      bind(GbaWsRestPersonListsResources.class);
      bind(GbaWsRestInfoResource.class);

      Matcher<Class> matchers = Matchers.subclassesOf(GbaWsRestDienstenbusResource.class);
      bindInterceptor(matchers, Matchers.annotatedWith(Path.class), new ProRestDefaultMethodInterceptor());

      // serve
      serve("/rest/*").with(GuiceContainer.class);
    }
  }
}
