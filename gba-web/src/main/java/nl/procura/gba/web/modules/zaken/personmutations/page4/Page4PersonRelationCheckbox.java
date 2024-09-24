/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.personmutations.page4;

import com.vaadin.ui.CheckBox;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.vaadin.component.dialog.ConfirmDialog;

public class Page4PersonRelationCheckbox extends CheckBox {

  private boolean asked = false;

  public Page4PersonRelationCheckbox() {
    super("Doorverwerking naar onderstaande gerelateerden");
  }

  @Override
  public void attach() {
    if (!asked) {
      asked = true;
      GbaApplication appl = (GbaApplication) getApplication();
      appl.getParentWindow().addWindow(new ConfirmDialog("Vraag over doorverwerking",
          "Wilt u de wijzigingen doorverwerken naar de gerelateerden?",
          "450px") {

        @Override
        public void buttonYes() {
          setValue(Boolean.TRUE);
          super.buttonYes();
        }

        @Override
        public void buttonNo() {
          setValue(Boolean.FALSE);
          super.buttonNo();
        }
      });
      super.attach();
    }
  }
}
