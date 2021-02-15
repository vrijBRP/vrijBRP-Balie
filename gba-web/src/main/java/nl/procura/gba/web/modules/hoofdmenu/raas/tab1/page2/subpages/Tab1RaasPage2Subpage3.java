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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages;

import static nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages.Tab1RaasPage2Bean.*;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab1RaasPage2Subpage3 extends Tab1RaasPage2Subpage {

  public Tab1RaasPage2Subpage3(PageLayout parentLayout, DocAanvraagDto aanvraag) {
    super(parentLayout, aanvraag);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      setForm(new Form(getAanvraag()));
      addComponent(getForm());
    }

    super.event(event);
  }

  public class Form extends Tab1RaasPage2Form {

    public Form(DocAanvraagDto documentAanvraag) {
      super(documentAanvraag);
      setCaption("Aanvraag");
    }

    @Override
    protected void createFields() {
      setOrder(NAAM_P, VOORV_P, TP_P, HUW_SRT, RDN_ONTB);
    }
  }
}
