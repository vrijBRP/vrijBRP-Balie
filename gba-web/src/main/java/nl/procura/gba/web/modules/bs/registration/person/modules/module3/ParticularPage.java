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

package nl.procura.gba.web.modules.bs.registration.person.modules.module3;

import java.util.function.Consumer;

import com.vaadin.ui.Layout;

import nl.procura.gba.web.modules.bs.registration.person.modules.AbstractPersonPage;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class ParticularPage extends AbstractPersonPage {

  private ParticularForm  particularForm;
  private DeclarationForm declarationForm;

  public ParticularPage(DossierPersoon person, Consumer<DossierPersoon> addPersonListener) {
    super(person, addPersonListener);
  }

  @Override
  protected void initPage() {
    particularForm = new ParticularForm(getPerson());
    declarationForm = new DeclarationForm(getPerson());
    addComponent(particularForm);
    addComponent(getDeclarationLayout());
    super.initPage();
  }

  @Override
  public void checkPage(Runnable next) {
    onSave();
    next.run();
  }

  @Override
  public void onSave() {
    particularForm.save();
    declarationForm.save();
    getAddPersonListener().accept(getPerson());
  }

  private Layout getDeclarationLayout() {
    final VLayout vLayout = new VLayout();
    InfoLayout layout = new InfoLayout("Ter informatie",
        "P, G, O en K kan alleen worden gekozen als er bewijs is voor de relatie");
    vLayout.addComponent(layout);
    vLayout.addComponent(declarationForm);
    return new Fieldset("Aangifte", vLayout);
  }
}
