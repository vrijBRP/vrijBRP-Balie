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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab4;

import static nl.procura.gba.web.services.zaken.documenten.DocumentType.PL_UITTREKSEL;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.Page8ZakenTab;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.afdrukken.Page8ZakenAfdrukTab;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerkType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page8ZakenTab4 extends Page8ZakenAfdrukTab {

  public Page8ZakenTab4() {
    super("Zakenregister: documenten afdrukken");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPreview);
      addButton(buttonPrint);
      addButton(buttonStatus);
      addButton(buttonRefresh);

      toevoegenBegeleidendeBrieven(DocumentKenmerkType.POST);

      addComponent(getForm());

      setInfo("Alle uittreksels die in de applicatie op <b>opgenomen</b> of <b>in behandeling</b> staan.");

      setTable(new Table1());
      addComponent(getTable());
    }

    super.event(event);
  }

  class Table1 extends Page8ZakenTab4Table {

    @Override
    public void setRecords() {

      try {
        Page8ZakenTab parent = VaadinUtils.getParent(this, Page8ZakenTab.class);

        for (Zaak zaak : parent.getAlle()) {
          addUittreksel(getDocumenten(PL_UITTREKSEL), zaak);
        }
      } catch (Exception e) {
        getApplication().handleException(e);
      }

      super.setRecords();
    }
  }
}
