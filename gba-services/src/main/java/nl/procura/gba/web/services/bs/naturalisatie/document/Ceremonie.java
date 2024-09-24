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

package nl.procura.gba.web.services.bs.naturalisatie.document;

import static java.lang.Boolean.TRUE;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.DateTime;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

public class Ceremonie {

  private final Integer  ceremonie;
  private final DateTime datum;
  private final String   tijdstip;
  private final Boolean  bijgewoond;

  public Ceremonie() {
    this(null, null, null, null);
  }

  public Ceremonie(Integer ceremonie, Date datum, BigDecimal tijdstip, Boolean bijgewoond) {
    this.ceremonie = ceremonie;
    this.datum = new DateTime(datum);
    this.tijdstip = toTijdstip(tijdstip);
    this.bijgewoond = bijgewoond;
  }

  private static String toTijdstip(BigDecimal tijdstip) {
    return tijdstip != null && tijdstip.intValue() > 0 ? new TimeFieldValue(tijdstip.toString()).getDescription() : "";
  }

  public String getCeremonie() {
    return ceremonie + "e ceremonie";
  }

  public DateTime getDatum() {
    return datum;
  }

  public String getTijdstip() {
    return tijdstip;
  }

  public String getBijgewoond() {
    if (bijgewoond != null) {
      return TRUE.equals(bijgewoond) ? "bijgewoond" : "niet bijgewoond";
    }
    return "";
  }

  public boolean isCorrect() {
    return datum != null && StringUtils.isNotBlank(datum.toString());
  }

  @Override
  public String toString() {
    return isCorrect() ? String.format("%de ceremonie op %s%s%s",
        ceremonie, datum, tijdstip != null
            ? " / " + new TimeFieldValue(tijdstip)
            : "",
        (bijgewoond != null
            ? (String.format(" (%s)", getBijgewoond()))
            : ""))
        : "Geen ceremonie";
  }
}
