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

package nl.procura.gba.web.modules.zaken.identificatie.page1;

import static nl.procura.gba.common.MiscUtils.to;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieType;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.rdw.processen.p0252.f08.NATPRYBMAATR;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1IdentificatieRijbewijsWindow extends GbaModalWindow {

  private final NATPRYBMAATR       maatregelen;
  private final Page1Identificatie page1;
  private Page1IdentificatieForm3  form3;

  public Page1IdentificatieRijbewijsWindow(Page1Identificatie page1, NATPRYBMAATR maatregelen) {

    super("Rijbewijsgegevens (Esc om te sluiten)", "600px");

    this.page1 = page1;
    this.maatregelen = maatregelen;
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();

    addComponent(mainModule);

    mainModule.getNavigation().addPage(new Page());
  }

  public class Page extends NormalPageTemplate {

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {

        buttonNext.setCaption("Selecteer dit rijbewijs (F2)");

        addButton(buttonNext, 1f);
        addButton(buttonClose);

        addComponent(new InfoLayout("De huidige rijbewijsgegevens", ""));

        form3 = new Page1IdentificatieForm3(maatregelen);

        addComponent(form3);
      }
    }

    @Override
    public void onClose() {

      to(getWindow(), GbaModalWindow.class).closeWindow();

      super.onClose();
    }

    @Override
    public void onNextPage() {

      page1.setDocumentGegevens(IdentificatieType.RIJBEWIJS, form3.getBean().getNummer());

      onClose();
    }
  }
}
