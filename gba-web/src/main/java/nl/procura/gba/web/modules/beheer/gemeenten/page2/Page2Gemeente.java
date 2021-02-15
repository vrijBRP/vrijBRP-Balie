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

package nl.procura.gba.web.modules.beheer.gemeenten.page2;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Gemeente extends NormalPageTemplate {

  private Page2GemeenteForm form = null;

  private Gemeente gemeente;

  public Page2Gemeente(Gemeente gemeente) {

    super("Toevoegen / muteren gemeente");

    this.gemeente = gemeente;

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new Page2GemeenteForm(gemeente);

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onNew() {

    form.reset();

    gemeente = new Gemeente();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    Page2GemeenteBean b = form.getBean();

    gemeente.setCbscode(toBigDecimal(b.getCbscode()));
    gemeente.setGemeente(b.getGemeente());
    gemeente.setAdres(b.getAdres());
    gemeente.setPc(b.getPc().getStringValue());
    gemeente.setPlaats(b.getPlaats());

    getServices().getGemeenteService().save(gemeente);

    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }
}
