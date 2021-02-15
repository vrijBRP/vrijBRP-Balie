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

package nl.procura.gba.web.modules.tabellen.riskprofile.page1;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.jpa.personen.types.RiskProfileRelatedCaseType;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.persoonslijst.overig.mark.ProfileMarkingsWindow;
import nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page3.Page3HuwelijkLocaties;
import nl.procura.gba.web.modules.tabellen.riskprofile.page2.Page2RiskProfile;
import nl.procura.gba.web.modules.tabellen.riskprofile.page3.Page3RiskProfile;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1RiskProfile extends NormalPageTemplate {

  protected final Button buttonImport  = new Button("Importeren");
  protected final Button buttonExport  = new Button("Exporteren");
  protected final Button buttonSignals = new Button("Signaleringen");

  private Table1              table = null;
  private RiskAnalysisService riskAnalysisService;

  public Page1RiskProfile() {

    super("Overzicht van Risicoprofielen");

    addButton(buttonNew);
    addButton(buttonDel);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      riskAnalysisService = getServices().getRiskAnalysisService();
      addButton(buttonImport);
      addButton(buttonExport);
      addButton(buttonSignals);

      table = new Table1();
      addExpandComponent(table);
      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonImport) {
      onImport();
    }
    if (button == buttonExport) {
      onExport();
    }
    if (button == buttonSignals) {
      getParentWindow().addWindow(new ProfileMarkingsWindow(() -> {}));
    }
    super.handleEvent(button, keyCode);
  }

  private void onImport() {
    getNavigation().goToPage(new Page3RiskProfile());
  }

  private void onExport() {

    List<RiskProfile> profiles = table.getSelectedValues(RiskProfile.class);

    if (profiles.isEmpty()) {
      throw new ProException(SELECT, WARNING, "Geen records geselecteerd.");
    }
    new RiskProfilesImportExportHandler(riskAnalysisService)
        .exportProfiles((GbaWindow) getWindow(), profiles);
  }

  @Override
  public void onDelete() {
    new DeleteProcedure<RiskProfile>(table) {

      @Override
      public void deleteValue(RiskProfile riskProfile) {
        riskAnalysisService.delete(riskProfile);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2RiskProfile(new RiskProfile(), new HashSet<>()));
    super.onNew();
  }

  @Override
  public void onNextPage() {
    getNavigation().goToPage(Page3HuwelijkLocaties.class);
    super.onNextPage();
  }

  public class Table1 extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {
      RiskProfile profile = (RiskProfile) record.getObject();
      Set<RiskProfileRelatedCaseType> types = riskAnalysisService.getTypes(profile);
      getNavigation().goToPage(new Page2RiskProfile(profile, types));
      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);
      addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

      addColumn("Naam", 300);
      addColumn("Toepassen op");
      addColumn("Drempel", 100);
      addColumn("Aantal regels", 100);
    }

    @Override
    public void setRecords() {

      List<RiskProfile> records = riskAnalysisService.getRiskProfiles();
      for (RiskProfile record : records) {
        Record r = addRecord(record);
        r.addValue(record.getName());
        r.addValue(riskAnalysisService.getTypesDescription(record));
        r.addValue(record.getThreshold());
        r.addValue(record.getRules().size());
      }

      super.setRecords();
    }
  }
}
