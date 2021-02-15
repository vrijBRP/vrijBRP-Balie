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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page5;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.beheer.KoppelActie.KOPPEL;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.dialogs.KoppelProcedure;
import nl.procura.gba.web.modules.beheer.overig.KoppelPage;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratie;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieSoortType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page5Documenten extends KoppelPage {

  private static final int                  INDEX_STATUS = 0;
  private final List<KoppelenumTable>       tables       = new ArrayList<>();
  private final KoppelEnumeratieSoortType[] soortTypes;
  private DocumentRecord                    documentRecord;

  public Page5Documenten(DocumentRecord documentRecord, KoppelEnumeratieSoortType... soortTypes) {

    super("Koppelen gegevensverstrekking: " + documentRecord.getDocument());

    this.documentRecord = documentRecord;
    this.soortTypes = soortTypes;

    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setSpacing(true);
      setInfo("Klik één keer op de regel om deze te selecteren, dubbelklik om de status te wijzigen.");
      setTable();
    }

    super.event(event);
  }

  public DocumentRecord getDocumentRecord() {
    return documentRecord;
  }

  public void setDocumentRecord(DocumentRecord documentRecord) {
    this.documentRecord = documentRecord;
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  protected void allesKoppelActie(KoppelActie koppelActie) {

    for (KoppelenumTable table : tables) {

      new KoppelProcedure(table, koppelActie, "document records", true) {

        @Override
        public void koppel(KoppelActie koppelActie, boolean wholeTable) {
          Page5Documenten.this.couple(koppelActie, wholeTable);
        }
      };
    }
  }

  protected void couple(KoppelActie koppelActie, boolean wholeTable) {

    for (KoppelenumTable table : tables) {
      List<KoppelEnumeratie> koppelenums = getSelectedKoppelenumen(wholeTable);
      koppelOntkoppelDocumentRecorden(koppelActie, koppelenums, documentRecord);
      setTableStatus(table, wholeTable ? table.getRecords() : table.getSelectedRecords(), koppelActie);
    }
  }

  @Override
  protected void koppelActie(KoppelActie koppelActie) {

    for (KoppelenumTable table : tables) {

      new KoppelProcedure(table, koppelActie, "document records", false) {

        @Override
        public void koppel(KoppelActie koppelActie, boolean wholeTable) {
          Page5Documenten.this.couple(koppelActie, wholeTable);
        }
      };
    }
  }

  private List<KoppelEnumeratie> getSelectedKoppelenumen(boolean wholeTable) {

    List<KoppelEnumeratie> l = null;

    for (KoppelenumTable table : tables) {
      if (wholeTable) {
        l = table.getAllValues(KoppelEnumeratie.class);
      } else {
        l = table.getSelectedValues(KoppelEnumeratie.class);
      }
    }

    return l;
  }

  private Object getStatus(KoppelActie koppelActie) {

    if (KOPPEL.equals(koppelActie)) {
      return setClass("green", "Gekoppeld");
    }

    // ontkoppelen
    return setClass("red", "Niet-gekoppeld");
  }

  private void setTable() {

    for (KoppelEnumeratieSoortType soortType : soortTypes) {

      KoppelenumTable table = new KoppelenumTable(getDocumentRecord(), soortType) {

      };

      tables.add(table);

      addComponent(new Fieldset(soortType.getOms(), table));
    }
  }

  private void setTableStatus(KoppelenumTable table, List<Record> selectedRecords, KoppelActie koppelActie) {

    for (Record r : selectedRecords) {
      table.setRecordValue(r, INDEX_STATUS, getStatus(koppelActie));
    }
  }
}
