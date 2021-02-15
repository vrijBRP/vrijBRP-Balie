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

package nl.procura.gbaws.web.vaadin.layouts.pages;

import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;

import nl.procura.gbaws.web.vaadin.application.GbaWsApplication;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class RefreshButton extends NativeButton implements ClickListener {

  private static final String REFRESH_TEXT = "refresh";
  private boolean             refresh      = false;

  public RefreshButton() {
    super("");
    setWidth("30px");
    setHeight("30px");
    addListener((ClickListener) this);
    setDescription("Ververs de gegevens elke 2 seconden");
  }

  public boolean isRefresh() {
    return refresh;
  }

  private void setRefresh(boolean refresh) {
    this.refresh = refresh;
    setStyleName(ProcuraTheme.BUTTON_LINK);
    addStyleName("button-refresh");
    addStyleName(refresh ? "on" : "off");

    GbaWsApplication app = getWebApplication();
    app.getSession().setAttribute(REFRESH_TEXT, refresh ? true : null);
  }

  @Override
  public void buttonClick(ClickEvent event) {
    setRefresh(!refresh);
  }

  @Override
  public void attach() {
    GbaWsApplication app = getWebApplication();
    setRefresh(app.getSession().getAttribute(REFRESH_TEXT) != null);
    super.attach();
  }

  private GbaWsApplication getWebApplication() {
    return (GbaWsApplication) getApplication();
  }
}
