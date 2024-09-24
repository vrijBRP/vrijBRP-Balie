/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.rijbewijs.page7;

import static org.apache.commons.lang3.ObjectUtils.anyNotNull;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.zaken.rijbewijs.RbwUpdateToDateCheck;
import nl.procura.gba.web.modules.zaken.rijbewijs.RijbewijsPage;
import nl.procura.gba.web.modules.zaken.rijbewijs.page17.Page17Rijbewijs;
import nl.procura.gba.web.modules.zaken.rijbewijs.page8.Page8Rijbewijs;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord;
import nl.procura.gba.web.services.zaken.rijbewijs.converters.P1653ToDocumentAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.converters.P1654ToDocumentAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.converters.P1658ToDocumentAanvraag;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.rdw.messages.P1653;
import nl.procura.rdw.messages.P1654;
import nl.procura.rdw.messages.P1658;
import nl.procura.rdw.processen.p1653.f02.AANVRRYBKRT;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Aanvraagresultaatscherm
 */
public class Page7Rijbewijs extends RijbewijsPage {

  private RijbewijsAanvraag         rijbewijsAanvraag         = null;
  private RijbewijsAanvraagAntwoord rijbewijsDocumentAanvraag = null;
  private P1653                     p1653;
  private P1654                     p1654;
  private P1658                     p1658;

  public Page7Rijbewijs() {

    super("Overzicht aanvraag");

    setMargin(true);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  public Page7Rijbewijs(RijbewijsAanvraag rijbewijsAanvraag, P1653 p1653) {
    this();
    this.rijbewijsAanvraag = rijbewijsAanvraag;
    this.p1653 = p1653;
  }

  public Page7Rijbewijs(RijbewijsAanvraag rijbewijsAanvraag, P1654 p1654) {
    this();
    this.rijbewijsAanvraag = rijbewijsAanvraag;
    this.p1654 = p1654;
  }

  public Page7Rijbewijs(RijbewijsAanvraag rijbewijsAanvraag, P1658 p1658) {
    this();
    this.rijbewijsAanvraag = rijbewijsAanvraag;
    this.p1658 = p1658;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (p1653 != null) {

        // Aanvraagsoort < 10

        setAskToReturn(true);

        AANVRRYBKRT response = (AANVRRYBKRT) p1653.getResponse().getObject();
        rijbewijsDocumentAanvraag = P1653ToDocumentAanvraag.get(response, getServices());

      } else if (p1654 != null) { // Aanvraagsoort >= 10 (Sprake van omwisseling)

        setAskToReturn(true);

        nl.procura.rdw.processen.p1654.f02.AANVRRYBKRT response = (nl.procura.rdw.processen.p1654.f02.AANVRRYBKRT) p1654
            .getResponse().getObject();

        rijbewijsDocumentAanvraag = P1654ToDocumentAanvraag.get(response, getServices());

      } else if (p1658 != null) {

        setAskToReturn(false);

        nl.procura.rdw.processen.p1658.f02.AANVRRYBKRT response = (nl.procura.rdw.processen.p1658.f02.AANVRRYBKRT) p1658
            .getResponse().getObject();

        rijbewijsDocumentAanvraag = P1658ToDocumentAanvraag.get(response, getServices());
      }

      GbaTabsheet tabs = new GbaTabsheet();
      tabs.addStyleName("zoektab");
      tabs.addTab(new Page7RijbewijsSubpage1(rijbewijsDocumentAanvraag), "Basis");
      tabs.addTab(new Page7RijbewijsSubpage2(this, rijbewijsDocumentAanvraag), "Meer gegevens");

      addComponent(tabs);
    }

    super.event(event);
  }

  public P1653 getP1653() {
    return p1653;
  }

  public P1654 getP1654() {
    return p1654;
  }

  public P1658 getP1658() {
    return p1658;
  }

  @Override
  public void onNextPage() {

    new RbwUpdateToDateCheck(rijbewijsAanvraag, (GbaWindow) getWindow()) {

      @Override
      public void onProceed(RijbewijsAanvraag checkedAanvraag) {
        rijbewijsAanvraag = checkedAanvraag;
        if (getServices().getReisdocumentBezorgingService().isEnabled() && anyNotNull(p1653, p1654)) {
          getNavigation().goToPage(new Page17Rijbewijs(rijbewijsDocumentAanvraag, checkedAanvraag));
        } else {
          getNavigation().goToPage(new Page8Rijbewijs(rijbewijsDocumentAanvraag, checkedAanvraag));
        }
      }

      @Override
      public void onNotUpdated() {
        // Not needed
      }
    };

    super.onNextPage();
  }
}
