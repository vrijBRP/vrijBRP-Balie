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

import static nl.procura.standard.Globalfunctions.*;

import java.text.MessageFormat;
import java.util.Calendar;

import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.standard.ProcuraDate;

public class DezeWeek extends ZaakPeriode {

  public DezeWeek() {

    ProcuraDate d1 = new ProcuraDate();

    Calendar cal = Calendar.getInstance();
    cal.setTime(d1.getDateFormat());
    int day = (cal.get(Calendar.DAY_OF_WEEK) - 1);
    setdFrom(along(new ProcuraDate(d1.getSystemDate()).addDays(-day).getSystemDate()));
    setdTo(along(new ProcuraDate(d1.getSystemDate()).addDays(7 - day).getSystemDate()));

    setDescr(MessageFormat.format("Deze week ({0} / {1})", date2str(astr(getdFrom())), date2str(astr(getdTo()))));
  }
}
