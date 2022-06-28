/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page2;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakBehandelaar;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class Page2ZaakBehandelaar extends NormalPageTemplate {

  private final ZaakBehandelaar     behandelaar;
  private Page2ZaakBehandelaarForm1 form1;

  public Page2ZaakBehandelaar(ZaakBehandelaar behandelaar) {
    this.behandelaar = behandelaar;

    setSpacing(true);
    setMargin(false);

    addButton(buttonPrev);
    addButton(buttonSave);
    buttonSave.setVisible(!behandelaar.isStored());
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      if (behandelaar.isStored()) {
        addComponent(new Page2ZaakBehandelaarForm2(behandelaar));
      } else {
        form1 = new Page2ZaakBehandelaarForm1(behandelaar);
        addComponent(form1);
      }
    }
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  public abstract void onReload();

  @Override
  public void onSave() {
    form1.commit();

    behandelaar.setBehandelaar(form1.getBean().getBehandelaar());
    behandelaar.setOpm(form1.getBean().getOpmerking());
    getServices().getZaakAttribuutService().save(behandelaar);

    successMessage("De gegevens zijn opgeslagen.");
    onReload();
    onPreviousPage();

    super.onSave();
  }
}
