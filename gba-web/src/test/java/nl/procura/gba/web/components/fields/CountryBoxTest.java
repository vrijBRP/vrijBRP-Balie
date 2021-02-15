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

package nl.procura.gba.web.components.fields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class CountryBoxTest {

  private static final int    START_DATE        = 1990_01_01;
  private static final int    END_DATE          = 1991_06_01;
  private static final String FIELD_VALUE       = "Value";
  private static final String FIELD_KEY         = "A";
  private static final String FIELD_DESCRIPTION = "Description";

  private static final int[] TEST_DATA = {
      1990_03_01, // within range
      1989_12_31, // just before start date
      1991_00_00, // valid from 1991_01_01 - 1991_12_31
      1991_06_00, // valid from 1991_06_01 - 1991_06_30
      1991_06_02, // just after end date
      1991_07_00, // valid from 1991_07_01 - 1991_07_31
      0, // always valid
      -1, // invalid date, all countries are valid
  };

  @Test
  public void itemWithBeginAndEndDate() {
    List<TabelFieldValue> list = new ArrayList<>();
    TabelFieldValue item = new TabelFieldValue(FIELD_KEY, FIELD_VALUE, FIELD_DESCRIPTION, START_DATE, END_DATE);
    list.add(item);

    CountryBox box = new CountryBox(list);
    // when, then
    boolean[] expected = { true, false, true, true, false, false, true, true };
    assertItemCaption(expected, item, box);
  }

  @Test
  public void itemWithoutEndDate() {
    List<TabelFieldValue> list = new ArrayList<>();
    TabelFieldValue item = new TabelFieldValue(FIELD_KEY, FIELD_VALUE, FIELD_DESCRIPTION, START_DATE, -1);
    list.add(item);

    CountryBox box = new CountryBox(list);
    // when, then
    boolean[] expected = { true, false, true, true, true, true, true, true };
    assertItemCaption(expected, item, box);
  }

  @Test
  public void itemWithoutBeginDate() {
    List<TabelFieldValue> list = new ArrayList<>();
    TabelFieldValue item = new TabelFieldValue(FIELD_KEY, FIELD_VALUE, FIELD_DESCRIPTION, -1, END_DATE);
    list.add(item);

    CountryBox box = new CountryBox(list);
    // when, then
    boolean[] expected = { true, true, true, true, false, false, true, true };
    assertItemCaption(expected, item, box);
  }

  @Test
  public void itemWithoutDates() {
    List<TabelFieldValue> list = new ArrayList<>();
    TabelFieldValue item = new TabelFieldValue(FIELD_KEY, FIELD_VALUE, FIELD_DESCRIPTION, -1, -1);
    list.add(item);

    CountryBox box = new CountryBox(list);
    // when, then
    boolean[] expected = { true, true, true, true, true, true, true, true };
    assertItemCaption(expected, item, box);
  }

  private static void assertItemCaption(boolean[] expected, TabelFieldValue item, CountryBox box) {
    for (int i = 0; i < TEST_DATA.length; i++) {
      if (expected[i]) {
        assertValid(item, box, i);
      } else {
        assertInvalid(item, box, i);
      }
    }
  }

  private static void assertValid(TabelFieldValue item, CountryBox box, int testDataIndex) {
    DateReference.setDate(box, TEST_DATA[testDataIndex]);
    assertEquals("Description (Value)", box.getItemCaption(item));
  }

  private static void assertInvalid(TabelFieldValue item, CountryBox box, int testDataIndex) {
    DateReference.setDate(box, TEST_DATA[testDataIndex]);
    assertTrue(box.getItemCaption(item).matches("^Description \\(niet geldig op [0-9]{2}-[0-9]{2}-[0-9]{4}\\)$"));
  }
}
