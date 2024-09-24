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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.verwijderen.page1;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.verwijderen.page2.Page2Verwijderen;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.opschonen.VerwijderZakenActie;
import nl.procura.gba.web.services.zaken.opschonen.VerwijderZakenOverzicht;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Verwijderen extends NormalPageTemplate {

  private Table table;

  public Page1Verwijderen() {
    super("Zaken verwijderen");
    setSpacing(true);
    addButton(buttonSearch);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (getApplication().isProfielActie(ProfielActie.DELETE_VERLOPEN_ZAKEN)) {
        addButton(buttonDel);
      }

      table = new Table();
      addExpandComponent(table);

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onSearch() {
    table.init();
    super.onSearch();
  }

  @Override
  public void onDelete() {
    if (getApplication().isProfielActie(ProfielActie.DELETE_VERLOPEN_ZAKEN)) {
      List<VerwijderZakenActie> records = table.getSelectedValues(VerwijderZakenActie.class);

      if (records.isEmpty()) {
        throw new ProException(SELECT, WARNING, "Geen records geselecteerd.");
      }

      getParentWindow().addWindow(new ConfirmDialog("Weet u het zeker?",
          "U wilt deze zaken verwijderen?", "350px") {

        @Override
        public void buttonYes() {
          int count = records.stream()
              .mapToInt(actie -> getServices().getZakenVerwijderService().verwijder(actie))
              .sum();
          successMessage("Er " + ((count == 1) ? "is 1 zaak" : "zijn " + count + " zaken") + " verwijderd");
          table.init();
          super.buttonYes();
        }
      });

      super.onDelete();
    }
  }

  public class Table extends GbaTable {

    public Table() {
    }

    @Override
    public void onDoubleClick(Record record) {
      getNavigation().goToPage(new Page2Verwijderen(record.getObject(VerwijderZakenActie.class)));
      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {
      setClickable(true);
      setSelectable(true);
      setMultiSelect(true);

      addColumn("Zaaktype", 500);
      addColumn("Bewaartermijn", 100);
      addColumn("Aantallen");
      super.setColumns();
    }

    @Override
    public void setRecords() {
      VerwijderZakenOverzicht overzicht = new VerwijderZakenOverzicht();
      for (VerwijderZakenActie actie : overzicht.getActies()) {
        Record record = addRecord(actie);
        record.addValue(actie.getVerwijderActie().getType().getOmschrijving());
        record.addValue(actie.getVerwijderActie().getBewaarTermijnInJaren() + " jaar");
        record.addValue(actie.getVerwijderActie().getAantal());
      }
      super.setRecords();
    }
  }
}
