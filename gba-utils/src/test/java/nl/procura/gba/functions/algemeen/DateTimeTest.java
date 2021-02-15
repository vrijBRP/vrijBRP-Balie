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

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.time2str;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;

public class DateTimeTest {

  @Test
  public final void max4Digits() {

    timeMax(1, "00:01:00", TimeType.TIME_4_DIGITS);
    timeMax(12, "00:12:00", TimeType.TIME_4_DIGITS);
    timeMax(123, "01:23:00", TimeType.TIME_4_DIGITS);
    timeMax(1234, "12:34:00", TimeType.TIME_4_DIGITS);
    timeMax(12345, "01:23:45", TimeType.TIME_4_DIGITS);
    timeMax(123456, "12:34:56", TimeType.TIME_4_DIGITS);

    timeMax(1, "00:00:01", TimeType.TIME_6_DIGITS);
    timeMax(12, "00:00:12", TimeType.TIME_6_DIGITS);
    timeMax(123, "00:01:23", TimeType.TIME_6_DIGITS);
    timeMax(1234, "00:12:34", TimeType.TIME_6_DIGITS);
    timeMax(12345, "01:23:45", TimeType.TIME_6_DIGITS);
    timeMax(123456, "12:34:56", TimeType.TIME_6_DIGITS);
  }

  private void timeMax(long i, String expectedFormat, TimeType type) {

    DateTime dt = new DateTime(0, i, type);

    assertEquals(expectedFormat, dt.getFormatTime());
    assertEquals(dt.getFormatTime(), time2str(astr(dt.getLongTime())));
  }
}
