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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab3;

import static nl.procura.gba.web.services.zaken.documenten.DocumentType.PL_UITTREKSEL;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.Page8ZakenTab;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.afdrukken.Page8ZakenAfdrukTab;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerkType;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page8ZakenTab3 extends Page8ZakenAfdrukTab {

  public Page8ZakenTab3() {
    super("Zakenregister: documenten afdrukken");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPreview);
      addButton(buttonPrint);
      addButton(buttonStatus);
      addButton(buttonRefresh);

      toevoegenBegeleidendeBrieven(DocumentKenmerkType.MIJN_OVERHEID);
      toevoegenUitvoer(PrintOptieType.MIJN_OVERHEID);

      addComponent(getForm());

      setInfo("Alle uittreksels die in de applicatie op <b>opgenomen</b> of <b>in behandeling</b> staan.");

      setTable(new Table1());
      addExpandComponent(getTable());
    }

    super.event(event);
  }

  class Table1 extends Page8ZakenTab3Table {

    @Override
    public void setRecords() {

      try {
        Page8ZakenTab parent = getParentTab();
        for (Zaak zaak : parent.getWel()) {
          addUittreksel(getDocumenten(PL_UITTREKSEL), zaak);
        }
      } catch (Exception e) {
        getApplication().handleException(e);
      }

      super.setRecords();
    }
  }
}
