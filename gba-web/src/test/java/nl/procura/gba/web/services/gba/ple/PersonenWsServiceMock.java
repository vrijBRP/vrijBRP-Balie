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

package nl.procura.gba.web.services.gba.ple;

import static nl.procura.gbaws.testdata.Testdata.DataSet.DEMO;

import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLNumber;
import nl.procura.gba.web.services.Services;
import nl.procura.gbaws.testdata.Testdata;

public class PersonenWsServiceMock extends PersonenWsService {

  public PersonenWsServiceMock(Services services) {
    setServices(services);
  }

  @Override
  protected PLEResult findPersonData(PLEArgs commandArgs) {
    // only a single number is supported for now
    if (commandArgs.getNumbers().size() != 1) {
      throw new UnsupportedOperationException();
    }
    PLNumber number = commandArgs.getNumbers().iterator().next();
    return Testdata.getPersonData(number.getBsn(), DEMO);
  }
}
