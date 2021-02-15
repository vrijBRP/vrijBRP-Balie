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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.commentaar.ZaakCommentaarLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.contacten.ZaakContactLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.relatie.ZaakRelatieLayout;
import nl.procura.gba.web.modules.zaken.common.ZaakHeaderForm;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.CommentaarZaak;

public class ZaakAlgemeenLayout extends OptieLayout implements ZaakTabLayout {

  private Zaak zaak;

  public ZaakAlgemeenLayout(Zaak zaak) {
    this.zaak = zaak;
    getRight().setWidth("180px");
    getRight().setCaption("Opties");
  }

  @Override
  public void attach() {
    updateComponents();
    super.attach();
  }

  @Override
  public void reloadLayout(GbaApplication application, Zaak zaak) {
    this.zaak = zaak;
    updateComponents();
  }

  /**
   * Na het updaten van de persoonsgevens via de controle moeten de
   * layouts wel opnieuw worden toegevoegd
   */
  private void updateComponents() {

    ZaakHeaderForm zaakHeaderForm = new ZaakHeaderForm(zaak, ZaakHeaderForm.VIER);
    ZaakContactLayout contactLayout = new ZaakContactLayout(zaak);
    ZaakCommentaarLayout commentaarLayout = new ZaakCommentaarLayout(zaak);
    ZaakRelatieLayout relatieLayout = new ZaakRelatieLayout(zaak);

    getLeft().removeAllComponents();
    getLeft().addComponent(zaakHeaderForm);

    if (zaak instanceof CommentaarZaak) {
      if (((CommentaarZaak) zaak).getCommentaren().exists()) {
        getLeft().addComponent(commentaarLayout);
      }
    }

    getLeft().addComponent(contactLayout);

    if (zaak.getZaakHistorie().getRelaties().exists()) {
      getLeft().addComponent(relatieLayout);
    }

    // Opties verbergen
    removeRightVisible(getRight().getComponentCount() == 0);
  }
}
