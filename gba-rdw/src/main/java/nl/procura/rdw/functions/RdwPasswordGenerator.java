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

package nl.procura.rdw.functions;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Password generator voor RDW wachtwoorden
 */
public class RdwPasswordGenerator {

  private static final RdwPasswordGenerator generator = new RdwPasswordGenerator();
  private static final List<Character>      availableChars;

  static {
    List<Character> chars = new ArrayList<>(100);
    for (char ch = 'a'; ch <= 'z'; ch++) {
      chars.add(Character.toLowerCase(ch));
      chars.add(Character.toUpperCase(ch));
    }

    for (int i = 0; i < 3; i++) {
      for (char ch = '0'; ch <= '9'; ch++) {
        chars.add(ch);
      }
    }
    availableChars = Collections.unmodifiableList(chars);
  }

  private final Random  random = new SecureRandom();
  private final Pattern rule   = Pattern.compile("^[a-zA-Z0-9]*$");

  public static String newPassword() {
    return generator.generate();
  }

  public static boolean check(String password) {
    return generator.passingRules(password);
  }

  public String generate() {

    String ww = "";
    while (!passingRules(ww)) {
      StringBuilder pass = new StringBuilder();
      for (int i = 0; i < 8; i++) {
        pass.append(availableChars.get(random.nextInt(availableChars.size())));
      }
      ww = pass.toString();
    }

    return ww;
  }

  private boolean passingRules(String password) {
    return passes1(password) && passes2(password);
  }

  /**
   * @return <code>password</code> bestaat alleen uit toegestane tekens
   */
  public boolean passes1(String password) {
    return rule.matcher(password).matches();
  }

  /**
   * @return De lengte van <code>password</code> is tussen zes en acht karakters
   */
  public boolean passes2(String password) {
    return (password != null) && (password.length() >= 6) && (password.length() <= 8);
  }
}
