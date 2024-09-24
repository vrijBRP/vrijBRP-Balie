/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.onderhoud.page1.tab3;

import nl.procura.gba.web.modules.beheer.onderhoud.OnderhoudTabPage;
import nl.procura.vaadin.component.label.Break;
import nl.procura.vaadin.component.label.H3;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tab3OnderhoudPage extends OnderhoudTabPage {

  public Tab3OnderhoudPage() {
    super("Systeeminformatie");
    setMargin(true);
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      addSpaceInfo();
      addMemoryInfo();
    }

    super.event(event);
  }

  private void addMemoryInfo() {
    addComponent(new Break());
    addComponent(new H3("Geheugen"));
    addComponent(new Page3OnderhoudForm1());
  }

  private void addSpaceInfo() {
    addComponent(new Break());
    addComponent(new H3("Schijfruimte"));
    addComponent(new Page3OnderhoudForm2());
  }
}
