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

import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.RiskProfileSig;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.persoonslijst.overig.header.buttons.MarkButton;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Marking3 extends NormalPageTemplate {

  private final RiskProfileSig sig;
  private final ProTextArea    textArea = new ProTextArea();

  public Page2Marking3(RiskProfileSig sig) {
    this.sig = sig;
    setSpacing(true);
    setSizeFull();
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      Services services = getServices();
      Button markButton = new MarkButton(services, sig);

      addButton(buttonSave);
      addButton(markButton, 1f);
      addButton(buttonClose);

      textArea.setWidth("100%");
      textArea.setRows(30);
      textArea.setValue(astr(sig.getRemarks()));
      addComponent(new Fieldset("Opmerkingen", textArea));
    }

    super.event(event);
  }

  @Override
  public void onSave() {
    RiskAnalysisService service = getServices().getRiskAnalysisService();
    sig.setRemarks(astr(textArea.getValue()));
    service.saveSignal(sig);
    onClose();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }
}
