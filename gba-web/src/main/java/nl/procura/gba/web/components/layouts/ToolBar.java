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

package nl.procura.gba.web.components.layouts;

import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.*;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TEST_OMGEVING;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.account.meldingen.MeldingenWindow;
import nl.procura.gba.web.modules.account.ontbrekende.email.OntbrekendeDialog;
import nl.procura.gba.web.modules.account.ontbrekende.email.OntbrekendeEmailDialog;
import nl.procura.gba.web.modules.beheer.onderhoud.licenses.LicensesListWindow;
import nl.procura.gba.web.modules.locatiekeuze.locatie.LocatieDialog;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.ServiceListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.applicatie.meldingen.MeldingService;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory;
import nl.procura.gba.web.services.applicatie.onderhoud.Application;
import nl.procura.gba.web.services.applicatie.onderhoud.OnderhoudService;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.gba.web.windows.account.AccountWindow;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.theme.LoginValidatable;
import nl.procura.vaadin.theme.twee.layout.ToolBarLayout;

public class ToolBar extends ToolBarLayout {

  private final Label      locatieLabel     = new Label();
  private final MenuBar    gebruikerMenubar = new MenuBar();
  private final MenuBar    locatieMenubar   = new MenuBar();
  private MenuBar.MenuItem gebruikerMenu    = null;
  private MenuBar.MenuItem locatieMenu      = null;

  private Button meldingenButton0 = null;
  private Button meldingenButton1 = null;
  private Button meldingenButton2 = null;
  private Button meldingenButton3 = null;

  public ToolBar() {
  }

  @Override
  public void attach() {

    if (getComponentCount() == 0) {

      HorizontalLayout left = new HorizontalLayout();
      left.setSizeUndefined();
      left.addStyleName("left");
      addComponent(left);

      HorizontalLayout right = new HorizontalLayout();
      right.addStyleName("right");
      addComponent(right);

      left.addComponent(getLogo());
      right.addComponent(getMeldingenButton1());

      right.addComponent(getSeparator());
      right.addComponent(getLocatieLabel());
      right.addComponent(getLocatieMenu());
      right.addComponent(getSeparator());
      right.addComponent(getGebruikerMenu());
      right.addComponent(getLogoutButton());

      if (isTestOmgeving()) {
        Label testLabel = getTestLabel();
        right.addComponent(getSeparator());
        right.addComponent(testLabel);
        right.setComponentAlignment(testLabel, Alignment.MIDDLE_CENTER);
      }

      setExpandRatio(left, 1);
    }

    super.attach();

    final String windowName = getWindow().getName();

    ServiceListener meldingListener = new ServiceListener() {

      @Override
      public void action(ServiceEvent event) {
        if (event == ServiceEvent.CHANGE) {
          updateMeldingen();
        }
      }

      @Override
      public String getId() {
        return "meldingListener" + windowName;
      }
    };

    ServiceListener serviceListener = new ServiceListener() {

      @Override
      public void action(ServiceEvent event) {

        if (event == ServiceEvent.CHANGE) {
          updateLocatie();
        }
      }

      @Override
      public String getId() {
        return "locatieListener" + windowName;
      }
    };

    getApplication().getServices().getMeldingService().setListener(meldingListener);
    getApplication().getServices().getLocatieService().setListener(serviceListener);

    updateGemeente();
    updateLocatie();
    updateGebruiker();
    updateOntbrekendeGegevens();
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  protected boolean isTestOmgeving() {

    ParameterService parameters = getApplication().getServices().getParameterService();
    return isTru(parameters.getGebruikerParameters(Gebruiker.getDefault()).get(TEST_OMGEVING).getValue());
  }

  private MenuBar getGebruikerMenu() {

    gebruikerMenu = gebruikerMenubar.addItem("", null);
    gebruikerMenu.setIcon(new ThemeResource("../procura2/icons/16/bullet_arrow_down.png"));
    gebruikerMenu.addItem("Eigen instellingen",
        selectedItem -> getApplication().openWindow(getWindow(), new AccountWindow(),
            "wachtwoord"));
    gebruikerMenu.addItem("Locatie wijzigen", selectedItem -> getWindow().addWindow(new LocatieDialog()));

    List<Manual> handleidingen = new ArrayList<>();
    for (Manual manual : Manual.values()) {
      if (isTru(getApplication().getParmValue(manual.parameter()))) {
        handleidingen.add(manual);
      }
    }

    MenuItem handleidingItem = null;

    if (handleidingen.size() > 1) {
      handleidingItem = gebruikerMenu.addItem("Handleiding", null);
    } else if (handleidingen.size() > 0) {
      handleidingItem = gebruikerMenu;
    }

    gebruikerMenu.addItem("De kennisbank", selectedItem -> {
      String resourceURL = Services.getInstance().getKennisbankService().getURL("");
      getApplication().getParentWindow().open(new ExternalResource(resourceURL), "De kennisbank");
    });

    if (handleidingItem != null) {
      for (Manual manual : handleidingen) {
        handleidingItem.addItem((handleidingen.size() == 1 ? "Handleiding" : manual.title()),
            item -> getWindow().open(new ExternalResource(
                getApplication().getExternalURL("rest/v1.0/bestand/zoeken/" + manual.fileName()))));
      }
    }

    gebruikerMenu.addItem("Licenties", selectedItem -> {
      getApplication().getParentWindow().addWindow(new LicensesListWindow());
    });

    gebruikerMenubar.addStyleName("right-separator");

    return gebruikerMenubar;
  }

  private MenuBar getLocatieMenu() {

    List<Application> apps = getApplication().getServices().getOnderhoudService().getActiveApps(false);

    locatieMenu = locatieMenubar.addItem("", null);

    if (!apps.isEmpty()) {
      locatieMenu.setIcon(new ThemeResource("../procura2/icons/16/bullet_arrow_down.png"));
    }

    for (Application app : apps) {
      locatieMenu.addItem(app.getEntity().getName(), item -> {
        String ticket = getApplication().getServices().getOnderhoudService().getAppTicket(app);
        getWindow().open(new ExternalResource(
            MessageFormat.format("{0}?{1}={2}", app.getEntity().getUrl(), OnderhoudService.TICKET,
                ticket)));

      });

    }

    return locatieMenubar;
  }

  private Component getLocatieLabel() {

    locatieLabel.addStyleName("right-separator");

    return locatieLabel;
  }

  private Label getLogo() {

    Label logo_label = new Label("Burgerzaken");
    logo_label.setStyleName("toolbar-logo");

    return logo_label;
  }

  private Button getLogoutButton() {
    Button b = new NativeButton("", e -> openLogoutWindow());
    b.setIcon(new ThemeResource(GbaWebTheme.ICOON_18.UITLOGGEN));
    return b;
  }

  private HorizontalLayout getMeldingenButton1() {

    HorizontalLayout meldingenLayout = new HorizontalLayout();
    meldingenLayout.setStyleName("melding-layout");

    meldingenButton0 = new NativeButton("Geen meldingen", (Button.ClickListener) event -> {
      getApplication().getMainWindow().addWindow(new MeldingenWindow(FAULT));
    });

    meldingenButton1 = new NativeButton("", (Button.ClickListener) event -> {
      getApplication().getMainWindow().addWindow(new MeldingenWindow(FAULT));
    });

    meldingenButton2 = new NativeButton("", (Button.ClickListener) event -> {
      getApplication().getMainWindow().addWindow(new MeldingenWindow(SYSTEM));
    });

    meldingenButton3 = new NativeButton("", (Button.ClickListener) event -> {
      getApplication().getMainWindow().addWindow(new MeldingenWindow(WORK));
    });

    if (getWindow().getName().equals(HomeWindow.NAME)) {
      // Check alle services
      GbaApplication gbaApplication = ToolBar.this.getApplication();
      for (AbstractService db : gbaApplication.getServices().getServices()) {
        db.check();
      }
    }

    meldingenButton0.setHtmlContentAllowed(true);
    meldingenButton0.setCaption("<span class='melding-ok'>Geen meldingen</span>");
    meldingenButton0.setStyleName("melding-button");
    meldingenButton1.setStyleName("melding-button");
    meldingenButton2.setStyleName("melding-button");
    meldingenButton3.setStyleName("melding-button");

    meldingenLayout.addComponent(meldingenButton0);
    meldingenLayout.addComponent(meldingenButton1);
    meldingenLayout.addComponent(meldingenButton2);
    meldingenLayout.addComponent(meldingenButton3);

    updateMeldingen();

    if (HomeWindow.NAME.equalsIgnoreCase(getWindow().getName())) {
      MeldingService meldingService = getApplication().getServices().getMeldingService();
      if (!meldingService.isMessagePopupShown()) {
        if (meldingService.isShowMessagesPopup()) {
          meldingService.disableMessagePopupShown();
          Stream.of(meldingenButton1, meldingenButton2, meldingenButton3)
              .filter(AbstractComponent::isVisible)
              .findFirst()
              .ifPresent(Button::click);
        }
      }
    }

    return meldingenLayout;
  }

  private Component getSeparator() {
    Label separator = new Label();
    separator.addStyleName("separator");
    return separator;
  }

  private Label getTestLabel() {

    Label label = new Label("Testomgeving");
    label.setStyleName("test-warning");
    label.setDescription("Dit is de testomgeving van deze applicatie");
    label.setHeight("20px");

    return label;
  }

  private void openLogoutWindow() {

    ConfirmDialog confirmDialog = new ConfirmDialog("Weet u zeker dat u wilt uitloggen?") {

      @Override
      public void buttonYes() {

        ((LoginValidatable) ((GbaApplication) getApplication()).getLoginWindow()).getLoginValidator()
            .deleteCredentials();
        getApplication().close();
      }
    };
    getWindow().addWindow(confirmDialog);
  }

  /**
   * Update gebruiker menu
   */
  private void updateGebruiker() {
    gebruikerMenu.setText(getApplication().getServices().getGebruiker().getNaam());
  }

  /**
   * Update gemeente omschrijving
   */
  private void updateGemeente() {

    if (getApplication() != null) {
      String gemeenteNaam = astr(GbaConfig.get(GbaConfigProperty.GEMEENTE));
      locatieMenu.setText(gemeenteNaam);
    }
  }

  /**
   * Update locatie label
   */
  private void updateLocatie() {

    if (getApplication() != null) {
      Locatie locatie = getApplication().getServices().getGebruiker().getLocatie();
      locatieLabel.setValue(pos(locatie.getCLocation()) ? locatie.getOmschrijving() : "Geen locatie");
    }
  }

  /**
   * Update de meldingen label
   */
  private void updateMeldingen() {
    if (getApplication() != null) {
      boolean b1 = updateButton("Fouten", meldingenButton1, FAULT);
      boolean b2 = updateButton("Systeem", meldingenButton2, SYSTEM);
      boolean b3 = updateButton("Herinnering", meldingenButton3, WORK);
      meldingenButton0.setVisible(!(b1 || b2 || b3));
    }
  }

  private boolean updateButton(String type, Button button, ServiceMeldingCategory category) {

    MeldingService service = getApplication().getServices().getMeldingService();
    List<ServiceMelding> meldingen = service.getMeldingen(category);

    long errors = meldingen.stream().filter(m -> ERROR.equals(m.getSeverity())).count();
    long warnings = meldingen.stream().filter(m -> WARNING.equals(m.getSeverity())).count();

    String bCaption = "";
    if (errors > 0) {
      bCaption = "<span class='melding-error'>" + type + "</span>";
    } else if (warnings > 0) {
      bCaption = "<span class='melding-warning'>" + type + "</span>";
    }

    button.setHtmlContentAllowed(true);
    button.setCaption(bCaption);
    button.setVisible(errors + warnings > 0);

    return errors + warnings > 0;
  }

  /**
   * Toon ontbrekende gegevens scherm
   */
  private void updateOntbrekendeGegevens() {

    if (getWindow() instanceof HomeWindow) {

      List<OntbrekendeDialog> dialogs = new ArrayList<>();
      dialogs.add(new OntbrekendeEmailDialog(getApplication().getServices()));

      for (OntbrekendeDialog dialog : dialogs) {
        if (dialog.isNodig()) {
          getWindow().addWindow((Window) dialog);
        }
      }
    }
  }
}
