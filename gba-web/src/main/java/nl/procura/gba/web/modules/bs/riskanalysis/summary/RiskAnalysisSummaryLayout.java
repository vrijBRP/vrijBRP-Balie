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

import static nl.procura.gba.web.modules.bs.riskanalysis.summary.RiskAnalysisSummaryBean.F_PROFILE;
import static nl.procura.gba.web.modules.bs.riskanalysis.summary.RiskAnalysisSummaryBean.F_RELATEDCASE;
import static nl.procura.gba.web.modules.bs.riskanalysis.summary.RiskAnalysisSummaryBean.F_THRESHOLD;
import static nl.procura.java.reflection.ReflectionUtil.deepCopyBean;

import com.vaadin.ui.Embedded;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.jpa.personen.db.DossRiskAnalysisSubject;
import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabLayout;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.riskanalysis.DossierRiskAnalysis;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.theme.twee.Icons;

public class RiskAnalysisSummaryLayout extends GbaVerticalLayout implements ZaakTabLayout {

  private DossierRiskAnalysis zaakDossier;

  public RiskAnalysisSummaryLayout(final DossierRiskAnalysis zaakDossier) {
    this.zaakDossier = zaakDossier;
    setSpacing(true);
    setComponents();
  }

  @Override
  public void reloadLayout(GbaApplication application, Zaak zaak) {
    this.zaakDossier = (DossierRiskAnalysis) ((Dossier) zaak).getZaakDossier();
    setComponents();
  }

  /**
   * Reload all components
   */
  private void setComponents() {
    removeAllComponents();
    RulesTable rulesTable = new RulesTable();
    SubjectsTable subjectsTable = new SubjectsTable();

    Zaak relatedCase = getRelatedCase(zaakDossier);
    RiskAnalysisSummaryForm form = new RiskAnalysisSummaryForm(zaakDossier, relatedCase) {

      @Override
      public void setCaptionAndOrder() {
        setCaption("Risicoanalyse");
        setOrder(F_PROFILE, F_RELATEDCASE, F_THRESHOLD);
      }
    };

    addComponent(form);
    addComponent(new Fieldset("Betreffende personen", new InfoLayout("Klik op de regel voor meer informatie")));
    addComponent(subjectsTable);
    addComponent(new Fieldset("Regels", rulesTable));
    rulesTable.focus();
  }

  /**
   * Search the related case
   */
  private Zaak getRelatedCase(DossierRiskAnalysis riskAnalysis) {
    String zaakId = riskAnalysis.getRefCaseId();
    ZaakType zaakType = ZaakType.get(riskAnalysis.getRefCaseType().longValue());
    ZaakArgumenten za = new ZaakArgumenten(new ZaakKey(zaakId, zaakType));
    return Services.getInstance()
        .getZakenService()
        .getMinimaleZaken(za).stream().findFirst()
        .orElse(null);
  }

  public class RulesTable extends GbaTable {

    @Override
    public void setColumns() {
      addColumn("Score", 50);
      addColumn("Naam");
      super.setColumns();
    }

    @Override
    public void setRecords() {
      RiskProfile riskProfile = zaakDossier.getRiskProfile();
      if (riskProfile != null) {
        for (RiskProfileRule rule : riskProfile.getRules()) {
          IndexedTable.Record r = addRecord(rule);
          r.addValue(rule.getScore());
          r.addValue(rule.getName());
        }
      }
    }
  }

  class SubjectsTable extends GbaTable {

    @Override
    public void setColumns() {
      setClickable(true);
      addColumn("&nbsp;", 20).setClassType(Embedded.class);
      addColumn("Persoon", 400);
      addColumn("Score");
    }

    @Override
    public void onClick(Record record) {
      getParentWindow().addWindow(new RiskAnalysisSummaryWindow(record.getObject(DossRiskAnalysisSubject.class)));
      super.onClick(record);
    }

    @Override
    public void setRecords() {
      RiskProfile riskProfile = zaakDossier.getRiskProfile();
      if (riskProfile != null) {
        for (DossRiskAnalysisSubject subject : zaakDossier.getSubjects()) {
          Record r = addRecord(subject);
          DossierPersoon person = deepCopyBean(DossierPersoon.class, subject.getPerson());

          // Show if threshold is reached.
          if (subject.getScore().longValue() > 0) {
            if (subject.getScore().longValue() >= riskProfile.getThreshold().longValue()) {
              r.addValue(new TableImage(Icons.getIcon(Icons.ICON_WARN)));
            } else {
              r.addValue(new TableImage(Icons.getIcon(Icons.ICON_OK)));
            }
          } else {
            r.addValue(null);
          }

          // Set name
          r.addValue(person.getNaam().getNaam_naamgebruik_eerste_voornaam());

          // Set score
          if (subject.getScore().longValue() >= 0) {
            r.addValue(subject.getScore().toString());
          } else {
            r.addValue("Onbekend");
          }
        }
      }
    }
  }
}
