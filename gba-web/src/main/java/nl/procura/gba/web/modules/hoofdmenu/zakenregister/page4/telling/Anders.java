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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.date2str;

import java.text.MessageFormat;

import nl.procura.gba.web.common.misc.ZaakPeriode;

public class Anders extends ZaakPeriode {

  public Anders() {

    setDescr("Anders");
    setdFrom(-1);
    setdTo(-1);
  }

  @Override
  public String getDescr() {

    if (getdFrom() > 0) {
      return MessageFormat.format("Periode van {0} t/m {1}", date2str(astr(getdFrom())),
          date2str(astr(getdTo())));
    }

    return super.getDescr();
  }
}
