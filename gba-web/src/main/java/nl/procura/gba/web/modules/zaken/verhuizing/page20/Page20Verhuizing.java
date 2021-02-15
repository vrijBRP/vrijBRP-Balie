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

package nl.procura.gba.web.modules.zaken.verhuizing.page20;

import com.vaadin.data.Property.ValueChangeListener;

import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAanvraagPage;
import nl.procura.gba.web.modules.zaken.verhuizing.page13.Page13Verhuizing;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Hervestiging
 */

public class Page20Verhuizing extends VerhuisAanvraagPage {

  private Page20VerhuizingForm1 form1      = null;
  private Page20VerhuizingForm2 form2      = null;
  private InfoLayout            infoLayout = null;

  public Page20Verhuizing(VerhuisAanvraag verhuisAanvraag) {

    super("Verhuizing", verhuisAanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    form1 = new Page20VerhuizingForm1();
    form2 = new Page20VerhuizingForm2();

    addComponent(form1);

    infoLayout = new InfoLayout("Het verblijf is korter dan 4 maanden",
        "Als niet wordt voldaan aan het verblijfscriterium wordt de aangifte opgeslagen als incompleet. Uit de behandeling van de aangifte zal blijken of deze wordt geweigerd. "
            + "In geval van weigering volgt een Awb-procedure.");

    addComponent(infoLayout);

    // Tonen als verblijf korter is dan 12 maanden
    infoLayout.setVisible(getAanvraag().getHervestiging().getDuur().toLowerCase().contains("korter"));

    addComponent(new Fieldset("Rechtsfeiten", form2));
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1.getDuurVeld().addListener((ValueChangeListener) event1 -> {

        boolean korter = form1.getDuurVeld().getValue().toString().toLowerCase().contains("korter");

        infoLayout.setVisible(korter);

      });
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form1.commit();
    form2.commit();

    Page20VerhuizingBean1 b1 = form1.getBean();
    Page20VerhuizingBean2 b2 = form2.getBean();

    getAanvraag().getHervestiging().setDuur(b1.getDuur().getOms());
    getAanvraag().getHervestiging().setLand(b1.getLand());
    getAanvraag().getHervestiging().setRechtsfeiten(b2.getRechtsfeiten());

    getNavigation().goToPage(new Page13Verhuizing(getAanvraag()));

    super.onNextPage();
  }
}
