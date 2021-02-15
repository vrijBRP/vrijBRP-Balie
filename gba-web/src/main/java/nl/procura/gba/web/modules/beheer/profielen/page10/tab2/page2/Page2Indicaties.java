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

package nl.procura.gba.web.modules.beheer.profielen.page10.tab2.page2;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Indicaties extends NormalPageTemplate {

  private Page2IndicatiesForm form = null;

  private PlAantekeningIndicatie indicatie;

  public Page2Indicaties(PlAantekeningIndicatie indicatie) {

    super("Toevoegen / muteren indicatie");

    this.indicatie = indicatie;

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setForm(new Page2IndicatiesForm(indicatie));

      addComponent(form);
    }

    super.event(event);
  }

  public Page2IndicatiesForm getForm() {
    return form;
  }

  public void setForm(Page2IndicatiesForm form) {
    this.form = form;
  }

  @Override
  public void onNew() {

    indicatie = new PlAantekeningIndicatie();

    form.setIndicatie(indicatie);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    getForm().commit();

    Page2IndicatiesBean b = getForm().getBean();

    indicatie.setProbev(b.getProbev());
    indicatie.setIndicatie(b.getIndicatie());
    indicatie.setOmschrijving(b.getOms());
    indicatie.setButton(b.getButton());

    getServices().getAantekeningService().save(indicatie);

    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }
}
