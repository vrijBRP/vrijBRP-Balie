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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page4;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.BeheerTabsheet;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabChangeListener;

public class Tab5DocumentenPage4 extends NormalPageTemplate {

  private final BeheerTabsheet tabs = new BeheerTabsheet();

  public Tab5DocumentenPage4(List<PrintOptie> printOptions) {

    super("Locaties koppelen aan printopties");

    addButton(buttonPrev);

    final PrintOptionsTab printOptiesTab = new PrintOptionsTab(printOptions);
    final CoupleLocsToPrintOptions locTab = new CoupleLocsToPrintOptions(printOptions);

    tabs.addTab("Geselecteerde printopties", (LazyTabChangeListener) () -> printOptiesTab);

    tabs.addTab("Locaties", (LazyTabChangeListener) () -> locTab);

    addComponent(tabs);
    setMargin(true);

  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

}
