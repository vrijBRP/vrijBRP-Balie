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

package nl.procura.gba.web.modules.bs.common;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.COMPLETE;
import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.EMPTY;
import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.ui.Component;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.zaken.algemeen.ZaakMetPagina;

public abstract class BsProcessen {

  private static final String    RESET         = "reset";
  private Dossier                dossier       = null;
  private BsProces               currentProces = null;
  private GbaApplication         application   = null;
  private List<BsProces>         processen     = new ArrayList<>();
  private List<BsProcesListener> listeners     = new ArrayList<>();

  public BsProcessen() {
  }

  public BsProcessen(GbaApplication application) {
    this.application = application;
  }

  protected static boolean isPaginaReset(ZaakMetPagina zaak) {
    return RESET.equalsIgnoreCase(zaak.getPagina());
  }

  public void addProces(String id, Class<? extends BsPage<? extends ZaakDossier>> pageClass) {
    getProcessen().add(new BsProces(getProcessen().size(), this, id, pageClass));
  }

  public GbaApplication getApplication() {
    return application;
  }

  public Dossier getDossier() {
    return dossier;
  }

  public void setDossier(Dossier dossier) {
    this.dossier = dossier;
  }

  /**
   * Laatste proces dat 'enabled' is.
   */
  public BsProces getLastProces() {

    int last = 0;

    for (int i = 0; i < getProcessen().size(); i++) {
      if (getProcessen().get(i).isStatus(BsProcesStatus.COMPLETE)) {
        last = i;
      }
    }

    return getProcessen().get(last);
  }

  public List<BsProcesListener> getListeners() {
    return listeners;
  }

  public void setListeners(List<BsProcesListener> listeners) {
    this.listeners = listeners;
  }

  public BsProces getNextProces() {
    return getNext(currentProces, getProcessen());
  }

  public BsProces getPreviousProces() {
    List<BsProces> rProcessen = new ArrayList<>(getProcessen());
    Collections.reverse(rProcessen);
    return getNext(currentProces, rProcessen);
  }

  public BsProces getProces(Class<?> pageClass) {
    for (BsProces proces : processen) {
      if (pageClass.isAssignableFrom(proces.getPage())) {
        return proces;
      }
    }

    return null;
  }

  public BsProces getProces(String id) {
    for (BsProces proces : processen) {
      if (equalsIgnoreCase(proces.getId(), id)) {
        return proces;
      }
    }

    return null;
  }

  public List<BsProces> getProcessen() {
    return processen;
  }

  public void setProcessen(List<BsProces> processen) {
    this.processen = processen;
  }

  /**
   * Prefil
   */
  public void initStatusses(GbaApplication application) {
    this.application = application;
  }

  /**
   * Zet huidig proces
   */
  public void setCurrentProces(BsProces currentProces) {
    this.currentProces = currentProces;
    update();
  }

  public abstract void updateStatus();

  /**
   * Rest to first proces
   */
  protected void goToFirst() {
    for (BsProces proces : getProcessen()) {
      if (proces.getStatus() == COMPLETE) {
        proces.setStatus(EMPTY);
      }
    }
  }

  private BsProces getNext(BsProces proces, List<BsProces> processen) {

    boolean found = false;
    for (BsProces bproces : processen) {
      if (found) {
        if (bproces.isStatus(BsProcesStatus.DISABLED)) {
          return getNext(bproces, processen);
        }

        return bproces;
      }

      if (bproces.getPage() == proces.getPage()) {
        found = true;
      }
    }

    return proces;
  }

  /**
   * Update styles
   */
  private void update() {

    for (BsProces proces : getProcessen()) {

      Component label = proces.getButton();

      label.setStyleName("");

      if (proces.getIncrement() == 0) {
        label.addStyleName("first");
      }

      if (proces.getIncrement() == (getProcessen().size() - 1)) {
        label.addStyleName("last");
      }

      if (proces == currentProces) {
        label.addStyleName("current");
      } else if (proces.getIncrement() > currentProces.getIncrement()) {
        label.addStyleName("next");
      } else {
        label.addStyleName("prev");
      }

      if (proces.isStatus(BsProcesStatus.COMPLETE)) {
        label.addStyleName("complete");
      }
      if (proces.isStatus(BsProcesStatus.DISABLED)) {
        label.addStyleName("next");
      } else if (proces.isStatus(BsProcesStatus.INCOMPLETE)) { // Levert een waarschuwingsicoontje op
        label.addStyleName("incomplete");
      }
    }
  }
}
