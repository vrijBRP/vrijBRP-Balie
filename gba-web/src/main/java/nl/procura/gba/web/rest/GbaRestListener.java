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

package nl.procura.gba.web.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.ws.rs.Path;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.bestand.GbaRestBestandResources;
import nl.procura.gba.web.rest.v1_0.document.GbaRestDocumentResources;
import nl.procura.gba.web.rest.v1_0.gebruiker.GbaRestAuthenticatieResources;
import nl.procura.gba.web.rest.v1_0.gebruiker.GbaRestAuthenticatieValidator;
import nl.procura.gba.web.rest.v1_0.persoon.GbaRestPersoonResources;
import nl.procura.gba.web.rest.v1_0.persoon.contact.GbaRestPersoonContactgegevensResources;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestKlapperResources;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakAantekeningResources;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakAttribuutResources;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakBestandResources;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakIdentificatieResources;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakRelatieResources;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakResources;
import nl.procura.gba.web.rest.v2.resources.GbaRestDataImportResource;
import nl.procura.gba.web.rest.v2.resources.GbaRestEventLogResourceV2Server;
import nl.procura.gba.web.rest.v2.resources.GbaRestGebruikerResourceV2Server;
import nl.procura.gba.web.rest.v2.resources.GbaRestInfoResource;
import nl.procura.gba.web.rest.v2.resources.GbaRestVerhuizingResourceV2Server;
import nl.procura.gba.web.rest.v2.resources.GbaRestZaakDmsResourceV2Server;
import nl.procura.gba.web.rest.v2.resources.GbaRestZaakResourceV2Server;
import nl.procura.gba.web.rest.v2.services.GbaRestAbstractService;
import nl.procura.gba.web.rest.v2.services.GbaRestServices;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.methodinterception.TransactionInterceptor;
import nl.procura.proweb.rest.guice.misc.ProRestAuthenticatieValidator;
import nl.procura.proweb.rest.guice.misc.ProRestDefaultMethodInterceptor;
import nl.procura.proweb.rest.guice.modules.ProRestServletModule;

public class GbaRestListener extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new GbaRestModule());
  }

  public class GbaRestModule extends ProRestServletModule {

    @Override
    protected void configureServlets() {

      super.configureServlets();

      bind(ProvisionExceptionMapper.class);
      bind(GbaRestJsonProvider.class);
      bind(ProRestAuthenticatieValidator.class).to(GbaRestAuthenticatieValidator.class);

      // v1.0
      bind(GbaRestAuthenticatieResources.class);
      bind(GbaRestDocumentResources.class);
      bind(GbaRestBestandResources.class);
      bind(GbaRestPersoonContactgegevensResources.class);
      bind(GbaRestPersoonResources.class);
      bind(GbaRestKlapperResources.class);

      // Zaak
      bind(GbaRestZaakResources.class);
      bind(GbaRestZaakBestandResources.class);
      bind(GbaRestZaakAantekeningResources.class);
      bind(GbaRestZaakAttribuutResources.class);
      bind(GbaRestZaakIdentificatieResources.class);
      bind(GbaRestZaakRelatieResources.class);

      // V2.0
      bind(GbaRestZaakResourceV2Server.class);
      bind(GbaRestZaakDmsResourceV2Server.class);
      bind(GbaRestVerhuizingResourceV2Server.class);
      bind(GbaRestEventLogResourceV2Server.class);
      bind(GbaRestGebruikerResourceV2Server.class);
      bind(GbaRestInfoResource.class);
      bind(GbaRestDataImportResource.class);

      bind(GbaRestServices.class);

      Matcher<Class> resourceMatcher = Matchers.subclassesOf(GbaRestServiceResource.class);
      bindInterceptor(resourceMatcher, Matchers.annotatedWith(Path.class), new ProRestDefaultMethodInterceptor());

      Matcher<Class> serviceMatcher = Matchers.subclassesOf(GbaRestAbstractService.class);
      bindInterceptor(serviceMatcher, getMatcher(Transactional.class), new TransactionInterceptor());

      // serve
      serve("/rest/*").with(GuiceContainer.class);
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
}
