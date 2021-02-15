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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page2;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.hoofdmenu.klapper.KlapperGatenLayout;
import nl.procura.gba.web.modules.hoofdmenu.klapper.PageKlapperTemplate;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperZoekargumenten;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page2Klapper extends PageKlapperTemplate {

  private KlapperGatenLayout          gatenLayout = null;
  private Table                       table       = null;
  private final KlapperZoekargumenten zoekArgumenten;

  public Page2Klapper(KlapperZoekargumenten zoekArgumenten) {
    super("Klapper - gaten");
    this.zoekArgumenten = zoekArgumenten;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonSave);

      table = new Table();
      addExpandComponent(table);

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    List<Record> records = table.getSelectedRecords();

    if (records.isEmpty()) {
      throw new ProException(WARNING, "Geen records geselecteerd");
    }

    for (Record record : records) {
      DossierAkte dossierAkte = record.getObject(DossierAkte.class);

      getServices().getAkteService().save(dossierAkte);

      if (dossierAkte.isStored()) {
        table.setRecordValue(record, 0, setClass(true, "Ja"));
      } else {
        table.setRecordValue(record, 0, setClass(true, "Nee"));
      }
    }

    super.onSave();
  }

  public class Table extends GbaTable {

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Ingevoerd", 70).setUseHTML(true);
      addColumn("Nr", 30);
      addColumn("Datum", 100);
      addColumn("Akte", 100);
      addColumn("Soort");

      super.setColumns();
    }

    @Override
    public void setRecords() {

      List<DossierAkte> gaten = updateInfoLayout();

      int nr = 0;
      for (DossierAkte akteGat : gaten) {
        nr++;
        Record r = addRecord(akteGat);

        if (akteGat.isStored()) {
          r.addValue(setClass(true, "Ja"));
        } else {
          r.addValue(setClass(false, "Nee"));
        }

        r.addValue(nr);
        r.addValue(akteGat.getDatumIngang());
        r.addValue(akteGat.getAkte());
        r.addValue(akteGat.getAkteRegistersoort());
      }
    }

    private List<DossierAkte> updateInfoLayout() {

      List<DossierAkte> aktes = getServices().getAkteService().getAktes(zoekArgumenten);
      gatenLayout = new KlapperGatenLayout(aktes, zoekArgumenten, true, false);

      if (gatenLayout.isGaten()) {
        gatenLayout.append("<hr/>Selecteer de gaten en druk op opslaan om deze te vullen met blanco aktes.");
      }

      List<DossierAkte> gaten = gatenLayout.getGaten();
      removeComponent(gatenLayout);
      addComponent(gatenLayout, getComponentIndex(table));

      return gaten;
    }
  }
}
