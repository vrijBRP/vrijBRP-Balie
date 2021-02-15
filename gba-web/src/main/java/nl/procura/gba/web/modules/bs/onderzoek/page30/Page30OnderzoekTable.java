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

package nl.procura.gba.web.modules.bs.onderzoek.page30;

import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow.BronWindow;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;

public class Page30OnderzoekTable extends GbaTable {

  private final DossierOnderzoek zaakDossier;

  public Page30OnderzoekTable(DossierOnderzoek zaakDossier) {
    this.zaakDossier = zaakDossier;
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr.", 50);
    addColumn("Bron");
    addColumn("Betreft");
    addColumn("Adres");

    super.setColumns();
  }

  @Override
  public void setRecords() {
    int nr = 0;

    for (DossierOnderzoekBron bron : zaakDossier.getBronnen()) {
      Record r = addRecord(bron);
      r.addValue(++nr);
      r.addValue(bron.getBron());
      r.addValue(trim(bron.getInst() + ", t.a.v. " + bron.getInstTav()));
      if (VermoedAdresType.BUITENLAND.equals(bron.getAdresType())) {
        r.addValue(bron.getAdres().getAdres());
      } else {
        r.addValue(bron.getAdres().getAdres_pc_wpl_gem());
      }
    }
    super.setRecords();
  }

  @Override
  public void onDoubleClick(Record record) {

    getWindow().addWindow(new BronWindow(zaakDossier, record.getObject(DossierOnderzoekBron.class)) {

      @Override
      public void closeWindow() {
        init();
        super.closeWindow();
      }
    });

    super.onDoubleClick(record);
  }
}
