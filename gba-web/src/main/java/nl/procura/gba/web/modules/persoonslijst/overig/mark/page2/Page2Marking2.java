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

package nl.procura.gba.web.modules.persoonslijst.overig.mark.page2;

import nl.procura.gba.jpa.personen.db.RiskProfileSig;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.QuickSearchAddressWindow;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Marking2 extends NormalPageTemplate {

  private Page2MarkingTable2 table;

  public Page2Marking2() {
    setSpacing(true);
    setSizeFull();
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonNew.setCaption("Toevoegen (F7)");
      addButton(buttonNew);
      addButton(buttonDel, 1f);
      addButton(buttonClose);

      table = new Page2MarkingTable2();
      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onNew() {
    getParentWindow().addWindow(new QuickSearchAddressWindow(address -> {
      RiskAnalysisService service = getServices().getRiskAnalysisService();
      service.switchSignaling(service.buildSignal(address));
      table.init();

    }, address -> {
      RiskAnalysisService service = getServices().getRiskAnalysisService();
      return service.getSignal(service.buildSignal(address)).isPresent();
    }));

    super.onNew();
  }

  @Override
  public void onDelete() {
    table.getSelectedValues(RiskProfileSig.class)
        .forEach(sig -> getServices()
            .getRiskAnalysisService().delete(sig));
    table.init();
    super.onDelete();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }
}
