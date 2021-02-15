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

package nl.procura.gba.common;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Password generator voor BPR wachtwoorden
 */
public class BrpPasswordGenerator {

  private final static BrpPasswordGenerator generator = new BrpPasswordGenerator();
  private final static List<Character>      availableChars;

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

  private final Random  random       = new SecureRandom();
  private final Pattern rule1        = Pattern.compile("^[\\w\\d _=+/!@*].*$");
  private final Pattern rule4Letters = Pattern.compile("[a-zA-Z]{3}");
  private final Pattern rule4Numbers = Pattern.compile("[0-9]{3}");
  private final Pattern rule4Misc    = Pattern.compile("[^a-zA-Z0-9 ]{3}");
  private final Pattern rule6        = Pattern.compile("[a-zA-Z]{2,}");
  private final Pattern rule7        = Pattern.compile("[0-9]{2,}");
  private final Pattern rule8        = Pattern.compile("[^a-zA-Z0-9 ]");

  public static String newPassword() {
    return generator.generate();
  }

  public static boolean checkWachtwoord(String ww) {
    return generator.passingRules(ww);
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
    return passes1(password) && passes2(password) && passes3(password) && passes4(password) && passes8(password);
  }

  /**
   * @return <code>password</code> bestaat alleen uit toegestane tekens
   */
  public boolean passes1(String password) {
    return rule1.matcher(password).matches();
  }

  /**
   * @return De lengte van <code>password</code> is tussen zes en acht karakters
   */
  public boolean passes2(String password) {
    return (password != null) && (password.length() >= 6) && (password.length() <= 8);
  }

  /**
   * @return Hetzelfde teken komt maximaal 2 keer in <code>password</code> voor.
   */
  public boolean passes3(String password) {

    if ((password == null) || (password.length() <= 2)) {
      return true;
    }

    for (char c : password.toCharArray()) {
      int i = 0, count = 0;
      while ((i = password.indexOf(c, i)) != -1) {
        i++;
        count++;
      }

      if (count > 2) {
        return false;
      }
    }

    return true;
  }

  /**
   * Een opeenvolgende reeks van 3 tekens mag niet telkens met 1 oplopen of afdalen.
   * Voorbeelden: de tekenreeksen ABC, XYZ, KLM, PQR, 456, 321, cba, vut, gfe en zyx mogen niet in een wachtwoord voorkomen
   */
  public boolean passes4(String password) {
    return passesFor(password, rule4Letters) && passesFor(password, rule4Numbers) && passesFor(password, rule4Misc);
  }

  private boolean passesFor(String password, Pattern p) {

    if ((password == null) || (password.length() < 3)) {
      return true;
    }

    Matcher m = p.matcher(password);
    while (m.find()) {
      char[] chars = m.group().toCharArray();
      if (oplopend(chars) || aflopend(chars)) {
        return false;
      }
    }

    return true;
  }

  private boolean oplopend(char[] chars) {

    if (chars.length < 3) {
      return false;
    }
    return (chars[0] == chars[1] - 1) && (chars[1] == chars[2] - 1);
  }

  private boolean aflopend(final char[] chars) {

    char[] reversed = new char[chars.length];
    for (int i = 0; i < chars.length; i++) {
      reversed[i] = chars[chars.length - (i + 1)];
    }
    return oplopend(reversed);
  }

  /**
   * @return Spaties komen alleen in de zevende en/of achtste positie van <code>password</code> voor.
   */
  public boolean passes5(String password) {

    if (password == null) {
      return false;
    }

    int eersteSpatieOp = password.indexOf(' ');
    return (eersteSpatieOp == -1) || (eersteSpatieOp > 5);
  }

  /**
   * @return aaneengesloten reeksen van letters (groot of klein) hebben een lengte van 1 of 3 in <code>password</code>
   */
  public boolean passes6(String password) {

    Matcher m = rule6.matcher(password);
    boolean fouteLengte = false;
    while (m.find() && !fouteLengte) {
      fouteLengte |= m.group().length() != 3;
    }
    return !fouteLengte;
  }

  /**
   * @return aaneengesloten reeksen van cijfers hebben een lengte van 1 of 3 in <code>password</code>
   */
  public boolean passes7(String password) {

    Matcher m = rule7.matcher(password);
    boolean fouteLengte = false;
    while (m.find() && !fouteLengte) {
      fouteLengte = m.group().length() != 3;
    }
    return !fouteLengte;
  }

  /**
   * @return <code>password</code> heeft minstens 3 overige tekens of voldoet aan regels 5, 6 en 7
   */
  public boolean passes8(String password) {

    Matcher m = rule8.matcher(password);
    int count = 0;
    while (m.find()) {
      count++;
    }

    return (count >= 3) || passes5(password) && passes6(password) && passes7(password);
  }
}
