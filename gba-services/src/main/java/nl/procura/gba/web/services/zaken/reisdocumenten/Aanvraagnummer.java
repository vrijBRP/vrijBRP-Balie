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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static nl.procura.gba.common.MiscUtils.trimNr;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.aval;

public class Aanvraagnummer {

  private String nummer       = "";
  private String formatNummer = "";

  public Aanvraagnummer(String aanvraagnummer) {
    setNummer(aanvraagnummer);
    setFormatNummer(aanvraagnummer);
  }

  public String getFormatNummer() {
    return formatNummer;
  }

  public Long toLong() {
    return along(nummer);
  }

  public String getNummer() {
    return String.format("%09d", along(nummer));
  }

  public boolean isCorrect() {

    String nr = getNummer();

    if (nr == null) {
      return false;
    }

    nr = nr.trim().replaceAll("\\D", "");

    if (nr.length() != 9) {
      return false;
    }

    int[] nrs = { 0, 9, 7, 1, 3, 7, 9, 3, 1 };
    int orvalue = aval(nr.substring(0, 1));
    int j = 0;

    for (int i = 1; i < 9; i++) {
      int x = nrs[i];
      int y = aval(nr.substring(i, i + 1));
      j += (x * y);
    }

    int newvalue = 9 - (j % 10);
    return newvalue == orvalue;
  }

  @Override
  public String toString() {
    return "Aanvraagnummer [nummer=" + nummer + ", formatNummer=" + formatNummer + "]";
  }

  private void setNummer(String nummer) {
    this.nummer = trimNr(nummer);
  }

  @SuppressWarnings("unused")
  private void setFormatNummer(String formatNummer) {
    String s = String.format("%09d", along(getNummer()));
    this.formatNummer = String.format("%s-%s-%s", s.substring(0, 1), s.substring(1, 5), s.substring(5, 9));
  }
}
