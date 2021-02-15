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

package nl.procura.gba.web.modules.zaken.vog.page20;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.vog.page21.Page21Vog;
import nl.procura.gba.web.services.zaken.vog.VogBelanghebbende;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Overzicht belanghebbenden
 */
public class Page20Vog extends ZakenPage {

  private GbaTable table = null;

  public Page20Vog() {

    super("Belanghebbenden");

    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonDel);

    table = new GbaTable() {

      @Override
      public void onDoubleClick(Record record) {
        selectRecord(record);
      }

      @Override
      public void setColumns() {

        setSelectable(true);
        setMultiSelect(true);

        addColumn("Nr", 30);
        addColumn("Naam");
        addColumn("Vertegenwoordiger");

        super.setColumns();
      }

      @Override
      public void setRecords() {

        int i = 0;

        for (VogBelanghebbende b : getApplication().getServices().getVogService().getBelangHebbenden()) {

          i++;

          Record r = addRecord(b);

          r.addValue(i);
          r.addValue(b.getNaam());
          r.addValue(b.getVertegenwoordiger());
        }
      }
    };

    addComponent(table);
  }

  @Override
  public void attach() {

    table.getRecords().clear();

    super.attach();
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<VogBelanghebbende>(table) {

      @Override
      public void deleteValue(VogBelanghebbende belanghebbende) {

        getServices().getVogService().deleteBelanghebbende(belanghebbende);
      }
    };
  }

  @Override
  public void onEnter() {

    if (table.getRecord() != null) {
      selectRecord(table.getRecord());
    }
  }

  @Override
  public void onNew() {

    getNavigation().goToPage(new Page21Vog(new VogBelanghebbende()));

    super.onNew();
  }

  private void selectRecord(Record record) {

    VogBelanghebbende belanghebbende = (VogBelanghebbende) record.getObject();

    getNavigation().goToPage(new Page21Vog(belanghebbende));
  }
}
