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

package nl.procura.gba.web.modules.account.wachtwoord.passwordStrength;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.regex.Pattern;

/**
 * Bepaalt de sterkte van een ingevoerd wachtwoord aan de hand van
 * een aantal vooraf bepaalde condities.
 *

 * <p>
 * 2012
 */

public class Password {

  private static final int MIN_LENGTE = 8; // minimale lengte van ww

  /**
   * Bepaalt de sterkte van het wachtwoord aan de hand van
   * het aantal condities waaraan het voldoet.
   */
  public static PasswordStrength getPasswordStrength(String password) {

    if (password.length() < MIN_LENGTE) {
      return PasswordStrength.TEKORT;
    }

    if (!hasNumbers(password)) {
      return PasswordStrength.CIJFER;
    }

    if (!hasWeirdSymbols(password)) {
      return PasswordStrength.LEESTEKEN;
    }

    if (!hasUpperCase(password)) {
      return PasswordStrength.HOOFDLETTER;
    }

    if (!hasLowerCase(password)) {
      return PasswordStrength.KLEINELETTER;
    }

    return PasswordStrength.GOED;
  }

  public static boolean hasLowerCase(Object checkString) {
    return exists("[a-z]", checkString);
  }

  // de volgende condities bepalen hoe sterk een wachtwoord is.
  public static boolean hasNumbers(Object checkString) {
    return exists("\\d", checkString);
  }

  public static boolean hasSpaces(Object checkString) {
    return exists("\\s+", checkString);
  }

  public static boolean hasUpperCase(Object checkString) {
    return exists("[A-Z]", checkString);
  }

  public static boolean hasWeirdSymbols(Object checkString) {
    return exists("[^a-zA-Z\\d]", checkString);
  }

  private static boolean exists(String pattern, Object value) {
    Pattern p = Pattern.compile(pattern);
    return p.matcher(astr(value)).find();
  }
}
