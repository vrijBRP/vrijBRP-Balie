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

package nl.procura.gba.web.modules.beheer.documenten.printoptie;

import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.components.dialogs.KoppelProcedure;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.overig.KoppelForm;
import nl.procura.gba.web.modules.beheer.overig.KoppelPage;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanPrintOptie;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class KoppelPrintoptiePage<K extends KoppelbaarAanPrintOptie> extends KoppelPage {

  private final KoppelForm         form;
  private final List<K>            koppelList;
  private KoppelPrintoptieTabel<K> table = null;
  private final String             type;

  public KoppelPrintoptiePage(List<K> koppelList, String type) {

    super("");
    setMargin(false);

    this.type = type;
    this.koppelList = koppelList;

    setInfo("Klik één keer op de regel om deze te selecteren, dubbelklik om de status te wijzigen. <br>"
        + "De status is 'Gekoppeld' als <b>alle</b> geselecteerde " + type + " gekoppeld zijn aan de printoptie.");

    form = new KoppelForm();

    HorizontalLayout hL = new HorizontalLayout();
    hL.addComponent(form);

    setTable();

    hL.addComponent(new GbaIndexedTableFilterLayout(table));
    hL.setExpandRatio(form, 1f);
    addComponent(hL);
    addExpandComponent(table);
  }

  /**
   * Nodig als deze pagina ergens ingevoegd wordt waar al een 'vorige' knop aanwezig is.
   */
  public void disablePreviousButton() {
    buttonPrev.setVisible(false);
  }

  @Override
  protected void allesKoppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, type, true) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        KoppelPrintoptiePage.this.couple(koppelActie, wholeTable);
      }
    };
  }

  protected void couple(KoppelActie koppelActie, boolean wholeTable) {

    List<PrintOptie> printOpties = getSelectedPrintoptions(wholeTable);

    koppelActiePrintOpties(koppelActie, koppelList, printOpties);

    setTableStatus(wholeTable ? table.getRecords() : table.getSelectedRecords(), koppelActie);
  }

  @Override
  protected void koppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, type, false) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        KoppelPrintoptiePage.this.couple(koppelActie, wholeTable);
      }
    };
  }

  private List<PrintOptie> getSelectedPrintoptions(boolean wholeTable) {

    List<PrintOptie> l;

    if (wholeTable) {
      l = table.getAllValues(PrintOptie.class);
    } else {
      l = table.getSelectedValues(PrintOptie.class);
    }

    return l;
  }

  private void setTable() {

    table = new KoppelPrintoptieTabel<K>(koppelList) {

      @Override
      public void setContainerDataSource(Container newDataSource) {

        super.setContainerDataSource(newDataSource);
        form.check(this);
      }

      @Override
      public void setRecordValue(Record record, Object propertyId, Object value) {
        super.setRecordValue(record, propertyId, value);
        form.check(this);
      }
    };
  }

  private void setTableStatus(List<Record> selectedRecords, KoppelActie koppelActie) {

    for (Record r : selectedRecords) {

      table.setRecordValue(r, 0, koppelActie.getStatus());
    }
  }
}
