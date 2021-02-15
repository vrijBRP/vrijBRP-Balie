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

package nl.procura.gba.web.modules.zaken.rijbewijs;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsService;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.vaadin.component.dialog.ConfirmDialog;

public abstract class RbwUpdateToDateCheck {

  public RbwUpdateToDateCheck(RijbewijsAanvraag crbAanvraag, GbaWindow window) {

    final GbaApplication gbaApplication = window.getGbaApplication();
    final RijbewijsService rdw = gbaApplication.getServices().getRijbewijsService();
    final RijbewijsAanvraag dbAanvraag = rdw.findByAanvraagnummer(crbAanvraag.getAanvraagNummer());

    if (!isRaadplegingOpslagen(dbAanvraag)) { // Raadpleging invoeren zoals CRB die geeft.
      onNotUpdated();
      window.addWindow(new ConfirmDialog("Weet u het zeker",
          "Deze aanvraag komt niet voor in de database van de applicatie.<br/>"
              + "De belangrijkste oorzaak kan zijn dat de aanvraag niet in deze applicatie is gedaan. "
              + "Voor verdere vervolgacties moet de aanvraag worden opgenomen in de database. <hr/>Wilt u dit nu doen?",
          "500px") {

        @Override
        public void buttonYes() {
          closeWindow();
          crbAanvraag.setIngevoerdDoor(new UsrFieldValue(0, "")); // Ingevoerd door onbekend
          gbaApplication.getServices().getZaakIdentificatieService().getDmsZaakId(crbAanvraag);
          rdw.save(crbAanvraag);
          onProceed(crbAanvraag);
        }
      });
    } else if (!isRaadplegingUptoDate(crbAanvraag, dbAanvraag)) {// Raadpleging invoeren, maar status van CRB toevoegen
      dbAanvraag.getStatussen().addStatus(crbAanvraag.getStatussen().getStatus());
      rdw.save(dbAanvraag);
      onProceed(dbAanvraag);

    } else { // Database is up-to-date
      onProceed(dbAanvraag);
    }
  }

  public abstract void onProceed(RijbewijsAanvraag checkedAanvraag);

  public abstract void onNotUpdated();

  /**
   * Als er sprake is van een raadpleging dan controleren of deze wel is opgeslagen.
   */
  private boolean isRaadplegingOpslagen(RijbewijsAanvraag dbAanvraag) {
    return dbAanvraag != null;
  }

  /**
   * Als er sprake is van een raadpleging dan controleren of deze wel is opgeslagen.
   */
  private boolean isRaadplegingUptoDate(RijbewijsAanvraag crbAanvraag, RijbewijsAanvraag dbAanvraag) {
    if (crbAanvraag != null && dbAanvraag != null) {
      String codeNieuw = astr(crbAanvraag.getStatussen().getStatus().getStatus().getCode());
      String codeOud = astr(dbAanvraag.getStatussen().getStatus().getStatus().getCode());
      return along(codeOud) == along(codeNieuw);
    }

    return true;
  }
}
