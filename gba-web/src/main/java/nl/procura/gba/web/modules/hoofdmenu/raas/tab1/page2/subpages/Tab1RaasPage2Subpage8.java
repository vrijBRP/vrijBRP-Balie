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
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Tab1RaasPage2Subpage8 extends Tab1RaasPage2Subpage {

  public Tab1RaasPage2Subpage8(PageLayout parentLayout, DocAanvraagDto aanvraag) {
    super(parentLayout, aanvraag);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      setForm(new Form(getAanvraag()));
      OptieLayout optieLayout = new OptieLayout();
      optieLayout.getLeft().addComponent(getForm());

      if (!getWindow().isModal()) {
        optieLayout.getRight().addButton(buttonPersoon, this);
        optieLayout.getRight().addButton(buttonZaak, this);
      }

      optieLayout.getRight().addButton(buttonRaasComm, this);
      optieLayout.getRight().addButton(buttonUitreiken, this);

      optieLayout.getRight().setWidth("160px");
      optieLayout.getRight().setCaption("Opties");
      addComponent(optieLayout);
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
      setOrder(STATUS_AANVR, DATUM_TIJD_AANVR, D_VERSTREK, STATUS_LEV, DATUM_TIJD_LEV, NR_NL_DOC, NR_NL_DOC_IN,
          STATUS_AFSL, DATUM_TIJD_AFSL, STATUS_VERW);
    }

    @Override
    public void setColumn(TableLayout.Column column, Field field, Property property) {

      if (property.is(STATUS_LEV)) {
        getLayout().addFieldset("Levering");
      }

      if (property.is(STATUS_AFSL)) {
        getLayout().addFieldset("Afsluiting");
      }

      if (property.is(STATUS_VERW)) {
        getLayout().addFieldset("Verwerking van bericht");
      }

      super.setColumn(column, field, property);
    }
  }
}
