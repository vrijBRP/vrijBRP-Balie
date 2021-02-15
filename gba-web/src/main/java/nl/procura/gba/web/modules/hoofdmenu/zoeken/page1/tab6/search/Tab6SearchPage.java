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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search;

import nl.bprbzk.bcgba.v14.MatchIdenGegBI;
import nl.bprbzk.bcgba.v14.MatchIdenGegBU;
import nl.procura.bcgba.v14.actions.ActionMatchIdenGeg;
import nl.procura.bcgba.v14.actions.IDGegevens;
import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekTabPage;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results.Tab6ResultsPage;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagAntwoord;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagService;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab6SearchPage extends ZoekTabPage {

  private final PresentievraagLayout zoekLayout = new PresentievraagLayout();

  public Tab6SearchPage() {
    super("Presentievraag");
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInlogOpmerking();

      addButton(buttonSearch);
      addButton(buttonReset);

      buttonSearch.setCaption("Zoeken (Enter)");
      addComponent(zoekLayout);
    }

    super.event(event);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onNew() {
    zoekLayout.reset();
    super.onNew();
  }

  @Override
  public void onSearch() {

    zoekLayout.commit();

    PresentievraagService bcgba = getApplication().getServices().getPresentievraagService();
    IDGegevens idGegevens = zoekLayout.getZoekForm().getIDGegevens();
    BcGbaVraagbericht vraagbericht = zoekLayout.getVraagForm().getBean().getVraag();
    ActionMatchIdenGeg actie = bcgba.getActionMatchIdenGeg(vraagbericht, "1", idGegevens);
    bcgba.zoek(actie);

    PresentievraagAntwoord antwoord = new PresentievraagAntwoord();
    antwoord.setVraagBericht(vraagbericht.getCode());
    antwoord.setBsnRelatie("");
    antwoord.setRequest((MatchIdenGegBI) actie.getRequestObject());
    antwoord.setResponse((MatchIdenGegBU) actie.getResponseObject());

    Presentievraag presentievraag = getServices().getPresentievraagService().getNew(antwoord);
    getNavigation().addPage(new Tab6ResultsPage(presentievraag, true));
  }
}
