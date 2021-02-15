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

package nl.procura.gba.web.components.layouts.page;

import com.vaadin.ui.Label;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.Services;

public abstract class MainLabel extends Label {

  private final GbaApplication application;
  private boolean              add = false;

  public MainLabel(GbaApplication application, String header) {
    super(header);
    setSizeUndefined();
    this.application = application;
    setStyleName("indicatie warn");
    doCheck();
  }

  public abstract void doCheck();

  public boolean isAdd() {
    return add;
  }

  public void setAdd(boolean add) {
    this.add = add;
  }

  protected Services getServices() {
    return application.getServices();
  }
}
