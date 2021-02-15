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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2.Page2ZakenTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.telling.Telling;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Afdrukken zaken
 */
public class Page7Zaken extends ZakenregisterPage {

  private final ZaakPeriode     zaakPeriode;
  private final Page2ZakenTable table;
  private PrintMultiLayout      printLayout;

  public Page7Zaken(ZaakPeriode zaakPeriode, Page2ZakenTable table) {

    super(null, "Zakenregister - afdrukken");

    this.table = table;
    this.zaakPeriode = zaakPeriode;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      List<ZaakKey> zaakKeys = table.getAllValues(ZaakKey.class);
      ZakenService zaken = getApplication().getServices().getZakenService();
      List<Zaak> minimaleZaken = zaken.getMinimaleZaken(new ZaakArgumenten(zaakKeys));
      Telling telling = new Telling(getApplication().getServices(), zaakPeriode, minimaleZaken);

      printLayout = new PrintMultiLayout(telling, null, null, DocumentType.TELLING);
      printLayout.setInfo(getInfo());

      addButton(buttonPrev);
      addButton(getPrintButtons());
      addExpandComponent(printLayout);
    }

    super.event(event);
  }

  public Button[] getPrintButtons() {
    return printLayout.getButtons();
  }

  public PrintMultiLayout getPrintLayout() {
    return printLayout;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    printLayout.handleActions(button, keyCode);
    super.handleEvent(button, keyCode);
  }

  private String getInfo() {
    return "Controleer de gegevens op het document nauwkeurig voordat u het document uitreikt.";
  }
}
