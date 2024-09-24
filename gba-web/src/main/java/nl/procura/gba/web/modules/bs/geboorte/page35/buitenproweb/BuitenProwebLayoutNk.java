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

package nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class BuitenProwebLayoutNk extends VerticalLayout implements ClickListener {

  private final Button               buttonReset = new Button("Reset");
  private final Page35GeboorteFormNk form;

  public BuitenProwebLayoutNk(DossierGeboorte geboorte) {
    form = new Page35GeboorteFormNk(geboorte);
    OptieLayout optieLayout2 = new OptieLayout();
    optieLayout2.getLeft().addComponent(form);
    optieLayout2.getRight().addButton(buttonReset, this);
    optieLayout2.getRight().setWidth("150px");

    addComponent(new Fieldset("Naamskeuzegegevens"));

    String info = "Vul de velden handmatig in.";
    if (geboorte.isOvergangsperiodeNaamskeuze()) {
      info +=
          "<hr><b>Als de naamskeuze gedaan is in 2023 dan is mogelijk destijds ook gekozen voor een dubbele achternaam."
              + "<br>Vul dan bij 2e gekozen naam deze dubbele achternaam in. Dit wordt afgedrukt op de latere vermelding</b>";
    }
    addComponent(new InfoLayout("Ter informatie", info));
    setSpacing(true);
    addComponent(optieLayout2);
  }

  @Override
  public void buttonClick(ClickEvent event) {
    if (event.getButton() == buttonReset) {
      reset();
    }
  }

  public Page35GeboorteFormNk getForm() {
    return form;
  }

  public void reset() {
    form.setGeboorte(null);
  }
}
