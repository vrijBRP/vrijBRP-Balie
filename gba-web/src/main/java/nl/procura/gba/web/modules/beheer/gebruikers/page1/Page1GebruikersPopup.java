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

package nl.procura.gba.web.modules.beheer.gebruikers.page1;

import java.util.List;

import nl.procura.gba.web.common.misc.email.Verzending;
import nl.procura.gba.web.components.layouts.navigation.GbaPopupButton;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.gebruikers.email.page1.Page1SendEmail;
import nl.procura.gba.web.modules.beheer.gebruikers.page10.Page10Gebruikers;
import nl.procura.gba.web.modules.beheer.gebruikers.page11.Page11Gebruikers;
import nl.procura.gba.web.modules.beheer.gebruikers.page12.Page12Gebruikers;
import nl.procura.gba.web.modules.beheer.gebruikers.page13.Page13Gebruikers;
import nl.procura.gba.web.modules.beheer.gebruikers.page7.Page7Gebruikers;
import nl.procura.gba.web.modules.beheer.gebruikers.page8.Page8Gebruikers;
import nl.procura.gba.web.modules.beheer.gebruikers.page9.Page9Gebruikers;
import nl.procura.gba.web.modules.beheer.overig.TabelToonType;
import nl.procura.gba.web.modules.beheer.overig.TableSelectionCheck;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.component.layout.page.PageNavigation;

public class Page1GebruikersPopup extends GbaPopupButton {

  public Page1GebruikersPopup(final GbaTable table, final TabelToonType toonType) {

    super("Opties", "150px", "145px");

    addChoice(new Choice("Map") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<Gebruiker> allSelectedUsers = getAllSelectedUsers();

        if (!isParentMapOnlySelectedRecord(allSelectedUsers)) {
          getNavigation().goToPage(new Page7Gebruikers(allSelectedUsers, toonType));
        }
      }

    });

    addChoice(new Choice("Documenten koppelen") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<Gebruiker> allSelectedUsers = getAllSelectedUsers();

        if (!isParentMapOnlySelectedRecord(allSelectedUsers)) {
          getNavigation().goToPage(new Page8Gebruikers(allSelectedUsers));
        }
      }
    });

    addChoice(new Choice("Profielen koppelen") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<Gebruiker> allSelectedUsers = getAllSelectedUsers();

        if (!isParentMapOnlySelectedRecord(allSelectedUsers)) {
          getNavigation().goToPage(new Page10Gebruikers(allSelectedUsers));
        }
      }
    });

    addChoice(new Choice("Locaties koppelen") {

      @Override
      public void onClick() {
        TableSelectionCheck.checkSelection(table);
        List<Gebruiker> allSelectedUsers = getAllSelectedUsers();

        if (!isParentMapOnlySelectedRecord(allSelectedUsers)) {
          getNavigation().goToPage(new Page9Gebruikers(allSelectedUsers));
        }
      }
    });

    addChoice(new Choice("Instellingen kopiëren") {

      @Override
      public void onClick() {
        getNavigation().goToPage(new Page11Gebruikers());
      }
    });

    addChoice(new Choice("Controleer gegevens") {

      @Override
      public void onClick() {
        getNavigation().goToPage(new Page12Gebruikers());
      }
    });

    addChoice(new Choice("E-mails") {

      @Override
      public void onClick() {
        List<Gebruiker> gebruikers = table.getApplication().getServices().getGebruikerService().setInformatie(
            getAllSelectedUsers());
        getNavigation().goToPage(new Page1SendEmail(Verzending.getVerzendingen(gebruikers)));
      }
    });

    addChoice(new Choice("Wachtwoord resetten") {

      @Override
      public void onClick() {
        TableSelectionCheck.checkSelection(table);
        List<Gebruiker> allSelectedUsers = getAllSelectedUsers();
        getNavigation().goToPage(new Page13Gebruikers(allSelectedUsers));
      }
    });
  }

  protected List<Gebruiker> getAllSelectedUsers() {
    return null;
  }

  protected PageNavigation getNavigation() {
    return null;
  }

  @SuppressWarnings("unused")
  protected boolean isParentMapOnlySelectedRecord(List<Gebruiker> allSelectedUsers) { // Override
    return false;
  }
}
