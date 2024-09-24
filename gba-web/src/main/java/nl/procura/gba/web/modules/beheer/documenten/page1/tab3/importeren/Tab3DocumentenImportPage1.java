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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab3.importeren;

import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;

public class Tab3DocumentenImportPage1 extends DocumentenTabPage {

  private GbaTable table = null;

  public Tab3DocumentenImportPage1() {

    super("Afnemers importeren uit PROBEV");

    setMargin(true);
  }

  @Override
  public void event(nl.procura.vaadin.component.layout.page.pageEvents.PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSave);

      if (getWindow().isModal()) {
        addButton(buttonClose);
      }

      table = new GbaTable() {

        @Override
        public void setColumns() {

          setSelectable(true);
          setMultiSelect(true);

          addColumn("ID", 50);
          addColumn("Afnemer");
          addColumn("Adres");
        }

        @Override
        public void setRecords() {

          int nr = 0;
          String sql = "select a.afnemer, a.straat, a.hnr, a.hnrL, a.hnrT, a.pc, a.plaats from ProtAfn a";

          for (String[] row : getServices().getProbevSqlService().find(sql)) {

            String straat = row[1];
            String hnr = row[2];
            String hnrL = row[3];
            String hnrT = row[4];
            String pc = row[5];
            String plaats = row[6];

            Adresformats af = new Adresformats();
            af.setValues(straat, hnr, hnrL, hnrT, "", "", pc, "", plaats, "", "", "", "", "", "", "");
            String adres = af.getAdres_pc_wpl();

            DocumentAfnemer afnemer = new DocumentAfnemer();
            afnemer.setDocumentAfn(row[0]);
            afnemer.setAdres(fil(af.getAdres()) ? af.getAdres() : "");
            afnemer.setPostcode(new FieldValue(pc));
            afnemer.setPlaats(plaats);

            nr++;
            Record record = addRecord(afnemer);
            record.addValue(nr);
            record.addValue(row[0]);
            record.addValue(fil(adres) ? adres : "");
          }
        }
      };

      addComponent(table);

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
    }
  }

  public void importAfnemers(List<DocumentAfnemer> afnemers) {

    int size = 0;

    for (DocumentAfnemer afnemer : afnemers) {

      getServices().getDocumentService().save(afnemer);

      size++;
    }

    if (size == 1) {
      successMessage("1 afnemer toegevoegd");
    } else {
      successMessage(size + " afnemers toegevoegd");
    }
  }

  @Override
  public void onClose() {

    if (getWindow().isModal()) {
      getWindow().closeWindow();
    }

    super.onClose();
  }

  @Override
  public void onSave() {

    if (table.isSelectedRecords()) {

      importAfnemers(table.getSelectedValues(DocumentAfnemer.class));
    } else {
      throw new ProException("Geen records geselecteerd");
    }

    super.onSave();
  }
}
