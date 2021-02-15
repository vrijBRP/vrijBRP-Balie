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

package nl.procura.gba.web.modules.zaken.uittreksel.page1;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.modules.zaken.common.ZakenListPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.modules.zaken.uittreksel.page2.Page2Uittreksel;
import nl.procura.gba.web.modules.zaken.uittreksel.page3.Page3Uittreksel;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Overzicht
 */
public class Page1Uittreksel extends ZakenListPage<DocumentZaak> {

  public Page1Uittreksel() {

    super("Uittreksel ");
    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonStatus);
    addButton(buttonDel);
  }

  @Override
  protected ZaakArgumenten getZaakArgumenten() {
    return new ZaakArgumenten(getPl(), ZaakType.UITTREKSEL);
  }

  @Override
  protected void setTableColumns(ZakenListTable table) {
    table.addColumn("Nr", 30);
    table.addColumn("Ingevoerd op", 130);
    table.addColumn("Status", 120).setUseHTML(true);
    table.addColumn("Ingevoerd door", 200);
    table.addColumn("Soort", 150);
    table.addColumn("Document");
  }

  @Override
  protected void selectTableRecord(ZaakRecord<DocumentZaak> zaakRecord) {
    getNavigation().goToPage(new Page2Uittreksel(zaakRecord.getZaak()));
  }

  @Override
  protected void loadTableRecord(Record record, ZaakRecord<DocumentZaak> zaakRecord) {
    DocumentZaak zaak = zaakRecord.getZaak();
    record.getValues().get(0).setValue(zaakRecord.getNr());

    String subLabel = "";

    if (fil(zaak.getDocumentDoel())) {
      subLabel += " doel: " + zaak.getDocumentDoel();
    }

    if (fil(zaak.getDocumentAfn())) {
      subLabel += fil(subLabel) ? " en " : "";
      subLabel += " afnemer: " + zaak.getDocumentAfn();
    }

    record.getValues().get(1).setValue(zaak.getDatumTijdInvoer());
    record.getValues().get(2).setValue(ZaakUtils.getStatus(zaak.getStatus()));
    record.getValues().get(3).setValue(zaak.getIngevoerdDoor().getDescription());
    record.getValues().get(4).setValue(DocumentType.getType(zaak.getDoc().getType()).getOms());
    record.getValues().get(5).setValue(zaak.getDoc().getDocument() + subLabel);
  }

  @Override
  public void onNew() {
    IdentificatieContactUtils.startProcess(this, new Page3Uittreksel(), false);
  }
}
