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

package nl.procura.gba.web.services.gba.ple.opslag;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.NO_RESULTS;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.standard.exceptions.ProException;

public class PersonenWsOpslag {

  private final static Logger      LOGGER   = LoggerFactory.getLogger(PersonenWsOpslag.class.getName());
  private final List<Object>       objecten = new ArrayList<>();
  private PersoonslijstOpslagEntry huidige  = null;

  /**
   * PL'en die gecached zijn verwijderen.
   */
  public void clear() {
    LOGGER.debug("PersonenWS - cache verwijderd");
    objecten.clear();
  }

  public void clear(Class clazz) {
    for (Object obj : new ArrayList<>(objecten)) {
      if (clazz.isAssignableFrom(obj.getClass())) {
        LOGGER.debug("PersonenWS - verwijderd: " + obj);
        objecten.remove(obj);
      }
    }
  }

  public PersoonIndicatieOpslagEntry get(PersoonIndicatieOpslagEntry zoekEntry) {
    return get(zoekEntry, "Aantekening uit geheugen geladen");
  }

  public PersoonslijstOpslagEntry get(PersoonslijstOpslagEntry zoekEntry, boolean specifiek) {

    PersoonslijstOpslagEntry entry = get(zoekEntry, "PL uit geheugen geladen");

    if (entry != null && specifiek) {
      setHuidige(entry);
    }

    return entry;
  }

  public PersoonStatusOpslagEntry get(PersoonStatusOpslagEntry zoekEntry) {
    return get(zoekEntry, "Status uit geheugen geladen");
  }

  public WoningkaartOpslagEntry get(WoningkaartOpslagEntry entry) {
    return get(entry, "WK uit geheugen geladen");
  }

  public BasePLExt getHuidige() {
    if (huidige != null) {
      return huidige.getPl();
    }
    return null;
  }

  public void setHuidige(PersoonslijstOpslagEntry entry) {
    if (!entry.getPl().isToonBeperking()) {
      this.huidige = entry;
    } else {
      throw new ProException(NO_RESULTS, WARNING,
          "De persoon wordt niet getoond vanwege een verstrekkingsbeperking");
    }
  }

  public PersoonIndicatieOpslagEntry set(PersoonIndicatieOpslagEntry zoekEntry) {
    return set(zoekEntry, "Aantekening uit geheugen geladen");
  }

  public PersoonslijstOpslagEntry set(PersoonslijstOpslagEntry zoekEntry, boolean specifiek) {

    PersoonslijstOpslagEntry entry = set(zoekEntry, "PL in geheugen opgeslagen");

    if (entry != null && specifiek) {
      setHuidige(entry);
    }

    return entry;
  }

  public PersoonStatusOpslagEntry set(PersoonStatusOpslagEntry zoekEntry) {
    return set(zoekEntry, "Status in geheugen geladen");
  }

  public WoningkaartOpslagEntry set(WoningkaartOpslagEntry entry) {
    return set(entry, "WK in geheugen opgeslagen");
  }

  private <T> T get(T entry, String message) {

    int index = objecten.indexOf(entry);

    if (index >= 0) { // Komt voor in lijst
      LOGGER.debug("PersonenWS - get - " + message + entry);
      return (T) objecten.get(index);
    }

    return null;
  }

  private <T> T set(T entry, String message) {

    int index = objecten.indexOf(entry);

    if (index < 0) { // Komt voor in lijst
      objecten.add(entry);
      LOGGER.debug("PersonenWS - set - " + message + entry);
    }

    return entry;
  }
}
