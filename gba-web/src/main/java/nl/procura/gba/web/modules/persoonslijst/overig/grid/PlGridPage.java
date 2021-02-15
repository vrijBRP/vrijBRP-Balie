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

package nl.procura.gba.web.modules.persoonslijst.overig.grid;

import static nl.procura.standard.Globalfunctions.fil;

import java.util.Comparator;

import com.vaadin.ui.Embedded;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.persoonslijst.overig.meta.PlMetaPage;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.Icons;

public class PlGridPage extends PlMetaPage {

  private final GbaTable table;

  public PlGridPage(String title, BasePLSet set, BasePLRec record) {

    super(title, set, record);

    table = new GbaTable() {

      @Override
      public void setColumns() {

        addColumn("Authentiek", 80).setClassType(Embedded.class);
        addColumn("Element", 80);
        addColumn("Omschrijving", 300);
        addColumn("Waarde");

        super.setColumns();
      }

      @Override
      public void setRecords() {

        try {

          BasePLRec gbaRecord = getGbaRecord();

          gbaRecord.getElems().sort(new Sorter());

          for (BasePLElem gbaElement : gbaRecord.getElems()) {

            RecordElementCombo re = new RecordElementCombo(gbaRecord, gbaElement);

            Record r = addRecord(re);
            String w = gbaElement.getValue().getDescr();

            r.addValue(new TableImage(
                Icons.getIcon(re.getPleElement().isAuthentic() ? Icons.ICON_OK : Icons.ICON_ERROR)));
            r.addValue(re.getGbaElement().getElemCode());
            r.addValue(re.getPleElement().getElem().getDescr());

            r.addValue(fil(w) ? w : "-");
          }
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }
    };

    addExpandComponent(table);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      removeButton(getTypeButton(), getZoekButton());
    }

    super.event(event);
  }

  @Override
  public void setLayouts() {

    super.setLayouts();

    if (table != null) {
      table.init();
    }
  }

  private class Sorter implements Comparator<BasePLElem> {

    @Override
    public int compare(BasePLElem o1, BasePLElem o2) {
      return Integer.compare(o1.getElemCode(), o2.getElemCode());
    }
  }
}
