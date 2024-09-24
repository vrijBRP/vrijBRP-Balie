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

package nl.procura.gba.web.modules.account.wachtwoord;

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.modules.account.wachtwoord.pages.ChangePasswordForm;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;

public class SaveNewPassword {

  public static String getMessage(GebruikerService gebruikers, Gebruiker gebruiker) {

    gebruiker = getGebruikerFromDatabase(gebruikers, gebruiker); // haal gebruiker uit database: ww historie is nu aangepast!!
    int verloopTermijn = gebruikers.getVerlooptermijn(gebruiker);

    StringBuilder msg = new StringBuilder();
    if (pos(verloopTermijn)) {
      String dagen = verloopTermijn == 1 ? " dag" : " dagen";
      String datumEindeWachtwoord = getDatumEindeWachtwoord(gebruikers, gebruiker);

      msg.append("Uw wachtwoord is gewijzigd.");
      if (pos(verloopTermijn)) {
        msg.append(" Het nieuwe wachtwoord is ");
        msg.append("<b>");
        msg.append(verloopTermijn);
        msg.append(dagen);
        msg.append("</b> geldig.");
        msg.append("<br/>De verloopdatum van het nieuwe wachtwoord is <b>");
        msg.append(datumEindeWachtwoord);
        msg.append("</b>.");
      }
    } else { // geen wachtwoordverloop! Zie ook isWachtwoordKanVerlopen in GebruikerImpl
      msg.append("Uw wachtwoord is succesvol gewijzigd.");
    }

    return msg.toString();
  }

  public static void saveNewPw(ChangePasswordForm form, GebruikerService gebruikers, Gebruiker gebruiker) {

    form.commit(); // audit-eisen gecheckt!

    gebruikers.setWachtwoord(gebruiker, form.getBean().getNieuwWw(), false);
  }

  private static String getDatumEindeWachtwoord(GebruikerService dG, Gebruiker gebruiker) {
    return dG.getWachtwoordVerloopdatum(gebruiker).getFormatDate();
  }

  private static Gebruiker getGebruikerFromDatabase(GebruikerService gebruikers, Gebruiker gebruiker) {
    return gebruikers.getGebruikerByGebruiker(gebruiker);
  }
}
