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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.navigation.GbaPopupButton;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status.ZaakStatusUpdater;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page3.Page3Zaken;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page2ZakenOpties extends GbaPopupButton {

  private static final String CAPTION_VERWIJDEREN = "Verwijderen";

  private final Page2ZakenTable table;

  public Page2ZakenOpties(final Page2ZakenTable table) {

    super("Opties", "110px", "105px");

    this.table = table;

    if (table != null) {

      boolean updateAllowed = table.getApplication().getServices().getGebruiker().getProfielen().isProfielActie(
          ProfielActie.UPDATE_HOOFD_ZAKENREGISTER);
      boolean deleteAllowed = table.getApplication().getServices().getGebruiker().getProfielen().isProfielActie(
          ProfielActie.DELETE_HOOFD_ZAKENREGISTER);

      if (deleteAllowed) {

        addChoice(new Choice(CAPTION_VERWIJDEREN) {

          @Override
          public void onClick() {
            delete();
          }
        });
      }

      if (updateAllowed) {

        addChoice(new Choice("Status wijzigen") {

          @Override
          public void onClick() {

            new ZaakStatusUpdater(table) {

              @Override
              protected void reload() {
                reloadTree();
              }
            };
          }
        });
      }

      addChoice(new Choice("Diagrammen") {

        @Override
        public void onClick() {
          VaadinUtils.getParent(table, ZakenregisterPage.class).getNavigation().goToPage(
              new Page3Zaken(table));
        }
      });
    }
  }

  public void delete() {

    if (isChoice(CAPTION_VERWIJDEREN)) {

      new DeleteProcedure<ZaakKey>(table) {

        @Override
        public void afterDelete() {
          reloadTree();
        }

        @Override
        public void deleteValue(ZaakKey zaakKey) {
          table.getApplication().getServices().getZakenService().delete(zaakKey);
        }
      };
    }
  }

  protected void reloadTree() {
    ZakenregisterAccordionTab tab = VaadinUtils.getChild(getWindow(), ZakenregisterAccordionTab.class);
    tab.reloadTree();
  }
}
