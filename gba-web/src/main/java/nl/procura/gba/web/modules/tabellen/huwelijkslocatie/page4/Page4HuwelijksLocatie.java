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

package nl.procura.gba.web.modules.tabellen.huwelijkslocatie.page4;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatieOptie.HuwelijksLocatieOptie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page4HuwelijksLocatie extends NormalPageTemplate {

  private Page4HuwelijksLocatieForm form = null;

  private HuwelijksLocatieOptie optie;

  public Page4HuwelijksLocatie(HuwelijksLocatieOptie optie) {

    super("Toevoegen / muteren locatie optie");

    this.optie = optie;

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setForm(new Page4HuwelijksLocatieForm(optie));

      addComponent(form);
    }

    super.event(event);
  }

  public Page4HuwelijksLocatieForm getForm() {
    return form;
  }

  public void setForm(Page4HuwelijksLocatieForm form) {
    this.form = form;
  }

  @Override
  public void onNew() {

    getForm().reset();

    optie = new HuwelijksLocatieOptie();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    getForm().commit();

    Page4HuwelijksLocatieOptiesBean b = getForm().getBean();

    optie.setHuwelijksLocatieOptie(b.getOptie());
    optie.setOptieType(b.getType());
    optie.setVerplichteOptie(b.isVerplicht());
    optie.setVnr(b.getVnr());
    optie.setMin(b.getMin());
    optie.setMax(b.getMax());
    optie.setAlias(b.getAlias());

    getServices().getHuwelijkService().save(optie);

    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }
}
