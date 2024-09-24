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

package nl.procura.gba.web.modules.beheer.kassa.page2;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.kassa.KassaProduct;
import nl.procura.gba.web.services.beheer.kassa.KassaService;

public class Page2KassaTable extends GbaTable {

  private KassaProduct kassaProduct;

  public Page2KassaTable() {
  }

  public void init(KassaProduct kassaProduct) {

    this.kassaProduct = kassaProduct;

    init();
  }

  @Override
  public void onDoubleClick(Record record) {

    KassaProduct koppelProduct = (KassaProduct) record.getObject();

    boolean gekoppeld = kassaProduct.isGekoppeld(koppelProduct);

    getService().koppelActie(kassaProduct, koppelProduct);

    setRecordValue(record, 1, KoppelActie.get(!gekoppeld).getStatus());

    super.onDoubleClick(record);
  }

  @Override
  public void setColumns() {

    setSelectable(true);

    addColumn("ID", 50);
    addColumn("Status", 120).setUseHTML(true);
    addColumn("Type", 200);
    addColumn("Omschrijving");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    if (kassaProduct != null) {
      kassaProduct = getService().getKassaProduct(kassaProduct);

      for (KassaProduct kassa : getService().getKassaProducten()) {
        if (!kassa.isKassaBundel()) {
          boolean gekoppeld = kassaProduct.isGekoppeld(kassa);
          Record r = addRecord(kassa);
          r.addValue(kassa.getCKassa());
          r.addValue(KoppelActie.get(gekoppeld).getStatus());
          r.addValue(kassa.getKassaType());
          r.addValue(kassa.getDescr());
        }
      }
    }

    super.setRecords();
  }

  private KassaService getService() {
    return getApplication().getServices().getKassaService();
  }
}
