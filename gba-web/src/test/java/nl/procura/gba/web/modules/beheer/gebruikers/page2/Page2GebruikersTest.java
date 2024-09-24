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

package nl.procura.gba.web.modules.beheer.gebruikers.page2;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import nl.procura.commons.core.exceptions.ProException;

/**
 * Testen voor de makeLegalPath() functie.
 *

 * <p>
 * 2012
 */

public class Page2GebruikersTest {

  /**
   * Deze functie controleert of er geen lege mapnamen voorkomen.
   * We testen de functionaliteit van deze functie in deze klasse.
   *
   * @return mapstructuur zonder lege mappen.
   */
  private StringBuilder makeLegalPath(String cleanedPath) {
    List<String> dirs = new ArrayList<String>();
    StringBuilder legalPath = new StringBuilder();
    String[] maps = cleanedPath.split("/");
    int index = 0;

    if (!emp(cleanedPath)) {
      for (String map : maps) {

        if (emp(map)) {
          if (maps.length > 1) { // er zijn in ieder geval twee mapnamen toegevoegd
            throw new ProException(ENTRY, WARNING, "Gebruik geen lege mapnamen a.u.b.");
          }
        } else { // niet-lege mapnaam
          dirs.add(map);
        }
      }

      for (String dir : dirs) {
        if (index == dirs.size() - 1) {
          legalPath = legalPath.append(dir);
        } else {
          legalPath = legalPath.append(dir).append("/");
        }
        index++;
      }
    }
    return legalPath;
  }

  @Test(expected = ProException.class)
  public final void doNotMakeEmptyDirs1() throws ProException {
    final String path = "/a/ /";
    makeLegalPath(path);
  }

  @Test(expected = ProException.class)
  public final void doNotMakeEmptyDirs2() throws ProException {
    final String path = "/a/ /b/c";
    makeLegalPath(path);
  }

  @Test(expected = ProException.class)
  public final void doNotMakeEmptyDirs3() throws ProException {
    final String path = "//a/b";
    makeLegalPath(path);
  }

  @Test
  public final void enteringEmptyPathShouldResultInEmptyPath() {
    final String path = "";
    final String actual = makeLegalPath(path).toString();

    assertEquals(String.format("het antwoord is %s", actual), path, actual);
  }

  @Test
  public final void testSplitFunction() {
    final String path = "a/b";
    final String[] splitPath = path.split("/");
    String[] expected = { "a", "b" };
    assertArrayEquals(expected, splitPath);
  }

  @Test
  public final void testSplitFunctionForOneSlash() {
    final String path = "/";
    final String[] splitPath = path.split("/");
    String[] expected = new String[0]; // lege array

    assertArrayEquals(expected, splitPath);
  }

  @Test
  public final void testSplitFunctionForSpaces() {

    final String path = " / ";
    final String[] splitPath = path.split("/");
    String[] expected = new String[2]; // lege array
    expected[0] = " ";
    expected[1] = " ";

    assertArrayEquals(expected, splitPath);

  }

}
