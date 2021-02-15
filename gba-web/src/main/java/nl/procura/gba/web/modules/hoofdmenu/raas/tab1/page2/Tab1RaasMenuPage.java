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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.proces.ProcessLayout;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages.*;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab1RaasMenuPage extends NormalPageTemplate {

  private final DocAanvraagDto aanvraag;
  private HeaderForm           headerForm;

  public Tab1RaasMenuPage(DocAanvraagDto aanvraag) {
    super("Reisdocumentaanvraag naar RAAS");
    this.aanvraag = aanvraag;
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      headerForm = new HeaderForm(aanvraag);

      final ProcessLayout processLayout = new ProcessLayout();
      processLayout.setWidths("230px", "220px");
      processLayout.getInnerLayout().add(new Fieldset("&nbsp;"));

      ProcessLayout.ProcessButton button1 = processLayout.addButton("1. Statusinformatie / uitreiken", button -> {
        processLayout.getNavigation().goToPage(new Tab1RaasPage2Subpage8(this, aanvraag));
      });

      processLayout.addButton("2. Aanvrager", button -> {
        processLayout.getNavigation().goToPage(new Tab1RaasPage2Subpage1(this, aanvraag));
      });

      processLayout.addButton("3. Adres", button -> {
        processLayout.getNavigation().goToPage(new Tab1RaasPage2Subpage2(this, aanvraag));
      });

      processLayout.addButton("4. Partner", button -> {
        processLayout.getNavigation().goToPage(new Tab1RaasPage2Subpage3(this, aanvraag));
      });

      processLayout.addButton("5. Documentgegevens / nationaliteit", button -> {
        processLayout.getNavigation().goToPage(new Tab1RaasPage2Subpage4(this, aanvraag));
      });

      processLayout.addButton("6. Toestemming / identiteit", button -> {
        processLayout.getNavigation().goToPage(new Tab1RaasPage2Subpage5(this, aanvraag));
      });

      processLayout.addButton("7. Documenthistorie / vermissing", button -> {
        processLayout.getNavigation().goToPage(new Tab1RaasPage2Subpage6(this, aanvraag));
      });

      processLayout.addButton("8. Clausules", button -> {
        processLayout.getNavigation().goToPage(new Tab1RaasPage2Subpage7(this, aanvraag));
      });

      if (getWindow().isModal()) {
        addButton(buttonClose);
      } else {
        addButton(buttonPrev);
      }

      addComponent(headerForm);
      addComponent(processLayout.getLayout());
      button1.onClick();
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
    super.onClose();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  public HeaderForm getHeaderForm() {
    return headerForm;
  }
}
