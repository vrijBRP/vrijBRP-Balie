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

import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.standard.ProcuraDate;

public class Gisteren extends ZaakPeriode {

  public Gisteren() {

    setdFrom(along(new ProcuraDate().addDays(-1).getSystemDate()));
    setdTo(along(new ProcuraDate().addDays(-1).getSystemDate()));

    setDescr(MessageFormat.format("Gisteren ({0})", date2str(astr(getdFrom()))));
  }
}
