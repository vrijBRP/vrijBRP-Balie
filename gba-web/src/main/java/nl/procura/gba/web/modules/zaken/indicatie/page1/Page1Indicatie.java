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

package nl.procura.gba.web.modules.zaken.indicatie.page1;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.modules.zaken.common.ZakenListPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.modules.zaken.indicatie.page2.Page2Indicatie;
import nl.procura.gba.web.modules.zaken.indicatie.page4.Page4Indicatie;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.indicaties.IndicatieAanvraag;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Overzicht
 */
public class Page1Indicatie extends ZakenListPage<IndicatieAanvraag> {

  public Page1Indicatie() {
    super("Indicatie ");
    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonStatus);
    addButton(buttonDel);
  }

  @Override
  protected ZaakArgumenten getZaakArgumenten() {
    return new ZaakArgumenten(getPl(), ZaakType.INDICATIE);
  }

  @Override
  protected void setTableColumns(ZakenListTable table) {
    table.addColumn("Nr", 30);
    table.addColumn("Ingevoerd op", 130);
    table.addColumn("Status", 120).setUseHTML(true);
    table.addColumn("Datum ingang", 100);
    table.addColumn("Actie", 100);
    table.addColumn("Indicatie");
  }

  @Override
  protected void selectTableRecord(ZaakRecord<IndicatieAanvraag> zaakRecord) {
    IndicatieAanvraag zaak = zaakRecord.getZaak();
    getNavigation().goToPage(new Page4Indicatie(zaak));
  }

  @Override
  protected void loadTableRecord(Record record, ZaakRecord<IndicatieAanvraag> zaakRecord) {
    IndicatieAanvraag zaak = zaakRecord.getZaak();
    record.getValues().get(0).setValue(zaakRecord.getNr());
    record.getValues().get(1).setValue(zaak.getDatumTijdInvoer());
    record.getValues().get(2).setValue(ZaakUtils.getStatus(zaak.getStatus()));
    record.getValues().get(3).setValue(zaak.getDatumIngang());
    record.getValues().get(4).setValue(zaak.getActie().getAfkort());
    record.getValues().get(5).setValue(zaak.getIndicatie().getOmschrijving());
  }

  @Override
  public void onNew() {
    IdentificatieContactUtils.startProcess(this, new Page2Indicatie(), true);
  }
}
