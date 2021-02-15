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

package nl.procura.gba.web.modules.zaken.naamgebruik.page1;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.dialogs.ZaakConfiguratieDialog;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.modules.zaken.common.ZakenListPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.modules.zaken.naamgebruik.page2.Page2Naamgebruik;
import nl.procura.gba.web.modules.zaken.naamgebruik.page4.Page4Naamgebruik;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.naamgebruik.NaamgebruikAanvraag;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Overzicht
 */
public class Page1Naamgebruik extends ZakenListPage<NaamgebruikAanvraag> {

  public Page1Naamgebruik() {
    super("Naamgebruik ");
    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonStatus);
    addButton(buttonDel);
  }

  @Override
  protected ZaakArgumenten getZaakArgumenten() {
    return new ZaakArgumenten(getPl(), ZaakType.NAAMGEBRUIK);
  }

  @Override
  protected void setTableColumns(ZakenListTable table) {
    table.addColumn("Nr", 30);
    table.addColumn("Ingevoerd op", 130);
    table.addColumn("Status", 120).setUseHTML(true);
    table.addColumn("Ingevoerd door", 200);
    table.addColumn("Datum ingang", 100);
    table.addColumn("Naamgebruik");
  }

  @Override
  protected void selectTableRecord(ZaakRecord<NaamgebruikAanvraag> zaakRecord) {
    getNavigation().goToPage(new Page4Naamgebruik(zaakRecord.getZaak()));
  }

  @Override
  protected void loadTableRecord(Record record, ZaakRecord<NaamgebruikAanvraag> zaakRecord) {
    NaamgebruikAanvraag zaak = zaakRecord.getZaak();
    record.getValues().get(0).setValue(zaakRecord.getNr());
    record.getValues().get(1).setValue(zaak.getDatumTijdInvoer());
    record.getValues().get(2).setValue(ZaakUtils.getStatus(zaak.getStatus()));
    record.getValues().get(3).setValue(zaak.getIngevoerdDoor().getDescription());
    record.getValues().get(4).setValue(zaak.getDatumIngang());
    record.getValues().get(5).setValue(zaak.getNaamgebruikType().getDescription());
  }

  @Override
  public void onNew() {
    NaamgebruikAanvraag zaak = (NaamgebruikAanvraag) getServices().getNaamgebruikWijzigingService().getNewZaak();
    ZaakConfiguratieDialog.of(getApplication(), zaak, getServices(), () -> {
      Page2Naamgebruik nextPage = new Page2Naamgebruik(zaak);
      IdentificatieContactUtils.startProcess(this, nextPage, true);
    });
  }
}
