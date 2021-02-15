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

package nl.procura.gba.web.modules.zaken.tmv.page4;

import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page80.Page80Zaken;
import nl.procura.gba.web.modules.zaken.tmv.layouts.TmvTabPage;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page4Tmv extends TmvTabPage {

  public Page4Tmv(TerugmeldingAanvraag tmv) {
    super(tmv);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (fil(getTmv().getWaarschuwing().getMsg())) {
        addComponent(new InfoLayout("Melding", getTmv().getWaarschuwing().getMsg()));
      }

      if (!isZakenregister()) {
        addComponent(new Page4TmvForm1(getTmv()));
      }

      addComponent(new Page4Table(getTmv()));

      GbaTabsheet tabs = new GbaTabsheet();
      tabs.addStyleName(GbaWebTheme.TABSHEET_BORDERLESS);
      tabs.addStyleName(GbaWebTheme.TABSHEET_LIGHT);

      tabs.addComponent(new Page4TmvForm2(getTmv()));

      VerticalLayout externeAfhandelingTab = new VerticalLayout();
      externeAfhandelingTab.setCaption("Externe registratie");
      externeAfhandelingTab.setSpacing(true);

      externeAfhandelingTab.addComponent(new Page4TmvForm3(getTmv()));
      externeAfhandelingTab.addComponent(new Page4TmvForm4(getTmv()));

      tabs.addComponent(externeAfhandelingTab);

      addComponent(tabs);
    }

    super.event(event);
  }

  private boolean isZakenregister() {
    return VaadinUtils.getParent(this, Page80Zaken.class) != null;
  }
}
