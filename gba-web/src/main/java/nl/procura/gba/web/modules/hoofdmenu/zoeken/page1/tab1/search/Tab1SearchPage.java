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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab1.search;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.templates.custom.CustomTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekBean;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekTabPage;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab1.result.Tab1ResultPage;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab1.search.favorieten.FavorietenWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab1.search.landelijk.LandelijkPopup;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class Tab1SearchPage extends ZoekTabPage {

  private final Button buttonFavorieten = new Button("Favorieten (F5)");
  private Tab1Form     form             = new Tab1Form();

  public Tab1SearchPage() {
    super("Zoeken in de gemeentelijke database");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInlogOpmerking();

      addButton(buttonSearch);
      addButton(buttonReset);
      addButton(buttonFavorieten);

      buttonSearch.setCaption("Zoeken (Enter)");

      addComponent(getForm());

      getForm().getField(ZoekBean.BSN).focus();
    }

    super.event(event);
  }

  public Tab1Form getForm() {
    return form;
  }

  public void setForm(Tab1Form form) {
    this.form = form;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (isKeyCode(button, keyCode, KeyCode.F5, buttonFavorieten)) {
      onFavorieten();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onNew() {
    getForm().reset();
  }

  @Override
  public void onSearch() {

    getServices().getPersonenWsService().getOpslag().clear();

    getForm().commit();

    final ZoekBean b = getForm().getBean();

    PLEArgs args = new PLEArgs();

    args.addNummer(b.getBsn().getStringValue());
    args.addNummer(b.getAnr().getStringValue());
    args.setGeboortedatum(b.getGeboortedatum().getStringValue());

    if (b.getGeslacht() != null) {
      args.setGeslacht(b.getGeslacht().getAfkorting());
    }

    args.setGeslachtsnaam(astr(b.getGeslachtsnaam()));

    if (b.getVoorvoegsel() != null) {
      args.setVoorvoegsel(b.getVoorvoegsel().getDescription());
    }

    args.setVoornaam(b.getVoornaam());
    args.setTitel(b.getTitel());

    if (b.getStraat() != null) {
      args.setStraat(b.getStraat().getDescription());
    }

    args.setHuisnummer(b.getHnr());
    args.setHuisletter(b.getHnrl());
    args.setHuisnummertoevoeging(b.getHnrt());
    args.setPostcode(b.getPostcode());

    if (b.getGemeentedeel() != null) {
      args.setGemeentedeel(b.getGemeentedeel().getDescription());
    }

    args.setShowHistory(false);
    args.setShowArchives(false);

    args.addCat(PERSOON, VB, INSCHR, VERW);
    args.setCat(HUW_GPS, isTru(getApplication().getServices().getGebruiker().getParameters().get(
        ParameterConstant.ZOEK_PLE_NAAMGEBRUIK).getValue()));

    args.setCustomTemplate(CustomTemplate.PERSON);
    args.setDatasource(PLEDatasource.PROCURA);
    args.setMaxFindCount(aval(getApplication().getServices().getGebruiker().getParameters().get(
        ParameterConstant.ZOEK_MAX_FOUND_COUNT).getValue()));

    boolean isStraat = fil(args.getStraat());
    boolean isHnr = fil(args.getHuisnummer());
    boolean isPc = fil(args.getPostcode());
    boolean isGem = fil(args.getGemeente());
    boolean isGemDeel = fil(args.getGemeentedeel());

    args.setShowSuspended(!(isStraat || isHnr || isPc || isGem || isGemDeel));

    PLResultComposite result = getApplication().getServices().getPersonenWsService().getPersoonslijsten(args,
        false);

    switch (result.getBasisPLWrappers().size()) {

      case 0:
        if (getServices().getPersonenWsService().isGbavZoeken()) {

          getWindow().addWindow(new LandelijkPopup() {

            @Override
            public void buttonYes() {

              try {
                zoekLandelijk(b);
              } finally {
                super.buttonYes();
              }
            }
          });
        } else {
          new Message(getWindow(), "Geen zoekresultaten", Message.TYPE_WARNING_MESSAGE);
        }

        break;

      case 1:
        BasePLExt pl = result.getBasisPLWrappers().get(0);
        getApplication().goToPl(getWindow(), "", pl.getDatasource(), pl);
        break;

      default:
        getNavigation().goToPage(new Tab1ResultPage(args, isRekeninghoudenAdres(args), result));
        break;
    }
  }

  private boolean isRekeninghoudenAdres(PLEArgs args) {
    return fil(args.getStraat()) || fil(args.getHuisnummer()) || fil(args.getPostcode()) || fil(args.getGemeente());
  }

  private void onFavorieten() {
    getWindow().addWindow(new FavorietenWindow());
  }
}
