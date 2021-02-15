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

package nl.procura.gba.web.modules.beheer.protocollering.page2;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.protocollering.page3.Page3Protocollering;
import nl.procura.gba.web.modules.zaken.protocol.page2.FormAndTablePage2;
import nl.procura.gba.web.services.zaken.protocol.ProtocolRecord;
import nl.procura.gba.web.services.zaken.protocol.ProtocolZoekopdracht;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Protocollering extends NormalPageTemplate {

  private final ProtocolZoekopdracht zoekopdracht;

  public Page2Protocollering(ProtocolZoekopdracht zoekopdracht) {

    super("Zoeken naar protocolleringsgegevens");

    this.zoekopdracht = zoekopdracht;

    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      FormAndTablePage2 form = new FormAndTablePage2(zoekopdracht) {

        @Override
        public void onSelectRecord(ProtocolRecord record) {
          selectRecord(record);
        }
      };

      addExpandComponent(form);

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(form.getTable()));
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private void selectRecord(ProtocolRecord record) {
    getNavigation().goToPage(new Page3Protocollering(record));
  }
}
