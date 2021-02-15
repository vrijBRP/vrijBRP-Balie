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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages;

import static java.lang.String.format;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;

import java.util.Optional;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.Tab1RaasMenuPage;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.windows.RaasCommWindow;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.windows.RaasUitreikWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.theme.twee.ProcuraTheme;
import nl.procura.validation.Bsn;

public abstract class Tab1RaasPage2Subpage extends NormalPageTemplate {

  final Button buttonUitreiken = new Button("Uitreikbericht sturen");
  final Button buttonPersoon   = new Button("Zoek persoon");
  final Button buttonZaak      = new Button("Toon zaak");
  final Button buttonRaasComm  = new Button("Toon berichten");

  private Tab1RaasPage2Form    form;
  private final PageLayout     parent;
  private final DocAanvraagDto aanvraag;

  Tab1RaasPage2Subpage(PageLayout parent, DocAanvraagDto aanvraag) {
    this.parent = parent;
    this.aanvraag = aanvraag;
    setMargin(false);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (buttonUitreiken.equals(button)) {
      getParentWindow().addWindow(new ConfirmDialog("Weet u het zeker?",
          "Dit is NIET de correcte manier om een reisdocument uit te reiken. " +
              "Reik het document uit via de uitreikprocedure van de reisdocumentaanvraag. " +
              "Wilt u toch doorgaan en een bericht sturen naar het RAAS?",
          "500px", ProcuraTheme.ICOON_24.WARNING) {

        @Override
        public void buttonYes() {
          showAfsluiting();
          super.buttonYes();
        }
      });
    }

    if (buttonPersoon.equals(button)) {
      goToPersoon();
    }

    if (buttonZaak.equals(button)) {
      goToZaak();
    }

    if (buttonRaasComm.equals(button)) {
      goToRaasComm();
    }

    super.handleEvent(button, keyCode);
  }

  private void goToRaasComm() {
    if (getAanvraag().getAanvraagNr().isNotBlank()) {
      getParentWindow().addWindow(new RaasCommWindow(getAanvraag()));
    }
  }

  private void goToPersoon() {
    Bsn bsn = new Bsn(getAanvraag().getAanvrager().getBsn().toString());
    if (bsn.isCorrect()) {
      getApplication().goToPl(getParentWindow(), "", PLEDatasource.STANDAARD, bsn.getDefaultBsn());
    }
  }

  private void goToZaak() {
    if (getAanvraag().getAanvraagNr().isNotBlank()) {
      Tab1RaasMenuPage parent = VaadinUtils.getParent(this, Tab1RaasMenuPage.class);
      Optional<Zaak> zaak = getServices().getZakenService()
          .getMinimaleZaken(new ZaakArgumenten(getAanvraag().getAanvraagNr().getValue().toString()))
          .stream()
          .findFirst();

      if (zaak.isPresent()) {
        ZaakregisterNavigator.navigatoTo(zaak.get(), parent, false);
      } else {
        throw new ProException(INFO, format("Geen zaak gevonden met aanvraagnummer: %s",
            getAanvraag().getAanvraagNr()));
      }
    }
  }

  private void showAfsluiting() {
    getApplication().getParentWindow().addWindow(new RaasUitreikWindow(aanvraag) {

      @Override
      public void closeWindow() {
        reload(aanvraag);
      }
    });
  }

  public void reload(final DocAanvraagDto aanvraag) {
    HeaderForm headerForm = VaadinUtils.getChild(parent, HeaderForm.class);
    headerForm.initFields(aanvraag);
    form.initFields(aanvraag);
  }

  public Tab1RaasPage2Form getForm() {
    return form;
  }

  public void setForm(Tab1RaasPage2Form form) {
    this.form = form;
  }

  public DocAanvraagDto getAanvraag() {
    return aanvraag;
  }
}
