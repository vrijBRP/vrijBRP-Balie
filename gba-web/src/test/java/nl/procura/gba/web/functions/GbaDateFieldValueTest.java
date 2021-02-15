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

package nl.procura.gba.web.functions;

import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;

public class GbaDateFieldValueTest {

  @Test
  public void leeg() {

    GbaDateFieldValue case1 = new GbaDateFieldValue(toBigDecimal(-1));
    assertEquals("", case1.getFormatDate());
    assertEquals(-1, case1.getLongDate());

    GbaDateFieldValue case2 = new GbaDateFieldValue(-1);
    assertEquals("", case2.getFormatDate());
    assertEquals(-1, case2.getLongDate());

    GbaDateFieldValue case3 = new GbaDateFieldValue("");
    assertEquals("", case3.getFormatDate());
    assertEquals(-1, case3.getLongDate());

    GbaDateFieldValue case4 = new GbaDateFieldValue("-1");
    assertEquals("", case4.getFormatDate());
    assertEquals(-1, case4.getLongDate());
  }

  @Test
  public void geenJaar() {

    GbaDateFieldValue case1 = new GbaDateFieldValue(toBigDecimal("0"));
    assertEquals("00-00-0000", case1.getFormatDate());
    assertEquals(0, case1.getLongDate());

    GbaDateFieldValue case2 = new GbaDateFieldValue(0);
    assertEquals("00-00-0000", case2.getFormatDate());
    assertEquals(0, case2.getLongDate());

    GbaDateFieldValue case3 = new GbaDateFieldValue("0");
    assertEquals("00-00-0000", case3.getFormatDate());
    assertEquals(0, case3.getLongDate());
  }

  @Test
  public void geenDag() {

    GbaDateFieldValue case1 = new GbaDateFieldValue(toBigDecimal("19900100"));
    assertEquals("00-01-1990", case1.getFormatDate());
    assertEquals(19900100, case1.getLongDate());

    GbaDateFieldValue case2 = new GbaDateFieldValue(19900100);
    assertEquals("00-01-1990", case2.getFormatDate());
    assertEquals(19900100, case2.getLongDate());

    GbaDateFieldValue case3 = new GbaDateFieldValue("19900100");
    assertEquals("00-01-1990", case3.getFormatDate());
    assertEquals(19900100, case3.getLongDate());
  }

  @Test
  public void correct() {

    GbaDateFieldValue case1 = new GbaDateFieldValue(toBigDecimal("19900101"));
    assertEquals("01-01-1990", case1.getFormatDate());
    assertEquals(19900101, case1.getLongDate());

    GbaDateFieldValue case2 = new GbaDateFieldValue(19900101);
    assertEquals("01-01-1990", case2.getFormatDate());
    assertEquals(19900101, case2.getLongDate());

    GbaDateFieldValue case3 = new GbaDateFieldValue("19900101");
    assertEquals("01-01-1990", case3.getFormatDate());
    assertEquals(19900101, case3.getLongDate());
  }
}
