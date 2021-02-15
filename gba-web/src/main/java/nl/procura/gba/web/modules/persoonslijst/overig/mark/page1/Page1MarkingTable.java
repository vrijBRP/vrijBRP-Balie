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

import static nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService.SIGNALTYPE.ADDRESS;
import static nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService.SIGNALTYPE.BSN;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.bag.ProcuraPersonListAddress;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page1MarkingTable extends GbaTable {

  private BasePLExt pl;

  public Page1MarkingTable(BasePLExt pl) {
    this.pl = pl;
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);
    addStyleName(GbaWebTheme.TABLE.ALIGN_TOP);

    addColumn("Gemarkeerd", 90).setUseHTML(true);
    addColumn("Gegeven", 100);
    addColumn("Waarde");

    super.setColumns();
  }

  @Override
  public void onDoubleClick(Record record) {

    RiskAnalysisService service = Services.getInstance().getRiskAnalysisService();

    if (BSN.equals(record.getObject())) {
      service.switchSignaling(service.buildBsnSignal(pl));
    }

    if (ADDRESS.equals(record.getObject())) {
      service.switchSignaling(service.buildAddressSignal(pl));
    }

    init();

    super.onDoubleClick(record);
  }

  @Override
  public void setRecords() {

    boolean markedByBsn = isSignaledByBsn();
    boolean markedByAddress = isSignaledByAddress();

    Record record1 = addRecord(BSN);
    record1.addValue(markedByBsn ? MiscUtils.setClass(false, "Ja") : "Nee");
    record1.addValue("Persoon");
    record1.addValue(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());

    Record record2 = addRecord(ADDRESS);
    record2.addValue(markedByAddress ? MiscUtils.setClass(false, "Ja") : "Nee");
    record2.addValue("Adres");
    record2.addValue(new ProcuraPersonListAddress(pl).getLabel());

    super.setRecords();
  }

  private boolean isSignaledByBsn() {
    RiskAnalysisService service = Services.getInstance().getRiskAnalysisService();
    return service.getSignal(service.buildBsnSignal(pl)).isPresent();
  }

  private boolean isSignaledByAddress() {
    RiskAnalysisService service = Services.getInstance().getRiskAnalysisService();
    return service.getSignal(service.buildAddressSignal(pl)).isPresent();
  }
}
