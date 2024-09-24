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

package nl.procura.gba.web.modules.persoonslijst.overig.mark.page2;

import static org.apache.commons.lang3.StringUtils.abbreviate;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

import java.util.List;

import com.vaadin.ui.Embedded;

import nl.procura.gba.jpa.personen.db.RiskProfileSig;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.persoonslijst.overig.mark.MarkedTableImage;
import nl.procura.gba.web.modules.persoonslijst.overig.mark.ProfileMarkCommentWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page2MarkingTable2 extends GbaTable {

  public Page2MarkingTable2() {
    setHeight("400px");
  }

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);
    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);
    addStyleName(GbaWebTheme.TABLE.ALIGN_TOP);

    addColumn("Nr.", 50);
    addColumn("&nbsp;", 15).setClassType(Embedded.class);
    addColumn("Adres");
    addColumn("Opmerking", 200);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    RiskAnalysisService service = Services.getInstance().getRiskAnalysisService();
    List<RiskProfileSig> signals = service.getSignals(RiskAnalysisService.SIGNALTYPE.ADDRESS);
    int nr = 0;
    for (RiskProfileSig signal : signals) {
      Record record = addRecord(signal);
      record.addValue(++nr);
      record.addValue(new MarkedTableImage(signal.isEnabled()));
      record.addValue(signal.getLabel());
      record.addValue(abbreviate(normalizeSpace(signal.getRemarks()), 30));
    }

    super.setRecords();
  }

  @Override
  public void onDoubleClick(Record record) {
    RiskProfileSig sig = record.getObject(RiskProfileSig.class);
    ProfileMarkCommentWindow window = new ProfileMarkCommentWindow(sig, this::init);
    getApplication().getParentWindow().addWindow(window);
    super.onDoubleClick(record);
  }
}
