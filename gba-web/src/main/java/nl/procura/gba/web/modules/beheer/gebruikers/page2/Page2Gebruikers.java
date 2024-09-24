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

package nl.procura.gba.web.modules.beheer.gebruikers.page2;

import static java.util.Arrays.asList;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;
import static nl.procura.gba.common.MiscUtils.cleanPath;
import static nl.procura.gba.web.modules.beheer.gebruikers.page2.Page2GebruikersBean.MAP;

import java.util.Collections;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.email.Verzending;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.page.buttons.ActieButton;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelDocumentenAanGebruikersPage;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelLocatiesAanGebruikers;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelProfielenAanGebruikersPage;
import nl.procura.gba.web.modules.beheer.gebruikers.email.SendEmailWindow;
import nl.procura.gba.web.modules.beheer.gebruikers.page4.Page4Gebruikers;
import nl.procura.gba.web.modules.beheer.gebruikers.page5.Page5Gebruikers;
import nl.procura.gba.web.modules.beheer.overig.CheckPadEnOpslaanGebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoType;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.dialog.OkDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Gebruikers extends NormalPageTemplate {

  private final Button buttonProfielen  = new Button("Profielen koppelen");
  private final Button buttonLocaties   = new Button("Locaties koppelen");
  private final Button buttonParameters = new Button("Parameters instellen");
  private final Button buttonGebrgg     = new Button("Extra gebruikersgegevens");
  private final Button buttonDoc        = new Button("Documenten koppelen");
  private final Button buttonResww      = new ActieButton(ProfielActieType.UPDATE, "Reset wachtwoord");
  private final Button buttonDeblok     = new ActieButton(ProfielActieType.UPDATE, "Deblokkeren");
  private final Button buttonEmail      = new ActieButton(ProfielActieType.UPDATE, "E-mails");

  private Page2GebruikersForm form = null;
  private Gebruiker           gebruiker;

  public Page2Gebruikers(Gebruiker gebruiker) {

    super("Toevoegen / muteren gebruikers");

    this.gebruiker = gebruiker;

    addButton(buttonPrev, buttonNew, buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    GebruikerService dG = getApplication().getServices().getGebruikerService();

    if (event.isEvent(InitPage.class)) {

      form = new Page2GebruikersForm(gebruiker, dG) {

        @Override
        public String getAfdeling() {
          return getGebruiker().getAfdeling();
        }

        @Override
        public String getEmail() {
          return getGebruiker().getEmail();
        }

        @Override
        public String getTelefoonnummer() {
          return getGebruiker().getTelefoonnummer();
        }
      };

      OptieLayout ol = new OptieLayout();
      ol.getLeft().addComponent(form);

      ol.getRight().setWidth("200px");
      ol.getRight().setCaption("Opties");

      ol.getRight().addButton(buttonProfielen, this);
      ol.getRight().addButton(buttonLocaties, this);
      ol.getRight().addButton(buttonParameters, this);
      ol.getRight().addButton(buttonGebrgg, this);
      ol.getRight().addButton(buttonDoc, this);
      ol.getRight().addButton(buttonResww, this);
      ol.getRight().addButton(buttonDeblok, this);
      ol.getRight().addButton(buttonEmail, this);

      checkButtons();
      addComponent(ol);
    } else if (event.isEvent(AfterReturn.class)) {

      // haal gebruiker uit database: bij het profielen koppelen wordt nl. gebruiker opnieuw uit database gehaald
      gebruiker = getServices().getGebruikerService().getGebruikerByCode(gebruiker.getCUsr(), true);
    }

    super.event(event);
  }

  public Gebruiker getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(Gebruiker gebruiker) {
    this.gebruiker = gebruiker;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonProfielen) {
      getNavigation().goToPage(new KoppelProfielenAanGebruikersPage(getGebruiker()));
    } else if (button == buttonLocaties) {
      getNavigation().goToPage(new KoppelLocatiesAanGebruikers(getGebruiker()));
    } else if (button == buttonParameters) {
      getNavigation().goToPage(new Page4Gebruikers(getGebruiker()));
    } else if (button == buttonGebrgg) {
      getNavigation().goToPage(new Page5Gebruikers(getGebruiker()));
    } else if (button == buttonDoc) {
      getNavigation().goToPage(new KoppelDocumentenAanGebruikersPage(getGebruiker()));
    } else if (button == buttonResww) {
      resetWachtwoord();
    } else if (button == buttonDeblok) {
      deblokkeren();
    } else if (button == buttonEmail) {
      emails();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNew() {

    setGebruiker(new Gebruiker());
    form.reset();
    checkButtons();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    Page2GebruikersBean bean = form.getBean();
    String cleanedPath = cleanPath(bean.getMap());

    if (!getGebruiker().isStored()) {
      checkGebruikersnaam(bean.getGebruikersnaam());
    }

    checkVolledigeGebruikersnaam(getGebruiker(), bean.getVolledigeNaam());
    checkEmail(getGebruiker(), bean.getEmail());

    new CheckPadEnOpslaanGebruiker(cleanedPath, bean, form) {

      @Override
      protected void nietOpslaanGebruikerActies() {
        form.setGebruikermapContainer(); // zorgt ervoor dat de ingevoerde mapnaam niet in het lijstje wordt opgenomen
        form.getField(MAP).setValue(getGebruiker().getPad());
      }

      @Override
      protected void opslaanGebruiker(String legalPath, Page2GebruikersBean bean) {
        saveGebruiker(legalPath, bean);
        form.setGebruikermapContainer(); // maakt veldje leeg bij eventueel lege map na clean()
      }

      @Override
      protected void welOpslaanGebruikerActies(String legalPath, Page2GebruikersBean bean) {
        saveGebruiker(legalPath, bean);
        form.setGebruikermapContainer(); // nieuwe gebruiker direct in alfabetische volgorde toegevoegd.
      }
    };
  }

  private void checkButtons() {

    for (Button b : new Button[]{ buttonProfielen, buttonLocaties, buttonParameters, buttonGebrgg, buttonDoc,
        buttonResww }) {
      b.setEnabled(getGebruiker().isStored());
    }

    buttonDeblok.setEnabled(getGebruiker().isGeblokkeerd());
    buttonEmail.setEnabled(getGebruiker().isStored());
  }

  /**
   * E-mail adres moet uniek zijn
   */
  private void checkEmail(Gebruiker gebruiker, String email) {
    getServices().getGebruikerService().checkEmail(gebruiker, email);
  }

  /**
   * Gebruikersnaam moet uniek zijn
   */
  private void checkGebruikersnaam(String gebruikersnaam) {
    for (Gebruiker gebruiker : getAlleGebruikers()) {
      if (gebruiker.getGebruikersnaam().equals(gebruikersnaam)) {
        throw new ProException(ENTRY, WARNING,
            "De ingevoerde gebruikersnaam komt reeds voor.<br/> Voer een unieke gebruikersnaam in a.u.b.");
      }
    }
  }

  /**
   * Volledige naam moet uniek zijn
   */
  private void checkVolledigeGebruikersnaam(Gebruiker gebruiker, String volledigeNaam) {

    for (Gebruiker g : getServices().getGebruikerService().getGebruikers(false)) {
      boolean isVolledigeNaam = g.getNaam().equals(volledigeNaam);
      if (!g.equals(gebruiker) && isVolledigeNaam) {

        String msg = "De ingevoerde volledige naam komt reeds voor bij gebruiker "
            + g.getGebruikersnaam()
            + ".<br/>Voer een unieke volledige naam in a.u.b.";

        throw new ProException(ENTRY, WARNING, msg);
      }
    }
  }

  private void deblokkeren() {

    getWindow().addWindow(
        new ConfirmDialog("Weet u het zeker?", "U wilt het account van deze gebruiker deblokkeren?", "400px") {

          @Override
          public void buttonYes() {

            getServices().getGebruikerService().deblokkeer(getGebruiker());

            form.initFields(getGebruiker());

            checkButtons();

            successMessage("Account is gedeblokkeerd");

            close();

            super.buttonYes();
          }
        });
  }

  private void emails() {

    form.commit();

    getWindow().addWindow(new SendEmailWindow(
        Collections.singletonList(new Verzending(getGebruiker(), form.getBean().getEmail()))));
  }

  private void extraInfoOpslaan() {
    gebruikerInfoOpslaan(GebruikerInfoType.email, form.getBean().getEmail());
    gebruikerInfoOpslaan(GebruikerInfoType.telefoon, form.getBean().getTelefoonnummer());
    gebruikerInfoOpslaan(GebruikerInfoType.afdelingsnaam, form.getBean().getAfdeling());
  }

  private void gebruikerInfoOpslaan(GebruikerInfoType type, String waarde) {
    getServices().getGebruikerInfoService().save(type, getGebruiker(), waarde);
  }

  private List<Gebruiker> getAlleGebruikers() {
    return getServices().getGebruikerService().getGebruikers(GeldigheidStatus.ALLES, false);
  }

  private void resetWachtwoord() {

    ConfirmDialog confirmDialog = new ConfirmDialog("Weet u het zeker?",
        "U wilt het wachtwoord van deze gebruiker resetten?", "400px") {

      @Override
      public void buttonYes() {

        // true geeft aan dat het om een reset gaat
        GebruikerService gebruikerService = getServices().getGebruikerService();
        String nieuwWachtwoord = gebruikerService.generateWachtwoord();
        gebruikerService.setWachtwoord(getGebruiker(), nieuwWachtwoord, true);

        // nodig, want close() is noodzakelijk om focus op WachtwoordWindow te krijgen!
        Window parentWindow = getWindow().getParent();

        close();
        parentWindow.addWindow(new WachtwoordWindow(getGebruiker(), nieuwWachtwoord));

        form.initFields(getGebruiker());

        checkButtons();

        super.buttonYes();
      }
    };

    getWindow().addWindow(confirmDialog);
  }

  private void saveGebruiker(String path, Page2GebruikersBean b) {

    getGebruiker().setGebruikersnaam(b.getGebruikersnaam());
    getGebruiker().setNaam(b.getVolledigeNaam());
    getGebruiker().setAdministrator(b.isApplicatieBeheerder());
    getGebruiker().setDatumIngang(new DateTime(b.getIngangGeldGebr().getBigDecimalValue()));
    getGebruiker().setDatumEinde(new DateTime(b.getEindeGeldGebr().getBigDecimalValue()));
    getGebruiker().setOmschrijving(b.getExtraInfo());
    getGebruiker().setPad(path);

    GebruikerService gebruikerService = getServices().getGebruikerService();

    if (getGebruiker().isStored()) {
      gebruikerService.save(getGebruiker());
      extraInfoOpslaan();
      successMessage("Gebruiker is opgeslagen.");
    } else {
      // Default ww wordt als een reset gezien, zodat dit ww meteen gewijzigd moet worden!
      gebruikerService.save(getGebruiker());
      extraInfoOpslaan();

      String nieuwWachtwoord = gebruikerService.generateWachtwoord();
      boolean resetPw = gebruikerService.setWachtwoord(getGebruiker(), nieuwWachtwoord, true);

      StringBuilder msg = new StringBuilder();
      msg.append("De nieuwe gebruiker is opgeslagen.<br> Gebruiker '");
      msg.append(getGebruiker().getGebruikersnaam());
      msg.append("' kan inloggen met het wachtwoord: <b>");
      msg.append(nieuwWachtwoord);
      if (resetPw) {
        msg.append("</b><br>De gebruiker zal dit wachtwoord moeten wijzigen bij het inloggen.");
      }
      String message = msg.toString();

      getWindow().addWindow(new OkDialog(message, 600));
    }

    checkButtons();
  }
}
