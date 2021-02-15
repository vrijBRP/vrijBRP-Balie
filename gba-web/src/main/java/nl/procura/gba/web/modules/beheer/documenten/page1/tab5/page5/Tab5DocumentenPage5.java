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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page5;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.BeheerTabsheet;
import nl.procura.gba.web.modules.beheer.documenten.KoppelDocumentPage;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabChangeListener;

public class Tab5DocumentenPage5 extends NormalPageTemplate {

  private final BeheerTabsheet   tabs = new BeheerTabsheet();
  private final List<PrintOptie> printopties;

  public Tab5DocumentenPage5(List<PrintOptie> printopties) {

    super("Documenten koppelen aan printopties");

    this.printopties = printopties;

    addButton(buttonPrev);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      final SelectedPrintOptionsPage printOptiesTab = new SelectedPrintOptionsPage(printopties);
      final KoppelDocumentPage docTab = new KoppelDocumentPage<>(printopties, "printopties");

      tabs.addTab("Geselecteerde printopties", (LazyTabChangeListener) () -> printOptiesTab);

      tabs.addTab("Documenten", (LazyTabChangeListener) () -> {
        docTab.disablePreviousButton();
        return docTab;
      });

      addComponent(tabs);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
