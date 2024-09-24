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

package nl.procura.gba.web.modules.zaken.verhuizing.page19;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import com.vaadin.data.Property.ValueChangeListener;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAanvraagPage;
import nl.procura.gba.web.modules.zaken.verhuizing.page17.Page17Verhuizing;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

/**
 * Emigratie
 */

public class Page19Verhuizing extends VerhuisAanvraagPage {

  private Page19VerhuizingForm1 form1      = null;
  private InfoLayout            infoLayout = null;

  public Page19Verhuizing(VerhuisAanvraag verhuisAanvraag, boolean allePersonen) {

    super("Verhuizing", verhuisAanvraag);

    addButton(buttonPrev);
    addButton(buttonNext);

    if (!allePersonen) {
      addComponent(new InfoLayout("Let op!", ProcuraTheme.ICOON_24.WARNING,
          "Niet voor alle personen op dit adres wordt aangifte van emigratie gedaan. "
              + "Alle personen die vertrekken moeten daarom in persoon aanwezig zijn."));
    }

    form1 = new Page19VerhuizingForm1();

    infoLayout = new InfoLayout("Het verblijf is korter dan 12 maanden",
        "Als niet wordt voldaan aan het verblijfscriterium wordt de aangifte opgeslagen als incompleet. "
            + "Uit de behandeling van de aangifte zal blijken of deze wordt geweigerd. In geval van weigering volgt een Awb-procedure.");

    addComponent(form1);
    addComponent(infoLayout);

    // Tonen als verblijf korter is dan 12 maanden
    infoLayout.setVisible(getAanvraag().getEmigratie().getDuur().toLowerCase().contains("korter"));
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

    checkAanvang(form1.getBean().getAanvang());

    Page19VerhuizingBean1 b = form1.getBean();

    String a1 = b.getAdres1();
    String a2 = b.getAdres2();
    String a3 = b.getAdres3();

    if (fil(a1) && emp(a2)) {
      throw new ProException(INFO, "Vul een woonplaats in.");
    }

    if (fil(a3) && (emp(a2) || emp(a1))) {
      throw new ProException(INFO, "Vul zowel het adres als de woonplaats in.");
    }

    getAanvraag().getEmigratie().setDatumVertrek(new DateTime(b.getAanvang()));
    getAanvraag().getEmigratie().setAdres1(b.getAdres1());
    getAanvraag().getEmigratie().setAdres2(b.getAdres2());
    getAanvraag().getEmigratie().setAdres3(b.getAdres3());
    getAanvraag().getEmigratie().setDuur(b.getDuur().getOms());
    getAanvraag().getEmigratie().setLand(b.getLand());

    getNavigation().goToPage(new Page17Verhuizing(getAanvraag()));

    super.onNextPage();
  }
}
