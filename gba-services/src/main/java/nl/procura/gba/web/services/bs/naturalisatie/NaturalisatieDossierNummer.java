/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.services.bs.naturalisatie;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

public class NaturalisatieDossierNummer {

  private static final String PATTERN = "(\\d{4}).(\\d{1}).(\\d{5})";

  @Getter
  private String value;

  @Getter
  private Long gemeenteCode;

  @Getter
  private Long yearDigit;

  @Getter
  private Long vnr;

  private NaturalisatieDossierNummer() {
  }

  private NaturalisatieDossierNummer(long gemeenteCode, long yearDigit, long vnr) {
    this.gemeenteCode = gemeenteCode;
    this.yearDigit = yearDigit;
    this.vnr = vnr;
    this.value = format("%04d.%d.%05d", gemeenteCode, yearDigit, vnr);
  }

  private NaturalisatieDossierNummer(String value) {
    this.value = value;
  }

  public static NaturalisatieDossierNummer ofValue(String value) {
    return parse(value);
  }

  public static NaturalisatieDossierNummer ofGemeente(long gemeenteCode) {
    return new NaturalisatieDossierNummer(gemeenteCode, getFinalYearDigit(), 1);
  }

  public boolean isValid() {
    return isValid(value);
  }

  public NaturalisatieDossierNummer getNextNumber() {
    return isValid()
        ? new NaturalisatieDossierNummer(gemeenteCode, getFinalYearDigit(), vnr + 1)
        : new NaturalisatieDossierNummer();
  }

  public static NaturalisatieDossierNummer parse(String value) {
    if (value != null) {
      Matcher matcher = Pattern.compile(PATTERN).matcher(value);
      if (matcher.matches()) {
        return new NaturalisatieDossierNummer(
            parseLong(matcher.group(1)),
            parseLong(matcher.group(2)),
            parseLong(matcher.group(3)));
      }
    }
    return new NaturalisatieDossierNummer(value);
  }

  public static boolean isValid(String value) {
    return value != null && value.matches(PATTERN);
  }

  private static long getFinalYearDigit() {
    return parseLong(String.valueOf(LocalDate.now().getYear()).substring(3));
  }

  @Override
  public String toString() {
    return value;
  }
}
