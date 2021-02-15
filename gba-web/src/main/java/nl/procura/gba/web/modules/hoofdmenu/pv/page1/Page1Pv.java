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

package nl.procura.gba.web.modules.hoofdmenu.pv.page1;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.hoofdmenu.pv.page3.Page3Pv;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.PresentievragenTable;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagZoekargumenten;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Pv extends NormalPageTemplate {

  private final List<Presentievraag> aanvragen       = new ArrayList<>();
  private final Button               buttonResetForm = new Button("Reset");
  private final Button               buttonPrint     = new Button("Afdrukken");
  private Table                      table           = null;
  private Page1PvForm                form            = null;

  public Page1Pv() {
    super("Presentievragen - overzicht");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSearch);
      addButton(buttonDel);
      addButton(buttonResetForm);
      addButton(buttonPrint);

      form = new Page1PvForm() {

        @Override
        public void onReload() {
          onSearch();
        }
      };

      // Alle componenten
      table = new Table();
      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
      addComponent(form);
      addExpandComponent(table);
      onSearch();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonResetForm) {
      form.reset();
    } else if (button == buttonPrint) {
      if (table.isSelectedRecords()) {
        Page3Pv page = new Page3Pv(table.getSelectedValues(Presentievraag.class));
        getNavigation().goToPage(page);
      } else {
        throw new ProException(ProExceptionSeverity.WARNING, "Geen records geselecteerd.");
      }
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<Presentievraag>(table) {

      @Override
      public void afterDelete() {
        onSearch();
      }

      @Override
      public void deleteValue(Presentievraag presentievraag) {
        getServices().getPresentievraagService().delete(presentievraag);
      }
    };
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onSearch() {

    zoekZaken();
    table.init();
  }

  private void zoekZaken() {

    form.commit();

    PresentievraagZoekargumenten z = new PresentievraagZoekargumenten();
    z.setDatumInvoerVanaf(form.getDatumVan());
    z.setDatumInvoerTm(form.getDatumTm());
    z.setInhoudBericht(form.getBean().getInhoudBericht());

    aanvragen.clear();
    aanvragen.addAll(getServices().getPresentievraagService().getPresenceQuestionsByArguments(z));
  }

  public class Table extends PresentievragenTable {

    public Table() {
      super(aanvragen);
    }

    @Override
    public void setColumns() {
      addColumn("Nr", 30);
      addColumn("Tijdstip", 150);
      addColumn("Bericht");
      addColumn("Resultaat", 300).setUseHTML(true);
      addColumn("Betrekking op zaak");
    }
  }
}
