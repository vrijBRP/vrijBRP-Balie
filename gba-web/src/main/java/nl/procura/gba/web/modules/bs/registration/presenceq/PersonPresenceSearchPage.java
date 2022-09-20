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

package nl.procura.gba.web.modules.bs.registration.presenceq;

import java.util.function.Consumer;

import com.vaadin.ui.Button;

import java.util.function.Supplier;
import nl.bprbzk.bcgba.v14.MatchIdenGegBI;
import nl.bprbzk.bcgba.v14.MatchIdenGegBU;
import nl.procura.bcgba.v14.actions.ActionMatchIdenGeg;
import nl.procura.bcgba.v14.actions.IDGegevens;
import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search.PresentievraagLayout;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search.PresentievraagZoekBean;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagAntwoord;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagService;

public class PersonPresenceSearchPage extends NormalPageTemplate {

  private final PresentievraagLayout             searchLayout;
  private final Button                           buttonSkip = new Button("Overslaan (alleen in testomgeving)");
  private final DossierRegistration              dossier;
  private final DossierPersoon                   person;
  private final Supplier<PresentievraagZoekBean> searchBean;
  private final Consumer<DossierPersoon>         nextListener;

  public PersonPresenceSearchPage(DossierRegistration dossier,
      DossierPersoon person,
      Supplier<PresentievraagZoekBean> searchBean,
      Consumer<DossierPersoon> nextListener) {
    super("Presentievraag - nieuwe inschrijver");
    this.dossier = dossier;
    this.person = person;
    this.searchBean = searchBean;
    this.nextListener = nextListener;

    searchLayout = new PresentievraagLayout(searchBean != null
        ? searchBean.get()
        : new PresentievraagZoekBean());

    addButton(buttonSearch);
    addButton(buttonReset);

    buttonClose.addListener(this);
    getMainbuttons().add(buttonClose);
    addComponent(searchLayout);
  }

  @Override
  protected void initPage() {
    if (getServices().isTestomgeving()) {
      addButton(buttonSkip);
    }
    super.initPage();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonSkip) {
      getWindow().closeWindow();
      nextListener.accept(person);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNew() {
    searchLayout.reset();
    super.onNew();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onSearch() {

    searchLayout.commit();

    final PresentievraagService bcgba = getApplication().getServices().getPresentievraagService();
    final IDGegevens idGegevens = searchLayout.getZoekForm().getIDGegevens();
    final BcGbaVraagbericht vraagbericht = searchLayout.getVraagForm().getBean().getVraag();
    final ActionMatchIdenGeg actie = bcgba.getActionMatchIdenGeg(vraagbericht, "1", idGegevens);
    bcgba.zoek(actie);

    PresentievraagAntwoord antwoord = new PresentievraagAntwoord();
    antwoord.setVraagBericht(vraagbericht.getCode());
    antwoord.setRequest((MatchIdenGegBI) actie.getRequestObject());
    antwoord.setResponse((MatchIdenGegBU) actie.getResponseObject());

    Presentievraag presentieVraag = getServices().getPresentievraagService().getNew(antwoord);
    getNavigation().goToPage(new PersonPresenceResultPage(dossier, person, nextListener, presentieVraag));
  }
}
