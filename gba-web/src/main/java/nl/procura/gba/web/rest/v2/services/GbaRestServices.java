/*
 * Copyright 2023 - 2024 Procura B.V.
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

import com.google.inject.Inject;

import nl.procura.gba.web.services.Services;

public class GbaRestServices {

  @Inject
  private GbaRestVerhuizingService verhuizingService;

  @Inject
  private GbaRestGeboorteService geboorteService;

  @Inject
  private GbaRestDossierService dossierService;

  @Inject
  private GbaRestContactService contactService;

  @Inject
  private GbaRestZaakService zaakService;

  @Inject
  private GbaRestZaaksysteemService zaakDmsService;

  @Inject
  private GbaRestDmsService dmsService;

  @Inject
  private GbaRestErkenningService erkenningService;

  @Inject
  private GbaRestNaamskeuzeService naamskeuzeService;

  @Inject
  private GbaRestHuwelijkService huwelijkService;

  @Inject
  private GbaRestOverlijdenService overlijdenService;

  @Inject
  private GbaRestGebruikerService gebruikerService;

  @Inject
  private GbaRestDataImportService dataImportService;

  private Services services;

  public GbaRestServices setServices(Services services) {
    this.services = services;
    return this;
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

  public GbaRestNaamskeuzeService getNaamskeuzeService() {
    return naamskeuzeService;
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

  public GbaRestDataImportService getDataImportService() {
    return dataImportService;
  }

  public Services getServices() {
    return services;
  }
}
