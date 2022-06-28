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

package nl.procura.gba.web.modules.beheer.verkiezing.page6;

import nl.procura.gba.jpa.personen.db.KiesrVerkInfo;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.verkiezing.page5.Page5Verkiezing;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page6Verkiezing extends NormalPageTemplate {

  private Page6VerkiezingForm form = null;
  private final KiesrVerkInfo verkInfo;

  public Page6Verkiezing(KiesrVerkInfo verkInfo) {
    super("Toevoegen / muteren tekstblok");
    this.verkInfo = verkInfo;
    addButton(buttonPrev, buttonSave);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new Page6VerkiezingForm(verkInfo);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goToPage(Page5Verkiezing.class);
  }

  @Override
  public void onSave() {
    form.commit();
    verkInfo.setNaam(form.getBean().getNaam());
    verkInfo.setInhoud(form.getBean().getInhoud());
    getServices().getKiezersregisterService().save(verkInfo);
    successMessage("De gegevens zijn opgeslagen.");
    super.onSave();
  }
}
