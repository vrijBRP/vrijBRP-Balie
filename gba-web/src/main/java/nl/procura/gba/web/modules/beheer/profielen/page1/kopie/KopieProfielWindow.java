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

package nl.procura.gba.web.modules.beheer.profielen.page1.kopie;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.ProfielService;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class KopieProfielWindow extends ModalWindow {

  public KopieProfielWindow() {
    setCaption("Kopieer gebruikers naar ander profiel");
    setWidth("500px");
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page1()));
  }

  public class Page1 extends NormalPageTemplate {

    private final KopieProfielForm form = new KopieProfielForm();

    public Page1() {
      setSpacing(true);
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {
        buttonSave.setCaption("Uitvoeren (F9)");

        addButton(buttonSave, 1f);
        addButton(buttonClose);

        addComponent(new InfoLayout("", "Koppel de gebruikers van <b>profiel A</b> ook aan <b>profiel B</b>."));
        addComponent(form);
      }

      super.event(event);
    }

    @Override
    public void onClose() {

      closeWindow();

      super.onClose();
    }

    @Override
    public void onSave() {

      form.commit();

      long bronProfielCode = along(form.getBean().getProfielVan().getValue());
      long doelProfielCode = along(form.getBean().getProfielNaar().getValue());

      if (bronProfielCode == doelProfielCode) {
        throw new ProException(WARNING, "Selecteer verschillende profielen");
      }

      final Profiel bronProfiel = getProfiel(bronProfielCode);
      final Profiel doelProfiel = getProfiel(doelProfielCode);

      String msg = "Wilt u de " + getAantal(bronProfiel) + " gebruiker(s) van profiel <b>" + bronProfiel +
          "</b> ook aan profiel <b>" + doelProfiel + "</b> koppelen.";

      getWindow().getParent().addWindow(new ConfirmDialog(msg) {

        @Override
        public void buttonYes() {

          for (Gebruiker gebruiker : getServiceGebruikers().getGebruikers(false)) {

            if (bronProfiel.isGekoppeld(gebruiker)) {

              getServiceProfielen().koppelActie(asList(gebruiker),
                  asList(doelProfiel),
                  KoppelActie.KOPPEL);
            }
          }

          successMessage("Gebruiker(s) gekopieerd");

          KopieProfielWindow.this.closeWindow();

          super.buttonYes();
        }
      });

      super.onSave();
    }

    private int getAantal(Profiel profiel) {

      int count = 0;

      for (Gebruiker gebruiker : getApplication().getServices().getGebruikerService().getGebruikers(false)) {

        if (profiel.isGekoppeld(gebruiker)) {

          count++;
        }
      }

      return count;
    }

    private Profiel getProfiel(long cProfiel) {

      for (Profiel profiel : getServiceProfielen().getProfielen()) {

        if (profiel.getCProfile().equals(cProfiel)) {

          getServiceProfielen().herlaadProfiel(profiel);

          return profiel;
        }
      }

      throw new ProException(ERROR, "Profiel met code " + cProfiel + " niet gevonden.");
    }

    private GebruikerService getServiceGebruikers() {
      return getServices().getGebruikerService();
    }

    private ProfielService getServiceProfielen() {
      return getServices().getProfielService();
    }
  }
}
