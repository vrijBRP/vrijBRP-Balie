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

package nl.procura.gba.web.modules.zaken.verhuizing.page1;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.dialogs.ZaakConfiguratieDialog;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.modules.zaken.common.ZakenListPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.modules.zaken.verhuizing.page10.Page10Verhuizing;
import nl.procura.gba.web.modules.zaken.verhuizing.page2.Page2Verhuizing;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Overzicht verhuizingen
 */
public class Page1Verhuizing extends ZakenListPage<VerhuisAanvraag> {

  public Page1Verhuizing() {

    super("Verhuizingen");

    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonStatus);
    addButton(buttonDel);
  }

  @Override
  protected ZaakArgumenten getZaakArgumenten() {
    return new ZaakArgumenten(getPl(), ZaakType.VERHUIZING);
  }

  @Override
  protected void setTableColumns(ZakenListTable table) {
    table.addColumn("Nr", 30);
    table.addColumn("Ingevoerd op", 130);
    table.addColumn("Status", 120).setUseHTML(true);
    table.addColumn("Ingevoerd door", 200);
    table.addColumn("Soort", 150);
    table.addColumn("Adres");
  }

  @Override
  public void onNew() {
    VerhuisAanvraag zaak = (VerhuisAanvraag) getServices().getVerhuizingService().getNewZaak();
    ZaakConfiguratieDialog.of(getApplication(), zaak, getServices(), () -> {
      Page10Verhuizing nextPage = new Page10Verhuizing(zaak);
      IdentificatieContactUtils.startProcess(this, nextPage, true);
    });
  }

  @Override
  protected void selectTableRecord(ZaakRecord<VerhuisAanvraag> zaakRecord) {
    getNavigation().goToPage(new Page2Verhuizing(zaakRecord.getZaak()));
  }

  @Override
  protected void loadTableRecord(Record record, ZaakRecord<VerhuisAanvraag> zaakRecord) {
    VerhuisAanvraag zaak = zaakRecord.getZaak();
    record.getValues().get(0).setValue(zaakRecord.getNr());
    record.getValues().get(1).setValue(zaak.getDatumTijdInvoer());
    record.getValues().get(2).setValue(ZaakUtils.getStatus(zaak.getStatus()));
    record.getValues().get(3).setValue(zaak.getIngevoerdDoor().getDescription());
    record.getValues().get(4).setValue(zaak.getSoort());

    StringBuilder adres = new StringBuilder();

    if (zaak.getTypeVerhuizing() == VerhuisType.EMIGRATIE) {
      adres.append(zaak.getEmigratie().getAdres());
    } else {
      adres.append(zaak.getNieuwAdres().getAdres().getAdres_pc_wpl_gem());
      if (zaak.getNieuwAdres().getFunctieAdres() == FunctieAdres.BRIEFADRES) {
        adres.append(" (briefadres)");
      }
    }

    record.getValues().get(5).setValue(adres);
  }
}
