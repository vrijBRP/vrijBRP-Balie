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

package nl.procura.gba.web.modules.bs.riskanalysis.summary;

import static nl.procura.java.reflection.ReflectionUtil.deepCopyBean;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class RiskAnalysisSummaryWindow extends GbaModalWindow {

  private DossRiskAnalysisSubject subject;

  public RiskAnalysisSummaryWindow(DossRiskAnalysisSubject subject) {
    super(false, "", "900px");
    DossierPersoon person = deepCopyBean(DossierPersoon.class, subject.getPerson());
    setCaption("Risicoanalyse van " + person.getNaam().getNaam_naamgebruik_eerste_voornaam());
    this.subject = subject;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page()));
  }

  public class Page extends NormalPageTemplate {

    @Override
    public void event(PageEvent event) {
      super.event(event);

      buttonNext.setCaption("Persoonslijst (F2)");
      addButton(buttonNext, 1f);
      addButton(BUTTON_CLOSE);

      if (event.isEvent(InitPage.class)) {
        LogTable table = new LogTable();
        addComponent(table);
      }
    }

    @Override
    public void onNextPage() {
      getApplication().goToPl(getParentWindow(), "pl.persoon", PLEDatasource.STANDAARD,
          astr(subject.getPerson().getAnr()));
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
    }
  }

  public class LogTable extends GbaTable {

    @Override
    public void setColumns() {
      addColumn("Nr", 40);
      addColumn("LogLevel", 100).setUseHTML(true);
      addColumn("Regel");
      addStyleName(ProcuraTheme.TABLE.NEWLINE_WRAP);
      super.setColumns();
    }

    @Override
    public void setRecords() {
      int nr = 0;
      if (StringUtils.isBlank(subject.getLog())) {
        IndexedTable.Record r = addRecord("");
        r.addValue(++nr);
        r.addValue(ProExceptionSeverity.INFO.getDescr());
        r.addValue("De risicoanalyse is nog niet uitgevoerd");
      } else {
        for (String line : subject.getLog().split("\n")) {
          String[] split = line.split("\\:", 2);
          IndexedTable.Record r = addRecord(line);
          r.addValue(++nr);
          r.addValue(getColor(getSeverity(along(split[0]))));
          r.addValue(split.length > 1 ? split[1] : "");
        }
      }
    }

    /**
     * Color the column
     */
    private String getColor(ProExceptionSeverity severity) {

      switch (severity) {
        case ERROR:
          return MiscUtils.setClass(false, severity.getDescr());

        case WARNING:
          return MiscUtils.setClass("orange", severity.getDescr());

        default:
        case INFO:
          return severity.getDescr();
      }
    }

    /**
     * Find the correct severity
     */
    private ProExceptionSeverity getSeverity(long code) {
      return Arrays.stream(ProExceptionSeverity.values())
          .filter(s -> s.getCode() == code)
          .findFirst()
          .orElse(ProExceptionSeverity.INFO);
    }
  }
}
