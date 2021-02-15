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

package nl.procura.gba.web.modules.bs.common.pages;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.*;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

public class BsProcesStatussen {

  private final List<BspProcesStatusEntry> list = new ArrayList<>();

  public void add(BspProcesStatusEntry entry) {
    getList().add(entry);
  }

  public BspProcesStatusEntry add(Class<? extends BsPage> clazz, boolean isFilled, String message) {
    if (isFilled) {
      BsProcesStatus status = fil(message) ? INCOMPLETE : COMPLETE;
      return add(clazz, status, message);
    } else {
      return add(clazz, EMPTY, "");
    }
  }

  public BspProcesStatusEntry add(Class<? extends BsPage> clazz, BsProcesStatus status) {
    return add(clazz, status, "");
  }

  public BspProcesStatusEntry add(Class<? extends BsPage> clazz, BsProcesStatus status, String message) {
    BspProcesStatusEntry entry = new BspProcesStatusEntry(clazz, status, message);
    getList().add(entry);
    return entry;
  }

  public List<BspProcesStatusEntry> getList() {
    return list;
  }

  public List<String> getMessages() {
    List<String> messages = new ArrayList<>();
    for (BspProcesStatusEntry entry : list) {
      if (fil(entry.getMessage())) {
        messages.add(entry.getMessage());
      }
    }
    return messages;
  }

  public static class BspProcesStatusEntry {

    private Class          cl;
    private BsProcesStatus status;
    private String         message;

    public BspProcesStatusEntry(Class cl, BsProcesStatus status, String message) {
      super();
      this.cl = cl;
      this.status = status;
      this.message = message;
    }

    public Class getCl() {
      return cl;
    }

    public void setCl(Class cl) {
      this.cl = cl;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public BsProcesStatus getStatus() {
      return status;
    }

    public void setStatus(BsProcesStatus status) {
      this.status = status;
    }
  }
}
