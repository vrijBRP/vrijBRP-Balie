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

package nl.procura.gba.web.modules.zaken.protocol.page1;

import static nl.procura.standard.Globalfunctions.along;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.beheer.protocollering.page1.protocol.ProtocolExclusionWindow;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.protocol.page2.Page2ZakenProtocollering;
import nl.procura.gba.web.services.zaken.protocol.ProtocolZoekopdracht;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1ZakenProtocollering extends ZakenPage {

  protected final Button    buttonExclusions = new Button("Uitzonderingen");
  private FormAndTablePage1 form;

  public Page1ZakenProtocollering() {
    super("Protocolleringsgegevens");
    addButton(buttonSearch);
    addButton(buttonExclusions);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      try {
        long anr = along(getPl().getPersoon().getAnr().getCode());
        form = new FormAndTablePage1(anr) {

          @Override
          public void onSelectRecord(ProtocolZoekopdracht zoekopdracht) {
            getNavigation().goToPage(new Page2ZakenProtocollering(zoekopdracht));
            super.onSelectRecord(zoekopdracht);
          }
        };

        addExpandComponent(form);

        form.onSearch();
      } catch (Exception e) {
        getApplication().handleException(getWindow(), e);
      }
    }

    super.event(event);
  }

  public void onClick() {
  }

  @Override
  public void buttonClick(Button.ClickEvent event) {
    if (buttonExclusions == event.getButton()) {
      onShowExclusion();
    }
    super.buttonClick(event);
  }

  @Override
  public void onEnter() {
    onSearch();
    super.onEnter();
  }

  @Override
  public void onSearch() {
    form.onSearch();
    super.onSearch();
  }

  private void onShowExclusion() {
    getWindow().addWindow(new ProtocolExclusionWindow());
  }
}
