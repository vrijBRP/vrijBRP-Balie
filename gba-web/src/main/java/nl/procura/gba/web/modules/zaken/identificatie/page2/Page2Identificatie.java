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

package nl.procura.gba.web.modules.zaken.identificatie.page2;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.identificatie.page1.Page1Identificatie;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieStatusListener;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Identificatie overzicht
 */
public class Page2Identificatie extends NormalPageTemplate {

  private final Button                      buttonOpnieuw = new Button("Opnieuw vaststellen (F2)");
  private final IdentificatieStatusListener succesListener;
  private final Page2IdentificatieForm      form;

  public Page2Identificatie(IdentificatieStatusListener succesListener) {

    this.succesListener = succesListener;

    setSpacing(true);

    OptieLayout ol1 = new OptieLayout();

    form = new Page2IdentificatieForm();

    ol1.getLeft().addComponent(form);

    ol1.getRight().setWidth("200px");
    ol1.getRight().setCaption("Opties");

    buttonOpnieuw.setDescription("Stel de identiteit opnieuw vast.");

    ol1.getRight().addButton(buttonOpnieuw, this);

    H2 h2 = new H2("Identificatie");

    addButton(buttonClose);
    getButtonLayout().addComponent(h2, getButtonLayout().getComponentIndex(buttonClose));
    getButtonLayout().setExpandRatio(h2, 1f);
    getButtonLayout().setWidth("100%");

    setInfo("Identificatie van de persoon", "De identiteit is reeds vastgesteld.");

    addComponent(ol1);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      Identificatie id = getServices().getIdentificatieService().getIdentificatie(getPl());

      if (id != null) {

        form.getBean().setGebruiker(id.getGebruiker().getDescription());
        form.getBean().setTijdstip(astr(id.getTijdstip()));
        form.getBean().setType(id.getAanDeHandVan());
        form.getBean().setNummer(id.getDocumentnr());
        form.getField(Page2IdentificatieBean.NUMMER).setVisible(fil(id.getDocumentnr()));
      }

      form.repaint();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonOpnieuw) || (keyCode == KeyCode.F2)) {

      getNavigation().goToPage(new Page1Identificatie(succesListener));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onClose() {

    getWindow().closeWindow();
  }
}
