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

package nl.procura.gba.web.modules.zaken.reisdocument.page1;

import com.vaadin.ui.Button;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.common.IdentificatieContactUtils;
import nl.procura.gba.web.modules.zaken.common.ZakenListPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.BasisregisterButton;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.BrpReisdocumentenButton;
import nl.procura.gba.web.modules.zaken.reisdocument.page18.Page18Reisdocument;
import nl.procura.gba.web.modules.zaken.reisdocument.page2.Page2Reisdocument;
import nl.procura.gba.web.services.beheer.vrs.VrsService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Overzicht reisdocumenten
 */
public class Page1Reisdocument extends ZakenListPage<ReisdocumentAanvraag> {

  private final Button buttonDocumenten    = new BrpReisdocumentenButton(this::getPl, () -> Page1Reisdocument.this);
  private final Button buttonBasisregister = new BasisregisterButton(this::getPl, () -> null, this::onEnter);

  public Page1Reisdocument() {
    super("Reisdocumenten: overzicht");
    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonStatus);
    addButton(buttonDel);
  }

  @Override
  protected void initPage() {
    super.initPage();
    VrsService vrsService = getServices().getReisdocumentService().getVrsService();
    int index = getButtonLayout().getComponentIndex(buttonDel);
    if (vrsService.isBasisregisterEnabled()) {
      getButtonLayout().add(buttonBasisregister, index + 1);
    }
    if (!vrsService.isRegistratieMeldingEnabled()) {
      getButtonLayout().add(buttonDocumenten, index + 1);
    }
  }

  @Override
  protected ZaakArgumenten getZaakArgumenten() {
    return new ZaakArgumenten(getPl(), ZaakType.REISDOCUMENT);
  }

  @Override
  protected void setTableColumns(ZakenListTable table) {
    table.addColumn("Nr", 30);
    table.addColumn("Ingevoerd op", 130);
    table.addColumn("Status", 120).setUseHTML(true);
    table.addColumn("Ingevoerd door", 200);
    table.addColumn("Aanvraagnummer", 150);
    table.addColumn("Reisdocument", 300);
    table.addColumn("Levering/afsluiting").setUseHTML(true);
  }

  @Override
  protected void selectTableRecord(ZaakRecord<ReisdocumentAanvraag> zaakRecord) {
    getNavigation().goToPage(new Page2Reisdocument(zaakRecord.getZaak()));
  }

  @Override
  protected void loadTableRecord(Record record, ZaakRecord<ReisdocumentAanvraag> zaakRecord) {
    ReisdocumentAanvraag zaak = zaakRecord.getZaak();
    zaak = getServices().getReisdocumentService().getStandardZaak(zaak);
    record.getValues().get(0).setValue(zaakRecord.getNr());
    record.getValues().get(1).setValue(zaak.getDatumTijdInvoer());
    record.getValues().get(2).setValue(zaak.getStatus());
    record.getValues().get(3).setValue(zaak.getIngevoerdDoor().getDescription());
    record.getValues().get(4).setValue(zaak.getAanvraagnummer().getFormatNummer());
    record.getValues().get(5).setValue(zaak.getReisdocumentType());
    record.getValues().get(6)
        .setValue(isAanvraagApart(zaak) ? "<b>" + zaak.getSluitingStatus() + "</b>" : zaak.getSluitingStatus());
  }

  private boolean isAanvraagApart(ReisdocumentAanvraag aanvr) {
    switch (aanvr.getReisdocumentStatus().getStatusAfsluiting()) {
      case AANVRAAG_NIET_AFGESLOTEN:
        return true;
      case ONBEKEND:
      case DOCUMENT_NIET_OPGEHAALD:
      case DOCUMENT_NIET_UITGEREIKT_ONJUIST:
      case DOCUMENT_NIET_UITGEREIKT_OVERIGE_REDEN:
      case DOCUMENT_UITGEREIKT:
      case DOCUMENT_UITGEREIKT_DOOR_ANDERE_INSTANTIE:
      default:
        return false;
    }
  }

  @Override
  public void onNew() {
    Page18Reisdocument nextPage = new Page18Reisdocument(getServices().getReisdocumentService().getNewZaak(getPl()));
    IdentificatieContactUtils.startProcess(this, nextPage, true);
  }
}
