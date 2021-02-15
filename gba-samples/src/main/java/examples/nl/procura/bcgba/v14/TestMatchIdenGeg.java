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

package examples.nl.procura.bcgba.v14;

import java.text.MessageFormat;

import nl.bprbzk.bcgba.v14.ArrayOfMatchIdenGegResultaatDE;
import nl.bprbzk.bcgba.v14.MatchIdenGegBU;
import nl.procura.bcgba.v14.actions.ActionMatchIdenGeg;
import nl.procura.bcgba.v14.actions.IDGegevens;
import nl.procura.bcgba.v14.misc.BcGbaCode;
import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMatchIdenGeg extends MyTestCase {

  public TestMatchIdenGeg() {

    IDGegevens id = new IDGegevens();

    id.setGeslachtsnaam("Jansen");
    id.setGeslachtsaanduiding("M");
    id.setGeboortedatum("19700102");
    id.setNationaliteit("Maltese");
    id.setBuitenlandsPersoonsnummer("1234567A");

    setAction(new ActionMatchIdenGeg(BcGbaVraagbericht.VRAAG2, "1234", getAfzender(), id));

    init();

    send();

    test();

    ArrayOfMatchIdenGegResultaatDE result = ((MatchIdenGegBU) getAction().getResponseObject()).getResultaat();

    log.info(MessageFormat.format("Count: {0}", result.getMatchIdenGegResultaatDE()
        .get(0)
        .getIdenGegAntwoord()
        .getMatchIdenGegAntwoordDE()
        .size()));

    log.info("SUCCESS: " + isActionSuccess());
  }

  public static void main(String[] args) {
    new TestMatchIdenGeg();
  }

  @Override
  public boolean isActionSuccess() {
    return ((ActionMatchIdenGeg) getAction()).isResultaat(BcGbaCode.RESULTAAT_GEVONDEN);
  }
}
