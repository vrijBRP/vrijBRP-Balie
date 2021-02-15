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

package nl.procura.gba.web.modules.zaken.tmv.layouts;

import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.TextArea;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.tmv.objects.TmvRecord;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class TmvOpslagLayout extends GbaVerticalLayout {

  private List<TmvRecord> list        = new ArrayList<>();
  private GbaTable        table       = null;
  private TextArea        toelichting = new TextArea();

  public TmvOpslagLayout(List<TmvRecord> list) {

    setList(list);

    getToelichting().setWidth("100%");
    getToelichting().setInputPrompt("Geef een toelichting");
    getToelichting().setRequired(true);
    getToelichting().focus();
    addComponent(getToelichting());

    setTable(new GbaTable() {

      @Override
      public void setColumns() {

        addColumn("Categorie", 90);
        addColumn("Elem.", 40);
        addColumn("Omschrijving", 200);
        addColumn("Set", 20);
        addColumn("Huidige waarde");
        addColumn("Nieuwe waarde");

        super.setColumns();
      }

      @Override
      public void setPageLength(int pageLength) {
        super.setPageLength(getRecords().size() + 2);
      }

      @Override
      public void setRecords() {

        try {

          for (TmvRecord tmv : getList()) {

            Record r = addRecord(tmv);
            r.addValue(GBACat.getByCode(aval(tmv.getCat())).getDescr());
            r.addValue(tmv.getElem());
            r.addValue(tmv.getDescr());
            r.addValue(tmv.getSet() + 1);
            r.addValue(tmv.getHuidigeWaarde().getDescr());
            r.addValue(tmv.getNieuweFormatWaarde());
          }
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }
    });

    addExpandComponent(getTable());
  }

  public List<TmvRecord> getList() {
    return list;
  }

  public void setList(List<TmvRecord> list) {
    this.list = list;
  }

  public GbaTable getTable() {
    return table;
  }

  public void setTable(GbaTable table) {
    this.table = table;
  }

  public List<TmvRecord> getTmvRecords() {

    List<TmvRecord> l = new ArrayList<>();

    for (Record record : getTable().getRecords()) {
      l.add((TmvRecord) record.getObject());
    }

    return l;
  }

  public TextArea getToelichting() {
    return toelichting;
  }

  public void setToelichting(TextArea toelichting) {
    this.toelichting = toelichting;
  }
}
