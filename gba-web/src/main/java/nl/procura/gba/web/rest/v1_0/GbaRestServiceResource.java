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

package nl.procura.gba.web.rest.v1_0;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import nl.procura.gba.web.rest.v1_0.zaak.zoeken.GbaRestZaakZoekenHandler;
import nl.procura.gba.web.rest.v2.model.base.GbaRestAntwoord;
import nl.procura.gba.web.rest.v2.services.GbaRestServices;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.Services.TYPE;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruiker;
import nl.procura.standard.exceptions.ProException;

public class GbaRestServiceResource {

  private static final Logger LOGGER   = LoggerFactory.getLogger(GbaRestServiceResource.class);
  private static final int    ZERO     = 0;
  private static final int    ONE      = 1;
  @Inject
  private GbaRestServices     gbaRestServices;
  @Inject
  protected ProRestGebruiker  loggedInUser;
  private Services            services = null;

  protected Response getResponse(InputStream stream, String extension) {
    ResponseBuilder builder = Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM);
    StringBuilder attachment = new StringBuilder();
    attachment.append("attachment; filename = document-");
    attachment.append(new Date().getTime());
    attachment.append(".");
    attachment.append(extension);
    return builder.header("content-Disposition", attachment.toString()).build();
  }

  protected Services getServices() {
    if (fil(loggedInUser.getGebruikersnaam()) && services == null) {
      setServices(new Services(TYPE.REST));
      GebruikerService gebruikers = services.getGebruikerService();
      services.setGebruiker(gebruikers.getGebruikerByNaamWithCache(loggedInUser.getGebruikersnaam()));
      services.init();
    }

    return services;
  }

  public void setServices(Services services) {
    this.services = services;
    Services.setInstance(services);
  }

  protected Zaak getStandaardZaak(Zaak zaak) {
    return getServices().getZakenService().getStandaardZaken(asList(zaak)).get(0);
  }

  protected Zaak getMinimaleZaak(String zaakId) {

    if (emp(zaakId)) {
      throw new IllegalArgumentException("Geen zaak-id");
    }

    GbaRestZaakZoekenHandler handler = new GbaRestZaakZoekenHandler(getServices());
    ZaakArgumenten za = handler.getAlleZaakArgumenten(new ZaakArgumenten(zaakId));
    List<Zaak> zaken = getServices().getZakenService().getMinimaleZaken(za);

    switch (zaken.size()) {
      case ZERO:
        throw new ProException(ERROR, format("Geen zaak gevonden met zaak-id %s", zaakId));

      case ONE:
        return zaken.get(0);

      default:
        throw new ProException(ERROR, format("Meer dan één zaak gevonden met zaak-id %s", zaakId));
    }
  }

  protected GbaRestServices getGbaRestServices() {
    return gbaRestServices.setServices(getServices());
  }

  public static <T> GbaRestAntwoord<T> tryCall(Callable<GbaRestAntwoord<T>> callable) {
    try {
      return callable.call();
    } catch (NullPointerException e) {
      LOGGER.error(e.getMessage(), e);
      if (StringUtils.isNotBlank(e.getMessage())) {
        return new GbaRestAntwoord<>(e.getMessage());
      } else {
        return new GbaRestAntwoord<>("Er is een systeemfout (nullpointer) opgetreden.");
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return new GbaRestAntwoord<>(e.getMessage());
    }
  }
}
