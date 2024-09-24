/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.components.layouts.toolbar;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TEST_OMGEVING;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.Globalfunctions.pos;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Window;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.Separator;
import nl.procura.gba.web.components.layouts.Manual;
import nl.procura.gba.web.modules.account.ontbrekende.email.OntbrekendeDialog;
import nl.procura.gba.web.modules.account.ontbrekende.email.OntbrekendeEmailDialog;
import nl.procura.gba.web.modules.locatiekeuze.locatie.LocatieDialog;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.ServiceListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.applicatie.onderhoud.Application;
import nl.procura.gba.web.services.applicatie.onderhoud.OnderhoudService;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.gba.web.windows.account.AccountWindow;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.standard.Resource;
import nl.procura.vaadin.functies.downloading.StreamDownloader;
import nl.procura.vaadin.theme.LoginValidatable;
import nl.procura.vaadin.theme.twee.layout.ToolBarLayout;
import nl.vrijbrp.hub.client.HubContext;

public class ToolBar extends ToolBarLayout {

  private final Label      locatieLabel     = new Label();
  private final MenuBar    gebruikerMenubar = new MenuBar();
  private final MenuBar    locatieMenubar   = new MenuBar();
  private MenuBar.MenuItem gebruikerMenu    = null;
  private MenuBar.MenuItem locatieMenu      = null;
  private MeldingenButtons meldingenButtons;

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
      right.addComponent(getMeldingenButtons());

      right.addComponent(new Separator());
      right.addComponent(getLocatieLabel());
      right.addComponent(getLocatieMenu());
      right.addComponent(new Separator());
      right.addComponent(getGebruikerMenu());

      if (isTestOmgeving()) {
        Label testLabel = getTestLabel();
        right.addComponent(testLabel);
        right.addComponent(new Separator());
        right.setComponentAlignment(testLabel, Alignment.MIDDLE_CENTER);
      }

      right.addComponent(getLogoutButton());
      setExpandRatio(left, 1);
    }

    super.attach();

    final String windowName = getWindow().getName();

    ServiceListener meldingListener = new ServiceListener() {

      @Override
      public void action(ServiceEvent event) {
        if (event == ServiceEvent.CHANGE) {
          meldingenButtons.update();
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
    getApplication().getServices().getTaskService().setListener(meldingListener);

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
    gebruikerMenu.setIcon(new ThemeResource("../flat/icons/16/bullet_arrow_down.png"));
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
            item -> {
              InputStream manualStream = Resource.getAsInputStream(manual.fileName());
              StreamDownloader.download(manualStream, getApplication().getParentWindow(),
                  manual.fileName(), true);
            });
      }
    }

    gebruikerMenubar.addStyleName("right-separator");
    return gebruikerMenubar;
  }

  private MenuBar getLocatieMenu() {
    List<Application> apps = getApplication().getServices().getOnderhoudService().getActiveApps(false);
    locatieMenu = locatieMenubar.addItem("", null);

    if (!apps.isEmpty()) {
      locatieMenu.setIcon(new ThemeResource("../flat/icons/16/bullet_arrow_down.png"));
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
    Label logoLabel = new Label("vrijBRP | Balie");
    logoLabel.setStyleName("toolbar-logo");
    return logoLabel;
  }

  private Button getLogoutButton() {
    Button button = new NativeButton("", e -> logout());
    button.setIcon(new ThemeResource(GbaWebTheme.ICOON_18.UITLOGGEN));
    button.setWidth("100px");
    return button;
  }

  private Layout getMeldingenButtons() {
    meldingenButtons = new MeldingenButtons(getApplication(), getWindow());
    return meldingenButtons;
  }

  private Label getTestLabel() {
    Label label = new Label("Testomgeving");
    label.setStyleName("test-warning");
    label.setDescription("Dit is de testomgeving van deze applicatie");
    label.setHeight("20px");
    return label;
  }

  private void logout() {
    HubContext.instance().logout().requestReturnToHub();
    GbaApplication application = getApplication();
    LoginValidatable loginWindow = (LoginValidatable) application.getLoginWindow();
    loginWindow.getLoginValidator().deleteCredentials();
    getApplication().close();
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
