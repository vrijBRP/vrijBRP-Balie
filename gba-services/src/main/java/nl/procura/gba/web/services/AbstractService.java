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

package nl.procura.gba.web.services;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SSL_PROXY_URL;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.PROGRAMMING;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.EventLogEntity;
import nl.procura.gba.jpa.personen.db.EventType;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.applicatie.EventLogService;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.gba.ple.opslag.PersoonStatusOpslagEntry;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public abstract class AbstractService implements Serializable {

  private final List<ServiceListener> listeners = new ArrayList<>();
  private Services                    services  = null;
  private String                      name      = null;
  private boolean                     initiated = false;

  @Inject
  private EventLogService eventLogService;

  public AbstractService(String name) {
    setName(name);
  }

  public void check() {
  }

  public void addMessage(boolean adminOnly, ServiceMeldingCategory category, ProExceptionSeverity severity,
      String message) {
    addMessage(adminOnly, category, severity, message, null, null);
  }

  /**
   * Voeg melding toe dat bij de accountgegevens kan worden bekeken.
   */
  public void addMessage(boolean adminOnly, ServiceMeldingCategory category, ProExceptionSeverity severity,
      String message, String cause,
      Throwable t) {

    ServiceMelding melding = new ServiceMelding().setSeverity(severity)
        .setAdminOnly(adminOnly)
        .setCategory(category)
        .setMelding(message)
        .setOorzaak(cause)
        .setException(t);

    getServices().getMeldingService().add(melding);
  }

  public List<ServiceListener> getListeners() {
    return listeners;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = "Service: " + name;
  }

  public String getParm(ParameterType parameterType) {
    return getServices().getGebruiker().getParameters().get(parameterType).getValue();
  }

  public String getParm(ParameterType parameterType, boolean required) {
    return getServices().getGebruiker().getParameters().get(parameterType, required).getValue();
  }

  public String getSysteemParm(ParameterType parameterType, boolean required) {
    String value = getServices().getParameterService().getSysteemParameter(parameterType).getValue();
    if (required && emp(value)) {
      throw new ProException(ProExceptionSeverity.WARNING,
          "Parameter {0} is niet gevuld", parameterType.toString());
    }
    return value;
  }

  public String getProxyUrl(ParameterType parameterType, boolean required) {
    return getProxyUrl(getSysteemParm(parameterType, required), true);
  }

  public String getProxyUrl(String url, boolean required) {
    return getSysteemParm(SSL_PROXY_URL, required) + "/proxy?url=" + url;
  }

  public Services getServices() {
    return services;
  }

  public void setServices(Services services) {
    this.services = services;
  }

  public void init() {
    setInitiated(true);
  }

  public boolean isInitiated() {
    return initiated;
  }

  public void setInitiated(boolean initiated) {
    this.initiated = initiated;
  }

  /**
   * Voeg listener toe als deze nog niet bestaat
   */
  public void setListener(ServiceListener listener) {
    for (ServiceListener l : new ArrayList<>(getListeners())) {
      if (listener.getId().equalsIgnoreCase(l.getId())) {
        getListeners().remove(l);
      }
    }

    getListeners().add(listener);
  }

  public void setParm(ParameterType parm, String value, Gebruiker gebruiker, Profiel profiel,
      boolean updateGebruikerParameter) {
    getServices().getParameterService().saveParameter(parm, value, gebruiker, profiel, updateGebruikerParameter);
  }

  @Override
  public String toString() {
    return getName();
  }

  public BasePLExt findPL(FieldValue... fieldValues) {
    List<String> nummers = new ArrayList<>();
    for (FieldValue fv : fieldValues) {
      nummers.add(astr(fv.getValue()));
    }
    return getServices().getPersonenWsService().getPersoonslijst(nummers.toArray(new String[nummers.size()]));
  }

  /**
   * Roept listeners aan.
   */
  protected void callListeners(ServiceEvent event) {
    // Verwijder de persoonstatus
    getServices().getPersonenWsService().getOpslag().clear(PersoonStatusOpslagEntry.class);

    // Call Listeners
    for (ServiceListener l : getListeners()) {
      l.action(event);
    }
  }

  @ThrowException(severity = ERROR,
      value = "Fout bij het zoeken in de database")
  protected <T> T findEntity(Class<T> entityClass, Object primaryKey) {
    return GenericDao.find(entityClass, primaryKey);
  }

  @ThrowException(severity = ERROR,
      value = "Fout bij het zoeken in de database")
  protected <T> List<T> findEntity(T example) {
    return GenericDao.findByExample(example);
  }

  protected UsrFieldValue getGebruiker(long code) {
    return getServices().getGebruikerService().getGebruikerWaarde(code);
  }

  @SuppressWarnings("unchecked")
  protected <T> T getInstance(Object object, Class<T> cl) {
    if (cl.isAssignableFrom(object.getClass())) {
      return (T) object;
    }

    throw new ProException(PROGRAMMING, ERROR, "Verkeerd type voor deze functie.");
  }

  protected BasePLExt getPersoonslijst(String... nummers) {
    return getServices().getPersonenWsService().getPersoonslijst(nummers);
  }

  protected boolean isSaved(Object object) {
    return GenericDao.isSaved(object);
  }

  @Transactional
  @ThrowException(severity = ERROR,
      value = "Fout bij het bijwerken van de database")
  protected void removeEntities(List objects) {
    for (Object o : objects) {
      removeEntity(o);
    }
  }

  @Transactional
  @ThrowException(severity = ERROR,
      value = "Fout bij het bijwerken van de database")
  protected void removeEntity(Object o) {
    GenericDao.removeEntity(o);
  }

  @Transactional
  @ThrowException(severity = ERROR,
      value = "Fout bij het bijwerken van de database")
  protected <T> T saveEntity(T o) {
    EventType eventType = isNew(o) ? EventType.CREATED : EventType.SAVED;
    GenericDao.saveEntity(o);
    if (o instanceof EventLogEntity) {
      eventLogService.addEvent((EventLogEntity) o, eventType, services.getGebruiker());
    }
    return o;
  }

  private static boolean isNew(Object o) {
    if (o instanceof BaseEntity) {
      return !((BaseEntity<?>) o).isStored();
    }
    return false;
  }
}
