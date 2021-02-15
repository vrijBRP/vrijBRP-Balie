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

package nl.procura.gba.web.modules.beheer.aktes.page3;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.aktes.page2.Page2AktesForm1;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteCategorie;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteDeel;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3Aktes extends NormalPageTemplate {

  private Page2AktesForm2 form = null;

  private DossierAkteDeel deel;

  public Page3Aktes(DossierAkteDeel deel) {

    super("Toevoegen / muteren akte registerdeel");

    this.deel = deel;

    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setForm(new Page2AktesForm2(deel));

      addComponent(new CatForm(deel.getCategorie()));

      addComponent(form);
    }

    super.event(event);
  }

  public Page2AktesForm2 getForm() {
    return form;
  }

  public void setForm(Page2AktesForm2 form) {
    this.form = form;
  }

  @Override
  public void onNew() {

    this.deel = new DossierAkteDeel(deel.getCategorie());

    form.setAkteDeel(deel);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    getForm().commit();

    Page2AktesBean2 bean = getForm().getBean();

    deel.setRegisterSoort(bean.getSoort());
    deel.setRegisterdeel(bean.getCode());
    deel.setOmschrijving(bean.getOmschrijving());
    deel.setMin(toBigDecimal(bean.getMin()));
    deel.setMax(toBigDecimal(bean.getMax()));

    getServices().getAkteService().saveRegisterDeel(deel);

    successMessage("De gegevens zijn opgeslagen.");

    super.onSave();
  }

  class CatForm extends Page2AktesForm1 {

    public CatForm(DossierAkteCategorie categorie) {

      super(categorie);

      setReadOnly(true);
    }
  }
}
