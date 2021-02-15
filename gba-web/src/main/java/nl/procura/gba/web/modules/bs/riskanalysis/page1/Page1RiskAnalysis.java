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

package nl.procura.gba.web.modules.bs.riskanalysis.page1;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisRelatedCase;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1RiskAnalysis extends NormalPageTemplate {

  private RiskAnalysisRelatedCase relocation;
  private Page1RiskAnalysisForm1  form;
  private Page1RiskAnalysisTable1 table;
  private Page1RiskAnalysisForm2  relocationForm;
  private Page1RiskAnalysisTable2 relocationTable;
  private CssLayout               profileLayout;

  public Page1RiskAnalysis() {
    super("Risicoanalyse - betreft");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSave);

      profileLayout = new CssLayout();
      profileLayout.setSizeFull();

      form = new Page1RiskAnalysisForm1(e -> {
        onChangeProfile((RiskProfile) e.getProperty().getValue());
      });

      table = new Page1RiskAnalysisTable1();

      addComponent(new Fieldset("Risicoanalyse", form));
      addComponent(new Fieldset("Regels van het risicoprofiel", table));
      addComponent(profileLayout);
    }

    super.event(event);
  }

  private void onChangeProfile(RiskProfile riskProfile) {
    table.setRiskProfile(riskProfile);

    // Reload profileLayout
    profileLayout.removeAllComponents();
    if (riskProfile != null) {
      profileLayout.addComponent(getRelocationLayout());
    }
  }

  private Component getRelocationLayout() {
    relocationForm = new Page1RiskAnalysisForm2(relocation, returnValue -> {
      this.relocation = returnValue;
      updateRelocationLayout();
    });

    relocationTable = new Page1RiskAnalysisTable2();
    updateRelocationLayout();

    VLayout layout = new VLayout();
    layout.addComponent(relocationForm);
    layout.addComponent(relocationTable);
    return new Fieldset("Gekoppelde zaak", layout);
  }

  private void updateRelocationLayout() {
    relocationForm.setRelatedCase(relocation);
    relocationTable.setRelocation(relocation);
  }

  @Override
  public void onSave() {
    form.commit();

    if (relocation == null) {
      throw new ProException(WARNING, "Geen verhuizing geselecteerd");
    }

    RiskAnalysisService service = getServices().getRiskAnalysisService();
    Dossier dossier = service.getNewZaak(form.getBean().getProfile(), relocation);
    service.save(dossier);
    ZaakregisterNavigator.navigatoTo(dossier, this, true);
  }
}
