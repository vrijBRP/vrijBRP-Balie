/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.kassa.page1;

import static nl.procura.standard.Globalfunctions.eq;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.kassa.KassaProductAanvraag;

public class Page1KassaTable extends GbaTable {

  public Page1KassaTable() {
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 20);
    addColumn("Code", 60);
    addColumn("Tijd", 130);
    addColumn("Productgroep");
    addColumn("Product");
    addColumn("Zaak-id");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (KassaProductAanvraag kpa : getApplication().getServices().getKassaService().getVerversdeWinkelwagen()) {
      Record r = addRecord(kpa);
      r.addValue(getRecords().size());
      r.addValue(kpa.getKassaProduct().getKassa());
      r.addValue(kpa.getTijdstip());

      String type = kpa.getKassaProduct().getKassaType().getOms();
      String descr = kpa.getKassaProduct().getDescr();

      if (eq(type, descr)) {
        descr = "";
      }

      r.addValue(type);
      r.addValue(descr);
      r.addValue(defaultIfBlank(kpa.getZaakId(), "N.v.t."));
    }

    super.setRecords();
  }
}
