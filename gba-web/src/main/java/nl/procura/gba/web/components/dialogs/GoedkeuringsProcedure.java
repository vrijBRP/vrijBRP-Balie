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

package nl.procura.gba.web.components.dialogs;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.zaken.algemeen.goedkeuring.GoedkeuringType;
import nl.procura.gba.web.services.zaken.algemeen.goedkeuring.GoedkeuringZaak;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

/**

 * <p>
 * Stel de vraag of de aanvraag/aangifte is goedgekeurd door de aanvrager/aangever
 */
public abstract class GoedkeuringsProcedure {

  public GoedkeuringsProcedure(final GbaApplication gbaApplication, final GoedkeuringZaak zaak) {

    if (GoedkeuringType.GOEDGEKEURD.equals(zaak.getGoedkeuringsType())) {
      onAkkoord();
    } else {

      String msg = "Is de aanvrager/aangever akkoord met de inhoud van de aanvraag/aangifte?";
      ConfirmDialog confirmDialog = new ConfirmDialog("Akkoord?", msg, "500px", ProcuraTheme.ICOON_24.QUESTION) {

        @Override
        public void buttonYes() {
          super.buttonYes();
          zaak.setGoedkeuringsType(GoedkeuringType.GOEDGEKEURD);
          onAkkoord();
        }
      };

      gbaApplication.getParentWindow().addWindow(confirmDialog);
    }
  }

  public abstract void onAkkoord();
}
