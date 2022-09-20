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

package nl.procura.gba.web.components.fields.values;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;

import java.math.BigDecimal;

import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.gba.common.DateTime;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

public class GbaDateFieldValue extends DateFieldValue {

  public GbaDateFieldValue() {
  }

  public GbaDateFieldValue(BasePLValue value) {
    this(value.getCode());
  }

  public GbaDateFieldValue(BigDecimal date) {

    if (date != null) {
      convert(date.longValue() >= 0 ? astr(date.toString()) : "");
    }
  }

  public GbaDateFieldValue(DateTime value) {
    super(value.getLongDate());
  }

  public GbaDateFieldValue(long date) {
    convert(date >= 0 ? astr(date) : "");
  }

  public GbaDateFieldValue(String value) {
    convert(value);
  }

  public String getFormatDate() {
    return getDescription();
  }

  public long getLongDate() {
    return along(getValue());
  }

  public int toInt() {
    return aval(getValue());
  }

  public String getSystemDate() {
    return getStringValue();
  }

  public BigDecimal toBigDecimal() {
    return BigDecimal.valueOf(along(getStringValue()));
  }

  private void convert(String value) {
    ProcuraDate d = new ProcuraDate(astr(value).equals("-1") ? "" : value);
    d.setForceFormatType(ProcuraDate.SYSTEMDATE_ONLY);
    d.setAllowedFormatExceptions(true);
    d.autocomplete();
    setValue(d.getSystemDate());
    setDescription(d.getFormatDate());
  }

  @Override
  public int compareTo(DateFieldValue o) {
    return aval(this.getValue()) > aval(o.getValue()) ? 1 : -1;
  }
}
