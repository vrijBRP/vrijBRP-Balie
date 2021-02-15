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

package nl.procura.gba.web.modules.bs.geboorte.page81;

import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.geboorte.page80.Page80Geboorte;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.dialog.OkDialog;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geboorte
 */

public class Page81Geboorte<T extends DossierGeboorte> extends BsPage<T> {

  private DossierPersoon kind;

  private Page81GeboorteForm1 form = null;

  public Page81Geboorte(DossierPersoon kind) {

    super("Geboorte - kind");

    setKind(kind);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInfo(
          "Bepaal de geslachtsnaam, vul de voornamen en het geslacht van het kind in en druk op Volgende (F2) om verder te gaan. "
              + "Door op Naamskeuze (F4) kunnen gegevens betreffende een eventuele naamskeuze bij de geboorteaangifte worden vastgelegd.");

      form = new Page81GeboorteForm1(getZaakDossier(), getKind());

      addComponent(form);
    }

    super.event(event);
  }

  public DossierPersoon getKind() {
    return kind;
  }

  public void setKind(DossierPersoon kind) {
    this.kind = kind;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if ((button == buttonNext) || (keyCode == ShortcutAction.KeyCode.F2)) {

      if (stored()) {

        getNavigation().goBackToPage(Page80Geboorte.class);
      }
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {

    getWindow().addWindow(new ConfirmDialog("", "U wilt terug zonder de gegevens op te slaan?", "350px") {

      @Override
      public void buttonYes() {

        close();

        getNavigation().goBackToPage(Page80Geboorte.class);
      }
    });

    super.onPreviousPage();
  }

  private boolean stored() {

    form.commit();

    Page81GeboorteBean1 b = form.getBean();

    final DossierPersoon a = getKind();

    a.setDatumMoment(new DateTime());
    a.setAnummer(new AnrFieldValue(b.getAnr()));
    a.setBurgerServiceNummer(new BsnFieldValue(b.getBsn()));
    a.setTitel(b.getTitel());
    a.setDatumGeboorte(b.getGeboortedatum());
    a.setTijdGeboorte(new DateTime(0, along(b.getGeboortetijd().getValue()), TimeType.TIME_4_DIGITS));
    a.setVoornaam(b.getVoorn());
    a.setNaamgebruik(b.getNg());
    a.setGeslacht(b.getGeslacht());
    a.setVerstrekkingsbeperking(b.isVerstrek());
    a.setToelichting(b.getToelichting());

    if (emp(trim(b.getNaam())) && emp(b.getVoorn())) {
      getWindow().addWindow(new OkDialog("Het kind heeft geen geslachtsnaam. " +
          "De voornaam is daarom verplicht."));
      return false;

    } else {
      if (emp(b.getVoorn()) && !a.isVoornaamBevestigd()) {
        getWindow().addWindow(new ConfirmDialog("U heeft geen voornaam ingevuld. Is dat correct?") {

          @Override
          public void buttonYes() {
            a.setVoornaamBevestigd(true);
            getNavigation().goBackToPage(Page80Geboorte.class);
            super.buttonYes();
          }
        });

        return false;
      }
    }

    return true;
  }
}
