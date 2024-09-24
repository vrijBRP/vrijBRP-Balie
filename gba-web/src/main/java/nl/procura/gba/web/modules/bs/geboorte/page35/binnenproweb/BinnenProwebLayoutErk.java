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

package nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb;

import static nl.procura.standard.Globalfunctions.emp;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseListener;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.common.pages.erkenningpage.BsErkenningWindow;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class BinnenProwebLayoutErk extends GbaVerticalLayout implements ClickListener {

  private final Button          buttonZoek = new Button("Zoek erkenningen");
  private final DossierGeboorte geboorte;
  private Page35GeboorteFormErk form;

  public BinnenProwebLayoutErk(DossierGeboorte geboorte) {
    this.geboorte = geboorte;
    form = new Page35GeboorteFormErk(geboorte);

    OptieLayout optieLayout2 = new OptieLayout();
    optieLayout2.getLeft().addComponent(form);
    optieLayout2.getRight().addButton(buttonZoek, this);
    optieLayout2.getRight().setWidth("150px");

    addComponent(new Fieldset("Gekoppelde erkenning"));

    setInfo();
    setSpacing(true);
    addComponent(optieLayout2);
  }

  // Als er een erkenning is gevonden dan die melding tonen
  @Override
  public void buttonClick(ClickEvent event) {
    if (event.getButton() == buttonZoek) {
      BsErkenningWindow erkWindow = new BsErkenningWindow(geboorte);
      erkWindow.addListener((CloseListener) e -> form.setGeboorte(geboorte));
      getWindow().addWindow(erkWindow);
    }
  }

  public Page35GeboorteFormErk getForm() {
    return form;
  }

  public void setForm(Page35GeboorteFormErk form) {
    this.form = form;
  }

  private void setInfo() {
    DossierErkenning erkenning = geboorte.getErkenningVoorGeboorte();
    String info = "Koppeling een erkenning aan de geboorteaangifte.";
    if (emp(geboorte.getGezinssituatie().getCode()) && erkenning != null) {
      info = "Er is een erkenning gevonden van de moeder van " + erkenning.getDossier().getDatumIngang();
    }
    if (geboorte.isOvergangsperiodeNaamskeuze()) {
      info +=
          "<hr/><b>Als de erkenning gedaan is in 2023 dan is mogelijk destijds ook gekozen voor een dubbele achternaam."
              + "<br>Vul dan bij 2e gekozen naam deze dubbele achternaam in. Dit wordt afgedrukt op de latere vermelding</b>";
    }
    addComponent(new InfoLayout("Ter informatie", info));
  }
}
