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

package nl.procura.gba.web.modules.bs.erkenning.page5.akte;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.modules.bs.erkenning.page10.Page10Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page5.Page5Erkenning;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Erkenning
 */
public class Page5ErkenningAktePage extends ButtonPageTemplate {

  private final Page5Erkenning        page;
  private final DossierPersoon        persoon;
  private final Button                buttonPersoon = new Button("Persoonsgegevens");
  private Page5ErkenningAktePageForm1 form1;

  public Page5ErkenningAktePage(Page5Erkenning page, DossierPersoon persoon) {
    this.page = page;
    this.persoon = persoon;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSave);
      addButton(buttonPersoon, 1f);
      addButton(buttonClose);

      String header = "Gegevens over de geboorteakte van " + persoon.getNaam().getPred_adel_voorv_gesl_voorn();
      String msg = "Geef de aktegegevens in.";

      setInfo(header, msg);
      setSpacing(true);

      form1 = new Page5ErkenningAktePageForm1(persoon);
      addComponent(form1);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (buttonPersoon.equals(button)) {
      page.getNavigation().goToPage(new Page10Erkenning(persoon));
      getWindow().closeWindow();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onSave() {

    form1.commit();

    persoon.setGeboorteAkteNummer(form1.getBean().getBsAkteNummer());
    persoon.setGeboorteAkteBrpNummer(form1.getBean().getBrpAkteNummer());
    persoon.setGeboorteAkteJaar(toBigDecimal(form1.getBean().getJaar()));
    persoon.setGeboorteAktePlaats(form1.getBean().getPlaats());

    getServices().getDossierService().savePersoon(persoon);

    super.onSave();

    page.herlaadKindTabel();

    onClose();
  }
}
