/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.rest.v1_0.zaak.controles;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.DATUM_INVOER;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.OMSCHRIJVING;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.OPMERKINGEN;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.STATUS;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.TIJD_INVOER;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.TYPE;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.ZAAKID;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.ZAAK_AANGEPAST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakControlesVraag;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.ControleerbareService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controle;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ZaakControle;
import nl.procura.commons.core.exceptions.ProException;

/**
 * Vertaald de zaakcontroles naar GBA rest elementen
 */
public class GbaRestZaakControlesHandler extends GbaRestHandler {

  public GbaRestZaakControlesHandler(Services services) {
    super(services);
  }

  public List<GbaRestElement> getControles() {
    return getControles(new GbaRestZaakControlesVraag());
  }

  public List<GbaRestElement> getControles(GbaRestZaakControlesVraag vraag) {
    List<GbaRestElement> antwoord = new ArrayList<>();
    GbaRestElement elementen = new GbaRestElement(GbaRestElementType.CONTROLES);
    antwoord.add(elementen);

    Controles controles = getZaakControles(vraag.getFilterInclude(), vraag.getFilterExclude());
    for (Controle controle : controles) {
      GbaRestElement element = elementen.add(GbaRestElementType.CONTROLE);

      String oms = String.format("%s - %s - %s",
          controle.getOnderwerp(),
          controle.getId(),
          controle.getOmschrijving());

      GbaRestElementHandler.add(element, OMSCHRIJVING, oms);
      GbaRestElementHandler.add(element, OPMERKINGEN, controle.getOpmerkingenString());
      GbaRestElementHandler.add(element, ZAAK_AANGEPAST, controle.isGewijzigd());

      if (controle instanceof ZaakControle) {
        ZaakControle<?> zc = (ZaakControle<?>) controle;

        Zaak zaak = zc.getZaak();
        DateTime dt = zaak.getDatumTijdInvoer();
        ZaakStatusType status = zaak.getStatus();
        ZaakType zaakType = zaak.getType();

        GbaRestElementHandler.add(element, DATUM_INVOER, dt);
        GbaRestElementHandler.add(element, TIJD_INVOER, dt.getLongTime(), dt.getFormatTime());
        GbaRestElementHandler.add(element, TYPE, zaakType.getCode(), zaakType.getOms());
        GbaRestElementHandler.add(element, STATUS, status.getCode(), status.getOms());
        GbaRestElementHandler.add(element, ZAAKID, zaak.getZaakId());

      } else {
        GbaRestElementHandler.add(element, DATUM_INVOER, "");
        GbaRestElementHandler.add(element, TIJD_INVOER, "");
        GbaRestElementHandler.add(element, TYPE, "");
        GbaRestElementHandler.add(element, STATUS, "");
        GbaRestElementHandler.add(element, ZAAKID, "");
      }
    }

    return antwoord;
  }

  private Controles getZaakControles(String filterInclude, String filterExclude) {
    List<String> includeServices = Arrays.asList(StringUtils.split(filterInclude, ","));
    List<String> excludeServices = Arrays.asList(StringUtils.split(filterExclude, ","));
    List<ControleerbareService> services = getServices().getServices(ControleerbareService.class);
    Controles controles = new Controles();
    checkServices(includeServices, services);
    checkServices(excludeServices, services);
    services.stream()
        .filter(service -> includeServices.isEmpty() || isService(service, includeServices))
        .filter(service -> excludeServices.isEmpty() || !isService(service, excludeServices))
        .map(zdb -> zdb.getControles(null))
        .forEach(controles::addGewijzigdeControles);

    return controles;
  }

  private void checkServices(List<String> serviceList, List<ControleerbareService> services) {
    serviceList.stream()
        .filter(filteredService -> services.stream().noneMatch(service -> isService(filteredService, service)))
        .forEach(filteredService -> {
          throw new ProException("Service: '" + filteredService + "' bestaat niet");
        });
  }

  private static boolean isService(ControleerbareService service, List<String> filteredServices) {
    return filteredServices.stream().anyMatch(filteredService -> isService(filteredService, service));
  }

  private static boolean isService(String filteredService, ControleerbareService service) {
    boolean isName = service.getName().equalsIgnoreCase(filteredService);
    boolean isClassName = service.getClass().getSimpleName().toLowerCase().contains(filteredService.toLowerCase());
    return isName || isClassName;
  }
}
