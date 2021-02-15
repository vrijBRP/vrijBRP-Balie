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

import com.vaadin.ui.Field;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Tab1RaasPage2Subpage6 extends Tab1RaasPage2Subpage {

  public Tab1RaasPage2Subpage6(PageLayout parentLayout, DocAanvraagDto aanvraag) {
    super(parentLayout, aanvraag);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      setForm(new Form(getAanvraag()));
      addComponent(getForm());
      String caption = "Documenthistorie ten tijde van de aanvraag";
      addComponent(new Fieldset(caption, new Tab1RaasPage2Table(getAanvraag())));
    }

    super.event(event);
  }

  public class Form extends Tab1RaasPage2Form {

    public Form(DocAanvraagDto documentAanvraag) {
      super(documentAanvraag);
      setCaption("Vermissing");
    }

    @Override
    protected void createFields() {
      setOrder(IND_VERM, D_VERM, NR_VERM, AUTORIT_VERM, PV_VERM, VERZOEK_VERM, NR_VB_DOC, D_VB_DOC);
    }

    @Override
    public void setColumn(TableLayout.Column column, Field field, Property property) {

      if (property.is(NR_VB_DOC)) {
        getLayout().addFieldset("Verblijfsdocument");
      }

      super.setColumn(column, field, property);
    }
  }
}
