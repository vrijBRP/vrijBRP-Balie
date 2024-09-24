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

import static org.apache.commons.lang3.StringUtils.normalizeSpace;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.db.RiskProfileSig;
import nl.procura.gba.web.common.misc.spreadsheets.Spreadsheet;
import nl.procura.gba.web.common.misc.spreadsheets.SpreadsheetTemplate;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonWindow;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService.SIGNALTYPE;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Marking1 extends NormalPageTemplate {

  private Page2MarkingTable1 table;

  public Page2Marking1() {
    setSpacing(true);
    setSizeFull();
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonNew.setCaption("Toevoegen (F7)");
      addButton(buttonNew);
      addButton(buttonDel, 1f);

      table = new Page2MarkingTable1();
      addComponent(table);
      List<Spreadsheet> spreadsheets = new ArrayList<>();
      spreadsheets.add(new PersonsSpreadsheet());
      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table, spreadsheets));
      addButton(buttonClose);
    }

    super.event(event);
  }

  @Override
  public void onNew() {
    getParentWindow().addWindow(new QuickSearchPersonWindow(pl -> {
      RiskAnalysisService service = getServices().getRiskAnalysisService();
      RiskProfileSig bsnSignal = service.buildBsnSignal(pl);
      if (service.getSignal(bsnSignal).isPresent()) {
        throw new ProException(ProExceptionSeverity.INFO, "Deze persoon is al toegevoegd");
      }
      service.saveSignal(bsnSignal);
      table.init();
    }));
    super.onNew();
  }

  @Override
  public void onDelete() {
    table.getSelectedValues(RiskProfileSig.class)
        .forEach(sig -> getServices()
            .getRiskAnalysisService().delete(sig));
    table.init();
    super.onDelete();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  private class PersonsSpreadsheet extends SpreadsheetTemplate {

    public PersonsSpreadsheet() {
      super("Gemarkeerde personen", UitvoerformaatType.CSV_SEMICOLON);
    }

    @Override
    public void compose() {
      clear();

      add("Gemarkeerd");
      add("BSN");
      add("Naam");
      add("Opmerking");
      store();

      RiskAnalysisService service = Page2Marking1.this.getServices().getRiskAnalysisService();
      List<RiskProfileSig> signals = service.getSignals(SIGNALTYPE.BSN);

      for (RiskProfileSig signal : signals) {
        add(signal.isEnabled() ? "Ja" : "Nee");
        add(signal.getBsn());
        add(signal.getLabel());
        add(normalizeSpace(signal.getRemarks()));
        store();
      }

      super.compose();
    }
  }
}
