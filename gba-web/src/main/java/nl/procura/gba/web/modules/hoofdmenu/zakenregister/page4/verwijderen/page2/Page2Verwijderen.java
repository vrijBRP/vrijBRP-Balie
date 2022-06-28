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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.verwijderen.page2;

import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2.Page2ZakenTable;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.opschonen.VerwijderZakenActie;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;

public class Page2Verwijderen extends NormalPageTemplate {

  private final Table               table = new Table();
  private final VerwijderZakenActie actie;

  public Page2Verwijderen(VerwijderZakenActie actie) {
    super("Verouderde zaken");
    this.actie = actie;

    setSpacing(true);
    setMargin(true);

    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (getApplication().isProfielActie(ProfielActie.DELETE_VERLOPEN_ZAKEN)) {
        addButton(buttonDel);
      }

      Page2VerwijderenForm form = new Page2VerwijderenForm(actie, table::search);
      HorizontalLayout selectieLayout = new HorizontalLayout();
      IndexedTableFilterLayout filter = new IndexedTableFilterLayout(table);

      filter.addComponent(form, 0);
      filter.setComponentAlignment(form, Alignment.MIDDLE_LEFT);
      filter.setExpandRatio(form, 1f);
      filter.setSizeUndefined();
      filter.setWidth("100%");
      selectieLayout.addComponent(filter);

      addComponent(new Fieldset("Zaken"));
      addComponent(selectieLayout);
      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onDelete() {
    if (getApplication().isProfielActie(ProfielActie.DELETE_VERLOPEN_ZAKEN)) {
      if (!table.getRecords().isEmpty()) {
        getParentWindow().addWindow(new ConfirmDialog("Weet u het zeker?",
            "U wilt deze zaken verwijderen?", "350px") {

          @Override
          public void buttonYes() {
            int count = getServices().getZakenVerwijderService().verwijder(actie);
            successMessage("Er " + ((count == 1) ? "is 1 zaak" : "zijn " + count + " zaken") + " verwijderd");
            table.init();
            super.buttonYes();
          }
        });
      }
    }

    super.onDelete();
  }

  public class Table extends Page2ZakenTable {

    private int aantal = 50;

    public Table() {
    }

    @Override
    public void setColumns() {
      setSelectable(false);
      setClickable(true);

      addColumn("Nr", 50);
      addColumn("Zaaktype");
      addColumn("Gebruiker / profielen");
      addColumn("Status", 130);
      addColumn("Datum ingang", 100);
      addColumn("Ingevoerd op", 130);
    }

    @Override
    public void setRecords() {
      List<ZaakKey> results = actie.getVerwijderActie().getResultaten(aantal);
      int nr = 0;
      for (ZaakKey zaakKey : results) {
        Record r = addRecord(zaakKey).addValues(9);
        r.setValue(0, ++nr);
      }
      buttonDel.setEnabled(!results.isEmpty());
      super.setRecords();
    }

    @Override
    protected void loadZaak(int nr, Record record, Zaak zaak) {
      record.setValue(1, ZaakUtils.getTypeEnOmschrijving(zaak));
      record.setValue(2, getIngevoerdDoor(zaak));
      record.setValue(3, zaak.getStatus());
      record.setValue(4, zaak.getDatumIngang());
      record.setValue(5, zaak.getDatumTijdInvoer());
    }

    public void search(Integer aantal) {
      this.aantal = aantal;
      init();
    }
  }
}
