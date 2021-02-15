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

package nl.procura.gba.web.rest.v2.resources;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.services.ServicesMock;
import nl.procura.gba.web.services.TemporaryDatabase;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.proweb.rest.utils.JsonUtils;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruiker;

public final class GbaRestZaakResourceV2TestUtils {

  private GbaRestZaakResourceV2TestUtils() {
  }

  public static <T extends GbaRestServiceResource> T getResourceV2(Class<T> clazz) {
    TemporaryDatabase.ensureCleanMockDatabase();
    ServicesMock services = new ServicesMock();
    services.init();
    Injector injector = Guice.createInjector(new GbaRestZaakResourceV2TestUtils.Module(services.getGebruiker()));
    T instance = injector.getInstance(clazz);
    instance.setServices(services);
    return instance;
  }

  public static <T> T jsonResourceToObject(Class<?> resourceClass, String resource, Class<T> type) {
    try {
      return JsonUtils.toObject(
          FileUtils.readFileToByteArray(
              new File(resourceClass.getResource(resourceClass.getSimpleName() + "-" + resource).toURI())),
          type);
    } catch (IOException | URISyntaxException e) {
      throw new IllegalStateException(e);
    }
  }

  public static class Module extends AbstractModule {

    private final Gebruiker gebruiker;

    public Module(Gebruiker gebruiker) {
      this.gebruiker = gebruiker;
    }

    @Override
    protected void configure() {
      bindScope(RequestScoped.class, Scopes.SINGLETON);

      ProRestGebruiker restGebruiker = new ProRestGebruiker();
      restGebruiker.setGebruikersnaam(gebruiker.getGebruikersnaam());

      bind(ProRestGebruiker.class).toProvider(() -> restGebruiker).in(RequestScoped.class);
    }
  }
}
