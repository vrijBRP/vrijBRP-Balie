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

package nl.procura.gba.web.modules.bs.huwelijk.page20.locaties;

import nl.procura.gba.web.components.fields.GeldigheidField;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.bs.huwelijk.page20.Page20HuwelijkForm1;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class HuwelijkLocatiesPage extends ButtonPageTemplate {

  private final Page20HuwelijkForm1 form;
  private final GeldigheidField     geldigheidField;
  private Table1                    table1 = new Table1();

  public HuwelijkLocatiesPage(Page20HuwelijkForm1 form) {

    H2 h2 = new H2("Beschikbare locaties");
    geldigheidField = new GeldigheidField() {

      @Override
      public void onChangeValue(GeldigheidStatus value) {
        table1.init();
      }
    };
    GbaIndexedTableFilterLayout filter = new GbaIndexedTableFilterLayout(table1);

    addButton(buttonClose);
    getButtonLayout().add(h2, getButtonLayout().getComponentIndex(buttonClose));
    getButtonLayout().add(filter, getButtonLayout().getComponentIndex(buttonClose));
    getButtonLayout().add(geldigheidField, getButtonLayout().getComponentIndex(buttonClose));
    getButtonLayout().expand(h2, 1f).widthFull();

    this.form = form;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      setSpacing(true);
      setInfo("Selecteer een huwelijkslocatie");
      setTable1(table1);
      addComponent(getTable1());
    }

    super.event(event);
  }

  public Table1 getTable1() {
    return table1;
  }

  public void setTable1(Table1 table1) {
    this.table1 = table1;
  }

  @Override
  public void onClose() {

    getWindow().closeWindow();
  }

  class Table1 extends GbaTable {

    public Table1() {
      setHeight("400px");
    }

    @Override
    public void onClick(Record record) {
      selectRecord(record);
      super.onClick(record);
    }

    @Override
    public void setColumns() {

      addColumn("Locatie").setUseHTML(true);
      addColumn("Soort", 150);
      addColumn("Toelichting", 300);

      setSelectable(true);
      addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      HuwelijksLocatie lege = new HuwelijksLocatie();
      lege.setHuwelijksLocatie("(Nog geen locatie)");
      lege.setSoort("");
      lege.setToelichting("");

      add(lege);

      for (HuwelijksLocatie l : getServices().getHuwelijkService().getHuwelijksLocaties(
          geldigheidField.getValue())) {
        if (l.getCodeHuwelijksLocatie() > 0) {
          add(l);
        }
      }

      super.setRecords();
    }

    private void add(HuwelijksLocatie l) {

      Record r = addRecord(l);
      r.addValue(GeldigheidStatus.getHtml(l.getHuwelijksLocatie(), l));
      r.addValue(l.getLocatieSoort());
      r.addValue(l.getToelichting());
    }

    private void selectRecord(Record record) {

      HuwelijksLocatie locatie = (HuwelijksLocatie) record.getObject();
      form.setHuwelijksLocatie(locatie.isStored() ? locatie : null);

      form.repaint();
      ((ModalWindow) getWindow()).closeWindow();
    }
  }
}
