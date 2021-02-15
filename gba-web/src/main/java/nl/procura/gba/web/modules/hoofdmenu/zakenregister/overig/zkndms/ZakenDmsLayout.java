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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.zkndms.page1.Page1ZknDms;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class ZakenDmsLayout extends GbaVerticalLayout implements ZaakTabLayout, ClickListener {

  private Tab tab;

  public ZakenDmsLayout(Zaak zaak) {
    addComponent(new MainModuleContainer(false, new Page1ZknDms(zaak)));
  }

  @Override
  public void buttonClick(ClickEvent event) {
  }

  @SuppressWarnings("unused")
  public String getHeader(GbaApplication application) {
    return "Zaaksysteem";
  }

  @Override
  public void reloadLayout(GbaApplication application, Zaak zaak) {
    tab.setCaption(getHeader(application));
  }

  public void setTab(Tab tab) {
    this.tab = tab;
  }

  protected void reloadTree() {
  }
}
