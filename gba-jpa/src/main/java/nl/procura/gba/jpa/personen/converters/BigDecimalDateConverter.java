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

package nl.procura.gba.jpa.personen.converters;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import nl.procura.standard.exceptions.ProException;

@Converter
public class BigDecimalDateConverter implements AttributeConverter<Date, BigDecimal> {

  private static final String DATE_FORMAT = "yyyyMMdd";

  @Override
  public BigDecimal convertToDatabaseColumn(final Date date) {
    if (date != null) {
      return toBigDecimal(new SimpleDateFormat(DATE_FORMAT).format(date));
    }
    return toBigDecimal(-1L);
  }

  @Override
  public Date convertToEntityAttribute(final BigDecimal dbData) {
    if (dbData != null && (dbData.toString().length() == 8)) {
      try {
        return new SimpleDateFormat(DATE_FORMAT).parse(dbData.toString());
      } catch (ParseException e) {
        throw new ProException("Cannot parse this value as : " + dbData);
      }
    }
    return null;
  }
}
