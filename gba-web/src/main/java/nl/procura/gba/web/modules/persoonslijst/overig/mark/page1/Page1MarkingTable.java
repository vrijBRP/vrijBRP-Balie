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

package nl.procura.gba.web.modules.persoonslijst.overig.mark.page1;

import static org.apache.commons.lang3.StringUtils.abbreviate;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

import com.vaadin.ui.Embedded;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.RiskProfileSig;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.persoonslijst.overig.mark.MarkedTableImage;
import nl.procura.gba.web.modules.persoonslijst.overig.mark.ProfileMarkCommentWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.bag.ProcuraPersonListAddress;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page1MarkingTable extends GbaTable {

  private final BasePLExt pl;

  public Page1MarkingTable(BasePLExt pl) {
    this.pl = pl;
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);
    addStyleName(GbaWebTheme.TABLE.ALIGN_TOP);

    addColumn("&nbsp", 15).setClassType(Embedded.class);
    addColumn("Gegeven", 100);
    addColumn("Waarde");
    addColumn("Opmerking", 200);

    super.setColumns();
  }

  @Override
  public void onDoubleClick(Record record) {
    RiskProfileSig sig = record.getObject(RiskProfileSig.class);
    getApplication().getParentWindow().addWindow(new ProfileMarkCommentWindow(sig, this::init));
    init();
    super.onDoubleClick(record);
  }

  @Override
  public void setRecords() {

    RiskProfileSig markedByBsn = getSignaledByBsn();
    RiskProfileSig markedByAddress = getSignaledByAddress();

    Record record1 = addRecord(markedByBsn);
    record1.addValue(new MarkedTableImage(markedByBsn.isEnabled()));
    record1.addValue("Persoon");
    record1.addValue(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
    record1.addValue(abbreviate(normalizeSpace(markedByBsn.getRemarks()), 30));

    Record record2 = addRecord(markedByAddress);
    record2.addValue(new MarkedTableImage(markedByAddress.isEnabled()));
    record2.addValue("Adres");
    record2.addValue(new ProcuraPersonListAddress(pl).getLabel());
    record2.addValue(abbreviate(normalizeSpace(markedByAddress.getRemarks()), 30));

    super.setRecords();
  }

  private RiskProfileSig getSignaledByBsn() {
    RiskAnalysisService service = Services.getInstance().getRiskAnalysisService();
    RiskProfileSig sig = service.buildBsnSignal(pl);
    return service.getSignal(sig).orElse(sig);
  }

  private RiskProfileSig getSignaledByAddress() {
    RiskAnalysisService service = Services.getInstance().getRiskAnalysisService();
    RiskProfileSig sig = service.buildAddressSignal(pl);
    return service.getSignal(sig).orElse(sig);
  }
}
