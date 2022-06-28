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

package nl.procura.gba.web.modules.zaken.verkiezing.page3;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.gba.web.services.beheer.verkiezing.document.StempasTemplateData;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3Verkiezing extends NormalPageTemplate {

  private final Stempas    stempas;
  private PrintMultiLayout printLayout;

  public Page3Verkiezing(Stempas stempas) {
    super("Kiezersregister - afdrukken");
    this.stempas = stempas;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      StempasTemplateData data = new StempasTemplateData(stempas);
      printLayout = new PrintMultiLayout(data, null, null, DocumentType.STEMPAS);
      printLayout.setInfo("");
      addButton(buttonPrev);
      addButton(printLayout.getButtons());
      addComponent(printLayout);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    printLayout.handleActions(button, keyCode);
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
