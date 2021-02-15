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

package examples.nl.procura.bvbsn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.bvbsn.actions.ActionOpvrBsnIdenGeg;
import nl.procura.bvbsn.actions.IDGegevens;

public class TestOpvrBsnIdenGeg extends MyTestCase {

  private final static Logger LOGGER = LoggerFactory.getLogger(MyTestCase.class.getName());

  public TestOpvrBsnIdenGeg() {

    IDGegevens idGegevens = new IDGegevens();

    idGegevens.setGeboortedatum("19811109");
    idGegevens.setGeslachtsnaam("Samuel");
    idGegevens.setGeslachtsaanduiding("M");

    setAction(new ActionOpvrBsnIdenGeg(getAfzender(), "1", idGegevens));

    init();
    send();
    test();

    LOGGER.info(((ActionOpvrBsnIdenGeg) getAction()).getBsn());
  }

  public static void main(String[] args) {
    new TestOpvrBsnIdenGeg();
  }

  @Override
  public boolean isActionSuccess() {
    try {
      LOGGER.info("Action: " + ((ActionOpvrBsnIdenGeg) getAction()).isGegevensGevonden());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }
}
