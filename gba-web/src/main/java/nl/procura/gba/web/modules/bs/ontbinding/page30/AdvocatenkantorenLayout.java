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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import static nl.procura.gba.web.modules.bs.ontbinding.page30.Page30OntbindingBean5.NAAM;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class AdvocatenkantorenLayout extends OptieLayout implements ClickListener {

  private final Button       buttonBelang = new Button("Belanghebbenden (F3)");
  private final FormAdvocaat formAdvocaat;

  public AdvocatenkantorenLayout(GbaApplication gbaApplication, DossierOntbinding dossierOntbinding) {

    formAdvocaat = new FormAdvocaat(gbaApplication, dossierOntbinding);

    StringBuilder info = new StringBuilder();
    info.append("Selecteer iemand uit de lijst of voer handmatig gegevens in.");
    info.append(
        "<br/>Met de button <b>Belanghebbende</b> kunnen de belanghebbenden aan de lijst worden toegevoegd.");

    getLeft().addComponent(
        new Fieldset("Adressering", new InfoLayout("Addressering advocatenkantoor", info.toString())));
    getLeft().addComponent(formAdvocaat);

    getRight().setWidth("200px");
    getRight().setCaption("Opties");
    getRight().addButton(buttonBelang, this);
  }

  @Override
  public void buttonClick(ClickEvent event) {
    if (event.getButton() == buttonBelang) {
      getParentWindow().addWindow(new BelanghebbendenWindow() {

        @Override
        protected void close() {
          formAdvocaat.updateKantoren();
          super.close();
        }
      });
    }
  }

  public void commit() {
    formAdvocaat.commit();
  }

  public Page30OntbindingBean5 getBean() {
    return formAdvocaat.getBean();
  }

  public boolean isAdded() {
    return formAdvocaat.isAdded();
  }

  public boolean isGezet() {
    return formAdvocaat.getField(NAAM).isVisible();
  }

  public class FormAdvocaat extends Page30OntbindingForm5 {

    private FormAdvocaat(GbaApplication application, DossierOntbinding zaakDossier) {
      super(application);
      setBean(zaakDossier);
    }
  }
}
