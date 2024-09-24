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

package nl.procura.gba.web.modules.zaken.verhuizing.overzicht.bewoners.page1;

import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionType.NO_RESULTS;

import java.util.List;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.extensions.WKResultWrapper;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.modules.zaken.verhuizing.overzicht.bewoners.page1.Page1BewonersAdresContainer.BewonersAdres;
import nl.procura.gba.web.modules.zaken.verhuizing.overzicht.bewoners.page2.Page2BewonersPage;
import nl.procura.gba.web.modules.zaken.woningkaart.page1.PersoonslijstAdresContainer;
import nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectWindow;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraagAdres;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Overzicht bewoners
 */
public class Page1BewonersPage extends ButtonPageTemplate implements ValueChangeListener {

  protected final Button buttonObject  = new Button("Objectinformatie (F3)");
  protected final Button buttonBrieven = new Button("Brieven (F2)");

  private final VerhuisAanvraag    aanvraag;
  private final AdresSelect        select     = new AdresSelect();
  private final Page1BewonersTable table      = new Page1BewonersTable();
  private Page1BewonersInfoLayout  infoLayout = new Page1BewonersInfoLayout();

  public Page1BewonersPage(VerhuisAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      getButtonLayout().setWidth("100%");
      setSpacing(true);

      select.setHeight("25px");

      addComponent(getButtonLayout());
      addButton(buttonClose);
      getButtonLayout().addComponent(select);
      addButton(buttonBrieven);
      addButton(buttonObject);

      getButtonLayout().setExpandRatio(select, 1);
      getButtonLayout().setWidth("100%");
      getButtonLayout().setComponentAlignment(buttonClose, Alignment.MIDDLE_LEFT);
      getButtonLayout().setComponentAlignment(buttonBrieven, Alignment.MIDDLE_RIGHT);
      getButtonLayout().setComponentAlignment(buttonObject, Alignment.MIDDLE_RIGHT);
      getButtonLayout().setComponentAlignment(select, Alignment.MIDDLE_LEFT);

      Page1BewonersAdresContainer container = new Page1BewonersAdresContainer();
      container.addAdres(aanvraag.getNieuwAdres(), "Nieuwe adres");
      container.addAdres(aanvraag.getHuidigAdres(), "Oude adres");

      addComponent(infoLayout);
      addComponent(table);

      select.addListener(this);
      select.setContainerDataSource(container);
      select.setItemCaptionPropertyId(PersoonslijstAdresContainer.WAARDE);
    }

    super.event(event);
  }

  public BaseWKExt getWk() {

    BewonersAdres adres = (BewonersAdres) select.getValue();

    if (adres != null) {

      VerhuisAanvraagAdres verhuisAdres = adres.getAdres();

      String straat = verhuisAdres.getStraat().getDescription();
      long hnr = verhuisAdres.getHnr();
      String hnrL = verhuisAdres.getHnrL();
      String hnrT = verhuisAdres.getHnrT();
      String hnrA = verhuisAdres.getHnrA();
      String pc = astr(verhuisAdres.getPc().getValue());

      ZoekArgumenten args = new ZoekArgumenten();

      if (fil(straat)) {
        args.setStraatnaam(straat);
      }

      if (pos(hnr)) {
        args.setHuisnummer(astr(hnr));
      }

      if (fil(hnrL)) {
        args.setHuisletter(hnrL);
      }

      if (fil(hnrT)) {
        args.setHuisnummertoevoeging(hnrT);
      }

      if (fil(hnrA)) {
        args.setHuisnummeraanduiding(hnrA);
      }

      if (fil(pc)) {
        args.setPostcode(pc);
      }

      WKResultWrapper result = getApplication().getServices().getPersonenWsService().getAdres(args, true);

      switch (result.getBasisWkWrappers().size()) {
        case 0:
          throw new ProException(NO_RESULTS, INFO, "Dit adres komt niet voor binnen de gemeente.");

        case 1:
          return result.getBasisWkWrappers().get(0);

        default:
          throw new ProException(NO_RESULTS, INFO, "Dit adres is niet uniek binnen de gemeente");
      }
    }

    throw new ProException(NO_RESULTS, INFO, "Geen adres geselecteerd");
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (isKeyCode(button, keyCode, KeyCode.F2, buttonBrieven)) {

      if (!table.isSelectedRecords()) {
        throw new ProException(INFO, "Er zijn geen bewoners geselecteerd");
      }

      getNavigation().goToPage(new Page2BewonersPage(table.getSelectedValues(BasePLExt.class)));
    } else if (isKeyCode(button, keyCode, KeyCode.F3, buttonObject)) {
      getParentWindow().addWindow(new WoningObjectWindow(new ProcuraInhabitantsAddress(getWk())));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onClose() {

    getWindow().closeWindow();

    super.onClose();
  }

  @Override
  public void valueChange(ValueChangeEvent event) {

    BewonersAdres adres = (BewonersAdres) event.getProperty().getValue();

    if (adres != null) {

      VerhuisAanvraagAdres verhuisAdres = adres.getAdres();

      String straat = verhuisAdres.getStraat().getDescription();
      long hnr = verhuisAdres.getHnr();
      String hnrL = verhuisAdres.getHnrL();
      String hnrT = verhuisAdres.getHnrT();
      String hnrA = verhuisAdres.getHnrA();
      String pc = astr(verhuisAdres.getPc().getValue());
      String gemDeel = verhuisAdres.getGemeenteDeel().getDescription();

      PLEArgs args = new PLEArgs();

      if (fil(straat)) {
        args.setStraat(straat);
      }

      if (pos(hnr)) {
        args.setHuisnummer(astr(hnr));
      }

      if (fil(hnrL)) {
        args.setHuisletter(hnrL);
      }

      if (fil(hnrT)) {
        args.setHuisnummertoevoeging(hnrT);
      }

      if (fil(hnrA)) {
        args.setAanduiding(hnrA);
      }

      if (fil(pc)) {
        args.setPostcode(pc);
      }

      if (fil(gemDeel)) {
        args.setGemeentedeel(gemDeel);
      }

      args.setDatasource(PLEDatasource.PROCURA);
      args.setShowHistory(false);
      args.setShowArchives(false);
      args.setShowSuspended(false);

      PersonenWsService gbaWs = getServices().getPersonenWsService();
      List<BasePLExt> bewoners = gbaWs.getPersoonslijsten(args, false).getBasisPLWrappers();
      String adresTekst = verhuisAdres.getAdres().getAdres_pc_wpl_gem();

      table.update(getApplication(), bewoners);

      removeComponent(infoLayout);
      infoLayout = new Page1BewonersInfoLayout();
      infoLayout.update(adresTekst, table.getAantal(), table.getGeheimAantal());
      addComponent(infoLayout, getComponentIndex(table));
    }
  }

  public class AdresSelect extends GbaNativeSelect {

    public AdresSelect() {

      setImmediate(true);
      setSizeFull();
      setNullSelectionAllowed(false);
    }
  }
}
