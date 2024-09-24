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

package nl.procura.gba.web.modules.persoonslijst.overig;

import static nl.procura.gba.web.modules.persoonslijst.overig.header.HeaderBean.STATUS;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.isTru;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.PopupView;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.components.layouts.page.AantekeningButton;
import nl.procura.gba.web.components.layouts.page.ContactButton;
import nl.procura.gba.web.components.layouts.page.GbavLabel;
import nl.procura.gba.web.components.layouts.page.IdentificatieButton;
import nl.procura.gba.web.components.layouts.page.IndicatieLabel;
import nl.procura.gba.web.components.layouts.page.KassaButton;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.page.PersonListMutationsButton;
import nl.procura.gba.web.components.layouts.page.StatusButton;
import nl.procura.gba.web.components.layouts.page.relaties.ZoekRelatiesLayout;
import nl.procura.gba.web.components.layouts.page.relaties.ZoekRelatiesPopup;
import nl.procura.gba.web.components.layouts.page.zoekhistorie.ZoekHistorieLayout;
import nl.procura.gba.web.components.layouts.page.zoekhistorie.ZoekHistoriePopup;
import nl.procura.gba.web.modules.persoonslijst.overig.header.HeaderBean;
import nl.procura.gba.web.modules.persoonslijst.overig.header.HeaderForm;
import nl.procura.gba.web.modules.persoonslijst.overig.listpage.PlListPage;
import nl.procura.gba.web.modules.persoonslijst.overig.meta.PlMetaPage;
import nl.procura.gba.web.modules.persoonslijst.overzicht.ModuleOverzichtPersoonslijst;
import nl.procura.gba.web.modules.zaken.overzicht.ModuleOverzichtZaken;
import nl.procura.gba.web.modules.zaken.overzicht.page1.Page1OverzichtZaken;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.ServiceListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.gba.functies.PersoonslijstStatus;
import nl.procura.gba.web.services.gba.ple.opslag.PersoonStatusOpslagEntry;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.vaadin.theme.twee.Icons;

public class PlPage extends NormalPageTemplate {

  public PlPage(String title) {

    super(title);

    setMargin(true);

    addComponent(new HeaderForm() {

      @Override
      public void afterSetColumn(final Column column, Field field, Property property) {

        if (property.is(STATUS)) {

          PersoonStatusOpslagEntry status = PersoonslijstStatus.getStoredStatus(getServices(), getPl());
          final StatusButton button = new StatusButton(status != null ? status.getStatus() : "", getPl());

          if (status != null) {
            column.addComponent(button);

          } else {
            button.setCaption(PersoonslijstStatus.getStatus(getServices(), getPl()));
            column.addComponent(button);
          }
        }

        super.afterSetColumn(column, field, property);
      }

      @Override
      public void beforeSetBean() {

        // Set form

        BasePLExt pl = getPl();
        Cat1PersoonExt persoon = pl.getPersoon();
        HeaderBean bean = getBean();

        String naam = persoon.getNaam().getAdelTitelVoorvGeslVoornOverigeInit();
        boolean isNaamgebruik = isTru(
            getServices().getParameterService().getParm(ParameterConstant.ZOEK_PLE_NAAMGEBRUIK));

        if (isNaamgebruik) {
          naam = persoon.getNaam().getNaamNaamgebruikGeslachtsnaamVoornaamAanschrijf();
          naam += " (aanschrijfnaam)";
        }

        bean.setNaam(naam);
        bean.setBsn(persoon.getBsn().getDescr());
        bean.setAdres(pl.getVerblijfplaats().getAdres().getAdresPcWplGem());
        bean.setAnr(persoon.getAnr().getDescr());
        bean.setBurgstaat(astr(persoon.getBurgerlijkeStand().getStatus().getType().getBs()));
        bean.setGeboortedatum(persoon.getGeboorte().getDatumLeeftijd());
        bean.setGeslacht(persoon.getGeslacht().getDescr());
        bean.setAdresIndicatie(getServices(), pl);

        // Add buttons / labels

        addGBAVLabel();
        addIndicatieLabel();
        addAantekeningButton();
        addKassaButton();
        addPersonListMutationsButton();
        addIdentificatieButton();
        addContactButton();
        addHistorieButton();
        addRelatieButton();
      }
    });

    // Bij blokkering geen services tonen
    if (Services.getInstance()
        .getPersonenWsService()
        .getHuidige()
        .getPersoon()
        .getStatus()
        .isBlokkering()) {

      addComponent(new InfoLayout("Deze persoonslijst is geblokkeerd",
          Icons.getIcon(Icons.ICON_WARN),
          "De persoon zit in een verhuiscyclus. " +
              "Maak geen nieuwe zaken aan."));
    }
  }

  /**
   * Wat te doen als er op F1 wordt gedrukt.
   */
  @Override
  public void onPreviousPage() {

    if (getNavigation() != null) {

      PageLayout prevPage = getNavigation().getPreviousPage();
      PageLayout currentPage = getNavigation().getCurrentPage();

      if (prevPage == null) {

        if (currentPage.getClass() == Page1OverzichtZaken.class) {

          // Terug naar HomeWindow
          getApplication().goBackToWindow(getWindow(), new HomeWindow());
        } else if ((currentPage instanceof PlMetaPage) || (currentPage instanceof PlListPage)) {

          ((GbaWindow) getWindow()).getFragmentUtility().setFragment(ModuleOverzichtPersoonslijst.NAME, true);
        } else {
          // Terug naar Overzicht pagina
          getWindow().open(
              new ExternalResource(getApplication().getExternalURL("pl#" + ModuleOverzichtZaken.NAME)));
        }
      } else {

        // Terug naar vorige pagina
        getNavigation().goBackToPage(prevPage);
      }
    }

    super.onPreviousPage();
  }

  protected BasePLExt getPl(String nummer) {
    return getApplication().getServices().getPersonenWsService().getPersoonslijst(nummer);
  }

  private void addGBAVLabel() {
    GbavLabel label = new GbavLabel(getApplication());
    if (label.isAdd()) {
      getMainbuttons().addComponent(label);
      getMainbuttons().setComponentAlignment(label, Alignment.MIDDLE_LEFT);
    }
  }

  private void addIndicatieLabel() {

    final IndicatieLabel label = new IndicatieLabel(getApplication());

    getApplication().getServices().getIndicatiesService().setListener(new ServiceListener() {

      @Override
      public void action(ServiceEvent event) {
        label.doCheck();
        checkIndicatieLabel(label);
      }

      @Override
      public String getId() {
        return "IndicatieListener";
      }
    });

    checkIndicatieLabel(label);
  }

  private void addAantekeningButton() {
    addInfoButton(new AantekeningButton(), ProfielActie.SELECT_OPTIE_AANTEKENINGEN);
  }

  private void addKassaButton() {
    addInfoButton(new KassaButton(), ProfielActie.SELECT_HOOFD_KASSA);
  }

  private void addPersonListMutationsButton() {
    if (PLEDatasource.PROCURA == getServices().getPersonenWsService().getHuidige().getDatasource()) {
      addInfoButton(new PersonListMutationsButton(), ProfielActie.SELECT_PL_MUTATIONS);
    }
  }

  private void addIdentificatieButton() {
    addInfoButton(new IdentificatieButton(), ProfielActie.SELECT_OPTIE_IDENTIFICATIE);
  }

  private void addContactButton() {
    addInfoButton(new ContactButton(), ProfielActie.SELECT_OPTIE_CONTACTGEGEVENS);
  }

  private void addHistorieButton() {
    addInfoButton(getHistorieButton(), null);
  }

  private void addRelatieButton() {
    addInfoButton(getZoekRelatiesButton(), null);
  }

  private void addInfoButton(Component c, ProfielActie profielActie) {
    if (profielActie == null || getApplication().isProfielActie(profielActie)) {
      getMainbuttons().addComponent(c);
      getMainbuttons().setComponentAlignment(c, Alignment.MIDDLE_LEFT);
    }
  }

  private void checkIndicatieLabel(IndicatieLabel label) {
    if (label.isAdd()) {
      getMainbuttons().addComponent(label, 1);
      getMainbuttons().setComponentAlignment(label, Alignment.MIDDLE_LEFT);
    } else {
      getMainbuttons().removeComponent(label);
    }
  }

  private PopupView getHistorieButton() {

    ZoekHistorieLayout l = new ZoekHistorieLayout();
    ZoekHistoriePopup p = new ZoekHistoriePopup("", l);
    p.addStyleName("overlay");

    return p;
  }

  private PopupView getZoekRelatiesButton() {

    ZoekRelatiesLayout l = new ZoekRelatiesLayout();
    ZoekRelatiesPopup p = new ZoekRelatiesPopup("", l);
    p.addStyleName("overlay");

    return p;
  }
}
