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

package nl.procura.gba.functions.algemeen;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.procura.gba.common.BrpPasswordGenerator;

public class BrpPasswordGeneratorTest {

  BrpPasswordGenerator gen;

  @Before
  public void before() {
    gen = new BrpPasswordGenerator();
  }

  @Test
  public void testRule1() {

    assertTrue(gen.passes1("abcdefgh122345@#$%^&"));
    assertFalse(gen.passes1("\u09FD\u0FDE"));
  }

  @Test
  public void testRule2() {

    for (String fout : new String[]{ "a", "aaa", "aaaaa", "aaabbbccc" }) {
      assertFalse("verwacht dat '" + fout + "' niet zou slagen", gen.passes2(fout));
    }
    for (String goed : new String[]{ "aabbcc", "aaabbcc", "aaaddccc" }) {
      assertTrue("verwacht dat '" + goed + "' wel zou slagen", gen.passes2(goed));
    }
  }

  @Test
  public void testRule3() {

    for (String fout : new String[]{ "aaa", "111", "???" }) {
      assertFalse("verwacht dat '" + fout + "' niet zou slagen", gen.passes3(fout));
    }
    for (String goed : new String[]{ "aabbcc", "abcdef", "112334", "aaA" }) {
      assertTrue("verwacht dat '" + goed + "' wel zou slagen", gen.passes3(goed));
    }
  }

  @Test
  public void testRule4() {

    for (String fout : new String[]{ "ABC", "XYZ", "KLM", "PQR", "456", "321", "cba", "vut", "gfe", "zyx" }) {
      assertFalse("verwacht dat '" + fout + "' niet zou slagen", gen.passes4(fout));
    }
    for (String goed : new String[]{ "AB1C", "ABD", "DBA", "436" }) {
      assertTrue("verwacht dat '" + goed + "' wel zou slagen", gen.passes4(goed));
    }
  }

  @Test
  public void testRule5() {

    for (String fout : new String[]{ " 123", "1 334", "12 456", "123 567", "1234 678", "12345 789" }) {
      assertFalse("verwacht dat '" + fout + "' niet zou slagen", gen.passes5(fout));
    }
    for (String goed : new String[]{ "123456", "123456 ", "123456 8", "1234567 " }) {
      assertTrue("verwacht dat '" + goed + "' wel zou slagen", gen.passes5(goed));
    }
  }

  @Test
  public void testRule6() {

    for (String fout : new String[]{ "abcd", "AbCd", "Ab", "cD" }) {
      assertFalse("verwacht dat '" + fout + "' niet zou slagen", gen.passes6(fout));
    }
    for (String goed : new String[]{ "a123", "abc2", "123" }) {
      assertTrue("verwacht dat '" + goed + "' wel zou slagen", gen.passes6(goed));
    }
  }

  @Test
  public void testRule7() {

    for (String fout : new String[]{ "4123", "13a4", "12345" }) {
      assertFalse("verwacht dat '" + fout + "' niet zou slagen", gen.passes7(fout));
    }
    for (String goed : new String[]{ "126", "1abcd " }) {
      assertTrue("verwacht dat '" + goed + "' wel zou slagen", gen.passes7(goed));
    }
  }

  @Test
  public void testRule8() {

    for (String fout : new String[]{ "4123", "AbCd", "1 334" }) {
      assertFalse("verwacht dat '" + fout + "' niet zou slagen", gen.passes8(fout));
    }
    for (String goed : new String[]{ "!a3b1a2A", "-978G-h4 ", "!@#$%^&" }) {
      assertTrue("verwacht dat '" + goed + "' wel zou slagen", gen.passes8(goed));
    }
  }
}
