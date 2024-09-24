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

package nl.procura.gba.web.modules.zaken.verhuizing;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import java.util.Date;

import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.standard.ProcuraDate;
import nl.procura.commons.core.exceptions.ProException;

public class VerhuisAanvraagPage extends ZakenPage {

  private VerhuisAanvraag aanvraag = null;

  public VerhuisAanvraagPage(String msg, VerhuisAanvraag aanvraag) {

    super(msg);

    setAanvraag(aanvraag);
  }

  public VerhuisAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VerhuisAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  protected void checkAanvang(Date aanvang) {

    // Limiet aanvangsdatum in dagen

    int date_limit_pos = aval(getApplication().getParmValue(ParameterConstant.VERHUIS_DATUM_LIMIET_TOEKOMST));
    int date_limit_neg = aval(getApplication().getParmValue(ParameterConstant.VERHUIS_DATUM_LIMIET_VERLEDEN));

    ProcuraDate dAanv = new ProcuraDate(aanvang);

    if (pos(date_limit_pos) && (new ProcuraDate().diffInDays(dAanv) > date_limit_pos)) {
      String msg = String.format("Aanvangsdatum mag maximaal %s %s na vandaag liggen.", date_limit_pos,
          getDagen(date_limit_pos));
      throw new ProException(ENTRY, INFO, msg);
    }

    if (pos(date_limit_neg) && (dAanv.diffInDays(new ProcuraDate()) > date_limit_neg)) {
      String msg = String.format("Aanvangsdatum mag maximaal %s %s vóór vandaag liggen.", date_limit_neg,
          getDagen(date_limit_neg));
      throw new ProException(ENTRY, INFO, msg);
    }
  }

  private String getDagen(int limit) {
    return limit == 1 ? "dag" : "dagen";
  }
}
