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

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.standard.ProcuraDate;

public class GbaDateRange {

  private static final int LAST_DATE         = 9999_12_31;
  private static final int LAST_MONTH        = 12_31;
  private static final int LAST_DAY_OF_MONTH = 31;

  private final int start;
  private final int end;

  public GbaDateRange(long gbaDate) {
    this((int) gbaDate);
  }

  public GbaDateRange(int gbaDate) {
    start = gbaDate;
    if (gbaDate <= 0) {
      // invalid date and 0000-00-00
      end = LAST_DATE;
    } else if (gbaDate % 1_00_00 == 0) {
      // yyyy-00-00 missing month and day of month
      end = gbaDate + LAST_MONTH;
    } else if (gbaDate % 1_00 == 0) {
      // yyyy-mm-00 missing day of month
      end = gbaDate + LAST_DAY_OF_MONTH;
    } else {
      // yyyy-mm-dd
      end = gbaDate;
    }
  }

  public boolean isValidFor(long start, long end) {
    return isAfter(start) && isBefore(end);
  }

  @Override
  public String toString() {
    return new ProcuraDate()
        .setAllowedFormatExceptions(true)
        .setForceFormatType(ProcuraDate.SYSTEMDATE_ONLY)
        .setStringDate(astr(start)).getFormatDate();
  }

  public boolean isAfter(long date) {
    return date <= 0 || end >= date;
  }

  public boolean isBefore(long date) {
    return date <= 0 || start <= date;
  }
}
