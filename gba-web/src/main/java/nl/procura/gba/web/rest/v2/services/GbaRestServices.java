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

package nl.procura.gba.web.rest.v2.services;

import nl.procura.gba.web.services.Services;

public class GbaRestServices {

  private final GbaRestVerhuizingService  verhuizingService;
  private final GbaRestGeboorteService    geboorteService;
  private final GbaRestDossierService     dossierService;
  private final GbaRestContactService     contactService;
  private final GbaRestZaakService        zaakService;
  private final GbaRestZaaksysteemService zaakDmsService;
  private final GbaRestDmsService         dmsService;
  private final GbaRestErkenningService   erkenningService;
  private final GbaRestHuwelijkService    huwelijkService;
  private final GbaRestOverlijdenService  overlijdenService;
  private final GbaRestGebruikerService   gebruikerService;

  public GbaRestServices(Services services) {
    dossierService = new GbaRestDossierService(this, services);
    contactService = new GbaRestContactService(this, services);
    zaakService = new GbaRestZaakService(this, services);
    zaakDmsService = new GbaRestZaaksysteemService(this, services);
    dmsService = new GbaRestDmsService(services, zaakService);
    erkenningService = new GbaRestErkenningService(dossierService);
    verhuizingService = new GbaRestVerhuizingService(this, services);
    geboorteService = new GbaRestGeboorteService(this, services);
    huwelijkService = new GbaRestHuwelijkService(this, services);
    overlijdenService = new GbaRestOverlijdenService(this, services);
    gebruikerService = new GbaRestGebruikerService(this, services);
  }

  public GbaRestVerhuizingService getVerhuizingService() {
    return verhuizingService;
  }

  public GbaRestGeboorteService getGeboorteService() {
    return geboorteService;
  }

  public GbaRestDossierService getDossierService() {
    return dossierService;
  }

  public GbaRestContactService getContactService() {
    return contactService;
  }

  public GbaRestZaakService getZaakService() {
    return zaakService;
  }

  public GbaRestZaaksysteemService getZaakDmsService() {
    return zaakDmsService;
  }

  public GbaRestDmsService getDmsService() {
    return dmsService;
  }

  public GbaRestErkenningService getErkenningService() {
    return erkenningService;
  }

  public GbaRestHuwelijkService getHuwelijkService() {
    return huwelijkService;
  }

  public GbaRestOverlijdenService getOverlijdenService() {
    return overlijdenService;
  }

  public GbaRestGebruikerService getGebruikerService() {
    return gebruikerService;
  }
}
