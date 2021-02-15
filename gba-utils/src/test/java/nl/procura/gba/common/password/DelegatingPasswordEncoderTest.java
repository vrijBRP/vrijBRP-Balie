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

package nl.procura.gba.common.password;

import static org.junit.Assert.*;

import org.junit.Test;

public class DelegatingPasswordEncoderTest {

  @Test
  public void version0MustOnlyPrependPassword() {
    // given
    DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(0);
    // when
    String encoded = encoder.encode("test");
    // then
    assertEquals("{0}test", encoded);
  }

  @Test
  public void version1MustPrependAndHashPassword() {
    // given
    DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(1);
    // when
    String encoded = encoder.encode("test");
    // then
    assertEquals("{1}", encoded.substring(0, 3));
    assertNotEquals("{1}test", encoded);
  }

  @Test
  public void correctPasswordVersion0MustMatch() {
    // given
    DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(0);
    String given = "test";
    String encoded = encoder.encode(given);
    // when
    boolean matches = encoder.matches(given, encoded);
    // then
    assertTrue(matches);
  }

  @Test
  public void correctPasswordVersion1MustMatch() {
    // given
    DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(1);
    String given = "test";
    String encoded = encoder.encode(given);
    // when
    boolean matches = encoder.matches(given, encoded);
    // then
    assertTrue(matches);
  }

  @Test
  public void wrongPasswordMustNotMatch() {
    // given
    DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder(1);
    String encoded = encoder.encode("test");
    // when
    boolean matches = encoder.matches("wrong", encoded);
    // then
    assertFalse(matches);
  }

  @Test(expected = IllegalArgumentException.class)
  public void wrongVersionMustThrowException() {
    // when
    new DelegatingPasswordEncoder(-1);
  }
}
