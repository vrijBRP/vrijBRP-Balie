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

package nl.procura.gba.web.modules.bs.common.utils;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_EXTRA_TERMIJN;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.Date;

import com.vaadin.data.Property;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.ProDateField;

public class BsOnderzoekUtils {

  /**
   * Returns first value
   */
  public static Date getDate(Date... dates) {
    for (Date date : dates) {
      if (date != null) {
        return date;
      }
    }
    return null;
  }

  /**
   * Returns date base on startdate + period
   */
  public static Date getDateByTerm(Date value, Date other, ParameterType parameter) {
    ParameterService parameters = Services.getInstance().getParameterService();
    int termijn = aval(parameters.getSysteemParameter(parameter).getValue());
    if (value != null) {
      return value;
    }
    if (other != null && termijn > 0) {
      return new ProcuraDate(other).addDays(termijn).getDateFormat();
    }
    return null;
  }

  public static void setDateListener(ProDateField dIn, ProDateField dEnd, ParameterType parameter) {
    dIn.addListener((Property.ValueChangeListener) event -> {
      Date date1 = (Date) event.getProperty().getValue();
      if (date1 != null) {
        dEnd.setValue(getDateByTerm(null, date1, parameter));
      }
    });

    dEnd.setValue(getDateByTerm((Date) dEnd.getValue(), (Date) dIn.getValue(), ONDERZ_EXTRA_TERMIJN));
  }
}
