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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.page2;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Aantekening
 */
public abstract class Page2ZaakAttribuut extends NormalPageTemplate {

  private final ZaakAttribuut    attribuut;
  private Page2ZaakAttribuutForm form;

  public Page2ZaakAttribuut(ZaakAttribuut attribuut) {

    this.attribuut = attribuut;

    setSpacing(true);
    setMargin(false);

    addButton(buttonPrev);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new Page2ZaakAttribuutForm(attribuut);
      addComponent(form);
    }
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  public abstract void onReload();

  @Override
  public void onSave() {

    form.commit();

    ZaakAttribuutType type = form.getBean().getType();
    String attr = form.getBean().getAttribuut();

    if (ZaakAttribuutType.ANDERS == type) {
      attribuut.setAttribuut(attr);
    } else {
      attribuut.setAttribuut(type.getCode());
    }

    getServices().getZaakAttribuutService().save(attribuut);

    successMessage("De gegevens zijn opgeslagen.");

    onReload();

    onPreviousPage();

    super.onSave();
  }
}
