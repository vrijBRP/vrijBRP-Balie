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

package nl.procura.gba.web.modules.account.wachtwoord.pages.changePassword;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired.PasswordCheck;

/**
 * Class voor testen van isDiffEnough() in PasswordCheck.
 *

 * <p>
 * 2012
 */

public class PasswordCheckTest {

  final List<String> oldPwsEmpty = new ArrayList<String>();
  final List<String> oldPws1     = Arrays.asList("abc");
  final List<String> oldPws2     = Arrays.asList("abc", "def", "ghi");

  @Test
  public final void nullValueShouldGiveTrue1() {

    boolean test = PasswordCheck.isDiffEnough(oldPws1, null, 1);
    assertEquals(true, test);
  }

  @Test
  public final void nullValueShouldGiveTrue2() {

    boolean test = PasswordCheck.isDiffEnough(oldPwsEmpty, null, 1);
    assertEquals(true, test);
  }

  @Test
  public final void testForAtLeastOnePosDifferent1() {

    boolean test = PasswordCheck.isDiffEnough(oldPws1, "b", 1);
    assertEquals(true, test);
  }

  @Test
  public final void testForAtLeastOnePosDifferent2() {

    boolean test = PasswordCheck.isDiffEnough(oldPws1, "bbc", 1);
    assertEquals(true, test);
  }

  @Test
  public final void testForAtLeastOnePosDifferent3() {

    boolean test = PasswordCheck.isDiffEnough(oldPws2, "dbi", 1);
    assertEquals(true, test);
  }

  @Test
  public final void testForAtLeastOnePosDifferent4() {

    boolean test = PasswordCheck.isDiffEnough(oldPws2, "abcd", 1);
    assertEquals(true, test);
  }

  @Test
  public final void testForAtLeastTwoPosDifferent1() {

    boolean test = PasswordCheck.isDiffEnough(oldPws2, "bbe", 2);
    assertEquals(true, test);
  }

  @Test
  public final void testForAtLeastTwoPosDifferent2() {

    boolean test = PasswordCheck.isDiffEnough(oldPws2, "abc", 2);
    assertEquals(false, test);
  }

  @Test
  public final void testForAtLeastTwoPosDifferent3() {

    boolean test = PasswordCheck.isDiffEnough(oldPws2, "abcde", 2);
    assertEquals(true, test);
  }

  @Test
  public final void testNullForOldPws() {

    boolean test = PasswordCheck.isDiffEnough(null, "d", 1);
    assertEquals(true, test);
  }

  @Test
  public final void testForPosGreaterThanLength() {

    boolean test = PasswordCheck.isDiffEnough(oldPws2, "j", 3);
    assertEquals(true, test);
  }

  @Test
  public final void testForNullInput() {

    boolean test = PasswordCheck.isDiffEnough(null, null, 0);
    assertEquals(true, test);
  }
}
