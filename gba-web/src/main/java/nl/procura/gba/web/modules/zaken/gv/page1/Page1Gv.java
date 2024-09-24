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

package nl.procura.gba.web.modules.zaken.gv.page1;

import static nl.procura.gba.web.services.zaken.algemeen.ZaakUtils.getDatumEnDagenTekst;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.common.ZakenListPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.modules.zaken.gv.page2.Page2Gv;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Overzicht
 */
public class Page1Gv extends ZakenListPage<GvAanvraag> {

  public Page1Gv() {
    super("Gegevensverstrekking");
    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonStatus);
    addButton(buttonDel);
  }

  @Override
  protected ZaakArgumenten getZaakArgumenten() {
    return new ZaakArgumenten(getPl(), ZaakType.GEGEVENSVERSTREKKING);
  }

  @Override
  protected void setTableColumns(ZakenListTable table) {
    table.addColumn("Nr", 30);
    table.addColumn("Ingevoerd op", 130);
    table.addColumn("Status", 120).setUseHTML(true);
    table.addColumn("Ingevoerd door", 200);
    table.addColumn("Verloopdatum termijn", 220).setUseHTML(true);
    table.addColumn("Afnemer");
  }

  @Override
  protected void selectTableRecord(ZaakRecord<GvAanvraag> zaakRecord) {
    GvAanvraag zaak = zaakRecord.getZaak();
    getNavigation().goToPage(new Page2Gv(zaak));
  }

  @Override
  protected void loadTableRecord(Record record, ZaakRecord<GvAanvraag> zaakRecord) {
    GvAanvraag zaak = zaakRecord.getZaak();
    record.getValues().get(0).setValue(zaakRecord.getNr());
    record.getValues().get(1).setValue(zaak.getDatumTijdInvoer());
    record.getValues().get(2).setValue(ZaakUtils.getStatus(zaak.getStatus()));
    record.getValues().get(3).setValue(zaak.getIngevoerdDoor().getDescription());

    if (zaak.isDatumEindeTermijn()) {
      record.getValues().get(4).setValue(getDatumEnDagenTekst(zaak.getDatumEindeTermijn().getStringDate()));
    } else {
      record.getValues().get(4).setValue("Geen");
    }

    record.getValues().get(5).setValue(zaak.getAanvrager());
  }

  @Override
  public void onNew() {

    GvAanvraag gv = (GvAanvraag) getApplication().getServices().getGegevensverstrekkingService().getNewZaak();
    gv.setBasisPersoon(getPl());

    getApplication().getServices().getMemoryService().setObject(GvAanvraag.class, gv);
    getApplication().openWindow(getWindow(), new HomeWindow(), ZaakFragment.FR_GV);

    super.onNew();
  }
}
