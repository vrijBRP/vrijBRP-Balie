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

package nl.procura.gba.web.modules.zaken.common;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_MAX_STATUS_ZAAK_WIJZIGEN;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.Optional;

import com.vaadin.ui.Button;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutService;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakBehandelaar;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.theme.ProcuraWindow;

public class ZaakAanpassenButton extends Button {

  public ZaakAanpassenButton() {
    super("Aanpassen zaak");
  }

  public void onClick(Zaak zaak, Runnable postCheckAction) {
    GbaApplication app = (GbaApplication) getApplication();
    ZaakStatusType maxStatus = ZaakStatusType.get(along(app.getParmValue(ZAKEN_MAX_STATUS_ZAAK_WIJZIGEN)));
    if (!zaak.getStatus().isMaximaal(maxStatus)) {
      throw new ProException(WARNING,
          "Alleen zaken met maximale status <b>{0}</b> kunnen worden gewijzigd",
          maxStatus.getOms());
    }
    ZaakAttribuutService service = app.getServices().getZaakAttribuutService();
    if (service.isBehandelbareZaak(zaak)) {
      Optional<ZaakBehandelaar> behandelaar = service.getZaakBehandelaarHistorie(zaak).getBehandelaar();
      if (behandelaar.isPresent()) {
        UsrFieldValue naam = behandelaar.get().getBehandelaar();
        if (!app.getServices().getGebruiker().getCUsr().equals(naam.getLongValue())) {
          app.getParentWindow().addWindow(new ConfirmDialog(
              "Behandelaar van de zaak",
              "Deze zaak heeft <b>" + naam + "</b> als behandelaar." +
                  "<br>Wilt u toch de zaak aanpassen?",
              "450px") {

            @Override
            public void buttonYes() {
              goToZaak(zaak, postCheckAction, app);
              super.buttonYes();
            }
          });
        } else {
          goToZaak(zaak, postCheckAction, app);
        }
      } else {
        showBehandelaarPopup(zaak, postCheckAction, app, service);
      }
    } else {
      goToZaak(zaak, postCheckAction, app);
    }
  }

  private void goToZaak(Zaak zaak, Runnable postCheckAction, GbaApplication app) {
    // Open related Zaak
    app.getServices().getRequestInboxService().openRelatedRequestInboxItem(zaak);
    postCheckAction.run();
  }

  private void showBehandelaarPopup(Zaak zaak, Runnable postCheckAction, GbaApplication app,
      ZaakAttribuutService service) {
    ProcuraWindow parentWindow = app.getParentWindow();
    parentWindow.addWindow(new ConfirmDialog("Behandelaar van de zaak",
        "Wilt u uzelf toewijzen als behandelaar van deze zaak?",
        "400px") {

      @Override
      public void buttonYes() {
        service.koppelenAlsBehandelaar(zaak);
        ofNullable(VaadinUtils.getChild(parentWindow, ZakenregisterAccordionTab.class))
            .ifPresent(ZakenregisterAccordionTab::recountTree);
        goToZaak(zaak, postCheckAction, app);
        super.buttonYes();
      }

      @Override
      public void buttonNo() {
        goToZaak(zaak, postCheckAction, app);
        super.buttonNo();
      }
    });
  }
}
