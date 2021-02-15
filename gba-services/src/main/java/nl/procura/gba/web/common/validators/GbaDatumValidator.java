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

package nl.procura.gba.web.common.validators;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.standard.ProcuraDate;

public class GbaDatumValidator implements GbaValidator<String> {

  @Override
  public String getErrorMessage() {
    return "Incorrecte datum";
  }

  @Override
  public boolean isValid(String datum) {

    if (fil(datum)) {
      if (datum.equals("00-00-0000") || aval(datum) == 0) {
        return true;
      }

      ProcuraDate d = new ProcuraDate(datum);
      d.setAllowedFormatExceptions(true);
      return (d.isAutocompleteCorrect());
    }

    return false;
  }
}
