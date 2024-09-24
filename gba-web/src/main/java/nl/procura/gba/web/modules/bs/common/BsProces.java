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

import java.util.Arrays;

import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;

public class BsProces {

  private int                                            increment     = 0;
  private String                                         id            = "";
  private Class<? extends BsPage<? extends ZaakDossier>> page          = null;
  private BsProcesStatus                                 status        = BsProcesStatus.EMPTY;
  private String                                         statusMessage = "";
  private BsProcesButton                                 button        = null;

  public BsProces(int increment, final BsProcessen bsProcessen, String id,
      Class<? extends BsPage<? extends ZaakDossier>> page) {

    setIncrement(increment);
    setId(id);
    setPage(page);

    setButton(new BsProcesButton(getId()) {

      @Override
      public void buttonClick(ClickEvent event) {

        for (BsProcesListener listener : bsProcessen.getListeners()) {
          listener.onButtonClick(BsProces.this);
        }
      }
    });
  }

  public void forceStatus(BsProcesStatus status) {
    this.status = status;
  }

  public BsProcesButton getButton() {
    return button;
  }

  public void setButton(BsProcesButton label) {
    this.button = label;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getIncrement() {
    return increment;
  }

  public void setIncrement(int increment) {
    this.increment = increment;
  }

  public Class<? extends BsPage<? extends ZaakDossier>> getPage() {
    return page;
  }

  public void setPage(Class<? extends BsPage<? extends ZaakDossier>> page) {
    this.page = page;
  }

  public BsProcesStatus getStatus() {
    return status;
  }

  public void setStatus(BsProcesStatus status) {
    if (this.status != BsProcesStatus.DISABLED) {
      this.status = status;
    }
  }

  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public boolean isStatus(BsProcesStatus... statussen) {

    return Arrays.stream(statussen).anyMatch(s -> s == status);
  }

  public void setStatus(BsProcesStatus status, String message) {
    setStatus(status);
    setStatusMessage(message);
  }

  @Override
  public String toString() {
    return "BsProces [id=" + id + "]";
  }
}
