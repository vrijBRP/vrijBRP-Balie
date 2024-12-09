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

package nl.procura.gba.web.modules.zaken.inhouding.page1;

import static nl.procura.standard.Globalfunctions.astr;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.vaadin.ui.Button;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.common.ZakenListPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.modules.zaken.inhouding.page4.Page4Inhouding;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.BasisregisterButton;
import nl.procura.gba.web.modules.zaken.reisdocument.page10.BrpReisdocumentenButton;
import nl.procura.gba.web.services.beheer.vrs.VrsService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Overzicht inhoudingen
 */
public class Page1Inhouding extends ZakenListPage<DocumentInhouding> {

  private final Button buttonDocumenten    = new BrpReisdocumentenButton(this::getPl, () -> Page1Inhouding.this);
  private final Button buttonBasisregister = new BasisregisterButton(this::getPl, () -> null, this::onEnter);

  public Page1Inhouding() {
    super("Inhoudingen / vermissingen: overzicht");
    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonStatus);
    addButton(buttonDel);

    setInfo("", "Via dit scherm kunnen alleen vermissingen en inhoudingen van reisdocumenten worden ingevoerd.");
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
    return new ZaakArgumenten(getPl(), ZaakType.INHOUD_VERMIS);
  }

  @Override
  protected void setTableColumns(ZakenListTable table) {
    table.addColumn("Nr", 30);
    table.addColumn("Ingevoerd op", 130);
    table.addColumn("Status", 120).setUseHTML(true);
    table.addColumn("Ingevoerd door", 200);
    table.addColumn("Nummer", 150);
    table.addColumn("Document", 200);
    table.addColumn("Type melding / reden");
  }

  @Override
  protected void selectTableRecord(ZaakRecord<DocumentInhouding> zaakRecord) {
    DocumentInhouding inhouding = zaakRecord.getZaak();
    DocumentInhoudingenService inhoudingen = getServices().getDocumentInhoudingenService();
    Reisdocument reisdocument = inhoudingen.getReisdocument(getPl(), inhouding.getNummerDocument());
    getNavigation().goToPage(new Page4Inhouding(inhouding, reisdocument));
  }

  @Override
  protected void loadTableRecord(Record record, ZaakRecord<DocumentInhouding> zaakRecord) {
    DocumentInhouding zaak = zaakRecord.getZaak();
    record.getValues().get(0).setValue(zaakRecord.getNr());
    record.getValues().get(1).setValue(zaak.getDatumTijdInvoer());
    record.getValues().get(2).setValue(ZaakUtils.getStatus(zaak.getStatus()));
    record.getValues().get(3).setValue(zaak.getIngevoerdDoor().getDescription());
    record.getValues().get(4).setValue(zaak.getNummerDocument());
    record.getValues().get(5).setValue(astr(zaak.isSprakeVanRijbewijs() ? "Rijbewijs" : zaak.getDocumentType()));
    record.getValues().get(6).setValue(getSoort(zaak));
  }

  private static String getSoort(DocumentInhouding zaak) {
    if (isNotBlank(zaak.getVrsMeldingType())) {
      return zaak.getVrsMelding().toString() + " / " + zaak.getVrsReden().toString();
    }
    return zaak.getInhoudingType().getOms();
  }
}
