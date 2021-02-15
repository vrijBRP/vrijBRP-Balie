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

package nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired;

import static org.apache.commons.lang3.StringUtils.getLevenshteinDistance;

import java.util.List;

import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;

/**
 * Deze klasse checkt of een nieuw ingevoerd wachtwoord aan de audit eisen voldoet
 *

 * <p>
 * 2012
 */

public class PasswordCheck {

  public static boolean isDiff(List<String> oldPws, String newPw) {

    if (newPw == null) { // nog niets ingevuld
      return true;
    }

    if (oldPws == null || oldPws.isEmpty()) {
      return true;
    }

    for (String oldPw : oldPws) {

      if (oldPw == null) {
        throw new ProException(ProExceptionSeverity.ERROR,
            "Er is een fout opgetreden bij het evalueren van het nieuwe wachtwoord");
      }

      if (!oldPw.equals(newPw)) {
        continue;
      }

      return false;
    }

    return true;
  }

  /**
   * Controleert of de Levenshtein afstand tussen het nieuwe wachtwoord, newPw, en elk oud ww
   * uit het lijstje 'oldPws' minimaal 'pos' groot is. Dit garandeert dat mensen niet zomaar
   * naar hun oude ww terug kunnen wijzigen: deze eis garandeert nl. dat het nieuwe ww op minimaal
   * 'pos' posities verschilt van alle oude wachtwoorden.
   */

  public static boolean isDiffEnough(List<String> oldPws, String newPw, int pos) {

    if (newPw == null) { // nog niets ingevuld
      return true;
    }

    if (oldPws == null || oldPws.isEmpty()) {
      return true;
    }

    for (String oldPw : oldPws) {

      if (oldPw == null) {
        throw new ProException(ProExceptionSeverity.ERROR,
            "Er is een fout opgetreden bij het evalueren van het nieuwe wachtwoord");
      }

      if (getLevenshteinDistance(oldPw, newPw) >= pos) {
        continue;
      }

      return false;
    }

    return true;
  }
}
