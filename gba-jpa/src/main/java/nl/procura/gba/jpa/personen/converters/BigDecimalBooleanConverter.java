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

import java.math.BigDecimal;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts BigDecimal fields too Boolean fields
 */
@Converter
public class BigDecimalBooleanConverter implements AttributeConverter<Boolean, BigDecimal> {

  @Override
  public BigDecimal convertToDatabaseColumn(Boolean value) {
    if (value == null) {
      return BigDecimal.valueOf(-1L);
    } else {
      return BigDecimal.valueOf(value ? 1 : 0);
    }
  }

  @Override
  public Boolean convertToEntityAttribute(BigDecimal value) {
    if (value != null) {
      if (value.intValue() == 1) {
        return Boolean.TRUE;
      } else if (value.intValue() == 0) {
        return Boolean.FALSE;
      }
    }
    return null;
  }
}
