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

import static nl.procura.gba.common.MiscUtils.abbreviate;
import static nl.procura.gba.common.MiscUtils.cleanPath;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Testen voor de clean() functie.
 *

 * <p>
 * 2012
 */

public class FormatFunctionsTest {

  // De volgende testen zijn testen voor de clean() functie.

  @Test
  public final void deleteBeginAndEndSlashes() {
    String test = "//hallo//";
    String expectedResult = "hallo";
    assertEquals(String.format("Het antwoord is %s.", cleanPath(test)), expectedResult, cleanPath(test));

  }

  @Test
  public final void replaceMultipleSlashesForOne1() {
    String test = "test//hallo";
    String expectedResult = "test/hallo";
    assertEquals(String.format("Het antwoord is %s.", cleanPath(test)), expectedResult, cleanPath(test));
  }

  @Test
  public final void replaceMultipleSlashesForOne2() {
    String test = "test/////hallo///dag//ha";
    String expectedResult = "test/hallo/dag/ha";
    assertEquals(String.format("Het antwoord is %s.", cleanPath(test)), expectedResult, cleanPath(test));
  }

  @Test
  public final void replaceBackwardForForwardSlashes1() {

    String test = "test\\\\hallo";
    String expectedResult = "test/hallo";
    assertEquals(String.format("Het antwoord is %s.", cleanPath(test)), expectedResult, cleanPath(test));
  }

  @Test
  public final void replaceBackwardForForwardSlashes2() {

    String test = "test\\\\hallo\\dag";
    String expectedResult = "test/hallo/dag";
    assertEquals(String.format("Het antwoord is %s.", cleanPath(test)), expectedResult, cleanPath(test));
  }

  @Test
  public final void deleteSpacesBeginAndEnd() {

    String test = "    hallo   ";
    String expectedResult = "hallo";
    assertEquals(String.format("Het antwoord is %s.", cleanPath(test)), expectedResult, cleanPath(test));
  }

  @Test
  public final void testOptionsClean() {

    String test = "   a///b   //c \\\\ d/e   ";
    String expectedResult = "a/b   /c / d/e";
    assertEquals(String.format("Het antwoord is %s.", cleanPath(test)), expectedResult, cleanPath(test));
  }

  // een lege string valt niet op te schonen en moet dus onveranderd blijven.
  @Test
  public final void cleanEmptyString() {

    String test = "";
    String expectedResult = "";
    assertEquals(String.format("Het antwoord is %s.", cleanPath(test)), expectedResult, cleanPath(test));
  }

  // als er geen mapnaam wordt opgegeven (ComboBox zet de naam bij een commit() van de form
  // blijkbaar per default op 'null') moet deze als de lege string opgeslagen worden.

  @Test
  public final void cleanNullString() {

    String test = null;
    String expectedResult = "";
    assertEquals(String.format("Het antwoord is %s.", cleanPath(test)), expectedResult, cleanPath(test));
  }

  @Test
  public final void cleanPipeCharacter() {

    String test = "|";
    String expectedResult = "|";

    assertEquals(String.format("Het antwoord is %s.", cleanPath(test)), expectedResult, cleanPath(test));
  }

  // volgende tests zijn voor abbreviate() functie

  @Test
  public final void abbreviateStringWithSplitArgument() {

    String test = "tra, la, ladi, doe, ha";
    String expected = "tra,...";

    assertEquals(expected, abbreviate(test, 1, ','));
  }

  @Test
  public final void abbreviateStringWithSplitArgument2() {

    String test = "tra, la, ladi, doe, ha";
    String expected = "tra, la,...";

    assertEquals(expected, abbreviate(test, 2, ','));
  }

  @Test
  public final void abbreviateStringWithSplitArgument3() {

    String test = "tra la ladi doe   ha";
    String expected = "tra la ...";

    assertEquals(expected, abbreviate(test, 2, ' '));
  }

  @Test
  public final void abbreviateStringWithSplitArgument4() {

    String test = "tra/la/ladi/doe/ha";
    String expected = "tra/la/...";

    assertEquals(expected, abbreviate(test, 2, '/'));
  }

  // afkorten naar lengte 0 levert lege string op
  @Test
  public final void abbreviateStringWithSplitArgument5() {

    String test = "tra, la, ladi, doe, ha";
    String expected = "";

    assertEquals(expected, abbreviate(test, 0, ','));
  }

  // null string afkorten op een split levert altijd de lege string op
  @Test
  public final void abbreviateStringWithSplitArgument6() {

    String test = null;
    String expected = "";

    assertEquals(expected, abbreviate(test, 5, ','));
  }

  @Test
  public final void abbreviateStringWithSplitArgument7() {

    String test = "tra/la";
    String expected = test;

    assertEquals(expected, abbreviate(test, 2, '/'));
  }

  @Test
  public final void abbreviateStringWithSplitArgument8() {

    String test = "tra/la";
    String expected = "tra/...";

    assertEquals(expected, abbreviate(test, 1, '/'));
  }

  @Test
  public final void abbreviateStringWithSplitArgument9() {

    String test = "";
    String expected = "";

    assertEquals(expected, abbreviate(test, 7, ','));
  }

}
