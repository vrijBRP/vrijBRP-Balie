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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.aantekening.page1;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.modules.zaken.aantekening.page1.Page1AantekeningTable;
import nl.procura.gba.web.modules.zaken.aantekening.page2.Page2Aantekening;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekening;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1ZaakAantekening extends NormalPageTemplate {

  private final Zaak            zaak;
  private Page1AantekeningTable table;
  private final ChangeListener  listener;

  public Page1ZaakAantekening(Zaak zaak, ChangeListener listener) {
    this.zaak = zaak;
    this.listener = listener;
    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      table = new Page1AantekeningTable(zaak) {

        @Override
        public void onDoubleClick(Record record) {
          getNavigation().goToPage(
              new Page2Aantekening(record.getObject(PlAantekening.class), listener));
        }
      };

      addButton(buttonNew);
      addButton(buttonDel);

      setInfo("Aantekeningen", "Commentaar van gebruikers op de zaak");
      addComponent(table);

    } else if (event.isEvent(AfterBackwardReturn.class)) {

      table.init();
      listener.onChange();
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<PlAantekening>(table) {

      @Override
      public void deleteValue(PlAantekening plAantekening) {
        getServices().getAantekeningService().delete(plAantekening);
      }

      @Override
      protected void afterDelete() {
        listener.onChange();
      }
    };
  }

  @Override
  public void onNew() {

    PlAantekening aantekening = new PlAantekening();
    aantekening.setZaakId(zaak.getZaakId());
    getNavigation().goToPage(new Page2Aantekening(aantekening, listener));
    super.onNew();
  }
}
