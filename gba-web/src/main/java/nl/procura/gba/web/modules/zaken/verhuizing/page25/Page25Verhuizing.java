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

package nl.procura.gba.web.modules.zaken.verhuizing.page25;

import com.vaadin.ui.Button;

import nl.bprbzk.bcgba.v14.MatchIdenGegBI;
import nl.bprbzk.bcgba.v14.MatchIdenGegBU;
import nl.procura.bcgba.v14.actions.ActionMatchIdenGeg;
import nl.procura.bcgba.v14.actions.IDGegevens;
import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.web.components.containers.Container;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search.PresentievraagLayout;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search.PresentievraagZoekBean;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisRelatie;
import nl.procura.gba.web.modules.zaken.verhuizing.page27.Page27Verhuizing;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.gba.presentievraag.PresentieVraagResultaatType;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagAntwoord;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagService;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page25Verhuizing extends ZakenPage {

  private final PresentievraagLayout zoekLayout;
  private final VerhuisAanvraag      verhuisAanvraag;
  private final VerhuisRelatie       verhuisRelatie;

  private final Button buttonOverslaan = new Button("Overslaan (alleen in testomgeving)");

  public Page25Verhuizing(VerhuisAanvraag verhuisAanvraag, VerhuisRelatie verhuisRelatie) {

    super("Presentievraag");

    this.verhuisAanvraag = verhuisAanvraag;
    this.verhuisRelatie = verhuisRelatie;

    addButton(buttonPrev);
    addButton(buttonNext);

    setInfo("Vul eventueel de gegevens aan en druk op zoeken (enter).");

    BasePLExt pl = verhuisRelatie.getRelatie().getPl();
    Naam naam = pl.getPersoon().getNaam();
    Geboorte geboorte = pl.getPersoon().getGeboorte();

    PresentievraagZoekBean b = new PresentievraagZoekBean();
    b.setVoornamen(naam.getVoornamen().getValue().getVal());
    b.setVoorvoegsel(naam.getVoorvoegsel().getValue().getVal());
    b.setGeslachtsnaam(naam.getGeslachtsnaam().getValue().getVal());
    b.setGeslacht(Geslacht.get(pl.getPersoon().getGeslacht().getVal()));
    b.setGeboortedatum(new GbaDateFieldValue(geboorte.getGeboortedatum().getVal()));
    b.setGeboorteplaats(geboorte.getGeboorteplaats().getDescr());
    b.setGeboorteland(Container.LAND.get(geboorte.getGeboorteland().getVal()));

    zoekLayout = new PresentievraagLayout(b);
    addComponent(zoekLayout);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (getServices().isTestomgeving()) {
        addButton(buttonOverslaan);
      }
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonOverslaan) {

      verhuisRelatie.setPresentievraagType(PresentieVraagResultaatType.OVERGESLAGEN);

      onPreviousPage();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onEnter() {
    onNextPage();
  }

  @Override
  public void onNextPage() {

    zoekLayout.commit();

    PresentievraagService bcgba = getApplication().getServices().getPresentievraagService();
    IDGegevens idGegevens = zoekLayout.getZoekForm().getIDGegevens();
    BcGbaVraagbericht vraagbericht = zoekLayout.getVraagForm().getBean().getVraag();
    ActionMatchIdenGeg actie = bcgba.getActionMatchIdenGeg(vraagbericht, "1", idGegevens);
    bcgba.zoek(actie);

    PresentievraagAntwoord antwoord = new PresentievraagAntwoord();
    antwoord.setVraagBericht(vraagbericht.getCode());
    antwoord.setBsnRelatie(verhuisRelatie.getRelatie().getPl().getPersoon().getBsn().getVal());
    antwoord.setRequest((MatchIdenGegBI) actie.getRequestObject());
    antwoord.setResponse((MatchIdenGegBU) actie.getResponseObject());

    // Nieuwe presentievraag
    Presentievraag presentieVraag = getServices().getPresentievraagService().getNew(antwoord);

    getNavigation().addPage(new Page27Verhuizing(verhuisAanvraag, verhuisRelatie, presentieVraag));

    super.onSearch();
  }
}
