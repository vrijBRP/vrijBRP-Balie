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

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.ServiceListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.theme.GbaWebTheme;

public abstract class MainButton<T extends AbstractService> extends Button implements ClickListener {

  public MainButton() {
    addStyleName(GbaWebTheme.BUTTON_LINK);
    addStyleName("buttonlink");
    addListener((ClickListener) this);
  }

  @Override
  public void attach() {
    final String windowName = getWindow().getName();
    getService().setListener(new ServiceListener() {

      @Override
      public void action(ServiceEvent event) {
        if (event == ServiceEvent.CHANGE) {
          setStyleName(GbaWebTheme.BUTTON_LINK);
          addStyleName("buttonlink");
          doCheck();
        }
      }

      @Override
      public String getId() {
        return getListenerId() + windowName;
      }
    });

    doCheck();
    super.attach();
  }

  @Override
  public void buttonClick(ClickEvent event) {
    onClick();
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  protected abstract void doCheck();

  protected abstract String getListenerId();

  protected abstract T getService();

  protected Services getServices() {
    return getApplication().getServices();
  }

  protected abstract void onClick();

}
