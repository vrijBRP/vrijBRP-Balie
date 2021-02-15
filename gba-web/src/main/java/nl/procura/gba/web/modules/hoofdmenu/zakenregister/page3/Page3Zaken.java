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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page3;

import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2.Page2ZakenTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3Zaken extends ZakenregisterPage<Zaak> {

  private final Page2ZakenTable table;

  public Page3Zaken(Page2ZakenTable table) {

    super(null, "Diagrammen");

    if (table.getRecords().isEmpty()) {
      throw new ProException(INFO, "Er zijn geen zaken geselecteerd om een diagram van te maken.");
    }

    this.table = table;
    setSpacing(true);
    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addComponent(new ZakenChart(table.getAllValues(ZaakKey.class)));
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
