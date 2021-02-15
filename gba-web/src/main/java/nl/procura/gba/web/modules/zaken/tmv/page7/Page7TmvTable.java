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

package nl.procura.gba.web.modules.zaken.tmv.page7;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.tmv.page8.Page8Tmv;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingReactie;

public class Page7TmvTable extends GbaTable {

  private TerugmeldingAanvraag tmv = null;

  public Page7TmvTable(TerugmeldingAanvraag tmv) {
    setTmv(tmv);
  }

  public TerugmeldingAanvraag getTmv() {
    return tmv;
  }

  public void setTmv(TerugmeldingAanvraag tmv) {
    this.tmv = tmv;
  }

  @Override
  public void onDoubleClick(Record record) {

    ((Page7Tmv) getParent()).getNavigation().goToPage(
        new Page8Tmv(getTmv(), (TerugmeldingReactie) record.getObject()));

    super.onDoubleClick(record);
  }

  @Override
  public void setColumns() {

    addColumn("Vnr", 20);
    addColumn("Actie", 120);
    addColumn("Tijdstip", 150);
    addColumn("Status");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {

      setSelectable(true);
      setMultiSelect(true);

      List<TerugmeldingReactie> l = getTmv().getReacties();

      int i = l.size();

      for (TerugmeldingReactie reg : l) {

        Record r = addRecord(reg);
        r.addValue(i);
        r.addValue(reg.getIngevoerdDoor());
        r.addValue(reg.getDatumTijdInvoer());
        r.addValue(reg.getTerugmReactie());

        i--;
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
